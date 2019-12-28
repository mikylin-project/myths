package cn.mikylin.myths.common;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public final class TimeUtils {


    public static long currentTimeMillis(TimeUnit unit,long time) {
        switch (unit) {
            case DAYS: return Constants.Time.DAY_MILL_SECOND * time;
            case HOURS: return Constants.Time.HOURS_MILL_SECOND * time;
            case MINUTES: return Constants.Time.MINUTES_MILL_SECOND * time;
            case SECONDS: return Constants.Time.SECONDS_MILL_SECOND * time;
            case MILLISECONDS: return time;
        }
        throw new RuntimeException();
    }


    public static long until(TimeUnit unit,long time) {
        Instant.now().plusMillis(currentTimeMillis(unit, time));
        long l = currentTimeMillis(unit, time);
        long now = System.currentTimeMillis();
        System.out.println(currentTimeMillis(unit,time) + now);
        return Instant.ofEpochMilli(l).toEpochMilli();
    }


    public static void main(String[] args) {
        System.out.println(until(TimeUnit.MINUTES,3L));
    }




}
