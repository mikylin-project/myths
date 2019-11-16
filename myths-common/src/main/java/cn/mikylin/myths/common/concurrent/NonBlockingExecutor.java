package cn.mikylin.myths.common.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 使用 CAS 实现的非阻塞式线程安全工具
 *
 * @author mikylin
 * @date 20190623
 */
public interface NonBlockingExecutor<T> extends ThreadSafeExecutor<T> {

    @Override
    default T doSafeExecute(T t){
        AtomicBoolean casLock = lockMap.get(this);
        for(;;) {
            if(casLock.compareAndSet(true,false))
                try {
                    return doExecute(t);
                } finally {
                    casLock.set(true);
                }
        }
    }
}

