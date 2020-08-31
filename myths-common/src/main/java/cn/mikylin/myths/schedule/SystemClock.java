package cn.mikylin.myths.schedule;

import java.util.Timer;
import java.util.TimerTask;

/**
 * system clock.
 *
 * @author mikylin
 * @date 20200517
 */
public final class SystemClock {

    private volatile long now = 0L;

    private SystemClock() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                now = System.currentTimeMillis();
            }
        };

        task.run();

        Timer t = new Timer();
        t.schedule(task,1L,1L);
    }


    private static volatile SystemClock CLOCK = null;

    public static long now() {
        if(CLOCK == null) {
            synchronized (SystemClock.class) {
                if(CLOCK == null)
                    CLOCK = new SystemClock();
            }
        }
        return CLOCK.now;
    }

}
