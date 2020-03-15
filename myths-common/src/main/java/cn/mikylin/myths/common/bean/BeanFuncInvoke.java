package cn.mikylin.myths.common.bean;

/**
 * stream interface.
 *
 * @author mikylin
 * @date 20200314
 */
@FunctionalInterface
public interface BeanFuncInvoke<T,P> {

    void accept(T t, P pS);
}
