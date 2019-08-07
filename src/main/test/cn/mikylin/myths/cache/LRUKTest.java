package cn.mikylin.myths.cache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class LRUKTest {


    public static void main(String[] args) {
        int cap = 1000000;

        CacheBuilder.LRUKMapBuilder<String, String> objectObjectLRUKMapBuilder = CacheBuilder.lurK();
        Map<String,String> map = objectObjectLRUKMapBuilder.cap(cap).build();

        //Map<String,String> map = new HashMap<>(cap);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        System.out.println("set begin time : " + simpleDateFormat.format(new Date()));
        for(int i = 0 ; i < cap ; i ++){
            map.put(String.valueOf(i),String.valueOf(i));
        }
        System.out.println("set end time : " + simpleDateFormat.format(new Date()));

        String s = map.get(String.valueOf(cap - 1000));
        System.out.println("find end time : " + simpleDateFormat.format(new Date()));
        System.out.println(s);
    }
}
