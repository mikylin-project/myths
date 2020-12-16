package cn.mikylin.utils.cache.common;

import cn.mikylin.utils.cache.utils.ObjectUtils;

/**
 * object pool.
 *
 * @author mikylin
 * @date 20201203
 */
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

}
