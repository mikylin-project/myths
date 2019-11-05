package cn.mikylin.myths.common.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 开关工具
 * 适用于需要保障线程安全的代码部分
 *
 * @author mikylin
 * @date 20190623
 */
public interface ThreadSafeExecutor<T> {

    ThreadSafeExecutorMap<AtomicBoolean> lockMap
            = new ThreadSafeExecutorMap<>(() -> new AtomicBoolean(true)); //true - open , false - close


    /**
     * 由使用者去实现的业务代码
     */
    T doExecute(T t);

    /**
     * 不同的 on-off 策略所实现的线程安全策略代码
     */
    T doSafeExecute(T t);
}
