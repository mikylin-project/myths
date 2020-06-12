package cn.mikylin.myths.common;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * future utils.
 *
 * @author mikylin
 * @date 20191215
 */
public final class FutureUtils {

    public static <T> T get(Future<T> f) {

        if(f == null || f.isCancelled())
            return null;

        try {
            return f.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T tryGet(Future<T> f) {

        if(f == null || f.isCancelled())
            return null;

        try {
            return f.get(1L,TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
