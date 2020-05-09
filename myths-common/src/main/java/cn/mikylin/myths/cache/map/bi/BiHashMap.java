package cn.mikylin.myths.cache.map.bi;

import java.util.*;

/**
 * bi map.
 *
 * @author mikylin
 * @date 20190803
 */
public final class BiHashMap<K,V> extends AbstractBiMap<K,V> {

    public BiHashMap(int cap) {
        super(cap);
    }

    public BiHashMap() {
        this(16);
    }
}
