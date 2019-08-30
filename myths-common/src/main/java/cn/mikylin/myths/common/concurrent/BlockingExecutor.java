package cn.mikylin.myths.common.concurrent;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 使用 CAS 实现的一个阻塞线程安全开关工具
 *
 * 对于需要保障线程安全的代码，需要实现类写在 onOff0(...) 中
 * 并且调用 onOff(...) 方法即可
 * 使用 CAS 和线程阻塞相结合的方式实现了类似 Lock 的功能
 *
 * @author mikylin
 * @date 20190623
 */
public interface BlockingExecutor<T> extends ThreadSafeExecutor<T> {

    Queue<Thread> qt = new LinkedBlockingQueue<>(Runtime.getRuntime().availableProcessors() * 2);

    @Override
    default Object doSafeExecute(T t){
        AtomicBoolean casLock = getCasLock();
        for(;;){
            if(casLock.compareAndSet(true,false)){
                try {
                    Object o = doExecute(t);
                    Thread thread;
                    if(null != (thread = qt.poll()))
                        LockSupport.unpark(thread);
                    return o;
                } finally {
                    casLock.set(true);
                }
            }else{
                qt.offer(Thread.currentThread());
                LockSupport.park();
            }
        }

    }
}