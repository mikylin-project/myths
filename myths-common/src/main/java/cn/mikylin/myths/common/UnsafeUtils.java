package cn.mikylin.myths.common;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * unsafe utils
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
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Unsafe getUnsafe(){
        return u;
    }
}
