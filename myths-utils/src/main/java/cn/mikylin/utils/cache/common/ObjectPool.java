package cn.mikylin.utils.cache.common;

public interface ObjectPool<T> {

    T getObject();

    void returnObject(T t);



    interface ObjectLoader<T> {
        T load();
    }
}
