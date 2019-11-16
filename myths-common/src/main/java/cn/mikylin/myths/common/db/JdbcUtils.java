package cn.mikylin.myths.common.db;

import cn.mikylin.myths.common.BeanNameUtils;
import cn.mikylin.myths.common.BeanUtils;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jdbc assemble bean utils
 *
 * @author mikylin
 * @date 20191015
 */
public final class JdbcUtils {

    private static Map<Class,ResultSetFactory> factoryMap = new HashMap<>();
    static {
        factoryMap.put(Integer.class, (ResultSetFactory<Integer>) (rs, columName) -> rs.getInt(columName));
        factoryMap.put(String.class, (ResultSetFactory<String>) (rs, columName) -> rs.getString(columName));
        factoryMap.put(Long.class, (ResultSetFactory<Long>) (rs, columName) -> rs.getLong(columName));
        factoryMap.put(Double.class, (ResultSetFactory<Double>) (rs, columName) -> rs.getDouble(columName));
        factoryMap.put(Float.class, (ResultSetFactory<Float>) (rs, columName) -> rs.getFloat(columName));
        factoryMap.put(Date.class, (ResultSetFactory<Date>) (rs, columName) -> rs.getTimestamp(columName));
    }


    private interface ResultSetFactory<T> {
        T get(ResultSet rs, String columName) throws SQLException;
    }

    /**
     * assemble bean
     */
    public static <T> void invoke(ResultSet rs, T t,
                                  BeanNameUtils.BeanNameType originType,
                                  BeanNameUtils.BeanNameType transType) {

        Class tClass = t.getClass();
        Map<String, Method> setters = BeanUtils.sets(tClass);
        setters.forEach((k,v) -> {
            String underLineName = BeanNameUtils.trans(k,originType,transType);
            ResultSetFactory rf = factoryMap.get(tClass);
            if(rf != null){
                try {
                    v.invoke(t,rf.get(rs,underLineName));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("jdbc utils invoke exception");
                }
            }
        });
    }

    public static <T> void invoke(ResultSet rs, T t,
                                  String originType,
                                  String transType) {
        invoke(rs,t,
                BeanNameUtils.nameTypeMap.get(originType),
                BeanNameUtils.nameTypeMap.get(transType));
    }

    public static <T> void invoke(ResultSet rs, T t) {
        invoke(rs,t,
                BeanNameUtils.BeanNameType.UNDERLINE,
                BeanNameUtils.BeanNameType.HUMP);
    }
}
