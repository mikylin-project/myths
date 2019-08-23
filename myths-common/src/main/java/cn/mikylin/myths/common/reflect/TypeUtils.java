package cn.mikylin.myths.common.reflect;

import cn.mikylin.myths.common.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * type utils
 * @author mikylin
 * @date 20190707
 */
public final class TypeUtils {

    /**
     * get the type list of some object's generic params
     * @param type some object‘s class type
     */
    public static List<Type> genericTypes(Type type) {

        // 判空
        Objects.requireNonNull(type);

        // 判断该 type 参数是否是带有泛型的类的 Type
        // 如果不是，此处抛出非法参数错误
        if(! (type instanceof ParameterizedType))
            throw new IllegalArgumentException();

        // 通过 ParmeterizzedType 获取到该对象的泛型
        // 组装成 list 并返回
        Type[] tTypes = ((ParameterizedType)type).getActualTypeArguments();
        return CollectionUtils.newArrayList(tTypes);
    }

    /**
     * get the first generic param's class for the object
     * @param type some object's class type
     */
    public static <T> Class<T> firstGeneric(Type type){
        return (Class<T>)(genericTypes(type).get(0));
    }


    public static boolean isTheChildOrOwnType(Class parent,Class child){
        // 判断 child class 是否是 parent class 的子类
        Objects.requireNonNull(parent);
        Objects.requireNonNull(child);
        return parent.isAssignableFrom(child);
    }



}
