package cn.mikylin.utils.cache.common;

import cn.mikylin.myths.common.lang.ObjectUtils;

public interface ObjectPool<T> extends AutoCloseable {

    T getObject();

    void returnObject(T t);

    Object[] getAllObject();


    default void close() {
        Object[] allObject = getAllObject();
        for (Object o : allObject) {
            ObjectUtils.close(o);
        }
    }



    interface ObjectLoader<T> {
        T load();
    }
}
