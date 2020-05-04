package cn.mikylin.myths.common.lang;

import cn.mikylin.myths.common.Constants;
import java.util.concurrent.TimeUnit;

/**
 * time utils.
 *
 * @author mikylin
 * @date 20200205
 */
public final class TimeUtils {


    public static long currentTimeMillis(TimeUnit unit,long time) {
        switch (unit) {
            case DAYS:          return Constants.Time.DAY_MILL_SECOND * time;
            case HOURS:         return Constants.Time.HOURS_MILL_SECOND * time;
            case MINUTES:       return Constants.Time.MINUTES_MILL_SECOND * time;
            case SECONDS:       return Constants.Time.SECONDS_MILL_SECOND * time;
            case MILLISECONDS:  return time;
        }
        throw new RuntimeException("not support this time unit.");
    }


    public static long until(TimeUnit unit,long time) {
        long l = currentTimeMillis(unit,time);
        long now = System.currentTimeMillis();
        return now + l;
    }

}
