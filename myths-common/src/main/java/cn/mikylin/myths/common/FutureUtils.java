package cn.mikylin.myths.common;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * future utils
 *
 * @author mikylin
 * @date 20191215
 */
public final class FutureUtils {

    public static <T> T get(Future<T> f) {
        try {
            return f.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("future get exception.");
        }
    }

    public static <T> T tryGet(Future<T> f) {
        try {
            return f.get(1l,TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException("future get exception.");
        }
    }
}
