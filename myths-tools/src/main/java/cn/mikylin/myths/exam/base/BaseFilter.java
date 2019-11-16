package cn.mikylin.myths.exam.base;

import cn.mikylin.myths.common.TypeUtils;

public abstract class BaseFilter<T> {

    /**
     * 当前泛型的 class 对象
     */
    private Class<T> tClz = TypeUtils.firstGeneric(this.getClass().getGenericSuperclass());

    /**
     * 使用者要自定义的过滤业务逻辑
     *
     * 返回 ：
     * true - 通过校验
     * false - 没有通过校验
     */
    protected abstract Boolean doFilter(T t) throws Exception;

    /**
     * 使用者可以自定义的返回的业务信息
     * 可以根据业务需求重写该方法
     * @param columnName 参数名称
     */
    public String message(String columnName){
        return columnName + " can not be pass in " + getClass().getName();
    }

    /**
     * 判断传入参数的类型
     * @param t 传入的参数
     * @return 转换类型之后的 t
     */
    private T transClass(Object t){

        if(t == null)
            return null;

        if(TypeUtils.isTheChildOrOwnType(tClz,t.getClass()))
            return (T)t;

        throw new TransException();
    }


    /**
     * 过滤业务逻辑的包装方法
     * @param t
     */
    public final void filter(Object t){

        T objT = transClass(t);

        try {
            if(!doFilter(objT))
                throw new FilterException();

        } catch (Exception e) {
            throw new FilterException();
        }
    }

    /**
     * filter 权重设置
     * 权重数值越大则越先
     * @return 权重数值
     */
    public int weight(){
        return 0;
    }

}

