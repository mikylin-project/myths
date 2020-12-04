package cn.mikylin.myths;

import cn.mikylin.utils.cache.file.RandomAccessFileOperator;
import cn.mikylin.utils.cache.serialize.KryoSerialize;
import cn.mikylin.utils.cache.serialize.Serialize;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class CacheTest {


    @Test
    public void test() {
        Serialize kryos = new KryoSerialize(20);
        RandomAccessFileOperator file
                = new RandomAccessFileOperator("d://filetest.log",20);

        People p = new People();
        p.age = 10;
        p.name = "小明";
        p.birthDay = new Date();

        byte[] serialize = kryos.serialize(p, People.class);

        file.write(serialize);


    }


    public static class People {
        int age;
        String name;
        Date birthDay;
    }
}
