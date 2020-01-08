package cn.mikylin.myths.common.concurrent;

import cn.mikylin.myths.common.*;
import cn.mikylin.myths.common.lang.ThreadUtils;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * thread pool utils.
 *
 * @author mikylin
 * @date 20191110
 */
public final class ThreadPoolUtils {

    private static int spanTime; // span times
    private static long sleepTime; // thread sleep second
    public static void setSpanTime(int span) { spanTime = span; }
    public static void setSleepSecond(long sleepSecond) { sleepTime = sleepSecond; }

    static {
        setSpanTime(20);
        setSleepSecond(5L);
    }

    /**
     * latch for thread pool until all task is done.
     *
     * @param pool  thread pool
     * @param activeCount  active worker count
     * @param workCount  task count
     * @param spanTime  span times
     * @param sleepTime  sleep times
     */
    public static void latch(ThreadPoolExecutor pool,
                             int activeCount, int workCount,
                             int spanTime, long sleepTime) {

        Objects.requireNonNull(pool,"thread pool can not be null.");

        int i = 0;
        for ( ; !pool.isShutdown()
                && pool.getActiveCount() >= activeCount
                && pool.getQueue().size() > workCount; ) {
            if(i <= spanTime) {
                i ++;
                Thread.yield();
                continue;
            }
            ThreadUtils.sleep(sleepTime);
        }
    }

    public static void latch(ThreadPoolExecutor pool) {
        final int finalSpanTime = spanTime;
        final long finalSleepTime = sleepTime;
        latch(pool,0,0,finalSpanTime,finalSleepTime);
    }

    public static void latch(ExecutorService pool,
                             int activeCount, int workCount,
                             int spanTime, long sleepTime) {
        if(pool instanceof ThreadPoolExecutor)
            latch((ThreadPoolExecutor) pool,activeCount,workCount,spanTime,sleepTime);
        else
            throw new RuntimeException("thread pool only can be ThreadPoolExecutor");
    }

    public static void latch(ExecutorService pool) {
        final int finalSpanTime = spanTime;
        final long finalSleepTime = sleepTime;
        latch(pool,0,0,finalSpanTime,finalSleepTime);
    }

    /**
     * create thread pool.
     *
     * @return pool
     */
    public static ThreadPoolExecutor newSimplePool() {
        return newSimplePool(Constants.System.COMPUTER_CORE);
    }

    public static ThreadPoolExecutor newSimplePool(int size) {
        return new ThreadPoolExecutor (
                size, size, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000),
                new ThreadPoolExecutor.DiscardPolicy()
        );
    }
}
