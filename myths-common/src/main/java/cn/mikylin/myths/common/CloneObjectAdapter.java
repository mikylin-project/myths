package cn.mikylin.myths.common;

/**
 * clone util for object adapter.
 *
 * @author mikylin
 * @date 20191108
 */
public class CloneObjectAdapter<T> implements CloneObject<T> {

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
