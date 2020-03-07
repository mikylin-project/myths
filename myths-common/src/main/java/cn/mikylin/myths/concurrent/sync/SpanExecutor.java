package cn.mikylin.myths.concurrent.sync;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 使用 CAS 实现的非阻塞式线程安全工具
 *
 * @author mikylin
 * @date 20190623
 */
public interface SpanExecutor<T,V> extends ThreadSafeExecutor<T,V> {

    @Override
    default V safeExecutor(T t) {
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

