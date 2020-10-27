package cn.mikylin.utils.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * kryo serialize frameworker.
 *
 * @author mikylin
 * @date 20190819
 */
public final class KryoUtils {

    /**
     * thread safe kryos
     */
    private static ThreadLocal<Kryo> kryos;
    static {
        kryos = new ThreadLocal<>();
    }

    private static Kryo getKryo() {
        Kryo kryo = kryos.get();
        if(kryo == null) {
            kryo = new Kryo();
            kryos.set(kryo);
        }
        return kryo;
    }


    public static byte[] serialize(Object obj,int size,Class... clzs) {

        Kryo kryo = getKryo();

        for(Class clz : clzs)
            kryo.register(clz);

        Output output = new Output(size);
        kryo.writeObject(output,obj);

        byte[] bytes = output.toBytes();
        output.close();
        return bytes;
    }

    public static <T> T unSerialize(byte[] bs,Class<T> clz) {
        Kryo kryo = getKryo();

        kryo.register(clz);

        Input input = new Input(bs);
        Object o = kryo.readObject(input, clz);
        input.close();
        return (T)o;
    }

    /**
     * deep copy.
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T deepCopy(T t) {
        Kryo kryo = getKryo();
        return kryo.copy(t);
    }

}
