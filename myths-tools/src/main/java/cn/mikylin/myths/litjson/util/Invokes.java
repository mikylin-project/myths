package cn.mikylin.myths.litjson.util;

import cn.mikylin.myths.common.reflect.BeanUtils;
import cn.mikylin.myths.common.reflect.ClassUtils;
import cn.mikylin.myths.litjson.exception.JSONObjectInvokeException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * helper for java invoke api
 * @author mikylin
 */
public final class Invokes {

    /**
     * invoke to create the object
     */
    public static Object create(Class clz){
        try {
            return ClassUtils.instance(clz);
        } catch (RuntimeException e) {
            throw new JSONObjectInvokeException(e.getMessage());
        }
    }

    /**
     * bean to map
     */
    public static Map<String,Object> beanToMap(Object o){

        try{
            return BeanUtils.toMap(o);
        }catch (RuntimeException e){
            throw new JSONObjectInvokeException(e.getMessage());
        }

    }

    /**
     * get set methods
     */
    public static Map<String,Method> getSetMethods(Class clz){
        try{
            return BeanUtils.sets(clz);
        }catch (RuntimeException e){
            throw new JSONObjectInvokeException(e.getMessage());
        }
    }


    /**
     * set method
     */
    public static void invokeSet(Method setter,Object bean,Object value){
        try {
            setter.invoke(bean,value);
        } catch (Exception e) {
            throw new JSONObjectInvokeException("Value [" + value + "] set method [" + setter.getName() + "] exception");
        }
    }


    public static Class genericityClass(Class clazz,String fieldName){
        try {
            Field f = clazz.getDeclaredField(fieldName);
            ParameterizedType parameterizedType = (ParameterizedType) f.getGenericType();
            return (Class)parameterizedType.getActualTypeArguments()[0];
        } catch (NoSuchFieldException e) {
            throw new JSONObjectInvokeException("Object param [" + fieldName + "] genericity get exception");
        }
    }


    public static Class<?> methodParam(Method setter){
        return setter.getParameterTypes()[0];
    }
}
