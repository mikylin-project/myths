package cn.mikylin.myths.exam;

import cn.mikylin.myths.common.*;
import cn.mikylin.myths.exam.base.BaseFilter;

import java.util.Map;

final class FilterMap {

    static FilterMap create(BaseFilter... filters){
        return new FilterMap(filters);
    }

    private Map<Class<? extends BaseFilter>,BaseFilter> filterMap;
    private Boolean autoCreate;

    private FilterMap(BaseFilter... filters){

        if(ArrayUtils.isNotBlank(filters)) {
            this.filterMap = MapUtils.newHashMap(filters.length);
            for(BaseFilter f : filters)
                filterMap.put(f.getClass(),f);
        }else{
            this.filterMap = MapUtils.newHashMap();
        }

        autoCreate = Boolean.FALSE;
    }

    public void setAutoCreate(Boolean b){
        autoCreate = b;
    }

    BaseFilter get(Class<? extends BaseFilter> key){
        BaseFilter filter = filterMap.get(key);
        if(filter == null && autoCreate){
            filter = ClassUtils.instance(key);
        }
        return filter;
    }

    void put(BaseFilter filter){
        filterMap.put(filter.getClass(),filter);
    }

}
