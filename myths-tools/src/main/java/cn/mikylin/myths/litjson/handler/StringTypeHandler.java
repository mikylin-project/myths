package cn.mikylin.myths.litjson.handler;

public class StringTypeHandler implements TypeHandler<String> {
    @Override
    public String read(String value) {
        return value;
    }

    @Override
    public String write(String s) {
        return s;
    }
}
