package cn.mikylin.myths.cache.array;

import java.util.Objects;

public class DefaultArrayManager<T> implements ArrayManager<T>{

    private Object[] array;
    private int size;

    public DefaultArrayManager(int size){
        this.array = new Object[size];
        this.size = size;
    }

    @Override
    public T find(T t) {

        Objects.requireNonNull(t);

        for(int i = 0 ; i < size ; i ++){
            T e;
            if((e = (T)array[i]) != null && t.equals(e))
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

        for(int i = 0 ; i < size ; i ++){
            if(array[i] == null){
                array[i] = t;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean replace(T oldElement, T newElement) {

        Objects.requireNonNull(oldElement);

        for(int i = 0 ; i < size ; i ++){
            T e;
            if((e = (T)array[i]) != null && oldElement.equals(e)){
                array[i] = newElement;
                return true;
            }
        }
        return false;
    }

    @Override
    public void change(int newSize) {

        if(newSize <= 0)
            throw new RuntimeException("size exception.");

        Object[] newObjs = new Object[newSize];
        int newObjsSize = 0;

        for(int i = 0 ; i < size ; i ++){
            if(array[i] != null){
                newObjs[newObjsSize] = array[i];
                if(++ newObjsSize >= newSize)
                    return;
            }
        }
    }

}
