package cn.mikylin.myths.concurrent.sync;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 适用于需要保障线程安全的代码部分
 *
 * 对于需要保障线程安全的代码，需要实现类写在 doExecute(...) 中，
 * 并且调用 doSafeExecute(...) 方法即可
 *
 * @author mikylin
 * @date 20190623
 */
public interface ThreadSafeExecutor<T,V> {

    ThreadSafeExecutorMap<AtomicBoolean> lockMap
            = new ThreadSafeExecutorMap<>(() -> new AtomicBoolean(true)); //true - open , false - close


    /**
     * 由使用者去实现的业务代码
     */
    V doExecute(T t);

    /**
     * 不同的安全同步策略所实现的线程安全策略代码
     */
    V safeExecutor(T t);
}
