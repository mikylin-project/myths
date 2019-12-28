package cn.mikylin.myths.task;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class StopWatcher {

    private Instant watcher;
    private AtomicReference<Instant> begin;



    private StopWatcher() {
        watcher = Instant.EPOCH;
        begin = new AtomicReference<>();
        begin.set(Instant.EPOCH);
    }

    public static StopWatcher create() {
        return new StopWatcher();
    }

    public static StopWatcher start() {
        return create().begin();
    }

    public StopWatcher begin() {
        if(begin.compareAndSet(Instant.EPOCH,Instant.now()))
            return this;
        throw new RuntimeException();
    }

    public StopWatcher stop() {
        Instant b = begin.getAndSet(Instant.EPOCH);
        if(b != Instant.EPOCH) {
            Instant now = Instant.now();
            Duration between = Duration.between(b,now);
            add(between);
            return this;
        }
        throw new RuntimeException();
    }

    public long second() {
        return get().getLong(ChronoField.INSTANT_SECONDS);
    }

    public int nano() {
        return get().getNano();
    }

    public void clean() {
        synchronized (this) {
            watcher = Instant.EPOCH;
        }
    }


    private void add(Duration d) {
        synchronized (this) {
            watcher = watcher.plus(d);
        }
    }

    private Instant get() {
        synchronized (this) {
            return watcher;
        }
    }



    public static void main(String[] args) throws InterruptedException {
        StopWatcher w = StopWatcher.start();
        TimeUnit.SECONDS.sleep(5L);
        w.stop();
        System.out.println(w.second());
    }

}
