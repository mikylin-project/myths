package cn.mikylin.utils.cache.index;

public abstract class AbstractIndex implements Index {

    private final long offset;
    private final long len;

    protected AbstractIndex(long offset,long len) {
        this.offset = offset;
        this.len = len;
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
    public boolean equals(Object o) {
        return o instanceof Index ?
                keyHashCode() == ((Index)o).keyHashCode() : false;
    }
}
