package cn.mikylin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
                if(m == null)
                    m = new ObjectMapper();
            }
        }
        return m;
    }

    public static String toJson(Object o) {
        try {
            return objectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String json,Class<T> clz) {
        try {
            return objectMapper().readValue(json,clz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> toList(String json,Class<T> clz) {
        return (List<T>) toList(json,LinkedList.class,clz);
    }

    public static <C extends Collection,T> Collection<T> toList(String json,Class<C> coll,Class<T> clz) {

        if(json == null
                || coll == null
                || clz == null)
            throw new IllegalArgumentException();

        try {
            JavaType jt = getCollectionType(coll, clz);
            return objectMapper().readValue(json,jt);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper().getTypeFactory()
                .constructParametricType(collectionClass,elementClasses);
    }
}
