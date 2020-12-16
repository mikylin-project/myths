package cn.mikylin.utils.cache.common;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * single object pool.
 *
 * @author mikylin
 * @date 20201204
 */
public class OnePool<T> extends BlockingPool<T> {

    public OnePool(T t) {
        super(new ArrayBlockingQueue<>(1));
        pool.add(t);
    }
}
