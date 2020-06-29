package cn.mikylin.myths.common.datetime;

import cn.mikylin.myths.common.Constants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static cn.mikylin.myths.common.datetime.CalendarUtils.dayBaseChange;

/**
 * simple date utils.
 * @author mikylin
 * @date 20190714
 */
public final class DateUtils {

    /**
     * date and string
     */

    public static String toString(Date d,String format) {
        return new SimpleDateFormat(format).format(d);
    }

    public static String toDateTimeString(Date d) {
        return toString(d, Constants.Date.DATE_TIME_FORMAT);
    }

    public static String toDateString(Date d) {
        return toString(d,Constants.Date.DATE_FORMAT_YYYYMMDD);
    }

    public static String toTimeString(Date d) {
        return toString(d,Constants.Date.TIME_FORMAT);
    }

    public static Date toDateTime(String dateString,String format) {
        try {
            return new SimpleDateFormat(format).parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("date string is wrong");
        }
    }

    public static Date toDateTime(String dateString) {
        return toDateTime(dateString,Constants.Date.DATE_TIME_FORMAT);
    }

    public static Date toDate(String dateString) {
        return toDateTime(dateString,Constants.Date.DATE_FORMAT_YYYYMMDD);
    }


    /**
     * calendar change method
     */
    public static Date dayBeforeHours(Date d,int hours) {
        return dayBaseChange(d,Calendar.HOUR_OF_DAY,- hours);
    }

    public static Date dayAfterHours(Date d,int hours) {
        return dayBaseChange(d,Calendar.HOUR_OF_DAY,hours);
    }

    public static Date dayBeforeDays(Date d,int days) {
        return dayBaseChange(d,Calendar.DATE,- days);
    }

    public static Date dayAfterDays(Date d,int days) {
        return dayBaseChange(d,Calendar.DATE,days);
    }

    public static Date dayBeforeMinutes(Date d,int min) {
        return dayBaseChange(d,Calendar.MINUTE,- min);
    }

    public static Date dayAfterMinuts(Date d,int min) {
        return dayBaseChange(d,Calendar.MINUTE,min);
    }



}