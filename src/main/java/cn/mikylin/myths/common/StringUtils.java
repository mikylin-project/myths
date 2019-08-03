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
    public static Boolean isEmpty(String str){
        if(str == null || str.isEmpty())
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    /**
     * check the string is not null
     * null - false
     * not null - true
     */
    public static Boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    /**
     * check the string is blank
     * blank - true
     * not blank - false
     */
    public static Boolean isBlank(String str) {
        if(isEmpty(str))
            return Boolean.TRUE;

        String trimStr = str.trim();
        for (int i = 0; i < trimStr.length(); i ++)
            if (!Character.isWhitespace(str.charAt(i)))
                return Boolean.FALSE;
        return Boolean.TRUE;
    }

    /**
     * check the string is not blank
     * blank - false
     * not blank - true
     */
    public static Boolean isNotBlank(String str){
        return !isBlank(str);
    }
}
