package cn.mikylin.myths.common;


/**
 * close object utils
 *
 * @author mikylin
 * @date 20190816
 */
public final class CloseUtils {

    /**
     * close the object
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
}
