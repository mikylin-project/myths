package cn.mikylin.myths.cache.array;

/**
 * interface for array base manager.
 *
 * @author mikylin
 * @date 20190807
 */
public interface ArrayManager<T> {

    /**
     * find the element in the array
     */
    T find(T t);

    /**
     * remove the element in the array
     */
     boolean remove(T t);

    /**
     * add the element in the array
     */
    boolean add(T t);


    /**
     * replace the element in the array
     */
    boolean replace(T oldElement,T newElement);

    /**
     * change the array size
     */
    void change(int newSize);

}
