package cn.mikylin.myths.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static String dayTime(long changeDays,String format) {
        return dayTime(changeDays).format(formatter(format));
    }


    public static LocalDateTime dayTime(long changeDays) {
        return LocalDateTime.now().plusDays(changeDays);
    }



}
