package cn.mikylin.myths.common.concurrent;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * rate limit token util.
 *
 * @author mikylin
 * @date 20200222
 */
public class RateLimiter {

    private long maxLimitTime; // rate limit time.
    private long rateLimitNumber; // rate limit number.
    private long perSecond; // rate create number per second.

    // lock and condition use to await the
    private Condition con = new ConditionLock();

    public RateLimiter(long rateLimit,long rateSpeed) {
        this.perSecond = rateSpeed;
        this.rateLimitNumber = rateLimit;
        this.lastTime = System.currentTimeMillis();
        this.rateNumber = rateLimitNumber;

        if(rateLimit % rateSpeed == 0)
            this.maxLimitTime = (rateLimit / rateSpeed) * 1000;
        else
            this.maxLimitTime = (rateLimit / rateSpeed + 1) * 1000;
    }


    /**
     * try to acquire the rate in container.
     *
     * @param i  try to get rates.
     * @return true -- get success ; false -- get fail.
     */
    public boolean tryAcquire(long i) {
        if(i <= 0L || i > rateLimitNumber)
            throw new RuntimeException();
        rate();
        long rn = rateNumber;
        return rn >= i && RATE_NUMBER.compareAndSet(this,rn,rn - i);
    }

    /**
     * try to acquire the rate in container.
     */
    public boolean tryAcquire() {
        return tryAcquire(1);
    }

    /**
     * try to acquire the rate in container.
     * if fail,park the thread.
     *
     * @param i  try to get rates.
     */
    public void acquire(long i) {
        for(;!tryAcquire(i);)
            await(i);
    }

    /**
     * acquire the rate in container.
     */
    public void acquire() {
        acquire(1);
    }

    /**
     * await the thread if can not get the rate in container.
     */
    private void await(long i) {
        long t = System.currentTimeMillis() - lastTime;
        if(t < 1000L || !tryAcquire(i)) {
            try {
                con.await(t % 1000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException("thread intterupted.");
            }
        }
    }




    private long lastTime; // the last time to get the rate.
    private long rateNumber; // rate number in this time.
    private static VarHandle LAST_TIME;
    private static VarHandle RATE_NUMBER;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            LAST_TIME = lookup.findVarHandle(RateLimiter.class,"lastTime",long.class);
            RATE_NUMBER = lookup.findVarHandle(RateLimiter.class,"rateNumber",long.class);
        } catch (Exception e) {
            throw new RuntimeException("var handle create fail.");
        }
    }

    /**
     * create the wait.
     */
    private void rate() {
        long now = System.currentTimeMillis();
        if(now - lastTime < 1000L)
            return;

        long lastTime = (long)LAST_TIME.getAndSet(this,now);

        long time = now - lastTime;
        if(time >= maxLimitTime && rateNumber != rateLimitNumber) {
            RATE_NUMBER.set(this,rateLimitNumber);
        } else {
            long addNumber = time / 1000 * perSecond;
            for(;!RATE_NUMBER.compareAndSet(this,rateNumber,rateNumber + addNumber);) { }
        }
        con.signalAll();
    }
}
