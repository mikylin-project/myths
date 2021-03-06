package cn.mikylin.myths.common.db;

import cn.mikylin.myths.common.*;
import cn.mikylin.myths.common.bean.*;
import cn.mikylin.myths.common.invoke.ClassUtils;
import cn.mikylin.myths.common.lang.ObjectUtils;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jdbc assemble bean utils
 *
 * @author mikylin
 * @date 20191015
 */
public final class JdbcUtils {

    /**
     * type factory
     */
    private static Map<Class,ResultSetFactory> factoryMap = new HashMap<>();
    static {
        factoryMap.put(Integer.class, (ResultSetFactory<Integer>) (rs, columName) -> rs.getInt(columName));
        factoryMap.put(String.class, (ResultSetFactory<String>) (rs, columName) -> rs.getString(columName));
        factoryMap.put(Long.class, (ResultSetFactory<Long>) (rs, columName) -> rs.getLong(columName));
        factoryMap.put(Double.class, (ResultSetFactory<Double>) (rs, columName) -> rs.getDouble(columName));
        factoryMap.put(Float.class, (ResultSetFactory<Float>) (rs, columName) -> rs.getFloat(columName));
        factoryMap.put(Date.class, (ResultSetFactory<Date>) (rs, columName) -> rs.getTimestamp(columName));
    }
    public interface ResultSetFactory<T> {
        T get(ResultSet rs, String columName) throws SQLException;
    }

    public static <T> void addFactory(Class<T> clz,ResultSetFactory<T> factory) {
        if(factory != null && clz != null)
            factoryMap.put(clz,factory);
    }

    /**
     * assemble bean
     */
    private static <T> void invoke(ResultSet rs,T t,
                                  BeanNameUtils.BeanNameType originType,
                                  BeanNameUtils.BeanNameType transType) {

        Class tClass = t.getClass();
        Map<String, Method> setters = BeanUtils.sets(tClass);
        setters.forEach((k,v) -> {
            // name
            String underLineName = BeanNameUtils.trans(k,originType,transType);

            // get class
            Class[] paramTypes = v.getParameterTypes();
            if(paramTypes == null || paramTypes.length != 1)
                throw new RuntimeException("jdbc utils invoke set param exception.");
            Class paramType = paramTypes[0];

            // get factory
            ResultSetFactory rf = factoryMap.get(paramType);

            // invoke
            if(rf != null) {
                try {
                    v.invoke(t,rf.get(rs,underLineName));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("jdbc utils invoke exception");
                }
            }
        });
    }

    public static <T> List<T> select(Connection conn,String sql,Class<T> clz) {
        return select(conn,sql,clz,
                BeanNameUtils.BeanNameType.UNDERLINE,
                BeanNameUtils.BeanNameType.HUMP);
    }

    public static <T> List<T> select(Connection conn,String sql,Class<T> clz,
                                     BeanNameUtils.BeanNameType originType,
                                     BeanNameUtils.BeanNameType transType) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(sql);
            return select(ps,clz,originType,transType);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("");
        }
    }



    public static <T> List<T> select(PreparedStatement ps,Class<T> clz,
                                     BeanNameUtils.BeanNameType originType,
                                     BeanNameUtils.BeanNameType transType) {
        List<T> list = CollectionUtils.newArrayList();
        try {
            T domain = ClassUtils.instance(clz);
            ResultSet rs = ps.executeQuery();
            for(;rs.next();)
                invoke(rs,domain,originType,transType);
            list.add(domain);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("select exception.");
        } finally {
            ObjectUtils.close(ps);
        }
        return list;
    }
}
