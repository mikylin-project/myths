package cn.mikylin.myths.litioc;

import cn.mikylin.myths.common.reflect.ClassUtils;
import cn.mikylin.myths.litioc.annotation.Component;
import cn.mikylin.myths.litioc.scan.PackageScanner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Litioc {

    public static Map<Class,Object> container(Properties p){

        Map<Class,Object> map = new HashMap<>();

        String scan = "cn.mikylin";

        Set<Class<?>> classSet;
        try {
            classSet = PackageScanner.findClass(scan);
        } catch (IOException e) {
            throw new RuntimeException();
        }

        for(Class<?> clz : classSet){
            Component com = clz.getAnnotation(Component.class);
            if(com != null){
                map.put(clz,ClassUtils.instance(clz));
            }
        }
        return map;
    }


    public static void main(String[] args) {
        Map<Class, Object> container = Litioc.container(new Properties());
        Object o = container.get(Person.class);
        System.out.println(((Person)o).get());
    }
}
