package cn.mikylin.myths.cache.reference;

import java.lang.ref.SoftReference;

public class SoftReferenceManager<T> extends ReferenceManager<T,SoftReference<T>> {

    public SoftReferenceManager(int size){
        super(size);
    }

    @Override
    SoftReference<T> create(T t) {
        return new SoftReference<>(t);
    }



}
