package cn.mikylin.myths.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class SpanLock implements Lock {

    private volatile Thread ownerThread;

    private AtomicInteger status = new AtomicInteger(0);

    private volatile boolean beforCheck = true;

    @Override
    public void lock() {
        for ( ; tryLock() ; )
            break;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        for ( ; tryLock() ; ) {
            if (Thread.interrupted())
                throw new InterruptedException();
        }

        if (Thread.interrupted()) {
            status.set(0);
            ownerThread = null;
            throw new InterruptedException();
        }
    }

    @Override
    public boolean tryLock() {
        Thread t = Thread.currentThread();

        if (beforCheck
                && status.compareAndSet(0,1)) {
            beforCheck = false;
            ownerThread = t;
            return true;
        }

        if (t == ownerThread) {
            status.addAndGet(1);
            return true;
        }

        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return tryLock();
    }

    @Override
    public void unlock() {
        if (Thread.currentThread() != ownerThread)
            throw new IllegalMonitorStateException();
        if (status.decrementAndGet() == 0) {
            ownerThread = null;
            beforCheck = false;
        }
    }

    // todo
    @Override
    public Condition newCondition() {
        return null;
    }
}
