package cn.mikylin.myths.common;

import cn.mikylin.myths.common.lang.ArrayUtils;
import java.util.*;

/**
 * collection utils.
 *
 * @author mikylin
 * @date 20190619
 */
public final class CollectionUtils {

    /**
     * bool the list is blank.
     *
     * @param col collection
     * @return blank - true , not blank - false
     */
    public static boolean isBlank(Collection<?> col) {
        return col == null || col.isEmpty();
    }

    /**
     * bool the list is not blank.
     *
     * @param col collection
     * @return blank - false , not blank - true
     */
    public static boolean isNotBlank(Collection<?> col) {
        return !isBlank(col);
    }

    /**
     * list size.
     *
     * @param col collection
     * @return list size
     */
    public static int size(Collection<?> col) {
        return isBlank(col) ? 0 : col.size();
    }

    /**
     * bool the some of one element in collection.
     *
     * @param col collection
     * @param element collection element
     * @return exist - true , not exist - false
     */
    public static <T> boolean isInCollection(Collection<T> col, T element) {

        if(isBlank(col)) //collection can not be blank
            return false;

        for(T t : col)
            if(Objects.equals(t,element))
                return true;
        return false;
    }

    /**
     * create a new array list.
     *
     * @param ts array
     * @return list
     */
    public static <T> List<T> newArrayList(T... ts) {

        if(ArrayUtils.isNotBlank(ts)) {
            List<T> l = new ArrayList<>(ts.length + 8);
            Collections.addAll(l,ts);
            return l;
        }
        return new ArrayList<>(8);
    }

    /**
     * create a new array list.
     *
     * @param s set
     * @return list
     */
    public static <T> List<T> newArrayList(Set<T> s) {
        if(isBlank(s))
            return newArrayList();
        return new ArrayList<>(s);
    }

    /**
     * exception if blank.
     *
     * @param col collection
     * @param message message
     * @throws NullPointerException
     */
    public static <T> void requireNotBlank(Collection<T> col,String message) {
        if(isBlank(col)) throw new NullPointerException(message);
    }

    /**
     * exception if blank.
     *
     * @param col collection
     * @throws NullPointerException
     */
    public static <T> void requireNotBlank(Collection<T> col) {
        requireNotBlank(col,"collection can not be blank.");
    }

}
