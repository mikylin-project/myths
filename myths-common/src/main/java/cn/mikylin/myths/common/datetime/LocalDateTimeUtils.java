package cn.mikylin.myths.common.datetime;

import cn.mikylin.myths.common.MapUtils;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * jdk8 local date time utils.
 *
 * @author mikylin
 * @date 20190808
 */
public final class LocalDateTimeUtils {

    private static Map<String,DateTimeFormatter> formatterMap;
    static {
        formatterMap = MapUtils.newHashMap();
    }

    /**
     * lazy init the formatter.
     *
     * @param format  day format
     * @return formatter
     */
    private static DateTimeFormatter formatter(String format) {

        DateTimeFormatter formatter = formatterMap.get(format);

        if(formatter == null)
            synchronized (LocalDateTimeUtils.class) {
                if((formatter = formatterMap.get(format)) == null) {
                    formatter = DateTimeFormatter.ofPattern(format);
                    formatterMap.put(format,formatter);
                }
            }

        return formatter;
    }

    /**
     * day changes.
     *
     * @param changeDays
     *         example:
     *          now : 2019-11-07 00:00:00
     *          changeDays : 1  return : 2019-11-08 00:00:00
     *          changeDays : -1  return : 2019-11-06 00:00:00
     * @param format  day format
     * @return format
     */
    public static String dayTime(long changeDays,String format) {
        return dayTime(changeDays).format(formatter(format));
    }


    public static LocalDateTime dayTime(long changeDays) {
        return LocalDateTime.now().plusDays(changeDays);
    }


    /**
     * local-date-time to date.
     *
     * @param time  local-date-time
     * @return date
     */
    public static Date toDate(LocalDateTime time) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = time.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * local-date to date.
     *
     * @param localDate  local-date
     * @return date
     */
    public static Date toDate(LocalDate localDate) {
        return toDate(localDate.atStartOfDay());
    }
}
