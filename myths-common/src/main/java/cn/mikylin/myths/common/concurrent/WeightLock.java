package cn.mikylin.myths.common.concurrent;

import java.io.Serializable;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class WeightLock implements Lock, Serializable {

    @Override
    public void lock() {

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }


    public static class WeightThread extends Thread {
        private Thread t;
        private int weight;

        public WeightThread(Thread t,int weight){
            this.t = t;
            this.weight = weight;
        }

        public WeightThread(Thread t){
            this(t,0);
        }
    }

    public static class WeightThreadFactory implements ThreadFactory {



        @Override
        public Thread newThread(Runnable r) {
            return null;
        }
    }
}
