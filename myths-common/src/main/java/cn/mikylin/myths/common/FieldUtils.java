package cn.mikylin.myths.common;

import java.lang.reflect.Field;

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
     * @param f  param name
     * @param refFrom  the object
     * @throws RuntimeException
     * @return obj
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

    /**
     * get the value in the object.
     *
     * @param obj  object
     * @param fieldName  field name
     * @throws RuntimeException
     * @return obj
     */
    public static Object refField(Object obj,String fieldName) {
        Field field = getField(obj, fieldName);
        return refField(field,obj);
    }
}