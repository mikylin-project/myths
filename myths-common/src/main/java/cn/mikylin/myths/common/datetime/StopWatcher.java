package cn.mikylin.myths.common.datetime;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.concurrent.atomic.AtomicReference;

/**
 * stop watch
 *
 * @author mikylin
 * @date 20200114
 */
public class StopWatcher {

    private Instant watcher;
    private AtomicReference<Instant> begin;

    private StopWatcher() {
        init();
    }

    private void init() {
        watcher = Instant.EPOCH;
        begin = new AtomicReference<>();
        begin.set(Instant.EPOCH);
    }

    public static StopWatcher create() {
        return new StopWatcher();
    }

    public StopWatcher begin() {
        if(begin.compareAndSet(Instant.EPOCH,Instant.now()))
            return this;
        throw new RuntimeException();
    }

    /**
     * create and begin the stop watch.
     * @return  stop watch
     */
    public static StopWatcher start() {
        return create().begin();
    }

    /**
     * stop the watch.
     * @return  stop watch
     */
    public StopWatcher stop() {
        Instant b = begin.getAndSet(Instant.EPOCH);
        if(b != Instant.EPOCH) {
            Instant now = Instant.now();
            Duration between = Duration.between(b,now);
            synchronized (this) {
                watcher = watcher.plus(between);
            }
            return this;
        }
        throw new RuntimeException();
    }

    private Instant get() {
        synchronized (this) {
            return watcher;
        }
    }


    public long second() {
        return get().getLong(ChronoField.INSTANT_SECONDS);
    }

    public int nano() {
        return get().getNano();
    }

    public long milliSecond() {
        return get().getLong(ChronoField.MILLI_OF_SECOND);
    }

    public long min() {
        return second() / 60;
    }

    public void clean() {
        synchronized (this) {
            init();
        }
    }

}
