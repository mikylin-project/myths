package cn.mikylin.myths.common.lang;

/**
 * clone utils for object which implement this interface.
 *
 * @author mikylin
 * @date 20191107
 */
public interface CloneObject<T> extends Cloneable {

    /**
     * copy the object.
     *
     * @return new object
     */
    default T copy() {
        try {
            return (T)clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    Object clone() throws CloneNotSupportedException;

}
