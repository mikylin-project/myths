package cn.mikylin.myths.common;

/**
 * number utils.
 *
 * @author mikylin
 * @date 20191126
 */
public final class NumberUtils {

    /**
     * number equals.
     * @param n1  number
     * @param n2  number
     * @return boolean
     */
    public static boolean equals(Number n1,Number n2) {
        return n1 != null && n2 != null
                && n1.getClass() == n2.getClass()
                && n1.doubleValue() == n2.doubleValue();
    }

    /**
     * number equals ignore number type.
     * @param n1  number
     * @param n2  number
     * @return boolean
     */
    public static boolean equalsIgnoreType(Number n1,Number n2) {
        return n1 != null && n2 != null
                && n1.doubleValue() == n2.doubleValue();
    }
}