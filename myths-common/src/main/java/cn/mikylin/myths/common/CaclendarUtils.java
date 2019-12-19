package cn.mikylin.myths.common;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * caclendar utils.
 *
 * @author mikylin
 * @date 20191217
 */
public final class CaclendarUtils {

    private static TimeZone defaultZone;
    static {
        defaultZone = TimeZone.getTimeZone(Constants.Time.TIME_ZONE);
    }


    private static Calendar getCalendar() {
        return Calendar.getInstance(defaultZone);
    }

    /**
     * change the time zone.
     *
     * @param tz  new time zone
     */
    public static void setTimeZone(TimeZone tz) {
        defaultZone = tz;
    }

    /**
     * change time.
     * @param d  base date
     * @param changeType  caclendar enum type
     * @param changeNumber  change number
     * @return  new date
     */
    public static Date dayBaseChange(Date d,int changeType,int changeNumber) {
        Calendar calendar = getCalendar();
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
        Calendar start = getCalendar();
        start.set(Calendar.YEAR,year);
        start.set(Calendar.MONTH,month - 1);
        start.set(Calendar.DAY_OF_MONTH,1);
        start.set(Calendar.HOUR_OF_DAY, 0);
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
        Calendar end = getCalendar();
        end.set(Calendar.YEAR,year);
        end.set(Calendar.MONTH,month - 1);
        end.set(Calendar.DAY_OF_MONTH,end.getActualMaximum(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND, 999);
        return end.getTime();
    }

    /**
     * the end day number of the month.
     * @param year  year
     * @param month  month number
     * @return  day number,example : 30 or 31
     */
    public static int endDayThidMont(int year,int month) {
        Calendar end = getCalendar();
        end.set(Calendar.YEAR,year);
        end.set(Calendar.MONTH,month - 1);
        return end.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * the begin time of the day.
     * @param d  day time
     * @return  date 00:00:00
     */
    public static Date beginOfTheDay(Date d) {
        Calendar start = getCalendar();
        start.setTime(d);
        start.set(Calendar.HOUR_OF_DAY, 0);
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
        Calendar start = getCalendar();
        start.set(year,month - 1,day,0,0,0);
        return start.getTime();
    }

    /**
     * the end time of the day.
     * @param d  day time
     * @return  date 23:59:59
     */
    public static Date endOfTheDay(Date d) {
        Calendar end = getCalendar();
        end.setTime(d);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND, 999);
        return end.getTime();
    }

    /**
     * the end time of the day.
     * @param year  year
     * @param month  month
     * @param day  day
     * @return  date 23:59:59
     */
    public static Date endOfTheDay(int year,int month,int day) {
        Calendar end = getCalendar();
        end.set(year,month - 1,day,23,59,59);
        return end.getTime();
    }

}
