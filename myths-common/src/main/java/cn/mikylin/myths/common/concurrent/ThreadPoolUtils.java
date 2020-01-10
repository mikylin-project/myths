package cn.mikylin.myths.common.concurrent;

import cn.mikylin.myths.common.*;
import cn.mikylin.myths.common.lang.ThreadUtils;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.*;

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
    public static void latch(final ThreadPoolExecutor pool,
                             final int activeCount, final int workCount,
                             final int spanTime, final long sleepTime) {

        Objects.requireNonNull(pool,"thread pool can not be null.");
        Queue<Runnable> q = pool.getQueue();
        int i = 0;
        for ( ; !pool.isShutdown()
                && pool.getActiveCount() > activeCount
                && q.size() > workCount; ) {
            if(i <= spanTime) {
                i ++;
                Thread.yield();
                continue;
            }
            ThreadUtils.sleep(sleepTime);
        }
    }

    public static void latch(final ThreadPoolExecutor pool) {
        final int finalSpanTime = spanTime;
        final long finalSleepTime = sleepTime;
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
        final int finalSpanTime = spanTime;
        final long finalSleepTime = sleepTime;
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
                new PriorityRunnableBlockingQueue(10000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy()
        );
    }


    public static void main(String[] args) {
        PriorityQueue<String> p = new PriorityQueue<>();
        p.add("haha");
    }
}
