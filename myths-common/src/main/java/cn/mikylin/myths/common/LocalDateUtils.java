package cn.mikylin.myths.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * jdk8 local date utils
 * @author mikylin
 * @date 20190808
 */
public final class LocalDateUtils {

    private static Map<String,DateTimeFormatter> formatterMap;
    static {
        formatterMap = MapUtils.createMap();
    }

    private static DateTimeFormatter formatter(String format) {

        DateTimeFormatter formatter = formatterMap.get(format);

        if(formatter == null)
            synchronized (LocalDateUtils.class) {
                if((formatter = formatterMap.get(format)) == null) {
                    formatter = DateTimeFormatter.ofPattern(format);
                    formatterMap.put(format,formatter);
                }
            }
        return formatter;
    }

    /**
     * day changes
     *
     * @param changeDays
     *         example:
     *          now : 2019-11-07 00:00:00
     *          changeDays : 1  return : 2019-11-08 00:00:00
     *          changeDays : -1  return : 2019-11-06 00:00:00
     * @param format day format
     */
    public static String dayTime(long changeDays,String format) {
        return dayTime(changeDays).format(formatter(format));
    }


    public static LocalDateTime dayTime(long changeDays) {
        return LocalDateTime.now().plusDays(changeDays);
    }


    public static Date toDate(LocalDateTime time) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = time.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }


}
