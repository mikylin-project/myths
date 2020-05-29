package cn.mikylin.myths.common.lang;

public final class PatternUtils {

    static String chinesePatternStr = "[\u4e00-\u9fa5]";

    public static boolean isContainChinese(String str) {
        StringUtils.requireNotBlank(str);
        return str.matches(chinesePatternStr);
    }

    public static boolean isChinese(char c) {
        return isContainChinese(String.valueOf(c));
    }
}
