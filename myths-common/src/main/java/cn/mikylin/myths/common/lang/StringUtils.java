package cn.mikylin.myths.common.lang;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * string utils.
 *
 * @author mikylin
 * @date 20190619
 */
public final class StringUtils {

    private static final Charset c = CharsetUtils.getDefault();

    /**
     * check the string is blank.
     *
     * @param str string
     * @return blank - true , not blank - false
     */
    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }


    @Deprecated
    public static boolean isBlankForJdk8(String str) {

        if(str != null) {
            String trimStr = str.trim();
            for (int i = 0; i < trimStr.length(); i ++)
                if (!Character.isWhitespace(str.charAt(i)))
                    return false;
        }
        return true;
    }

    /**
     * exception if blank.
     *
     * @param str  string
     * @param message  message
     */
    public static void requireNotBlank(String str,String message) {
        if(isBlank(str))
            throw new NullPointerException(message);
    }

    /**
     * exception if blank.
     *
     * @param str  string
     */
    public static <T> void requireNotBlank(String str) {
        requireNotBlank(str,"string can not be blank.");
    }


    /**
     * check the string is not blank
     *
     * @param str  string
     * @return blank - false , not blank - true
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }


    /**
     * objects to string.
     *
     * @param objs  objects to string
     * @return string
     */
    public static String toString(Object... objs) {
        StringBuilder builder = new StringBuilder();
        for(Object obj : objs)
            builder.append(obj);
        return builder.toString();
    }

    public static String toString(Charset c,byte[] bs) {
        if(c == null || ArrayUtils.isBlank(bs))
            throw new IllegalArgumentException();
        return new String(bs,c);
    }

    public static String toString(byte[] bs) {
        return toString(c,bs);
    }

    public static byte[] toBytes(Charset c,String s) {
        if(c == null || isBlank(s))
            throw new IllegalArgumentException();
        return s.getBytes(c);
    }

    public static byte[] toBytes(String s) {
        return toBytes(c,s);
    }




}
