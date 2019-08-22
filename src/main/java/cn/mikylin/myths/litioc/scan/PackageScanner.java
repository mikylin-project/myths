package cn.mikylin.myths.litioc.scan;

import cn.mikylin.myths.common.NioLocal;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.List;

public class PackageScanner {

    public static List<Class> scanPackage(String path) throws IOException, URISyntaxException {

        URL resource = PackageScanner.class.getResource("");
        String path1 = resource.getPath();
        System.out.println();

        Path p = Paths.get(resource.toString() + path);

        if(Files.isDirectory(p)){
            DirectoryStream<Path> paths = Files.newDirectoryStream(p);
            paths.forEach(pt -> {
                if(pt.getFileName().endsWith(".class")){
                    System.out.println(pt.getFileName());
                }
            });
        }


        return null;
    }


    public static void test(String path){
        URL resource = PackageScanner.class.getResource("");
        System.out.println(resource.toString());
        System.out.println(resource.getPath());
    }


    public static Class scanClass(String path){
        return null;
    }


    public static void main(String[] args) throws IOException, URISyntaxException {
        //scanPackage("cn/mikylin/myths/common");
        scanPackage("java/base/javax/net");
    }
}
