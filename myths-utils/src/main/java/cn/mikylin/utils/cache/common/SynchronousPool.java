package cn.mikylin.utils.cache.common;

import java.util.concurrent.SynchronousQueue;

/**
 * single object pool.
 *
 * @author mikylin
 * @date 20201203
 */
public class SynchronousPool<T> implements ObjectPool<T> {

    private SynchronousQueue<T> pool;

    public SynchronousPool(T t) {
        this.pool = new SynchronousQueue<>();
        pool.add(t);
    }


    @Override
    public T getObject() {
        return pool.poll();
    }

    @Override
    public void returnObject(T t) {
        if (t != null) {
            pool.add(t);
        }
    }


    @Override
    public Object[] getAllObject() {
        return pool.toArray();
    }

}
