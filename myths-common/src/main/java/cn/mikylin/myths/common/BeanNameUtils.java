package cn.mikylin.myths.common;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * bean name utils
 * @author mikylin
 * @date 20191015
 */
public final class BeanNameUtils {

    public static Map<String,BeanNameType> nameTypeMap;
    static {
        nameTypeMap = MapUtils.createMap (
                new Object[]{"underline",BeanNameType.UNDERLINE},
                new Object[]{"hump",BeanNameType.HUMP}
        );
    }

    /**
     * 下划线转驼峰
     */
    private static Pattern underlinePattern = Pattern.compile("_(\\w)");
    public static String underlineToHump(String underlineOrigin) {

        if(StringUtils.isBlank(underlineOrigin))
            throw new RuntimeException("under line String can not be blank");

        String underline = underlineOrigin.toLowerCase();
        if(!underline.equals(underlineOrigin))
            throw new RuntimeException("under line String can not have upper case");

        Matcher matcher = underlinePattern.matcher(underline);
        StringBuffer sb = new StringBuffer();
        while (matcher.find())
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 驼峰转下划线
     */
    private static Pattern humpPattern = Pattern.compile("[A-Z]");
    public static String humpToUnderline(String hump) {
        Matcher matcher = humpPattern.matcher(hump);
        StringBuffer sb = new StringBuffer();
        while (matcher.find())
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * the name type trans
     */
    public static String trans(String nameOrigin,BeanNameType originType,BeanNameType transType){

        if(StringUtils.isBlank(nameOrigin)
                || originType == null || transType == null)
            throw new RuntimeException("bean name utils trans params exception");

        if(originType == transType)
            return nameOrigin;

        if(originType == BeanNameType.UNDERLINE
                && transType == BeanNameType.HUMP)
            return underlineToHump(nameOrigin);
        else if(transType == BeanNameType.UNDERLINE
                && originType == BeanNameType.HUMP)
            return humpToUnderline(nameOrigin);

        throw new RuntimeException("the type trans not support");
    }


    public enum BeanNameType {
        UNDERLINE,
        HUMP;
    }
}
