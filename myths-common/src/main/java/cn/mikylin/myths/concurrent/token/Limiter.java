package cn.mikylin.myths.concurrent.token;

/**
 * limiter
 *
 * @author mikylin
 * @date 20200430
 */
public interface Limiter {

    // 1s = 1000ms
    long ONE_SECOND = 1000L;

    boolean tryAcquire(long i);
    void acquire(long i);
    long maxLimit();

    /**
     * quick to create a token limiter.
     * @param rate  qps which want to limit.
     * @return limiter.
     */
    static Limiter tokenLimiter(long rate) {
        return new TokenLimiter(rate,rate);
    }
}
