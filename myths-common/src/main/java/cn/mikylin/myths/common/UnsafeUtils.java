package cn.mikylin.myths.common;

import sun.misc.Unsafe;
import java.lang.reflect.Field;

import static sun.misc.Unsafe.getUnsafe;

/**
 * unsafe utils
 *
 * @author mikylin
 * @date 20191022
 */
public final class UnsafeUtils {

    private static Unsafe u;

    static {
        try {
            //获取 Unsafe 内部的私有的实例化单例对象
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            //无视权限
            field.setAccessible(true);
            u = (Unsafe) field.get(null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("no unsafe");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("no unsafe");
        }
    }

    /**
     * get unsafe.
     *
     * @return unsafe
     */
    public static Unsafe getUnsafe() {
        return u;
    }

    public static boolean cas(Object obj,long offset,int oldValue,int newValue) {
        int i = u.getIntVolatile(obj, offset);
        return u.compareAndSwapInt(obj,offset,oldValue,newValue);
    }

    public static boolean cas(Object obj,long offset,long oldValue,long newValue) {
        return u.compareAndSwapLong(obj,offset,oldValue,newValue);
    }

    public static boolean cas(Object obj,long offset,Object oldValue,Object newValue) {
        return u.compareAndSwapObject(obj,offset,oldValue,newValue);
    }






}
