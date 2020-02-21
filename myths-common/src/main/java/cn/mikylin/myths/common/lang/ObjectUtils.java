package cn.mikylin.myths.common.lang;

import java.util.Objects;

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


    public static void requireNotNull(Object... objs) {
        if(objs == null || objs.length == 0)
            throw new RuntimeException("objs null.");
        for(Object o : objs) {
            if(o == null)
                throw new RuntimeException("exists obj is null.");
        }
    }

}
