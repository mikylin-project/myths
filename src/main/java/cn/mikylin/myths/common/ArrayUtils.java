package cn.mikylin.myths.common;

import java.lang.reflect.Array;
import java.util.Objects;

/**
 * 处理数组用的相关工具类
 * @author mikylin
 * @date 20190621
 */
public final class ArrayUtils {

    /**
     * 判断 byte[] 是否为空
     * 是空的 - true
     * 不是空的 - false
     */
    public static boolean isBlank(byte[] bs){
        if(bs == null || bs.length == 0)
            return true;
        return false;
    }

    /**
     * 判断 byte[] 是否不为空
     * 不是空的 - true
     * 是空的 - false
     */
    public static boolean isNotBlank(byte[] bs){
        return !isBlank(bs);
    }

    /**
     * 判断 object[] 是否为空
     * 是空的 - true
     * 不是空的 - false
     */
    public static boolean isBlank(Object[] bs){
        if(bs == null || bs.length == 0)
            return true;
        return false;
    }

    /**
     * 判断 object[] 是否不为空
     * 不是空的 - true
     * 是空的 - false
     */
    public static boolean isNotBlank(Object[] bs){
        return !isBlank(bs);
    }


    /**
     * bool the some of one element in collection
     * exist - true
     * not exist - false
     */
    public static <T> boolean isInArray(T[] col, T element){

        if(isBlank(col)) //collection can not be blank
            return false;

        for(T t : col)
            if(Objects.equals(t,element))
                return true;
        return false;
    }


    /**
     * 创建一个泛型数组
     */
    public static <T> T[] createInHeap(Class<T> clz,int cap){
        return (T[]) Array.newInstance(clz, cap);
    }
}
