package cn.mikylin.myths.common.concurrent.onoff;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 开关工具
 * 适用于需要保障线程安全的代码部分
 *
 * @author mikylin
 * @date 20190623
 */
public interface CasOnOff<T> {

    Map<CasOnOff,AtomicBoolean> lockMap = new ConcurrentHashMap<>();

    default AtomicBoolean getCasLock(){
        AtomicBoolean lock;
        if(Objects.isNull(lock = lockMap.get(this))){
            synchronized (this){
                if(Objects.isNull(lock = lockMap.get(this))){
                    lock = new AtomicBoolean(true); //true - open , false - close
                    lockMap.put(this,lock);
                }
            }
        }
        return lock;
    }

    /**
     * 由使用者去实现的业务代码
     */
    void onOff0(T t);

    /**
     * 不同的 on-off 策略所实现的线程安全策略代码
     */
    void onOff(T t);
}
