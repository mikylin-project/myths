package cn.mikylin.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * kryo serialize frameworker
 * @author mikylin
 * @date 20190819
 */
public final class KryoUtils {

    /**
     * thread safe
     */
    private static ThreadLocal<Kryo> kryos;
    static {
        kryos = new ThreadLocal<>();
    }


    public static byte[] serialize(Object obj,int size,Class... clzs){

        Kryo kryo = getKryo();

        for(Class clz : clzs)
            kryo.register(clz);

        Output output = new Output(size);
        kryo.writeObject(output,obj);

        byte[] bytes = output.toBytes();
        output.close();
        return bytes;
    }

    public static <T> T unSerialize(byte[] bs,Class<T> clz){
        Kryo kryo = getKryo();

        kryo.register(clz);

        Input input = new Input(bs);
        Object o = kryo.readObject(input, clz);
        input.close();
        return (T)o;
    }

    private static Kryo getKryo(){
        Kryo kryo = kryos.get();
        if(kryo == null){
            kryo = new Kryo();
            kryos.set(kryo);
        }
        return kryo;
    }


    public static void main(String[] args) {

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
        byte[] serialize = serialize(ue, 100, UserEntity.class, Date.class);
        UserEntity o = unSerialize(serialize, UserEntity.class);
        System.out.println(o);

        /**
         * 序列化 list
         */
        List<UserEntity> list = new ArrayList<>();
        list.add(ue);
        byte[] serializeList = serialize(list, 100, UserEntity.class, Date.class,ArrayList.class);
        List<UserEntity> l = (List<UserEntity>)unSerialize(serializeList,ArrayList.class);
        System.out.println(l.get(0));


    }

}


@Data
class UserEntity {
    private String name;
    private Date birthDay;
    private Integer age;
}