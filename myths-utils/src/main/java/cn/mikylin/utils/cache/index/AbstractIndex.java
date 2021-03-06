package cn.mikylin.utils.cache.index;

public abstract class AbstractIndex implements Index {

    private final long offset;
    private final long len;
    private final String fileName;

    protected AbstractIndex(long offset,long len,String fileName) {
        this.offset = offset;
        this.len = len;
        this.fileName = fileName;
    }

    @Override
    public long offset() {
        return offset;
    }

    @Override
    public long len() {
        return len;
    }

    @Override
    public String fileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Index ?
                keyHashCode() == ((Index)o).keyHashCode() : false;
    }
}
