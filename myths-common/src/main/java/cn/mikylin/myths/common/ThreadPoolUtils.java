package cn.mikylin.myths.common;

import cn.mikylin.myths.common.lang.*;
import cn.mikylin.myths.concurrent.PriorityThreadPoolExecutor;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * thread pool utils.
 *
 * @author mikylin
 * @date 20191110
 */
public final class ThreadPoolUtils {

    private static int defaultSpanTime; // span times
    private static long defaultSleepTime; // thread sleep second
    public static void setSpanTime(final int span) { defaultSpanTime = span < 0 ? 0 : span; }
    public static void setSleepSecond(final long sleepSecond) { defaultSleepTime = sleepSecond < 0 ? 0 : sleepSecond; }

    static {
        setSpanTime(10);
        setSleepSecond(10L);
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
    public static void latch(final ThreadPoolExecutor pool,
                             final int activeCount, final int workCount,
                             final int spanTime, final long sleepTime) {

        Objects.requireNonNull(pool,"thread pool can not be null.");

        Queue<Runnable> q = pool.getQueue();

        for (int i = 0 ;
                !pool.isShutdown()
                  && (pool.getActiveCount() > activeCount || q.size() > workCount) ;
        ) {

            if(i < spanTime) {
                i ++;
                Thread.yield();
                continue;
            }

            if(sleepTime > 0L)
                ThreadUtils.sleep(sleepTime);

            i = 0;
        }
    }

    public static void latch(final ThreadPoolExecutor pool) {
        final int finalSpanTime = defaultSpanTime;
        final long finalSleepTime = defaultSleepTime;
        latch(pool,0,0,finalSpanTime,finalSleepTime);
    }

    public static void latch(final ExecutorService pool,
                             final int activeCount, final int workCount,
                             final int spanTime, final long sleepTime) {

        if(!(pool instanceof ThreadPoolExecutor))
            throw new RuntimeException("thread pool only can be ThreadPoolExecutor");
        latch((ThreadPoolExecutor) pool,activeCount,
                workCount,spanTime,sleepTime);
    }

    public static void latch(final ExecutorService pool) {
        final int finalSpanTime = defaultSpanTime;
        final long finalSleepTime = defaultSleepTime;
        latch(pool,0,0,
                finalSpanTime,finalSleepTime);
    }

    /**
     * create thread pool.
     *
     * @return pool
     */
    public static ThreadPoolExecutor newSimplePool() {
        return newSimplePool(Constants.System.COMPUTER_CORE);
    }

    public static ThreadPoolExecutor newSimplePool(final int size) {
        return new ThreadPoolExecutor (
                size, size, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy()
        );
    }


    /**
     * create priority thread pool.
     *
     * @return pool
     */
    public static PriorityThreadPoolExecutor newPriorityPool(final int size) {
        return new PriorityThreadPoolExecutor (
                size, size, 60, TimeUnit.SECONDS,
                10000,
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy()
        );
    }

}
