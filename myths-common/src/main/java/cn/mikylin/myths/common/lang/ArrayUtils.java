package cn.mikylin.myths.common.lang;

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
     * @return  blank - true , not blank - false
     */
    public static boolean isBlank(byte[] array){
        return array == null || array.length == 0;
    }

    /**
     * the byte[] is not blank.
     *
     * @param array  array
     * @return  not blank - true , blank - false
     */
    public static boolean isNotBlank(byte[] array) {
        return !isBlank(array);
    }

    /**
     * the int[] is blank.
     *
     * @param array  array
     * @return  blank - true , not blank - false
     */
    public static boolean isBlank(int[] array){
        return array == null || array.length == 0;
    }

    /**
     * the int[] is not blank.
     *
     * @param array  array
     * @return  not blank - true , blank - false
     */
    public static boolean isNotBlank(int[] array){
        return !isBlank(array);
    }

    /**
     * the long[] is blank.
     *
     * @param array  array
     * @return  blank - true , not blank - false
     */
    public static boolean isBlank(long[] array){
        return array == null || array.length == 0;
    }

    /**
     * the long[] is not blank.
     *
     * @param array  array
     * @return  not blank - true , blank - false
     */
    public static boolean isNotBlank(long[] array){
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
     * @return  blank - true , not blank - false
     */
    public static boolean isBlank(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * the object[] is not blank.
     *
     * @param array  array
     * @return  not blank - true , blank - false
     */
    public static boolean isNotBlank(Object[] array){
        return !isBlank(array);
    }

    /**
     * bool the some of one element in collection.
     *
     * @param array  array
     * @param element  ele
     * @return  exist - true , not exist - false
     */
    public static <T> boolean isInArray(T[] array, T element) {

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
     * @return  array
     */
    public static <T> T[] createInHeap(Class<T> clz,int cap){
        return (T[]) Array.newInstance(clz,cap);
    }


    /**
     * distinct the array.
     * @param ints  array
     * @return  distinct array
     */
    public static int[] distinctArray(int[] ints) {
        if (isBlank(ints))
            throw new RuntimeException("array is blank.");

        if (ints.length == 1)
            return ints;

        int[] distinct = new int[ints.length];

        int distinctLen = 0;

        c1 : for(int i = 0 ; i < ints.length ; i ++) {
            int m = ints[i];
            c2 : for(int j = 0 ; j <= distinctLen ; j ++) {
                int d = distinct[j];
                if (m == d) continue c1;
            }
            distinct[distinctLen ++] = m;
        }

        int[] result = new int[distinctLen];
        for (int i = 0 ; i < distinctLen ; i ++)
            result[i] = distinct[i];

        return result;
    }

    /**
     * distinct the array.
     * @param objs  array
     * @return  distinct array
     */
    public static <T> T[] distinctArray(T[] objs) {
        if (isBlank(objs))
            throw new RuntimeException("array is blank.");

        if (objs.length == 1)
            return objs;

        T[] distinct = (T[])new Object[objs.length];

        int distinctLen = 0;

        c1 : for (int i = 0 ; i < objs.length ; i ++) {
            T m = objs[i];
            c2 : for (int j = 0 ; j <= distinctLen ; j ++) {
                T d = distinct[j];
                if (Objects.equals(m,d)) continue c1;
            }
            distinct[distinctLen ++] = m;
        }

        T[] result = (T[])new Object[distinctLen];
        for (int i = 0 ; i < distinctLen ; i ++)
            result[i] = distinct[i];

        return result;
    }

}
