package cn.mikylin.utils.cache.common;

import java.util.concurrent.BlockingQueue;

/**
 * blocking object pool.
 *
 * @author mikylin
 * @date 20201204
 */
public class BlockingPool<T> implements ObjectPool<T> {

    protected BlockingQueue<T> pool;

    public BlockingPool(BlockingQueue<T> pool) {
        this.pool = pool;
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
