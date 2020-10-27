package cn.mikylin.myths;

import cn.mikylin.utils.serial.KryoUtils;
import com.esotericsoftware.kryo.Kryo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KryoTest {

    @Test
    public void testKryo() {

        Kryo kryo = new Kryo();
        kryo.register(UserEntity.class);
        kryo.register(Date.class);


        UserEntity ue = new UserEntity();
        ue.setAge(1);
        ue.setBirthDay(new Date());
        ue.setName("hahaha");


        /**
         * 序列化 object
         */
        byte[] serialize = KryoUtils.serialize(ue, 100, UserEntity.class, Date.class);
        UserEntity o = KryoUtils.unSerialize(serialize, UserEntity.class);
        System.out.println(o);

        /**
         * 序列化 list
         */
        List<UserEntity> list = new ArrayList<>();
        list.add(ue);
        byte[] serializeList = KryoUtils.serialize(list, 100, UserEntity.class, Date.class,ArrayList.class);
        List<UserEntity> l = (List<UserEntity>)KryoUtils.unSerialize(serializeList,ArrayList.class);
        System.out.println(l.get(0));
    }
}
