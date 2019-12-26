package cn.mikylin.myths.common.lang;

/**
 * object utils.
 *
 * @author mikylin
 * @date 20191219
 */
public final class ObjectUtils {

    /**
     * close the object.
     *
     * @param closer  object wait to close.
     */
    public static void close(final AutoCloseable closer) {

        if(closer != null)
            try {
                closer.close();
            } catch (Exception e) {
                throw new RuntimeException("close failed");
            }
    }


    public static void wait(Object o) {
        if(o != null)
            synchronized (o) {
                try {
                    o.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException("wait exception.");
                }
            }
    }

    public static void notifyAll(Object o) {
        if(o != null)
            synchronized (o) {
                o.notifyAll();
            }
    }

    public static void notify(Object o) {
        if(o != null)
            synchronized (o) {
                o.notify();
            }
    }





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
                throw new RuntimeException("clone failed.");
            }
        }

        Object clone() throws CloneNotSupportedException;
    }


    /**
     * clone utils for object adapter.
     *
     * @author mikylin
     * @date 20191108
     */
    public static class CloneObjectAdapter<T> implements CloneObject<T> {

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
}
