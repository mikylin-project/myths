package cn.mikylin.utils.cache.common;

/**
 * object loader.
 *
 * @author mikylin
 * @date 20201204
 */
public interface ObjectLoader<T> {

    T load();
}
