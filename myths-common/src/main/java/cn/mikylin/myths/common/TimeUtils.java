package cn.mikylin.myths.common;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class TimeUtils {


    public static long currentTimeMillis(TimeUnit unit,long time) {
        switch (unit) {
            case DAYS: return Constants.Time.DAY_MILL_SECOND * time;
            case HOURS: return Constants.Time.HOURS_MILL_SECOND * time;
            case MINUTES: return Constants.Time.MINUTES_MILL_SECOND * time;
            case SECONDS: return Constants.Time.SECONDS_MILL_SECOND * time;
        }
        throw new RuntimeException();
    }



}
