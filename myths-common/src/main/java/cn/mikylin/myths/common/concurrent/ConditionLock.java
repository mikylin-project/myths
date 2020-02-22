package cn.mikylin.myths.common.concurrent;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * simple util for lock and condition.
 *
 * @author mikylin
 * @date 20200222
 */
public class ConditionLock implements Condition {

    private Lock lock;
    private Condition condition;

    public ConditionLock(Lock lock) {
        this.lock = lock;
        this.condition = lock.newCondition();
    }

    public ConditionLock() {
        this(new ReentrantLock());
    }


    @Override
    public void await() throws InterruptedException {
        try {
            lock.lock();
            condition.await();
        } catch (InterruptedException e) {
            lock.unlock();
            throw e;
        }

    }

    @Override
    public void awaitUninterruptibly() {
        lock.lock();
        condition.awaitUninterruptibly();
    }

    @Override
    public long awaitNanos(long t) throws InterruptedException {
        lock.lock();
        try {
            return condition.awaitNanos(t);
        } catch (InterruptedException e) {
            lock.unlock();
            throw e;
        }

    }

    @Override
    public boolean await(long time, TimeUnit unit) throws InterruptedException {
        lock.lock();
        try {
            return condition.await(time,unit);
        } catch (InterruptedException e) {
            lock.unlock();
            throw e;
        }
    }

    @Override
    public boolean awaitUntil(Date deadline) throws InterruptedException {
        lock.lock();
        try {
            return condition.awaitUntil(deadline);
        } catch (InterruptedException e) {
            lock.unlock();
            throw e;
        }

    }

    @Override
    public void signal() {
        condition.signal();
    }

    @Override
    public void signalAll() {
        condition.signalAll();
    }
}
