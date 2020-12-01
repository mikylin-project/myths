package cn.mikylin.utils.cache.serialize;

public interface Serialize {

    <T> byte[] serialize(Object o,Class<T> clz);

    <T> T unserialize(byte[] data,Class<T> clz);
}
