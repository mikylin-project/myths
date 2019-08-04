package cn.mikylin.myths.cache.map;

import cn.mikylin.myths.cache.CacheEntryFactory;
import cn.mikylin.myths.cache.CacheLoader;
import cn.mikylin.myths.cache.reference.SoftReferenceManager;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LRU-K Map
 * @author mikylin
 * @date 20190722
 */
public class LRUKCacheMap<K,V> implements ConcurrentMap<K,V> {

    private int cap; // 容量
    private volatile int size; // 目前的链表节点数
    private CacheEntryFactory<K,V> builder; // 用于创建链表节点的工厂类
    private SoftReferenceManager<CacheEntry<K,V>> softEntries; // 软链接管理类
    private CacheLoader<K,V> cacheLoader; // 缓存加载工厂
    private boolean defensePolicy; // 是否开启防止缓存穿透，默认不开启
    private boolean expire; // 是否开启缓存过期

    private CacheEntry<K,V> header;
    private CacheEntry<K,V> tail;

    private ReentrantLock removeLock = new ReentrantLock();
    private ReentrantLock addLock = new ReentrantLock();


    public LRUKCacheMap(int cap,int softRefCap,int k,long kTimes,
                        CacheLoader<K,V> loader, boolean defensePolicy,long expireTime){

        if(cap <= 0 || k <= 0 || kTimes <= 0L)
            throw new RuntimeException("LRUK Cache create exception");

        this.cap = cap;
        this.size = 0;

        // millisTime  LRU 触发的重置时间
        // k 触发 LRU 的次数
        this.builder = (key,value) -> new LRUKCacheEntry<>(key, value, k, kTimes, expireTime);

        this.softEntries = new SoftReferenceManager<>(softRefCap);

        if(loader != null)
            this.cacheLoader = loader;
        else
            this.cacheLoader = key -> null;

        this.defensePolicy = defensePolicy;

        if(expireTime > 0)
            expire = true;
    }




    /**
     * LRUK 链表节点对象
     */
    private static class LRUKCacheEntry<K,V> extends CacheEntry<K,V> {

        private volatile long lately; // 最近被调用的时间
        private int k; // 在规定时间内被调用的次数
        private boolean lru; // 是否开启 lru 检测,false 代表开启,true 代表关闭

        private int kLimit; // 调用次数阈值
        private long kTimesLimit; // 时间阈值


        LRUKCacheEntry(K key,V value,int k,long kTimes,long expireTime){
            super(key,value,expireTime);
            this.k = 0;
            this.lru = false;
            this.kLimit = k;
            this.kTimesLimit = kTimes;
        }


        /**
         * 检测 lru 策略
         */
        public boolean checkLRU(){

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

        static <K,V> boolean checkLRU(CacheEntry<K,V> entry){
            if(!(entry instanceof LRUKCacheEntry))
                throw new RuntimeException("entry class exception");
            return ((LRUKCacheEntry<K,V>)entry).checkLRU();
        }

    }

    /**
     * 从链表中找到 key 是某个 hash 值的那个节点
     */
    private CacheEntry<K,V> find(int hash){
        if(header == null)
            return null;

        CacheEntry<K,V> findEntry = null;
        CacheEntry<K,V> entry = header;

        if(entry.equals(hash))
            return entry;

        while(entry.getAfter() != null){
            entry = entry.getAfter();
            if(entry.equals(hash)){
                findEntry = entry;
                break;
            }
        }
        return findEntry;
    }

    /**
     * 添加到链表的头部
     */
    private void add(K key,V value){

        try {
            addLock.lock();

            if(cap == size && tail != null){
                expire();
                if(cap == size && tail != null){
                    remove(tail);
                }
            }

            CacheEntry<K, V> entry = builder.build(key,value);
            if(header == null) {
                header = entry;
                tail = entry;
            } else {
                header.setBefore(entry);
                entry.setAfter(header);
                header = entry;
            }

            size ++;
        } finally {
            addLock.unlock();
        }
    }

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



    private void remove(CacheEntry<K,V> entry){
        synchronized (this){
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

            this.size --;
        }

    }


    /**
     * 删除某个节点
     */
    private CacheEntry<K, V> remove(int hash){
        CacheEntry<K, V> entry = find(hash);
        if(entry == null)
            return null;

        try {
            removeLock.lock();
            remove(entry);
            softEntries.set(entry);
        } finally {
            removeLock.unlock();
        }
        return entry;
    }


    private void doLRUChange(CacheEntry<K,V> entry){
        if(LRUKCacheEntry.checkLRU(entry)){
            remove(entry.getHash());
            add(entry.getKey(),entry.getValue());
        }
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {

        K k = (K)key;

        CacheEntry<K, V> entity = find(CacheEntry.hash(k));
        if(entity != null && !entity.isExpire()){
            doLRUChange(entity);
            return entity.getValue();
        }

        CacheEntry<K, V> softEntry = softEntries.find(builder.build(k, null));
        if(softEntry != null)
            return softEntry.getValue();

        V loadCache = this.cacheLoader.load(k);
        if(loadCache != null || this.defensePolicy){
            add(k,null);
        }

        return loadCache;
    }

    @Override
    public V put(K key, V value) {

        CacheEntry<K, V> entity = find(CacheEntry.hash(key));

        if(entity != null)
            entity.setValue(value);
        else
            add(key,value);

        return value;
    }

    @Override
    public V remove(Object key) {
        CacheEntry<K, V> removeEntry = remove(key.hashCode());
        if(removeEntry == null)
            return null;
        return removeEntry.getValue();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach((k,v) -> put(k,v));
    }

    @Override
    public void clear() {

        synchronized (this){
            header = null;
            tail = null;
            size = 0;
        }

    }

    @Override
    public Set<K> keySet() {

        Set<K> keys = new LinkedHashSet<>(size);
        if(header != null)
            for(CacheEntry<K,V> thisEntry = header ;
                thisEntry.getAfter() != null ;
                thisEntry = thisEntry.getAfter())
                keys.add(thisEntry.getKey());

        return keys;
    }

    @Override
    public Collection<V> values() {

        Collection<V> values = new ArrayList<>(size);
        if(header != null)
            for(CacheEntry<K,V> thisEntry = header ;
                thisEntry.getAfter() != null ;
                thisEntry = thisEntry.getAfter())
                values.add(thisEntry.getValue());

        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {

        Set<Entry<K, V>> entites = new LinkedHashSet<>(size);
        if(header != null)
            for(CacheEntry<K,V> thisEntry = header ;
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

        CacheEntry<K, V> entry = find(CacheEntry.hash(key));
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
