package cn.mikylin.myths.common;

import java.lang.reflect.Array;
import java.util.Objects;

/**
 * array utils
 *
 * @author mikylin
 * @date 20190621
 */
public final class ArrayUtils {

    /**
     * the byte[] is blank.
     *
     * @param bs array
     * @return blank - true , not blank - false
     */
    public static boolean isBlank(byte[] bs){
        return bs == null || bs.length == 0;
    }

    /**
     * the byte[] is not blank.
     *
     * @param bs array
     * @return not blank - true , blank - false
     */
    public static boolean isNotBlank(byte[] bs){
        return !isBlank(bs);
    }

    /**
     * the object[] is blank.
     *
     * @param bs array
     * @return blank - true , not blank - false
     */
    public static boolean isBlank(Object[] bs){
        return bs == null || bs.length == 0;
    }

    /**
     * the object[] is not blank.
     *
     * @param bs array
     * @return not blank - true , blank - false
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
            if(Objects.equals(t,element)) return true;
        return false;
    }


    /**
     * create a array in type T
     */
    public static <T> T[] createInHeap(Class<T> clz,int cap){
        return (T[]) Array.newInstance(clz, cap);
    }
}
