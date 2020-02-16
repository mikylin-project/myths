package cn.mikylin.myths.common.datetime;

import cn.mikylin.myths.common.Constants;
import java.util.*;

/**
 * calendar utils.
 *
 * @author mikylin
 * @date 20191217
 */
public final class CalendarUtils {

    private static TimeZone defaultZone;
    static {
        defaultZone = TimeZone.getTimeZone(Constants.Time.TIME_ZONE);
    }


    private static Calendar getCalendar() {
        return Calendar.getInstance(defaultZone);
    }

    private static Calendar getCalendar(Date d) {
        Calendar c = getCalendar();
        c.setTime(d);
        return c;
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
        Calendar calendar = getCalendar(d);
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
        Calendar start = getCalendar(d);
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
        Calendar end = getCalendar(d);
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

    /**
     * get date month.
     * @param d  base date
     * @return  month of the date
     */
    public static int getMonth(Date d) {
        Calendar ca = getCalendar(d);
        return ca.get(Calendar.MONTH) - 1;
    }

    /**
     * get month day of the date.
     * @param d  base date
     * @return  month day of the date
     */
    public static int getMonthOfDay(Date d) {
        Calendar ca = getCalendar(d);
        return ca.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * get year day of the date.
     * @param d  base date
     * @return  year day of the date
     */
    public static int getYearOfDay(Date d) {
        Calendar ca = getCalendar(d);
        return ca.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * the day between start time and end time.
     * @param startTime  start time yyyyMMdd
     * @param endTime end time yyyyMMdd
     * @return  days for yyyyMMdd
     */
    public static List<String> betweenDays(String startTime, String endTime) {

        // 返回的日期集合
        List<String> days = new ArrayList<>();

        Date start = DateUtils.toDateTime(startTime,"yyyyMMdd");
        Date end = DateUtils.toDateTime(endTime,"yyyyMMdd");
        Calendar tempStart = getCalendar(start);
        Calendar tempEnd = getCalendar(end);

        tempEnd.add(Calendar.DATE, 1); // 日期加1(包含结束)
        while (tempStart.before(tempEnd)) {
            days.add(DateUtils.toString(tempStart.getTime(),"yyyyMMdd"));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }

        return days;
    }
}
