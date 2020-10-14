package cn.mikylin.myths.common.invoke;

import java.lang.reflect.*;
import java.util.*;

/**
 * type utils.
 *
 * @author mikylin
 * @date 20190707
 */
public final class TypeUtils {

    /**
     * get the type list of some object's generic params.
     *
     * @param type some object‘s class type
     * @throws NullPointerException
     * @throws IllegalArgumentException
     * @return object's generic params
     */
    public static Type[] genericTypes(Type type) {

        // 判空
        Objects.requireNonNull(type);

        // 判断该 type 参数是否是带有泛型的类的 Type
        // 如果不是，此处抛出非法参数错误
        if(! (type instanceof ParameterizedType))
            throw new IllegalArgumentException();

        // 通过 ParmeterizzedType 获取到该对象的泛型
        Type[] tTypes = ((ParameterizedType)type).getActualTypeArguments();
        return tTypes;
    }

    /**
     * get the first generic param's class for the object.
     *
     * @param type some object's class type
     * @return class
     */
    public static <T> Class<T> firstGeneric(Type type) {
        return (Class<T>)(genericTypes(type)[0]);
    }


    /**
     * check the class is the parent's child or the parent own.
     *
     * @param parent
     * @param child
     * @throws NullPointerException
     * @return true - is the child or own , false - not the child or own
     */
    public static boolean isTheChildOrOwnType(Class parent,Class child) {
        // 判断 child class 是否是 parent class 的子类
        /*
            parent = null - false
            child = null - false
            parent == child - true
            parent 是 child 的父类 - true
         */
        return parent != null && child != null &&
                (parent == child || parent.isAssignableFrom(child));
    }
}