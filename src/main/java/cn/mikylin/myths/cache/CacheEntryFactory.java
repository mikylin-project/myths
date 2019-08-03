package cn.mikylin.myths.cache;

import cn.mikylin.myths.cache.map.CacheEntry;

/**
 * factory to create the entry
 * @author mikylin
 * @date 20190722
 */
public interface CacheEntryFactory<K,V> {

    CacheEntry<K,V> build(K key, V value);
}
