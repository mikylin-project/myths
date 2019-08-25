package cn.mikylin.myths.litioc.container;

public class KeyDefinition {

    private String name;
    private Class<?> clz;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
