package cn.mikylin.myths.common.reflect;

import cn.mikylin.myths.common.MapUtils;
import cn.mikylin.myths.common.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * the util for java bean
 * @author mikylin
 * @date 20190714
 */
public final class BeanUtils {


    private static final String CLASS = "class";

    /**
     * java bean to map
     * @param bean object
     */
    public static Map<String,Object> toMap(Object bean) {

        Map<String,Object> map = MapUtils.newHashMap();

        BeanInfo info = info(bean.getClass());
        PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
        for(PropertyDescriptor descriptor : propertyDescriptors){

            String key = descriptor.getName();
            Method getterMethod = descriptor.getReadMethod();
            if(StringUtils.isNotBlank(key) && !key.equals(CLASS)){
                Object value = getValue(getterMethod,bean);
                map.put(key,value);
            }

        }
        return map;
    }

    /**
     * getter methods
     * @param bean object
     */
    public static Map<String,Method> gets(Object bean) {

        Map<String,Method> map = MapUtils.createMap();
        BeanInfo info = info(bean.getClass());
        for(PropertyDescriptor descriptor : info.getPropertyDescriptors()){
            String key = descriptor.getName();

            if(StringUtils.isNotBlank(key) && !key.equals(CLASS)){
                Method getterMethod = descriptor.getReadMethod();
                map.put(key,getterMethod);
            }
        }
        return map;
    }

    /**
     * setter methods
     * @param clz object's class
     */
    public static Map<String,Method> sets(Class clz) {

        Map<String,Method> map = MapUtils.createMap();
        BeanInfo info = info(clz);
        for(PropertyDescriptor descriptor : info.getPropertyDescriptors()){
            String key = descriptor.getName();

            if(StringUtils.isNotBlank(key) && !key.equals(CLASS)){
                Method setterMethod = descriptor.getWriteMethod();
                map.put(key,setterMethod);
            }
        }
        return map;
    }

    /**
     * getter methods and setter methods
     * @param bean object
     */
    public static Map<Method,Method> getAndSets(Object bean) {

        Map<Method,Method> map = MapUtils.newHashMap();
        BeanInfo info = info(bean.getClass());
        for(PropertyDescriptor descriptor : info.getPropertyDescriptors()){
            String key = descriptor.getName();

            if(StringUtils.isNotBlank(key) && !key.equals(CLASS)){
                Method setterMethod = descriptor.getWriteMethod();
                Method getterMethod = descriptor.getReadMethod();
                map.put(getterMethod,setterMethod);
            }
        }
        return map;
    }

    /**
     * get BeanInfo
     */
    private static BeanInfo info(Class clz) {
        try {
            return Introspector.getBeanInfo(clz);
        } catch (IntrospectionException e) {
            throw new RuntimeException("Object [" + clz.getName() + "] BeanInfo create exception");
        }
    }

    /**
     * set the value to the object
     */
    private static void setValue(Method setter,Object bean,Object value) {
        try {
            setter.invoke(bean,value);
        } catch (Exception e) {
            throw new RuntimeException("Bean method [" + setter.getName() + "] invoke exception");
        }
    }

    /**
     * get the value in the object
     */
    private static Object getValue(Method getter,Object bean) {
        try {
            return getter.invoke(bean);
        } catch (Exception e) {
            throw new RuntimeException("Bean method [" + getter.getName() + "] invoke exception");
        }
    }

}
