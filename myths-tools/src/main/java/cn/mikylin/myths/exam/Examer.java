package cn.mikylin.myths.exam;

import cn.mikylin.myths.common.*;
import cn.mikylin.myths.common.lang.StringUtils;
import cn.mikylin.myths.exam.annotation.Check;
import cn.mikylin.myths.exam.base.*;
import cn.mikylin.myths.exam.wrapper.FieldWrapper;
import java.lang.reflect.Field;
import java.util.*;

/**
 * exam 门面类
 * @author mikylin
 * @date 20191012
 */
public class Examer {

    private FilterMap filterMap;
    private String name;

    public Examer(String name, BaseFilter<?>... filters) {
        if(StringUtils.isBlank(name))
            throw new RuntimeException("name can not be blank");
        this.name = name;
        filterMap = FilterMap.create(filters);
    }

    public Examer(String name,boolean autoScan,BaseFilter<?>... filters){
        this(name,filters);

        if(autoScan)
            autoCreateFilter();
    }

    public Examer addFilter(BaseFilter<?> filter) {
        filterMap.put(filter);
        return this;
    }

    public void autoCreateFilter() {
        filterMap.setAutoCreate(true);
    }


    /**
     * 暴露出的校验方法
     * @param entity 要验证的参数对象
     */
    public CheckResult exam(Object entity){
        try{
            exam0(entity);
        }catch (ExamException e){
            return e.getResult();
        }
        return CheckResult.getSuccessBuild().build();
    }

    private void exam0(Object entity){

        Class<?> entityClass = entity.getClass();
        Field[] fields = entityClass.getDeclaredFields();

        List<FieldWrapper> wrappers = wrapperFields(fields,entity);

        wrappers.forEach(fw -> examFields(fw));
    }

    private void examFields(FieldWrapper wrapper){

        /**
         * 校验当前参数的有效性
         */

        List<BaseFilter> filters = CollectionUtils.newArrayList();
        for(Class<? extends BaseFilter> filterClass : wrapper.check().filters()) {
            BaseFilter filter = filterMap.get(filterClass);
            if(filter != null)
                filters.add(filter);
        }

        Collections.sort(filters,new FilterWeigtComparer());

        for(BaseFilter f : filters) {
            try{
                f.filter(wrapper.value());
            }catch (FilterException e){

                CheckResult result = CheckResult.getFailBuild()
                                                .message(f.message(wrapper.name()))
                                                .build();
                throw new ExamException(result);
            }
        }

        /**
         * 如果这个参数是集合，就进入此段逻辑
         * 校验其下属的元素的逻辑
         */
        if(TypeUtils.isTheChildOrOwnType(Collection.class,wrapper.type())){

            Collection<?> col = (Collection<?>) wrapper.value();

            col.forEach(o -> exam0(o));
        }
    }


    /**
     * 过滤不需要校验的参数，留下需要的
     * 并进行包装
     */
    private List<FieldWrapper> wrapperFields(Field[] fs, Object entity){

        List<FieldWrapper> wrappers = CollectionUtils.newArrayList();

        for(Field f : fs){

            Check check = f.getAnnotation(Check.class);
            String[] group;
            if(check == null
                    || (!(group= check.group())[0].equals(Null.NULL_STRING)
                            && !ArrayUtils.isInArray(group,name)))
                continue;

            FieldWrapper wrapper = new FieldWrapper(f,entity);

            wrappers.add(wrapper);
        }
        return wrappers;
    }

}