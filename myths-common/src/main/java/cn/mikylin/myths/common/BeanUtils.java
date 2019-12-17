package cn.mikylin.myths.common;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * the utils for java bean.
 *
 * @author mikylin
 * @date 20190714
 */
public final class BeanUtils {

    /**
     * java bean to map.
     *
     * @param bean  object
     * @return params
     */
    public static Map<String,Object> toMap(Object bean) {

        Map<String,Object> map = MapUtils.newHashMap();

        BeanInfo info = info(bean.getClass());
        PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
        for(PropertyDescriptor descriptor : propertyDescriptors){
            String key = descriptor.getName();
            Method getterMethod = descriptor.getReadMethod();
            if(StringUtils.isNotBlank(key) && !key.equals(Constants.System.CLASS)){
                Object value = getValue(getterMethod,bean);
                map.put(key,value);
            }
        }
        return map;
    }

    /**
     * getter methods.
     *
     * @param bean  object
     * @return getters
     */
    public static Map<String,Method> gets(Object bean) {

        Map<String,Method> map = MapUtils.createMap();
        BeanInfo info = info(bean.getClass());
        for(PropertyDescriptor descriptor : info.getPropertyDescriptors()){
            String key = descriptor.getName();

            if(StringUtils.isNotBlank(key) && !key.equals(Constants.System.CLASS)){
                Method getterMethod = descriptor.getReadMethod();
                map.put(key,getterMethod);
            }
        }
        return map;
    }

    /**
     * setter methods.
     *
     * @param clz  object's class
     * @return setters
     */
    public static Map<String,Method> sets(Class clz) {

        Map<String,Method> map = MapUtils.createMap();
        BeanInfo info = info(clz);
        for(PropertyDescriptor descriptor : info.getPropertyDescriptors()){
            String key = descriptor.getName();
            if(StringUtils.isNotBlank(key) && !key.equals(Constants.System.CLASS)){
                Method setterMethod = descriptor.getWriteMethod();
                map.put(key,setterMethod);
            }
        }
        return map;
    }

    /**
     * getter methods and setter methods.
     *
     * @param bean object
     * @return getters and setters
     */
    public static Map<Method,Method> getAndSets(Object bean) {

        Map<Method,Method> map = MapUtils.newHashMap();
        BeanInfo info = info(bean.getClass());
        for(PropertyDescriptor descriptor : info.getPropertyDescriptors()){
            String key = descriptor.getName();

            if(StringUtils.isNotBlank(key) && !key.equals(Constants.System.CLASS)){
                Method setterMethod = descriptor.getWriteMethod();
                Method getterMethod = descriptor.getReadMethod();
                map.put(getterMethod,setterMethod);
            }
        }
        return map;
    }

    /**
     * get BeanInfo.
     *
     * @param clz  class
     * @return class's BeanInfo
     */
    private static BeanInfo info(Class clz) {
        try {
            return Introspector.getBeanInfo(clz);
        } catch (IntrospectionException e) {
            throw new RuntimeException("Object [" + clz.getName() + "] BeanInfo create exception");
        }
    }

    /**
     * set the value to the object.
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
