package cn.mikylin.myths.common;

import java.io.File;
import java.util.List;
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
        classPath = dealClassPath(classPath);
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class load exception");
        }
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
            throw new RuntimeException("class load exception");
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
        Objects.requireNonNull(clz);
        String pakName;
        if(StringUtils.isNotBlank(pakName = clz.getPackageName()))
            return pakName;
        return null;
    }

    /**
     * get class's super class name
     *
     * @param clz class
     * @return class's super class
     */
    public static String superClassName(Class<?> clz) {
        Class<?> superClass;
        if((superClass = clz.getSuperclass()) != null)
            return superClass.getName();
        return null;
    }

    public static List<String> getInterfaces(Class<?> clz) {

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
     *
     * @param clz object class
     * @param params create params
     * @return T
     */
    public static <T> T instance(Class<T> clz,Object... params) {
        // 使用 constructor 去反射创建对象
        try {
            T t = clz.getConstructor().newInstance(params);
            return t;
        } catch (Exception e) {
            throw new RuntimeException("Bean [" + clz.getName() + "] create exception");
        }
    }
}
