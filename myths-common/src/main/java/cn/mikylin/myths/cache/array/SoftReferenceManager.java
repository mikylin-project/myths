package cn.mikylin.myths.cache.array;

import java.lang.ref.SoftReference;

/**
 * java soft reference manager.
 *
 * @author mikylin
 * @date 20190729
 */
public final class SoftReferenceManager<T> extends ReferenceManager<T,SoftReference<T>> {

    public SoftReferenceManager(int size){
        super(size);
    }

    @Override
    SoftReference<T> create(T t) {
        return new SoftReference<>(t);
    }


}
