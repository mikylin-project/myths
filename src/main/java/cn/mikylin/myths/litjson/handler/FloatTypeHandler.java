package cn.mikylin.myths.litjson.handler;

public class FloatTypeHandler implements TypeHandler<Float> {

    @Override
    public Float read(String value) {
        return Float.valueOf(value);
    }

    @Override
    public String write(Float aFloat) {
        return String.valueOf(aFloat);
    }
}
