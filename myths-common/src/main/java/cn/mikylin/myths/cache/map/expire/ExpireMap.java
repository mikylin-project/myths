package cn.mikylin.myths.cache.map.expire;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExpireMap<K,V> implements Map<K,V> {

    private long expireTime;

    private HashMap<K, ExpireMap.ExpireEntry<V>> map = new HashMap<>();

    public ExpireMap(long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public V get(Object key) {
        ExpireEntry<V> dataE = map.get(key);
        return dataE.sysTime + expireTime <= System.currentTimeMillis()
                ? remove(key) :dataE.obj;
    }

    @Override
    public V put(K key, V value) {
        ExpireEntry<V> e = new ExpireEntry<>();
        e.obj = value;
        map.put(key,e);
        return value;
    }

    static class ExpireEntry<V> {

        long sysTime = System.currentTimeMillis();
        V obj;

        @Override
        public int hashCode() {
            return obj.hashCode();
        }
    }

    @Override
    public V remove(Object key) {
        ExpireEntry<V> remove = map.remove(key);
        return remove == null ? null : remove.obj;
    }




    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsKey(value);
    }



    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }





}
