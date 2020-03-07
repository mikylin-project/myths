package cn.mikylin.myths.concurrent.sync;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 使用 CAS 实现的一个阻塞式线程安全队列工具
 *
 * @author mikylin
 * @date 20190623
 */
public interface BlockingExecutor<T,V> extends ThreadSafeExecutor<T,V> {

    ThreadSafeExecutorMap<Queue<Thread>> qtMap
            = new ThreadSafeExecutorMap<>(() -> new LinkedBlockingQueue<>());

    @Override
    default V safeExecutor(T t) {
        AtomicBoolean casLock = lockMap.get(this);
        for(;;) {
            if(casLock.compareAndSet(true,false)) {
                try {
                    V o = doExecute(t);
                    Thread thread;
                    if(null != (thread = qtMap.get(this).poll()))
                        LockSupport.unpark(thread);
                    return o;
                } finally {
                    casLock.set(true);
                }
            } else {
                qtMap.get(this).offer(Thread.currentThread());
                LockSupport.park();
            }
        }

    }
}
