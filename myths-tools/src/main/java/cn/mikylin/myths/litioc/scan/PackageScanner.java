package cn.mikylin.myths.litioc.scan;

import cn.mikylin.myths.common.NioLocal;
import cn.mikylin.myths.common.reflect.ClassUtils;
import cn.mikylin.myths.litioc.classload.PackageClassLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 */
public final class PackageScanner {

    private static PackageClassLoader pkgClassLoader = new PackageClassLoader();
    private static String tailClass = ".class";
    private static String tailJar = ".jar";

    /**
     * 从目录结构中获取 class 文件
     */
    private static void findClassesByFile(String classPath,String packagePath, Set<Class<?>> classes) throws IOException {

        // 获取 path 对象
        Path path = NioLocal.getDirPath(classPath);

        DirectoryStream<Path> paths = Files.newDirectoryStream(path);
        if(Files.size(path) == 0) return;

        paths.forEach(p -> {

            File f = p.toFile();
            String pcLoaclPath = f.getPath();
            String lastPath = f.getName();
            String newPackagePath = packagePath + File.separator + lastPath;
            if(Files.isDirectory(p)){  // 递归目录
                try {
                    findClassesByFile(pcLoaclPath,newPackagePath,classes);
                } catch (IOException e) { }
            }else if(lastPath.endsWith(tailClass)){  // 加载 class
                setClass(newPackagePath,classes);
            }
        });
    }

    /**
     * 从 jar 中获取 class 文件
     */
    private static void findClassesByJar(JarFile jar, Set<Class<?>> classes) {

        Enumeration<JarEntry> entry = jar.entries();

        while (entry.hasMoreElements()) {
            JarEntry jarEntry = entry.nextElement();

            String name = jarEntry.getName();
            if (name.charAt(0) == '/')
                name = name.substring(1);

            if (name.endsWith(tailClass)) {
                // 去掉后面的".class", 将路径转为package格式
                name = name.substring(0, name.length() - 6);
                name = name.replaceAll(File.separator, "\\.");
                setClass(name,classes);
            }
        }
    }

    /**
     * class 存入 set 当中
     */
    private static void setClass(String path,Set<Class<?>> set){
        Class<?> clz = ClassUtils.loadClass(path,pkgClassLoader);
        if (clz != null)
            set.add(clz);
    }






    /**
     * 获取 class 的接口
     */
    public static Set<Class<?>> findClass(String packagePath) throws IOException {

        Set<Class<?>> classes = new HashSet<>();

        //　获取本地绝对路径
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        String classPath = resource.getFile();
        packagePath = packagePath.trim();
        classPath = classPath + packagePath.replaceAll("\\.", File.separator);

        // 扫描 jar 包
        if(classPath.endsWith(tailJar)){
            JarFile file = new JarFile(classPath);
            findClassesByJar(file,classes);
            return classes;
        }

        // 扫描项目路径
        findClassesByFile(classPath,packagePath,classes);
        return classes;
    }


    public static void main(String[] args) throws IOException {

        String packagePath = "cn.mikylin";
        Set<Class<?>> classes = findClass(packagePath);
        System.out.println(classes.size());
    }

}
