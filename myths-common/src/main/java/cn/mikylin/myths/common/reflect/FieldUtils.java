package cn.mikylin.myths.common.reflect;

import java.lang.reflect.Field;

/**
 * field util
 * @author mikylin
 * @date 20190707
 */
public final class FieldUtils {

    /**
     * get the value in the object
     * @param f param name
     * @param refFrom the object
     */
    public static Object refField(Field f,Object refFrom){
        try {
            f.setAccessible(true);
            return f.get(refFrom);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("reflect to get the value is error");
        }
    }
}
