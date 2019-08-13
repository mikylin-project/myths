package cn.mikylin.myths.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * jdk8 local date util
 * @author mikylin
 * @date 20190808
 */
public final class LocalDateUtils {

    public static String dayTime(long changeDays,String format){
        return dayTime(changeDays).format(DateTimeFormatter.ofPattern(format));
    }


    public static LocalDateTime dayTime(long changeDays){
        return LocalDateTime.now().plusDays(changeDays);
    }

}
