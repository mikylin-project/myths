package cn.mikylin.myths.concurrent.sync;

import cn.mikylin.myths.common.MapUtils;
import java.util.*;

/**
 * 线程安全 全局 map 工具类
 *
 * @author mikylin
 * @date 20191105
 */
public class ThreadSafeExecutorMap<T> {

    private Map<ThreadSafeExecutor,T> map = MapUtils.newHashMap();

    private ValueFactory<T> factory;

    public ThreadSafeExecutorMap(ValueFactory<T> factory) {
        this.factory = factory;
    }

    public T get(ThreadSafeExecutor key) {
        if(null == map.get(key))
            synchronized (this) {
                if(map.get(key) == null)
                    map.put(key,factory.create());
            }

        return map.get(this);
    }

    public interface ValueFactory<T> {
        T create();
    }

}
