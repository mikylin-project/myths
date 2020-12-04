package cn.mikylin.utils.cache.common;

import java.util.concurrent.SynchronousQueue;

/**
 * single object pool.
 *
 * @author mikylin
 * @date 20201204
 */
public class SynchronousPool<T> extends BlockingPool<T> {

    public SynchronousPool(T t) {
        super(new SynchronousQueue<>());
        pool.add(t);
    }
}
