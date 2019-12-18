package cn.mikylin.myths.cache.map.tree;

import cn.mikylin.myths.cache.map.CacheEntry;
import cn.mikylin.myths.cache.map.CacheEntryFactory;

import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class TreeAbstractMap<K,V> implements Map<K,V> {

    /**
     * 根节点
     */
    Node<K,V> root;

    /**
     * size
     */
    int size;

    /**
     * entry factory
     */
    CacheEntryFactory factory = (k,v) -> new TreeEntry(k,v) ;


    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 节点对象
     */
    static final class Node<K,V> {
        Node<K,V> parent;
        Node<K,V> leftChild; // 左节点，比当前节点值小
        Node<K,V> rightChild; // 右节点，比当前节点值大
        K key;
        int keyHash;
        V value;

        Node(K key,int keyHash,V value) {
            this.key = key;
            this.keyHash = keyHash;
            this.value = value;
        }
    }

    /**
     * entry
     */
    static class TreeEntry<K,V> extends CacheEntry<K,V> {

        private K k;
        private V v;

        private TreeEntry(K k,V v) {
            super(k,v,0);
        }

        protected static <K,V> CacheEntry<K,V> create(K k,V v) {
            return new TreeEntry<>(k,v);
        }

    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }


    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach((k,v) -> put(k,v));
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }



    /**
     * 获取 key 的 hash 值
     */
    protected int getKeyHash(K key) {
        if(key instanceof Number)
            return ((Number) key).intValue();
        else if(key instanceof String)
            return String.valueOf(key).hashCode();
        return key.hashCode();
    }
}
