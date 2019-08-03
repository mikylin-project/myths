package cn.mikylin.myths.cache.reference;

import java.lang.ref.Reference;

public abstract class ReferenceManager<T,Ref extends Reference<T>> {

    private Object[] referenceArray;
    private int size;

    ReferenceManager(int size){
        this.size = size;
        referenceArray = new Object[size];
    }


    public T find(T t){

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

    public void set(T t){

        for(int i = 0 ; i < size ; i ++){
            Ref ref = (Ref)referenceArray[i];
            T value;
            if(ref != null && (value = ref.get()) != null){
                if(value == null){
                    referenceArray[i] = create(t);
                    return;
                }
            }
        }

    }

    abstract Ref create(T t);

}
