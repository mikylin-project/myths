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
public class TokenLimiter {

    private long maxToken; // token quantity.
    private long rate; // token create rate / per second.


    // condition queue use to await the thread.
    private Condition con = new ConditionLock();

    public TokenLimiter(long tokens, long rate) {
        this.rate = rate;
        this.maxToken = tokens;
        lastTime(System.currentTimeMillis()); // now.
        token(tokens); // create the limit number of rate once.
    }

    /**
     * quick to create a limiter.
     * @param rate  qps which want to limit.
     * @return limiter.
     */
    public static TokenLimiter create(long rate) {
        return new TokenLimiter(rate,rate);
    }

    /**
     * try to acquire the rate in container.
     *
     * @param i  try to get rates.
     * @return true -- get success ; false -- get fail.
     */
    public boolean tryAcquire(long i) {
        if(i <= 0L || i > maxToken)
            throw new RuntimeException();
        rate();
        long t = token();
        return t >= i && casToken(t,t - i);
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
        long t = System.currentTimeMillis() - lastTime();
        if(t < 1000L || !tryAcquire(i)) {
            try {
                con.await(t % 1000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException("thread intterupted.");
            }
        }
    }


    /**
     * create the rate token.
     */
    private void rate() {
        long now = System.currentTimeMillis();
        if(now - lastTime() < 1000L)
            return;

        long lastTime = getAndSetTime(now);

        long time = now - lastTime;
        long addNumber = time / 1000L * rate;
        for(;;) {
            long t = token();
            long newToken = t + addNumber;
            if(newToken > maxToken) {
                newToken = maxToken;
            }
            if(casToken(t,newToken))
                break;
        }
        con.signalAll();
    }


    /**
     * the code use in jdk9 +.
     * recommend this code.
     */
    private long lastTime; // the last time to get the rate.
    private long tokens; // how much token the container have.
    private static VarHandle LAST_TIME;
    private static VarHandle TOKEN;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            LAST_TIME = lookup.findVarHandle(TokenLimiter.class,"lastTime",long.class);
            TOKEN = lookup.findVarHandle(TokenLimiter.class,"tokens",long.class);
        } catch (Exception e) {
            throw new RuntimeException("var handle create fail.");
        }
    }

    private boolean casToken(long oldNum,long newNum) {
        return TOKEN.compareAndSet(this,oldNum,newNum);
    }

    private long getAndSetTime(long newTime) {
        return (long)LAST_TIME.getAndSet(this,newTime);
    }

    private long token() {
        return tokens;
    }

    private void token(long t) {
        tokens = t;
    }

    private long lastTime() {
        return lastTime;
    }

    private void lastTime(long t) {
        lastTime = t;
    }


    /**
     * the code use in all java version
     *
     * needs to add import:
     * import java.util.concurrent.atomic.AtomicLong;
     */
//    private AtomicLong lastTime = new AtomicLong(0L); // the last time to get the rate.
//    private AtomicLong tokens = new AtomicLong(0L); // how much token the container have.
//
//    private boolean casToken(long oldNum,long newNum) {
//        return tokens.compareAndSet(oldNum,newNum);
//    }
//
//    private long getAndSetTime(long newTime) {
//        return lastTime.getAndSet(newTime);
//    }
//
//    private long token() {
//        return tokens.get();
//    }
//
//    private void token(long t) {
//        tokens.set(t);
//    }
//
//    private long lastTime() {
//        return lastTime.get();
//    }
//
//    private void lastTime(long t) {
//        lastTime.set(t);
//    }
}
