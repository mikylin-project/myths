package cn.mikylin.myths.common;

import java.util.*;

/**
 * collection utils
 * @author mikylin
 * @date 20190619
 */
public final class CollectionUtils {

    /**
     * bool the list is blank
     * @param col -- collection
     *
     * blank - true
     * not blank - false
     */
    public static boolean isBlank(Collection<?> col){
        return col == null || col.isEmpty();
    }

    /**
     * bool the list is not blank
     * @param col -- collection
     *
     * blank - false
     * not blank - true
     */
    public static boolean isNotBlank(Collection<?> col){
        return !isBlank(col);
    }

    /**
     * bool the some of one element in collection
     * @param col  collection
     * @param element  collection element
     *
     * exist - true
     * not exist - false
     */
    public static <T> boolean isInCollection(Collection<T> col, T element){

        if(isBlank(col)) //collection can not be blank
            return false;

        for(T t : col)
            if(Objects.equals(t,element))
                return true;
        return false;
    }

    /**
     * create a new ArrayList
     * @param ts  array
     */
    public static <T> List<T> newArrayList(T... ts){

        if(ArrayUtils.isNotBlank(ts)){
            List<T> l = new ArrayList<>(ts.length + 8);
            Collections.addAll(l,ts);
            return l;
        }
        return new ArrayList<>(8);
    }

    /**
     * create a new ArrayList
     */
    public static <T> List<T> newArrayList(Set<T> s){
        if(isBlank(s))
            return newArrayList();
        return new ArrayList<>(s);
    }



}
