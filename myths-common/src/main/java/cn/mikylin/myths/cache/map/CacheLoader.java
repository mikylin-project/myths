package cn.mikylin.myths.cache.map;

/**
 * load the cache.
 *
 * @author mikylin
 * @date 20190725
 */
public interface CacheLoader<K,V> {

    V load(K key);

    /**
     * is open the defense for cache penetration.
     *
     * @return
     */
    boolean defenseCachePenetration();
}
