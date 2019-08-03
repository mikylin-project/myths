package cn.mikylin.myths.litjson.handler;

public class DoubleTypeHandler implements TypeHandler<Double> {
    @Override
    public Double read(String value) {
        return Double.valueOf(value);
    }

    @Override
    public String write(Double aDouble) {
        return String.valueOf(aDouble);
    }
}
