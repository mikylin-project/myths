package cn.mikylin.myths.litioc.container;

import cn.mikylin.myths.common.MapUtils;

import java.util.Map;

public class Container {

    private Map<String,BeanDefinition> beans;

    public Container(){
        beans = MapUtils.newConcurrentMap();
    }

    public void put(String name,BeanDefinition db){
        if(!beans.containsKey(name))
            beans.put(name,db);
    }

    public Object get(String name){
        BeanDefinition db = beans.get(name);
        if(db != null){
            return db.getBean();
        }
        return null;
    }
}
