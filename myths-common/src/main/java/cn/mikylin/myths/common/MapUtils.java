package cn.mikylin.myths.common;

import java.util.*;
import java.util.concurrent.*;

/**
 * map utils.
 *
 * @author mikylin
 * @date 20190621
 */
public final class MapUtils {

    /**
     * check map is empty
     * empty - true
     * not empty - false
     */
    public static <K,V> boolean isBlank(Map<K,V> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断 map 是否不为空
     * 是空的 - false
     * 不是空的 - true
     */
    public static <K,V> boolean isNotBlank(Map<K,V> map){
        return !isBlank(map);
    }

    /**
     * create a HashMap with default size
     */
    public static <K,V> Map<K,V> newHashMap(){
        return newHashMap(8);
    }

    /**
     * create a HashMap with size
     */
    public static <K,V> Map<K,V> newHashMap(int size) {
        return new HashMap<>(size);
    }

    /**
     * create a ConcurrentMap with default size
     */
    public static <K,V> ConcurrentMap<K,V> newConcurrentMap() {
        return newConcurrentMap(8);
    }

    /**
     * create a ConcurrentMap with size
     */
    public static <K,V> ConcurrentMap<K,V> newConcurrentMap(int size) {
        return new ConcurrentHashMap<>(size);
    }

    /**
     * create concurrent hashmap quick
     */
    public static <K,V> ConcurrentHashMap<K,V> newConcurrentMap(Object[]... kvs) {
        return newConcurrentMap(kvs == null || kvs.length == 0 ? 16 : kvs.length,kvs);
    }

    public static <K,V> ConcurrentHashMap<K,V> newConcurrentMap(int size,Object[]... kvs) {
        return (ConcurrentHashMap<K,V>)
                create ((MapFactory) ConcurrentHashMap::new,size,kvs);
    }


    /**
     * create hashmap quick
     */
    public static <K,V> HashMap<K,V> newHashMap(Object[]... kvs) {
        return newHashMap(kvs == null || kvs.length == 0 ? 16 : kvs.length,kvs);
    }


    public static <K,V> HashMap<K,V> newHashMap(int size,Object[]... kvs) {
        return (HashMap<K,V>)
                create((MapFactory) HashMap::new,size,kvs);
    }

    private static <K,V> Map<K,V> create(MapFactory factory, int size,Object[]... kvs) {

        Map<K,V> map = factory.getMap(size);

        if(kvs != null) {
            for(Object[] kv : kvs) {

                if(kv.length != 2)
                    throw new RuntimeException("param length not be 2");

                K key;
                V value;
                try {
                    key = (K) kv[0];
                    value = (V) kv[1];
                } catch (Exception e) {
                    throw new RuntimeException("param type trans exception");
                }

                if(key == null)
                    throw new RuntimeException("param key can not be null");

                map.put(key,value);
            }
        }

        return map;
    }


    private interface MapFactory {
        <K,V> Map<K,V> getMap(int size);
    }
}
