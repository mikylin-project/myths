package cn.mikylin.myths.cache.map;

import java.util.Map;

public abstract class CacheEntry<K,V> implements Map.Entry<K,V>{

    private int hash; // hash
    private K key; // key
    private V value; // value
    private CacheEntry<K,V> before;
    private CacheEntry<K,V> after;
    private long expireTime; // 过期时间,如果为 0 则永不过期
    private long lately;


    CacheEntry(K key,V value,long expireTime){
        this.key = key;
        this.value = value;
        this.hash = hash(key);
        this.expireTime = expireTime;
        this.lately = System.currentTimeMillis();
    }

    public boolean equals(int hashcode){
        return hashcode == hash;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return value;
    }

    public CacheEntry<K, V> getBefore() {
        return before;
    }

    public void setBefore(CacheEntry<K, V> before) {
        this.before = before;
    }

    public CacheEntry<K, V> getAfter() {
        return after;
    }

    public void setAfter(CacheEntry<K, V> after) {
        this.after = after;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    @Override
    public boolean equals(Object obj){
        return obj.getClass() == this.getClass() && hash(obj) == hash;
    }

    /**
     * 判断是否过期
     */
    public boolean isExpire(){
        return this.expireTime > 0
                && System.currentTimeMillis() - this.lately > this.expireTime;
    }

    static int hash(Object obj){
        return obj.hashCode();
    }
}
