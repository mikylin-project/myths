package cn.mikylin.utils.cache.common;

import java.util.concurrent.SynchronousQueue;

public class SynchronousQueuePool<T> implements ObjectPool<T> {

    private SynchronousQueue<T> pool;

    public SynchronousQueuePool() {
        this.pool = new SynchronousQueue<>();
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
