package cn.mikylin.myths.common.lang;

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
     * @return true - equals,
     *          false - not equals
     */
    public static boolean equals(Number n1,Number n2) {
        return equalsIgnoreType(n1,n2)
                && n1.getClass() == n2.getClass();
    }

    /**
     * number equals ignore number type.
     * @param n1  number
     * @param n2  number
     * @return true - equals,
     *          false - not equals
     */
    public static boolean equalsIgnoreType(Number n1,Number n2) {
        return n1 != null && n2 != null
                && n1.doubleValue() == n2.doubleValue();
    }
}