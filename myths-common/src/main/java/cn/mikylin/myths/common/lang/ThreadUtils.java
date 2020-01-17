package cn.mikylin.myths.common.lang;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

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

    public static long start(Runnable r,boolean isDaemon) {
        Thread t = isDaemon ? createDaemon(r) : create(r);
        t.start();
        return t.getId();
    }

    public static long start(Runnable r) {
        return start(r,false);
    }

    /**
     * create thread.
     */
    public static Thread create(ThreadFactory factory,Runnable r) {
        try{
            return factory.newThread(r);
        } catch (Exception e) {
            throw new RuntimeException("thread create failed.");
        }
    }

    /**
     * create thread.
     */
    public static Thread create(Runnable r) {
        return create(DEFAULT,r);
    }

    /**
     * create daemon thread.
     */
    public static Thread createDaemon(Runnable r) {
        return create(DEFAULT_DAEMON,r);
    }

    /**
     * default thread factory.
     */
    private static final ThreadFactory DEFAULT = new ThreadFactory() {

        private AtomicLong al = new AtomicLong(0L);
        private final String abstractThreadName
                = "Thread Utils Default Thread Factory - ";

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(false);
            t.setName(abstractThreadName + al.incrementAndGet());
            return t;
        }
    };

    /**
     * default daemon thread factory.
     */
    private static final ThreadFactory DEFAULT_DAEMON = new ThreadFactory() {

        private AtomicLong al = new AtomicLong(0L);
        private final String abstractThreadName
                = "Thread Utils Default Daemon Thread Factory - ";

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName(abstractThreadName + al.incrementAndGet());
            return t;
        }
    };


}
