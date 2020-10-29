package cn.mikylin.myths.concurrent;

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
public final class SimpleCondition implements Condition {

    private Lock lock;
    private Condition condition;

    public SimpleCondition(Lock lock) {
        this.lock = lock;
        this.condition = lock.newCondition();
    }

    public SimpleCondition() {
        this(new ReentrantLock());
    }


    @Override
    public void await() throws InterruptedException {
        lock.lock();
        try {
            condition.await();
        } finally {
            lock.unlock();
        }

    }

    @Override
    public void awaitUninterruptibly() {
        lock.lock();
        try {
            condition.awaitUninterruptibly();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long awaitNanos(long t) throws InterruptedException {
        lock.lock();
        try {
            return condition.awaitNanos(t);
        } finally {
            lock.unlock();
        }

    }

    @Override
    public boolean await(long time, TimeUnit unit) throws InterruptedException {
        lock.lock();
        try {
            return condition.await(time,unit);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean awaitUntil(Date deadline) throws InterruptedException {
        lock.lock();
        try {
            return condition.awaitUntil(deadline);
        } finally {
            lock.unlock();
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
