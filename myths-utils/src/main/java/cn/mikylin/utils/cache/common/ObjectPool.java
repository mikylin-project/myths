package cn.mikylin.utils.cache.common;

import cn.mikylin.myths.common.lang.ObjectUtils;

import java.io.Closeable;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ObjectPool<T> {

    private BlockingQueue<T> pool;
    private ObjectLoader<T> loader;

    public ObjectPool(int size,ObjectLoader<T> loader) {
        this.pool = new ArrayBlockingQueue<>(size);
        this.loader = loader;
    }


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



    public interface ObjectLoader<T> {
        T load();
    }
}
