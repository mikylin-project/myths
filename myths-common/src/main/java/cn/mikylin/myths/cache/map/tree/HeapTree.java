package cn.mikylin.myths.cache.map.tree;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 堆的实现
 *
 * @author mikylin
 * @date 20200417
 */
public class HeapTree<K,V> implements Map<K,V> {

    volatile HeapTreeEntry<K,V>[] entries;
    volatile int size;

    public HeapTree(int size) {
        entries = new HeapTreeEntry[size];
        this.size = 0;
    }


    static class HeapTreeEntry<K,V> implements Map.Entry<K,V> {

        K key;
        volatile V value;
        volatile int hash;

        HeapTreeEntry(K key,V value) {
            this.key = key;
            this.value = value;
            this.hash = hash(this.key);
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
            return this.value = value;
        }
    }

    private HeapTreeEntry<K,V> find(Object k) {
        int hash = hash(k);

        for(int index = 0;;) {
            HeapTreeEntry<K,V> node = entries[index];
            if(node == null)
                return null;

            int nodeHash = node.hash;

            if(hash == nodeHash)
                return node;
            else if(hash > nodeHash)
                index = right(index);
            else if(hash < nodeHash)
                index = left(index);

            if(index > entries.length - 1)
                return null;
        }
    }

    private boolean insert(K k,V v) {
        HeapTreeEntry<K,V> e = new HeapTreeEntry<>(k,v);
        int hash = e.hash;

        for(int index = 0;;) {
            HeapTreeEntry<K,V> node = entries[index];
            if(node == null) {
                entries[index] = e;
                return true;
            }
        }

    }

    static int hash(Object k) {
        return k.hashCode();
    }

    static int right(int index) {
        return (index + 1) * 2;
    }

    static int left(int index) {
        return (index + 1) * 2 - 1;
    }

    static int parent(int index) {
        return index == 0 ? -1 : (index - 1) / 2;
    }


    private synchronized void newArray() {
        final HeapTreeEntry<K, V>[] newArray = Arrays.copyOf(entries, entries.length * 2);
        entries = newArray;
    }



    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        HeapTreeEntry<K, V> entry = find(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
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
