package cn.mikylin.myths.common.reflect;


import cn.mikylin.myths.common.ArrayUtils;
import cn.mikylin.myths.common.CollectionUtils;
import cn.mikylin.myths.common.StringUtils;

import java.util.List;

/**
 * class utils
 * @author mikylin
 * @date 20190714
 */
public class ClassUtils {

    /**
     * get class's package name
     * @param clz class
     */
    public static String packageName(Class<?> clz){
        String pakName = clz.getPackageName();
        if(StringUtils.isNotBlank(pakName))
            return pakName;
        return null;
    }

    /**
     * get class's super class name
     * @param clz class
     */
    public static String superClassName(Class<?> clz){
        Class<?> superClass = clz.getSuperclass();
        if(superClass != null)
            return superClass.getName();
        return null;
    }

    public static List<String> getInterfaces(Class<?> clz){

        List<String> l = CollectionUtils.newArrayList();

        Class<?>[] interfaces = clz.getInterfaces();
        if(ArrayUtils.isNotBlank(interfaces)){
            for (Class<?> i : interfaces) {
                l.add(i.getName());
            }
        }
        return l;
    }


    /**
     * invoke to create the object
     * @param clz object class
     * @param params create params
     */
    public static <T> T instance(Class<T> clz,Object... params){
        // 使用 constructor 去反射创建对象
        try {
            T t = clz.getConstructor().newInstance(params);
            return t;
        } catch (Exception e) {
            throw new RuntimeException("Bean [" + clz.getName() + "] create exception");
        }
    }
}
