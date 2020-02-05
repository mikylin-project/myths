package cn.mikylin.myths.cache.map.bi;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * bi map.
 *
 * @author mikylin
 * @date 20190803
 */
public final class BiHashMap<K,V> implements BiMap<K,V> {

    private Map<K,V> normalMap;
    private Map<V,K> reverseMap;

    public BiHashMap(Map<K,V> normalMap,Map<V,K> reverseMap) {

        Objects.requireNonNull(normalMap);
        Objects.requireNonNull(reverseMap);

        if(!Objects.equals(reverseMap.getClass(),normalMap.getClass()))
            throw new RuntimeException("map class must be same.");

        this.normalMap = normalMap;
        this.reverseMap = reverseMap;
    }

    @Override
    public int size() {
        return normalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return normalMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return normalMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return normalMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return normalMap.get(key);
    }



    @Override
    public V put(K key, V value) {
        revPut(key,value);
        normalMap.put(key,value);
        return value;
    }

    public BiHashMap<K,V> set(K key, V value) {
        put(key,value);
        return this;
    }

    @Override
    public V remove(Object key) {
        V v = normalMap.get(key);
        reverseMap.remove(v);
        return normalMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach((k,v) -> put(k,v));
    }

    @Override
    public void clear() {
        normalMap.clear();
        reverseMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return normalMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return normalMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return normalMap.entrySet();
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return put(key,value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        reverseMap.remove(value);
        return normalMap.remove(key,value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        reverseMap.remove(oldValue);
        reverseMap.put(newValue,key);
        return normalMap.replace(key,oldValue,newValue);
    }

    @Override
    public V replace(K key, V value) {
        V replace = normalMap.replace(key, value);
        revPut(key,value);
        return replace;
    }

    @Override
    public void revPut(K key, V value) {
        reverseMap.remove(value);
        reverseMap.put(value,key);
    }

    @Override
    public K revGet(V value) {
        return reverseMap.get(value);
    }
}
