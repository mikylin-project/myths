package cn.mikylin.myths.common.concurrent;

import java.util.*;

public class ThreadSafeExecutorMap<T> {

    private Map<ThreadSafeExecutor,T> map = new HashMap<>();

    private ValueFactory<T> factory;

    public ThreadSafeExecutorMap(ValueFactory<T> factory) {
        this.factory = factory;
    }

    public T get(ThreadSafeExecutor key) {
        if(null == map.get(key)) {
            synchronized (this) {
                if(null == map.get(key)) {
                    map.put(key,factory.create());
                }
            }
        }
        return map.get(this);
    }

    interface ValueFactory<T> {
        T create();
    }

}
