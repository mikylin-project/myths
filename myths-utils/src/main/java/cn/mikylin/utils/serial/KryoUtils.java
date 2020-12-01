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

    public static void registeClass(Kryo kryo,Class... clzs) {
        for (Class clz : clzs)
            kryo.register(clz);
    }

    public static void registeClass(Class... clzs) {
        Kryo kryo = getKryo();
        registeClass(kryo,clzs);
    }



    public static <T> byte[] serialize(Object obj,Class<T> clz) {
        Kryo kryo = getKryo();
        return serialize(kryo,obj,clz);
    }

    public static <T> byte[] serialize(Kryo kryo,Object obj,Class<T> clz) {

        registeClass(kryo,clz);

        try (Output output = new Output()) {
            kryo.writeObject(output,obj);
            return output.toBytes();
        }
    }

    public static <T> T unSerialize(byte[] bs,Class<T> clz) {
        Kryo kryo = getKryo();
        return unSerialize(kryo,bs,clz);
    }

    public static <T> T unSerialize(Kryo kryo,byte[] bs,Class<T> clz) {

        registeClass(kryo,clz);

        try (Input input = new Input(bs)) {
            return (T) kryo.readObject(input, clz);
        }
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
