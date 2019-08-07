package cn.mikylin.myths.cache.array;

import java.lang.ref.Reference;
import java.util.Objects;

/**
 * java reference abstract base manager
 * @author mikylin
 * @date 20190729
 */
public abstract class ReferenceManager<T,Ref extends Reference<T>> implements ArrayManager<T>{

    private Object[] referenceArray;
    private int size;

    ReferenceManager(int size){
        this.size = size;
        referenceArray = new Object[size];
    }


    @Override
    public T find(T t){

        Objects.requireNonNull(t);

        for(int i = 0 ; i < size ; i ++){
            Ref ref = (Ref)referenceArray[i];
            T value;
            if(ref != null
                && (value = ref.get()) != null
                && t.equals(value))
                return value;
        }

        return null;
    }


    abstract Ref create(T t);



    @Override
    public boolean remove(T t) {
        throw new RuntimeException("can not remove.");
    }

    @Override
    public boolean add(T t) {
        for(int i = 0 ; i < size ; i ++) {
            Ref ref = (Ref)referenceArray[i];
            T value;
            if(ref != null && (value = ref.get()) != null){
                if(value == null){
                    referenceArray[i] = create(t);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean replace(T oldElement, T newElement) {
        throw new RuntimeException("can not replace.");
    }

    @Override
    public void change(int newSize) {
        throw new RuntimeException("can not change size.");
    }
}
