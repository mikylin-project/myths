package cn.mikylin.myths.schedule;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

/**
 * system clock.
 *
 * @author mikylin
 * @date 20200517
 */
public final class SystemClock {

    private static volatile SystemClock CLOCK = new SystemClock();

    private AtomicLong now = new AtomicLong();
    private long last;

    private SystemClock() {

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                now.set(System.currentTimeMillis());
            }
        },1L,1L);

        long l = System.currentTimeMillis();
        now.set(l);
        last = l;
    }

    public static long now() {
        long now = CLOCK.now.get();
        long last = CLOCK.last;
        return last > now ? last : now;
    }

}
