package cn.mikylin.myths.common.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 使用 CAS 实现的一个非阻塞式线程安全开关工具
 *
 * 对于需要保障线程安全的代码，需要实现类写在 doExecute(...) 中
 * 并且调用 onOff(...) 方法即可
 * 使用 yield() 方法降低延迟，但是性能要求较高
 *
 * @author mikylin
 * @date 20190623
 */
public interface NonBlockingExecutor<T> extends ThreadSafeExecutor<T> {

    @Override
    default T doSafeExecute(T t){
        AtomicBoolean casLock = lockMap.get(this);
        for(;;) {
            if(casLock.compareAndSet(true,false)) {
                try {
                    return doExecute(t);
                } finally {
                    casLock.set(true);
                }
            }
        }
    }
}

