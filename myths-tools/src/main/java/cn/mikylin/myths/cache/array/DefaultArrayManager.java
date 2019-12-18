package cn.mikylin.myths.cache.array;

import java.util.Objects;

/**
 * array manager
 * @author mikylin
 * @date 20190806
 */
public final class DefaultArrayManager<T> implements ArrayManager<T> {

    private Object[] array;
    private int cap;
    private volatile int index;

    public DefaultArrayManager(int cap) {
        this.array = new Object[cap];
        this.cap = cap;
        this.index = 0;
    }

    @Override
    public T find(T t) {

        Objects.requireNonNull(t);

        for(int i = 0 ; i <= index ; i ++) {
            T e;
            if((e = (T)array[i]) != null
                    && (t == e || t.equals(e)))
                return e;
        }
        return null;
    }

    @Override
    public boolean remove(T t) {
        return replace(t,null);
    }

    @Override
    public boolean add(T t) {
        Objects.requireNonNull(t);

        if(index + 1 == cap)
            change(cap);

        array[++ index] = t;
        return false;
    }

    @Override
    public boolean replace(T oldElement, T newElement) {

        Objects.requireNonNull(oldElement);

        int number = 0;

        for(int i = 0 ; i <= index ; i ++)
            if(Objects.equals(array[i],oldElement)) {
                array[i] = newElement;
                number ++;
            }

        return number != 0;
    }

    @Override
    public void change(int newSize) {

        if(newSize <= 0)
            throw new RuntimeException("size exception.");

        Object[] newObjs = new Object[newSize];
        int newObjsSize = 0;

        for(int i = 0 ; i < cap ; i ++) {
            if(array[i] != null){
                newObjs[newObjsSize] = array[i];
                if(++ newObjsSize >= newSize) {
                    index = newSize - 1;
                    return;
                }
            }
        }
    }

}
