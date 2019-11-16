package cn.mikylin.myths.common;

import sun.misc.Unsafe;
import java.lang.reflect.Field;

import static sun.misc.Unsafe.*;

/**
 * field util
 *
 * @author mikylin
 * @date 20190707
 */
public final class FieldUtils {

    /**
     * get the value in the object.
     *
     * @param f param name
     * @param refFrom the object
     */
    public static Object refField(Field f,Object refFrom) {
        try {
            f.setAccessible(true);
            return f.get(refFrom);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("reflect to get the value is error");
        }
    }

    /**
     * get the field in the object.
     *
     * @param obj  object
     * @param fieldName  field name
     * @throws RuntimeException
     * @return field
     */
    public static Field getField(Object obj,String fieldName) {
        try {
            return obj.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("no such field.");
        }
    }
}
