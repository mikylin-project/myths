package cn.mikylin.myths.common;

/**
 * string builder utils
 *
 * @author mikylin
 * @date 20190824
 */
public final class StringBuilderUtils {

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
