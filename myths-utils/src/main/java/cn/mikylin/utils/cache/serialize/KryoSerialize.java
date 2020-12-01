package cn.mikylin.utils.cache.serialize;

import cn.mikylin.utils.cache.common.ObjectPool;
import cn.mikylin.utils.serial.KryoUtils;
import com.esotericsoftware.kryo.Kryo;

public final class KryoSerialize implements Serialize {

    private final ObjectPool<Kryo> objectPool;

    public KryoSerialize(int size) {
        this.objectPool = new ObjectPool<>(size, () -> new Kryo());
    }

    @Override
    public <T> byte[] serialize(Object o,Class<T> clz) {
        Kryo kryo = null;
        try {
            kryo = objectPool.getObject();
            return KryoUtils.serialize(kryo,o,clz);
        } finally {
            objectPool.returnObject(kryo);
        }
    }

    @Override
    public <T> T unserialize(byte[] data, Class<T> clz) {
        Kryo kryo = null;
        try {
            kryo = objectPool.getObject();
            return KryoUtils.unSerialize(kryo,data,clz);
        } finally {
            objectPool.returnObject(kryo);
        }
    }
}
