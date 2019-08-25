package cn.mikylin.myths.litjson.handler;

public class IntegerTypeHandler implements TypeHandler<Integer> {

    @Override
    public Integer read(String value) {
        return Integer.valueOf(value);
    }

    @Override
    public String write(Integer integer) {
        return String.valueOf(integer);
    }
}
