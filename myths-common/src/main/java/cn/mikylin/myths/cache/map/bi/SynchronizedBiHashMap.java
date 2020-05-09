package cn.mikylin.myths.cache.map.bi;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * bi map for synchronized.
 *
 * @author mikylin
 * @date 20200509
 */
public final class SynchronizedBiHashMap<K,V> extends AbstractBiMap<K,V> {

    public SynchronizedBiHashMap(int cap) {
        super(cap);
    }

    public SynchronizedBiHashMap(){
        this(16);
    }

    @Override
    public synchronized int size() {
        return super.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public synchronized boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    @Override
    public synchronized V get(Object key) {
        return super.get(key);
    }

    @Override
    public synchronized V put(K key, V value) {
        return super.put(key,value);
    }

    @Override
    public synchronized BiMap<K,V> set(K key, V value) {
        return super.set(key,value);
    }

    @Override
    public synchronized V remove(Object key) {
        return super.remove(key);
    }

    @Override
    public synchronized void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
    }

    @Override
    public synchronized void clear() {
        super.clear();
    }

    @Override
    public synchronized Set<K> keySet() {
        return super.keySet();
    }

    @Override
    public synchronized Collection<V> values() {
        return super.values();
    }

    @Override
    public synchronized Set<Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    @Override
    public synchronized V putIfAbsent(K key, V value) {
        return super.putIfAbsent(key,value);
    }

    @Override
    public synchronized boolean remove(Object key, Object value) {
        return super.remove(key,value);
    }

    @Override
    public synchronized boolean replace(K key, V oldValue, V newValue) {
        return super.replace(key,oldValue,newValue);
    }

    @Override
    public synchronized V replace(K key, V value) {
        return super.replace(key, value);
    }

    @Override
    public synchronized K revGet(V value) {
        return super.revGet(value);
    }

    @Override
    public synchronized void revPut(K key, V value){
        super.revPut(key,value);
    }
}
