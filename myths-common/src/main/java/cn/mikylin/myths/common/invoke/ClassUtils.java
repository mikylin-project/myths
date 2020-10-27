package cn.mikylin.myths.common.invoke;

import cn.mikylin.myths.common.*;
import cn.mikylin.myths.common.lang.*;
import java.io.File;
import java.util.Objects;

/**
 * class utils.
 *
 * @author mikylin
 * @date 20190714
 */
public final class ClassUtils {

    /**
     * load class by default class loader.
     *
     * @param classPath  class path
     * @throws RuntimeException
     * @return class
     */
    public static Class<?> loadClass(String classPath) {
        return loadClass(classPath,null);
    }

    /**
     * load class by self class loader.
     *
     * @param classPath  class path
     * @param loader  class loader
     * @return class
     */
    public static Class<?> loadClass(String classPath,ClassLoader loader) {
        if(loader == null)
            loader = ClassUtils.class.getClassLoader();
        classPath = dealClassPath(classPath);
        try {
            return loader.loadClass(classPath);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * change the class path.
     *
     * @param classPath
     *          example 'java.lang.String.class'
     * @return new class path
     *          example 'java/lang/String'
     */
    private static String dealClassPath(String classPath) {
        StringUtils.requireNotBlank(classPath,"class path can not be blank");
        if(classPath.endsWith(Constants.System.POINT_CLASS) && classPath.length() > 6)
            classPath = classPath.substring(0,classPath.length() - 6);
        classPath = classPath.replaceAll(File.separator, Constants.System.POINT);
        return classPath;
    }


    /**
     * get class's package name.
     *
     * @param clz  class
     * @throws NullPointerException
     * @return class's package name
     */
    public static String packageName(Class<?> clz) {
        return Objects.isNull(clz) ? null : clz.getPackageName();
    }

    /**
     * get class's super class name
     *
     * @param clz class
     * @return class's super class
     */
    public static Class<?> superClass(Class<?> clz) {
        return Objects.isNull(clz) ? null : clz.getSuperclass();
    }

    public static Class<?>[] getInterfaces(Class<?> clz) {
        return Objects.isNull(clz) ? null : clz.getInterfaces();
    }


    /**
     * invoke to create the object
     *
     * @param clz object class
     * @param params create params
     * @return T
     */
    public static <T> T instance(Class<T> clz,Object... params) {
        // 使用 constructor 去反射创建对象
        try {
            return clz.getConstructor().newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException("Bean [" + clz.getName() + "] create exception");
        }
    }
}
