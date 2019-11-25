package cn.mikylin.myths.common;

import java.lang.reflect.Array;
import java.util.Objects;

/**
 * array utils.
 *
 * @author mikylin
 * @date 20190621
 */
public final class ArrayUtils {

    /**
     * the byte[] is blank.
     *
     * @param array  array
     * @return blank - true , not blank - false
     */
    public static boolean isBlank(byte[] array){
        return array == null || array.length == 0;
    }

    /**
     * the byte[] is not blank.
     *
     * @param array  array
     * @return not blank - true , blank - false
     */
    public static boolean isNotBlank(byte[] array){
        return !isBlank(array);
    }


    public static boolean isBlank(char[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNotBlank(char[] array) {
        return isBlank(array);
    }

    public static boolean isBlank(boolean[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNotBlank(boolean[] array) {
        return isNotBlank(array);
    }





    /**
     * the object[] is blank.
     *
     * @param array  array
     * @return blank - true , not blank - false
     */
    public static boolean isBlank(Object[] array){
        return array == null || array.length == 0;
    }

    /**
     * the object[] is not blank.
     *
     * @param array  array
     * @return not blank - true , blank - false
     */
    public static boolean isNotBlank(Object[] array){
        return !isBlank(array);
    }

    /**
     * bool the some of one element in collection.
     *
     * @param array  array
     * @param element  ele
     * @return exist - true , not exist - false
     */
    public static <T> boolean isInArray(T[] array, T element){

        if(isBlank(array)) //collection can not be blank
            return false;

        for(T t : array)
            if(Objects.equals(t,element)) return true;
        return false;
    }


    /**
     * create a array in type T.
     *
     * @param clz  type
     * @param cap  capacity
     * @return array
     */
    public static <T> T[] createInHeap(Class<T> clz,int cap){
        return (T[]) Array.newInstance(clz,cap);
    }
}
