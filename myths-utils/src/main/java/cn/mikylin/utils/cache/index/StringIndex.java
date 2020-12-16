package cn.mikylin.utils.cache.index;

public class StringIndex extends AbstractIndex {

    private final String key;

    public StringIndex(String key,long offset,long len,String fileName) {
        super(offset,len,fileName);
        this.key = key;
    }

    @Override
    public long keyHashCode() {
        return key == null ? 0 : key.hashCode();
    }

}
