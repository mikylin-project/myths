package cn.mikylin.myths.common;


/**
 * close object
 * @author mikylin
 * @date 20190816
 */
public class CloseUtils {

    /**
     * close the object
     * @param closer  object wait to close.
     */
    public static void close(AutoCloseable closer){

        if(closer != null)
            try {
                closer.close();
            } catch (Exception e) {
                throw new RuntimeException("close failed");
            }
    }
}
