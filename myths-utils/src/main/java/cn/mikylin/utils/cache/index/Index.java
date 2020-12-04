package cn.mikylin.utils.cache.index;

/**
 * index.
 *
 * @author mikylin
 * @date 20201204
 */
public interface Index {

    long keyHashCode();

    long offset();

    long len();
}
