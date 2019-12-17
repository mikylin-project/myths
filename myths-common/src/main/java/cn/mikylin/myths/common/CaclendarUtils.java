package cn.mikylin.myths.common;

import java.util.Calendar;
import java.util.Date;

/**
 * caclendar utils.
 *
 * @author mikylin
 * @date 20191217
 */
public final class CaclendarUtils {

    /**
     * change time.
     * @param d  base date
     * @param changeType  caclendar enum type
     * @param changeNumber  change number
     * @return  new date
     */
    public static Date dayBaseChange(Date d,int changeType,int changeNumber) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(changeType,calendar.get(changeType) + changeNumber);
        return calendar.getTime();
    }


    /**
     * the begin time of the month.
     * @param year  year
     * @param month  month number
     * @return  date 00:00:00
     */
    public static Date beginOfTheMonth(int year, int month) {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR,year);
        start.set(Calendar.MONTH,month);
        start.set(Calendar.DAY_OF_MONTH,1);
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.getTime();
    }

    /**
     * the end time of the month.
     * @param year  year
     * @param month  month number
     * @return  date 23:59:59
     */
    public static Date endOfTheMonth(int year,int month) {
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR,year);
        end.set(Calendar.MONTH,month);
        end.set(Calendar.DAY_OF_MONTH,end.getActualMaximum(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR,23);
        end.set(Calendar.MINUTE,59);
        end.set(Calendar.SECOND,59);
        end.set(Calendar.MILLISECOND,999);
        return end.getTime();
    }

    /**
     * the begin time of the day.
     * @param d  day time
     * @return  date 00:00:00
     */
    public static Date beginOfTheDay(Date d) {
        Calendar start = Calendar.getInstance();
        start.setTime(d);
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.getTime();
    }

    /**
     * the begin time of the day.
     * @param year  year
     * @param month  month
     * @param day  day
     * @return  date 00:00:00
     */
    public static Date beginOfTheDay(int year,int month,int day) {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR,year);
        start.set(Calendar.MONTH,month);
        start.set(Calendar.DAY_OF_MONTH,day);
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.getTime();
    }

    /**
     * the end time of the day.
     * @param d  day time
     * @return  date 23:59:59
     */
    public static Date endOfTheDay(Date d) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(d);
        todayStart.set(Calendar.HOUR, 23);
        todayStart.set(Calendar.MINUTE, 59);
        todayStart.set(Calendar.SECOND, 59);
        todayStart.set(Calendar.MILLISECOND, 999);
        return todayStart.getTime();
    }

    /**
     * the end time of the day.
     * @param year  year
     * @param month  month
     * @param day  day
     * @return  date 23:59:59
     */
    public static Date endOfTheDay(int year,int month,int day) {
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR,year);
        end.set(Calendar.MONTH,month);
        end.set(Calendar.DAY_OF_MONTH,day);
        end.set(Calendar.HOUR, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND,999);
        return end.getTime();
    }
}
