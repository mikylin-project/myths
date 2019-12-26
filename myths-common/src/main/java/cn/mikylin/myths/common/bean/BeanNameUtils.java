package cn.mikylin.myths.common.bean;

import cn.mikylin.myths.common.Constants;
import cn.mikylin.myths.common.MapUtils;
import cn.mikylin.myths.common.lang.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * bean name utils.
 *
 * @author mikylin
 * @date 20191015
 */
public final class BeanNameUtils {

    private static Pattern underlinePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    public static Map<String,BeanNameType> nameTypeMap;
    static {
        nameTypeMap = MapUtils.createMap (
                new Object[]{"underline",BeanNameType.UNDERLINE},
                new Object[]{"hump",BeanNameType.HUMP}
        );
    }

    /**
     * underline to hump.
     *
     * @param underlineOrigin  underline bean name
     * @return hump bean name
     */
    public static String underlineToHump(String underlineOrigin) {

        if(StringUtils.isBlank(underlineOrigin))
            throw new RuntimeException("under line String can not be blank");

        String underline = underlineOrigin.toLowerCase();
        if(!underline.equals(underlineOrigin))
            throw new RuntimeException("under line String can not have upper case");

        Matcher matcher = underlinePattern.matcher(underline);
        StringBuffer sb = new StringBuffer();
        while (matcher.find())
            matcher.appendReplacement(sb,
                    matcher.group(1).toUpperCase());
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * hump to underline.
     *
     * @param hump  hump bean name
     * @return undeline bean name
     */
    public static String humpToUnderline(String hump) {
        Matcher matcher = humpPattern.matcher(hump);
        StringBuffer sb = new StringBuffer();
        for ( ; matcher.find() ; )
            matcher.appendReplacement(sb,
                    Constants.System.UNDER_LINE + matcher.group(0).toLowerCase());
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
        HUMP
    }
}
