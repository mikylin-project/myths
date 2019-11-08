package cn.mikylin.myths.common;

/**
 * string utils
 *
 * @author mikylin
 * @date 20190619
 */
public final class StringUtils {

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

        if(str != null){
            String trimStr = str.trim();
            for (int i = 0; i < trimStr.length(); i ++)
                if (!Character.isWhitespace(str.charAt(i))) return false;
        }
        return true;
    }

    /**
     * exception if blank.
     *
     * @param str string
     * @param message message
     */
    public static void requireNotBlank(String str,String message) {
        if(isBlank(str)) throw new NullPointerException(message);
    }

    /**
     * exception if blank.
     *
     * @param str string
     */
    public static <T> void requireNotBlank(String str) {
        requireNotBlank(str,"string can not be blank.");
    }


    /**
     * check the string is not blank
     *
     * @param str string
     * @return blank - false , not blank - true
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }


    /**
     * objects to string.
     *
     * @param objs
     * @return string
     */
    public static String toString(Object... objs) {
        StringBuilder builder = new StringBuilder();
        for(Object obj : objs)
            builder.append(obj);
        return builder.toString();
    }
}
