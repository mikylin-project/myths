package cn.mikylin.myths.common;

/**
 * string utils
 * @author mikylin
 * @date 20190619
 */
public final class StringUtils {

    /**
     * check the string is blank
     * blank - true
     * not blank - false
     * @since jdk11
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

    public static void requireNotBlank(String str,String message){
        if(isBlank(str)) throw new NullPointerException(message);
    }


    /**
     * check the string is not blank
     * blank - false
     * not blank - true
     */
    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }
}
