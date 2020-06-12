package cn.mikylin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import java.util.*;

/**
 * jackson utils.
 *
 * @author mikylin
 * @date 20200515
 */
public final class JacksonUtils {

    private static volatile ObjectMapper m;

    private static ObjectMapper objectMapper() {
        if(m == null) {
            synchronized (JacksonUtils.class) {
                if(m == null) {
                    ObjectMapper om = new ObjectMapper();
                    om.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
                    // 忽略 java 对象中不存在的 json 字段
                    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    m = om;
                }
            }
        }
        return m;
    }


    /**
     * object to json string.
     *
     * @param o  object
     * @return  json string
     */
    public static String toJson(Object o) {
        try {
            return objectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json string to object.
     *
     * @param json  json string
     * @param clz  object class
     * @return  object
     */
    public static <T> T toObject(String json,Class<T> clz) {
        try {
            return objectMapper().readValue(json,clz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json string to collection.
     *
     * @param json  json string
     * @param clz  object class
     * @return  LinkedList<clz>
     */
    public static <T> List<T> toList(String json,Class<T> clz) {
        return (List<T>) toList(json,LinkedList.class,clz);
    }

    /**
     * json string to List<Map<String,Object>>.
     *
     * @param json  json string
     * @return  List<Map<String,Object>>
     */
    public static List<Map<String,Object>> toListMap(String json) {
        if(json == null)
            throw new IllegalArgumentException();

        try {
            JavaType jt = getCollectionType(LinkedList.class,Map.class);
            return objectMapper().readValue(json,jt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * List<Object> to List<Map<String,Object>>.
     *
     * @param objs  List<Object>
     * @return  List<Map<String,Object>>
     */
    public static List<Map<String,Object>> toListMap(List objs) {
        return toListMap(toJson(objs));
    }

    /**
     * json string to collection.
     *
     * @param json  json string
     * @param coll  collection class
     * @param clz  object class
     * @return  coll<clz>
     */
    public static <C extends Collection,T> Collection<T> toList(String json,Class<C> coll,Class<T> clz) {

        if(json == null
                || coll == null || clz == null)
            throw new IllegalArgumentException();

        try {
            JavaType jt = getCollectionType(coll, clz);
            return objectMapper().readValue(json,jt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper().getTypeFactory()
                .constructParametricType(collectionClass,elementClasses);
    }

}
