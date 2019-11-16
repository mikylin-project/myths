package cn.mikylin.myths.common;

/**
 * clone utils for object which implement this interface.
 *
 * @author mikylin
 * @date 20191107
 */
public interface CloneObject<T> extends Cloneable {

    default T copy() {
        try {
            return (T)clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException("clone failed.");
        }
    }

    Object clone() throws CloneNotSupportedException;
}
