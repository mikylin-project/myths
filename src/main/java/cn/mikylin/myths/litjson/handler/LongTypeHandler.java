package cn.mikylin.myths.litjson.handler;

public class LongTypeHandler implements TypeHandler<Long> {
    @Override
    public Long read(String value) {
        return Long.valueOf(value);
    }

    @Override
    public String write(Long aLong) {
        return String.valueOf(aLong);
    }
}
