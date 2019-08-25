package cn.mikylin.myths.litioc.classload;

/**
 * package class loader
 * @author mikylin
 * @date 20190824
 */
public class PackageClassLoader extends ClassLoader{

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }
}
