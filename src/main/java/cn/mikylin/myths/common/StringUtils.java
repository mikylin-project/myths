package cn.mikylin.myths.common;

/**
 * string utls
 * @author mikylin
 * @date 20190619
 */
public final class StringUtils {

    /**
     * check the string is null
     * null - true
     * not null - false
     */
    public static boolean isEmpty(String str){
        return str == null || str.isEmpty();
    }

    /**
     * check the string is not null
     * null - false
     * not null - true
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    /**
     * check the string is blank
     * blank - true
     * not blank - false
     */
    public static boolean isBlank(String str) {
        if(isEmpty(str))
            return true;

        String trimStr = str.trim();
        for (int i = 0; i < trimStr.length(); i ++)
            if (!Character.isWhitespace(str.charAt(i)))
                return false;
        return true;
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
