package cn.mikylin.myths.common.random;

import cn.mikylin.myths.common.TimeUtils;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * time random.
 *
 * @author mikylin
 * @date 20191217
 */
public final class TimeRandom {

    /**
     * random a date between begin date and end date.
     *
     * @param begin  begin date
     * @param end  end date
     * @return  random date
     */
    public static Date random(Date begin, Date end) {
        long beginTime = begin.getTime();
        long endTime = end.getTime();
        long r = RandomUtils.nextLong(beginTime,endTime);
        return new Date(r);
    }

    public static Date randomBehind(Date base,TimeUnit unit,long min,long max) {
        long begin = base.getTime() - TimeUtils.currentTimeMillis(unit,min);
        long end = base.getTime() - TimeUtils.currentTimeMillis(unit,max);
        long r = RandomUtils.nextLong(end,begin);
        return new Date(r);
    }

    public static Date randomBehind(Date base,TimeUnit unit,long time) {
        return randomBehind(base,unit,0L,time);
    }

    public static Date randomAfter(Date base,TimeUnit unit,long min,long max) {
        long begin = base.getTime() + TimeUtils.currentTimeMillis(unit,min);
        long end = base.getTime() + TimeUtils.currentTimeMillis(unit,max);
        long r = RandomUtils.nextLong(begin,end);
        return new Date(r);
    }

    public static Date randomAfter(Date base,TimeUnit unit,long time) {
        return randomAfter(base,unit,0L,time);
    }
}
