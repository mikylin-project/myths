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
        setSpanTime(50);
        setSleepSecond(1L);
    }

    /**
     * latch for thread pool until all task is done.
     *
     * @param pool  thread pool
     */
    public static void latch(ThreadPoolExecutor pool) {

        Objects.requireNonNull(pool,"thread pool can not be null.");

        final int finalSpanTime = spanTime;
        final long finalSleepTime = sleepTime;

        int i = 0;
        for ( ; !pool.isShutdown() && pool.getActiveCount() > 0 ; ) {
            if(i <= finalSpanTime) {
                i ++;
                Thread.yield();
                continue;
            }
            ThreadUtils.sleep(finalSleepTime);
        }
    }

    public static void latch(ExecutorService pool) {
        if(pool instanceof ThreadPoolExecutor)
            latch((ThreadPoolExecutor) pool);
        else
            throw new RuntimeException("thread pool only can be ThreadPoolExecutor");
    }

    /**
     * create thread pool.
     *
     * @return pool
     */
    public static ThreadPoolExecutor newSafePool() {
        return new ThreadPoolExecutor (
                            Constants.System.COMPUTER_CORE,
                            Constants.System.COMPUTER_CORE,
                            60,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(),
                            new ThreadPoolExecutor.DiscardPolicy()
                        );
    }
}
