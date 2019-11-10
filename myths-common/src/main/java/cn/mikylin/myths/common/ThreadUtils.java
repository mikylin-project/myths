package cn.mikylin.myths.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * thread utils
 *
 * @author mikylin
 * @date 20190926
 */
public final class ThreadUtils {

    /**
     * thread sleep for minutes.
     */
    public static void sleepMinutes(long time) {
        sleep(TimeUnit.MINUTES,time);
    }

    /**
     * thread sleep for second.
     */
    public static void sleepSecond(long time) {
        sleep(TimeUnit.SECONDS,time);
    }

    /**
     * thread sleep for second.
     */
    public static void sleep(long time) {
        sleepSecond(time);
    }

    /**
     * thread sleep.
     */
    public static void sleep(TimeUnit unit,long time) {
        try {
            unit.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException("thread sleep failed");
        }
    }

    /**
     * create thread.
     */
    public static Thread create(ThreadFactory factory,Runnable r) {
        try{
            return factory.newThread(r);
        } catch (Exception e){
            throw new RuntimeException("thread create failed");
        }
    }

    /**
     * create thread.
     */
    public static Thread create(Runnable r) {
        return create(DEFAULT,r);
    }

    /**
     * default thread factory.
     */
    private static final ThreadFactory DEFAULT = r -> new Thread(r);

}
