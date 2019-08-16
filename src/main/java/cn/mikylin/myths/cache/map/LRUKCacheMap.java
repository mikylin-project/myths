package cn.mikylin.myths.cache.map;

import cn.mikylin.myths.cache.CacheEntry;
import cn.mikylin.myths.cache.CacheEntryFactory;
import cn.mikylin.myths.cache.CacheLoader;
import cn.mikylin.myths.cache.array.ArrayManager;
import cn.mikylin.myths.cache.array.DefaultArrayManager;
import cn.mikylin.myths.cache.array.SoftReferenceManager;
import java.util.*;
import java.util.concurrent.*;

/**
 * LRU-K Map
 * @author mikylin
 * @date 20190722
 */
public final class LRUKCacheMap<K,V> implements ConcurrentMap<K,V> {

    /**
     * 容量
     */
    private int cap;
    /**
     * 软链接容量
     */
    private int softRefCap;
    /**
     * 是否开启防止缓存穿透，默认不开启
     */
    private boolean defensePolicy;
    /**
     * 是否开启缓存过期
     */
    private boolean expire;

    /**
     * 用于创建节点的工厂类
     */
    private CacheEntryFactory<K,V> builder;
    /**
     * 软链接管理对象
     */
    private SoftReferenceManager<CacheEntry<K,V>> softEntries;
    /**
     * 数组管理对象
     */
    private EntryArrayManager<K,V> arrayEntries;
    /**
     * 链表管理对象
     */
    private EntryLinkedManager<K,V> linkedEntries;
    /**
     * 缓存加载工厂
     */
    private CacheLoader<K,V> cacheLoader;

    public LRUKCacheMap(int cap,int softRefCap,int k,long kTimes,
                        CacheLoader<K,V> loader, boolean defensePolicy,long expireTime){

        if(cap <= 0 || k <= 0 || kTimes <= 0L)
            throw new RuntimeException("LRUK Cache create exception.");

        this.cap = cap;
        this.softRefCap = softRefCap;


        // millisTime  LRU 触发的重置时间
        // k 触发 LRU 的次数
        this.builder = (key,value) -> new LRUKCacheEntry<>(key, value, k, kTimes, expireTime);

        // 初始化数据区
        cacheInit(softRefCap);

        // 缓存加载工厂
        if(loader != null)
            this.cacheLoader = loader;
        else
            this.cacheLoader = key -> null;

        // 是否开启缓存防御机制
        this.defensePolicy = defensePolicy;

        if(expireTime > 0)
            expire = true;
    }



    /**
     * LRUK 链表节点对象
     */
    private static final class LRUKCacheEntry<K,V> extends CacheEntry<K,V> {

        private volatile long lately; // 最近被调用的时间
        private int k; // 在规定时间内被调用的次数
        private boolean lru; // 是否开启 lru 检测,false 代表开启,true 代表关闭

        private int kLimit; // 调用次数阈值
        private long kTimesLimit; // 时间阈值


        private LRUKCacheEntry(K key,V value,int k,long kTimes,long expireTime){
            super(key,value,expireTime);
            this.k = 0;
            this.lru = false;
            this.kLimit = k;
            this.kTimesLimit = kTimes;
        }


        /**
         * 检测 lru 策略
         */
        private boolean checkLRU(){

            long now = System.currentTimeMillis();

            synchronized (this){
                // 如果已经关闭 lru 检测了,就无需往下走了
                if(lru)
                    return false;

                // 超时了,重新计算,k 值归零
                if(now - this.lately > this.kTimesLimit)
                    this.k = 0;

                // 时间刷新
                this.lately = now;

                // 判断调用次数和阈值的关系,大于等于的话就会返回检测通过
                if( ++ this.k >= this.kLimit)
                    return (lru = true);
            }
            return false;
        }

        /**
         * 检测 lruk 策略的静态方法
         */
        private static <K,V> boolean checkLRU(CacheEntry<K,V> entry){
            if(entry.getClass() != LRUKCacheEntry.class)
                throw new RuntimeException("entry class exception");
            return ((LRUKCacheEntry<K,V>)entry).checkLRU();
        }
    }

    /**
     * 管理数组的总集合类
     */
    private class EntryArrayManager<K,V> implements ArrayManager<CacheEntry<K,V>>{

        private DefaultArrayManager<CacheEntry<K,V>>[] managers;
        private CacheEntryFactory<K,V> builder;

        private EntryArrayManager(CacheEntryFactory<K,V> builder){

            this.builder = builder;

            int size = cap / 5;

            managers = new DefaultArrayManager[10];

            for (int i = 0 ; i < 10 ; i ++){
                DefaultArrayManager<CacheEntry<K,V>> dam = new DefaultArrayManager<>(size);
                managers[i] = dam;
            }

        }

        private DefaultArrayManager<CacheEntry<K,V>> group(CacheEntry<K, V> entry){
            return managers[entry.getHash() % 10];
        }

        public CacheEntry<K, V> findByKey(K key){
            return find(builder.build(key,null));
        }

        @Override
        public CacheEntry<K, V> find(CacheEntry<K, V> entry) {
            return group(entry).find(entry);
        }

        @Override
        public boolean remove(CacheEntry<K, V> entry) {
            return group(entry).remove(entry);
        }

        @Override
        public boolean add(CacheEntry<K, V> entry) {
            return group(entry).add(entry);
        }

        @Override
        public boolean replace(CacheEntry<K, V> oldElement, CacheEntry<K, V> newElement) {
            throw new RuntimeException("can not replace");
        }

        @Override
        public void change(int newSize) {
            throw new RuntimeException("can not change");
        }
    }


    /**
     * 管理链表的总集合类
     */
    private class EntryLinkedManager<K,V>{

        private volatile int size; // 目前的链表节点数
        private CacheEntry<K,V> header;
        private CacheEntry<K,V> tail;

        private EntryArrayManager<K,V> arrayEntries;
        private CacheEntryFactory<K,V> builder;

        private EntryLinkedManager(EntryArrayManager<K,V> arrayEntries,CacheEntryFactory<K,V> builder){
            this.arrayEntries = arrayEntries;
            this.builder = builder;
        }

        /**
         * 新增元素
         */
        private void add(K key,V value){

            // 如果已满，此处需要执行过期策略
            if(cap == size && tail != null){
                expire();
                if(cap == size)
                    remove(tail);
            }

            CacheEntry<K, V> entry = builder.build(key,value);

            // 数组中新增该元素
            arrayEntries.add(entry);

            // 链表中新增该元素
            if(header == null) {
                header = entry;
                tail = entry;
            } else {
                header.setBefore(entry);
                entry.setAfter(header);
                header = entry;
            }

            size ++;
        }

        /**
         * 删除元素
         */
        private void remove(CacheEntry<K,V> entry){

            // 数组中删除该元素
            arrayEntries.remove(entry);

            // 链表中删除该元素
            if(entry == header && entry == tail){
                header = tail = null;
            }else if (entry == header){
                header = entry.getAfter();
                if(header != null){
                    header.setBefore(null);
                    entry.setAfter(null);
                }
                entry.setAfter(null);
            }else if(entry == tail){
                tail = entry.getBefore();
                if(tail != null){
                    tail.setAfter(null);
                    entry.setBefore(null);
                }
                entry.setBefore(null);
            }else{
                CacheEntry<K,V> before = entry.getBefore();
                CacheEntry<K,V> after = entry.getAfter();

                if(before != null)
                    before.setAfter(after);
                if(after != null)
                    after.setBefore(before);

                entry.setBefore(null);
                entry.setAfter(null);
            }

            size --;
        }

        /**
         * 对过期数据进行清洗
         */
        private void expire(){
            if(expire){
                CacheEntry<K,V> entry = header;
                CacheEntry<K,V> after;
                while(entry != null){
                    after = entry.getAfter();
                    if(entry.isExpire()){
                        remove(entry);
                    }
                    entry = after;
                }
            }
        }

        private int size(){
            return size;
        }
        private CacheEntry<K,V> header(){
            return header;
        }
    }


    /**
     * 从链表中找到 key 是某个 hash 值的那个节点
     */
    private CacheEntry<K,V> find(K key){
        return arrayEntries.findByKey(key);
    }

    /**
     * 数据区初始化
     */
    private void cacheInit(int softRefCap){
        synchronized (this){
            this.softEntries = new SoftReferenceManager<>(softRefCap);
            this.arrayEntries = new EntryArrayManager<>(builder);
            this.linkedEntries = new EntryLinkedManager<>(arrayEntries,builder);
        }
    }

    private void doLRUChange(CacheEntry<K,V> entry){
        if(LRUKCacheEntry.checkLRU(entry)){
            linkedEntries.remove(entry);
            linkedEntries.add(entry.getKey(),entry.getValue());
        }
    }


    @Override
    public int size() {
        return linkedEntries.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return keySet().contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public V get(Object key) {

        K k = (K)key;

        // 从数组中获取节点
        CacheEntry<K, V> entity = find(k);
        if(entity != null && !entity.isExpire()){
            doLRUChange(entity);
            return entity.getValue();
        }

        // 在数组没有获取到的情况
        CacheEntry<K, V> softEntry = softEntries.find(builder.build(k, null));
        if(softEntry != null)
            return softEntry.getValue();

        V loadCache;
        if((loadCache = this.cacheLoader.load(k)) != null
                || this.defensePolicy)
            linkedEntries.add(k,loadCache);

        return loadCache;
    }

    @Override
    public V put(K key, V value) {

        CacheEntry<K, V> entity = find(key);

        if(entity != null)
            entity.setValue(value);
        else
            linkedEntries.add(key,value);

        return value;
    }

    @Override
    public V remove(Object key) {
        CacheEntry<K, V> removeEntry = find((K)key);
        if(removeEntry == null)
            return null;
        linkedEntries.remove(removeEntry);
        return removeEntry.getValue();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach((k,v) -> put(k,v));
    }

    @Override
    public void clear() {
        cacheInit(softRefCap);
    }

    @Override
    public Set<K> keySet() {

        Set<K> keys = new LinkedHashSet<>(linkedEntries.size());
        if(linkedEntries.header() != null)
            for(CacheEntry<K,V> thisEntry = linkedEntries.header() ;
                thisEntry.getAfter() != null ;
                thisEntry = thisEntry.getAfter())
                keys.add(thisEntry.getKey());
        return keys;
    }

    @Override
    public Collection<V> values() {

        Collection<V> values = new ArrayList<>(linkedEntries.size());
        if(linkedEntries.header() != null)
            for(CacheEntry<K,V> thisEntry = linkedEntries.header() ;
                thisEntry.getAfter() != null ;
                thisEntry = thisEntry.getAfter())
                values.add(thisEntry.getValue());
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {

        Set<Entry<K, V>> entites = new LinkedHashSet<>(linkedEntries.size());
        if(linkedEntries.header() != null)
            for(CacheEntry<K,V> thisEntry = linkedEntries.header() ;
                thisEntry.getAfter() != null ;
                thisEntry = thisEntry.getAfter())
                entites.add(thisEntry);
        return entites;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return null;
    }

    @Override
    public boolean remove(Object key, Object value) {
        V val = remove(key);
        return val != null;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {

        CacheEntry<K, V> entry = find(key);
        if(entry != null && entry.getValue() == oldValue){
            entry.setValue(newValue);
            return true;
        }
        return false;
    }

    @Override
    public V replace(K key, V value) {
        return put(key,value);
    }

}
