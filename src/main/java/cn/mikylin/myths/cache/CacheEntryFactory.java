package cn.mikylin.myths.cache;

import cn.mikylin.myths.cache.map.CacheEntry;

public interface CacheEntryFactory<K,V> {

    CacheEntry<K,V> build(K key, V value);
}
