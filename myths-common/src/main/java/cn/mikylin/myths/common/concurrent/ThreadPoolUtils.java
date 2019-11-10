package cn.mikylin.myths.common.concurrent;

import cn.mikylin.myths.common.ThreadUtils;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * thread pool utils
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
}
