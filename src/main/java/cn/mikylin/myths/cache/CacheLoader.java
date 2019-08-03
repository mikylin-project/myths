package cn.mikylin.myths.cache;

public interface CacheLoader<K,V> {

    V load(K key);
}
