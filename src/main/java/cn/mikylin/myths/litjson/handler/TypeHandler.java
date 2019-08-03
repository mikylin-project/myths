package cn.mikylin.myths.litjson.handler;

public interface TypeHandler<T> {

    T read(String value);

    String write(T t);
}
