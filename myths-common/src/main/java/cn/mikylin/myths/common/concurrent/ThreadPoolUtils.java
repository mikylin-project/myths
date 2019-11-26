package cn.mikylin.myths.common.concurrent;

import cn.mikylin.myths.common.*;
import java.util.Objects;
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

    public static void latch(ThreadPoolExecutor pool) {

        Objects.requireNonNull(pool,"thread pool can not be null.");

        int i = 0;
        for ( ; !pool.isShutdown() && pool.getActiveCount() > 0 ; ) {
            if(i <= 50) {
                i ++;
                Thread.yield();
                continue;
            }
            ThreadUtils.sleep(1);
        }
    }

    public static ThreadPoolExecutor newSafePool() {
        return new ThreadPoolExecutor(Constants.System.COMPUTER_CORE,
                Constants.System.COMPUTER_CORE,
                60,
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.DiscardPolicy());
    }
}
