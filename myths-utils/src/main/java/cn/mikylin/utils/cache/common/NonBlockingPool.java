package cn.mikylin.utils.cache.common;

import cn.mikylin.utils.cache.utils.ObjectUtils;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * non-blocking object pool.
 *
 * @author mikylin
 * @date 20201203
 */
public class NonBlockingPool<T> implements ObjectPool<T> {

    private ArrayBlockingQueue<T> pool;
    private ObjectLoader<T> loader;

    public NonBlockingPool(int size, ObjectLoader<T> loader) {
        this.pool = new ArrayBlockingQueue<>(size);
        this.loader = loader;
    }

    @Override
    public T getObject() {
        try {
            return pool.remove();
        } catch (NoSuchElementException e) {
            return loader.load();
        }
    }

    public void returnObject(T t) {
        if (t != null) {
            try {
                pool.add(t);
            } catch (IllegalStateException e) {
                ObjectUtils.close(t);
            }
        }
    }

    @Override
    public Object[] getAllObject() {
        return pool.toArray();
    }
}
