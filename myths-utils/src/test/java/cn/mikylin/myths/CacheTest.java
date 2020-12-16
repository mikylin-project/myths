package cn.mikylin.myths;

import cn.mikylin.utils.cache.file.FileOperator;
import cn.mikylin.utils.cache.file.RandomAccessFileOperator;
import cn.mikylin.utils.cache.serialize.KryoSerialize;
import cn.mikylin.utils.cache.serialize.Serialize;
import org.junit.jupiter.api.Test;
import java.io.IOException;

public class CacheTest {


    @Test
    public void test() throws IOException {

        String fileName = "/Users/qutianyu/tmp/filetest.log";

        Serialize kryos = new KryoSerialize(20);
        FileOperator fileOperator
                = new RandomAccessFileOperator(fileName);

        People p = new People();
        p.age = 10;
        p.name = "小明";
//        p.birthDay = new Date();

        byte[] serialize = kryos.serialize(p, People.class);
        int len = serialize.length;

        long begin = fileOperator.write(serialize);

        byte[] read = fileOperator.read(begin, len);
        People unserialize = kryos.unserialize(read, People.class);

        System.out.println(unserialize);
    }


    public static class People {
        int age;
        String name;
//        Date birthDay;
    }
}
