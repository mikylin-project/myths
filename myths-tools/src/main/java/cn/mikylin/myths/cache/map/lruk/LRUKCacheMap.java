package cn.mikylin.myths.cache.map.lruk;

import cn.mikylin.myths.cache.map.CacheEntry;
import cn.mikylin.myths.cache.map.CacheEntryFactory;
import cn.mikylin.myths.cache.map.CacheLoader;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
     * 是否开启缓存过期
     */
    private boolean expire;

    /**
     * 用于创建节点的工厂类
     */
    private CacheEntryFactory<K,V> builder;
    /**
     * 缓存加载工厂
     */
    private CacheLoader<K,V> cacheLoader;

    private volatile int size; // 目前的链表节点数
    private CacheEntry<K,V> header;
    private CacheEntry<K,V> tail;

    public LRUKCacheMap(int cap,int k,long kTimes,
                        CacheLoader<K,V> loader,long expireTime) {

        if(cap <= 0 || k <= 0 || kTimes <= 0L)
            throw new RuntimeException("LRUK Cache create exception.");

        this.cap = cap;

        // millisTime  LRU 触发的重置时间
        // k 触发 LRU 的次数
        this.builder = (key,value) -> new LRUKCacheEntry<>(key,value,k,kTimes,expireTime);

        // 缓存加载工厂
        if(loader != null)
            this.cacheLoader = loader;
        else
            this.cacheLoader
                    = new CacheLoader<>() {
                            @Override
                            public V load(K key) {
                                return null;
                            }

                            @Override
                            public boolean defenseCachePenetration() {
                                return false;
                            }
                    };

        if(expireTime > 0)
            expire = true;
    }



    /**
     * LRUK 链表节点对象
     */
    private static final class LRUKCacheEntry<K,V> extends CacheEntry<K,V> {

        private int k; // 在规定时间内被调用的次数
        private boolean lru; // 是否开启 lru 检测,false 代表开启,true 代表关闭

        private int kLimit; // 调用次数阈值
        private long kTimesLimit; // 时间阈值


        private LRUKCacheEntry(K key,V value,int k,long kTimes,long expireTime) {
            super(key,value,expireTime);
            this.k = 0;
            this.lru = false;
            this.kLimit = k;
            this.kTimesLimit = kTimes;
        }


        /**
         * 检测 lru 策略
         */
        public boolean checkLRU() {

            long now = System.currentTimeMillis();

            synchronized (this) {
                // 如果已经关闭 lru 检测了,就无需往下走了
                if(lru)
                    return false;

                // 超时了,重新计算,k 值归零
                if(now - getLately() > this.kTimesLimit)
                    this.k = 0;

                // 时间刷新
                freshLately();

                // 判断调用次数和阈值的关系,大于等于的话就会返回检测通过
                if( ++ this.k >= this.kLimit)
                    return (lru = true);
            }
            return false;
        }
    }




    /**
     * 新增元素
     */
    private void add(K key,V value) {

        // 如果已满，此处需要执行过期策略
        if(cap == size && tail != null) {
            expire();
            if(cap == size)
                remove(tail);
        }

        CacheEntry<K, V> entry = builder.build(key,value);

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
    private void remove(CacheEntry<K,V> entry) {

        // 链表中删除该元素
        if(entry == header && entry == tail) {
            header = tail = null;
        } else if (entry == header) {
            header = entry.getAfter();
            if(header != null) {
                header.setBefore(null);
                entry.setAfter(null);
            }
            entry.setAfter(null);
        } else if(entry == tail) {
            tail = entry.getBefore();
            if(tail != null) {
                tail.setAfter(null);
                entry.setBefore(null);
            }
            entry.setBefore(null);
        } else {
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
    private void expire() {
        if(expire) {
            CacheEntry<K,V> entry = header;
            CacheEntry<K,V> after;
            while(entry != null) {
                after = entry.getAfter();
                if(entry.isExpire())
                    remove(entry);
                entry = after;
            }
        }
    }



    /**
     * 从链表中找到 key 是某个 hash 值的那个节点
     */
    private CacheEntry<K,V> find(K key) {

        int hash = CacheEntry.hash(key);

        if(header != null)
            for(CacheEntry<K,V> thisEntry = header ;
                thisEntry.getAfter() != null ;
                thisEntry = thisEntry.getAfter())
                if(thisEntry.getHash() == hash) return thisEntry;

        return null;
    }

    private void doLRUChange(CacheEntry<K,V> entry) {
        if(((LRUKCacheEntry<K,V>)entry).checkLRU()) {
            remove(entry);
            add(entry.getKey(),entry.getValue());
        }
    }


    @Override
    public int size() {
        return size;
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
        if(entity != null && !entity.isExpire()) {
            doLRUChange(entity);
            return entity.getValue();
        }

        V loadCache;
        if((loadCache = cacheLoader.load(k)) != null
                || cacheLoader.defenseCachePenetration())
            add(k,loadCache);

        return loadCache;
    }

    @Override
    public V put(K key, V value) {

        CacheEntry<K, V> entity = find(key);

        if(entity != null)
            entity.setValue(value);
        else
            add(key,value);

        return value;
    }

    @Override
    public V remove(Object key) {
        CacheEntry<K, V> removeEntry = find((K)key);
        if(removeEntry == null)
            return null;
        remove(removeEntry);
        return removeEntry.getValue();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach((k,v) -> put(k,v));
    }

    @Override
    public void clear() {
        synchronized (this) {
            header = null;
            tail = null;
            size = 0;
        }
    }

    @Override
    public Set<K> keySet() {

        Set<K> keys = new LinkedHashSet<>(size());
        if(header != null)
            for(CacheEntry<K,V> thisEntry = header ;
                thisEntry.getAfter() != null ;
                thisEntry = thisEntry.getAfter())
                keys.add(thisEntry.getKey());
        return keys;
    }

    @Override
    public Collection<V> values() {

        Collection<V> values = new ArrayList<>(size());
        if(header != null)
            for(CacheEntry<K,V> thisEntry = header ;
                thisEntry.getAfter() != null ;
                thisEntry = thisEntry.getAfter())
                values.add(thisEntry.getValue());
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {

        Set<Entry<K, V>> entites = new LinkedHashSet<>(size());
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
