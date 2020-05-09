package cn.mikylin.myths.cache.map.bi;

import java.util.Map;

/**
 * bi map interface.
 *
 * @author mikylin
 * @date 20191218
 */
public interface BiMap<K,V> extends Map<K,V> {

    /**
     * reverse get.
     *
     * @param value  value
     * @return  key
     */
    K revGet(V value);

    /**
     * reverse put.
     *
     * @param key  key
     * @param value  value
     */
    void revPut(K key, V value);

    /**
     * set k-v.
     *
     * @param key  key
     * @param value  value
     */
    BiMap<K,V> set(K key,V value);
}
