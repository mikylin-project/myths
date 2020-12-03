package cn.mikylin.utils.cache.serialize;

/**
 * serialize.
 *
 * @author mikylin
 * @date 20201203
 */
public interface Serialize {

    <T> byte[] serialize(Object o,Class<T> clz);

    <T> T unserialize(byte[] data,Class<T> clz);
}
