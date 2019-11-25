package cn.mikylin.myths.common.concurrent;

import cn.mikylin.myths.common.Constants;
import cn.mikylin.myths.common.ThreadUtils;

import java.security.PublicKey;
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
        int i = 0;
        for (;!pool.isShutdown() && pool.getTaskCount() > 0;) {
            if(i <= 50) {
                Thread.yield();
                i ++;
            } else {
                ThreadUtils.sleep(1);
            }
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
