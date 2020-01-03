package cn.mikylin.myths.cache.collection;

import cn.mikylin.myths.common.random.RandomUtils;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.*;
// import java.util.concurrent.atomic.AtomicLong;

/**
 * random collection.
 *
 * @author mikylin
 * @date 20191214
 */
public abstract class RandomCollection<T> {


    private static class Set<T> extends RandomCollection<T> {

        private Set() {
            super(new TreeSet<>((o1, o2) -> o1.index > o2.index ? 1 : -1));
        }

        @Override
        Entry<T> getEntry(int i) {
            Objects.requireNonNull(col);
            if(i > size() || i < 0)
                return null;
            TreeSet<Entry<T>> set = (TreeSet<Entry<T>>)col;
            Object o = set.toArray()[i];
            return (Entry<T>)o;
        }
    }

    private static class List<T> extends RandomCollection<T> {

        private List(int size) {
            super(new ArrayList<>(size));
        }

        private List() {
            super(new LinkedList<>());
        }

        @Override
        Entry<T> getEntry(int i) {
            Objects.requireNonNull(col);
            if(i > size() || i < 0)
                return null;
            java.util.List<Entry<T>>list = (java.util.List<Entry<T>>)col;
            return list.get(i);
        }
    }

    public static <T> RandomCollection<T> Set() {
        return new RandomCollection.Set<>();
    }

    public static <T> RandomCollection<T> List() {
        return new RandomCollection.List<>();
    }

    public static <T> RandomCollection<T> List(int size) {
        return new RandomCollection.List<>(size);
    }


    private Entry<T>[] sub;
    private boolean subFlag = false;


    // jdk 9 +，使用 var handler
    private long indexLong = 0L;
    private static VarHandle INDEX_LONG;
    static {
        try {
            INDEX_LONG = MethodHandles
                    .lookup()
                    .in(RandomCollection.class)
                    .findVarHandle(RandomCollection.class, "indexLong", long.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private long getIndex() {
        return (long)INDEX_LONG.getAndAdd(this,1L);
    }

    // jdk 8 中使用 atomic 类来替代 var handler
//    private AtomicLong indexLong = new AtomicLong(0L);
//    private long getIndex() {
//        return indexLong.getAndAdd(1L);
//    }



    protected Collection<Entry<T>> col;

    private RandomCollection(Collection<Entry<T>> col) {
        this.col = col;
    }


    public void addCollection(Collection<T> c,int weight) {
        for(T t : c) {
            col.add(Entry.create(t,getIndex(),weight));
        }
    }

    public void addCollection(Collection<T> c) {
        addCollection(c,1);
    }


    /**
     * 存值方法，输入 value 和权重，返回　index
     */
    public long add(T t, int weight) {
        Objects.requireNonNull(col);
        long index = getIndex();
        Entry<T> tEntry = Entry.create(t,index,weight);
        col.add(tEntry);
        subFlag = true;
        return index;
    }

    public long add(T t) {
        return add(t,1);
    }


    /**
     * 构筑全局随机缓存
     */
    private void buildSub() {
        if(sub == null || subFlag) {
            synchronized (this) {
                if(sub == null || subFlag) {
                    sub = entryArray(0,size() - 1);
                    subFlag = false;
                }
            }
        }
    }


    /**
     * 获取指定 index 的元素
     */
    public T get(long index) {
        return getAndChangeWeight(index,0);
    }

    /**
     * 获取指定 index 的元素，并且改变被选中的元素的权重
     * 此方法如果 weight 不传入 0 的话，会频繁修改随机数组缓存，消耗很大
     */
    public T getAndChangeWeight(long index,int weightChange) {
        Objects.requireNonNull(col);
        Entry<T> et = null;
        for(Entry<T> e : col)
            if(e.index == index)
                et = e;

        if(et != null) {
            weightChange(et,weightChange);
            return et.value;
        }
        return null;
    }

    private void weightChange(Entry<T> et,int weightChange) {
        if(et != null && weightChange != 0) {
            et.weight = et.weight + weightChange;
            if(et.weight <= 0)
                col.remove(et);
            subFlag = true;
        }
    }


    /**
     * 删除指定元素
     */
    public boolean remove(T o) {
        Entry<T> tEntry = Entry.create(o);
        if(col.remove(tEntry))
            return subFlag = true;
        return false;
    }


    /**
     * 删除指定 index 的元素
     */
    public T remove(long index) {
        for(Entry<T> e : col)
            if(e.index == index && col.remove(e)) {
                subFlag = true;
                return entryValue(e);
            }
        return null;
    }

    /**
     * 列表 size
     */
    public int size() {
        return col.size();
    }

    /**
     * 清空列表
     */
    public void clear() {
        col.clear();
        sub = null;
    }

    /**
     * 判断是否为空
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 根据插入下标获取元素
     */
    public T get(int i) {
        Entry<T> tEntry = getEntry(i);
        return entryValue(tEntry);
    }

    /**
     * 根据插入下标删除元素
     */
    public T remove(int i) {
        Objects.requireNonNull(col);
        Entry<T> entry = getEntry(i);
        col.remove(entry);
        return entry.value;
    }

    abstract Entry<T> getEntry(int i);

    private final Entry<T>[] entryArray(int begin,int end) {
        end = end >= size() ? size() - 1 : end;

        java.util.List<Entry<T>> list = new ArrayList<>(end - begin + 1);
        for(int i = begin ; i <= end ; i ++) {
            Entry<T> entry = getEntry(i);
            for(int j = 0 ; j < entry.weight ; j ++)
                list.add(entry);
        }

        Entry<T>[] sub = new Entry[list.size()];
        for(int i = 0 ; i < list.size() ; i ++)
            sub[i] = list.get(i);

        return sub;
    }

    /**
     * 在列表全范围内随机获取一个元素
     */
    public T random() {
        buildSub();
        return random(sub,0);
    }

    /**
     * 在指定范围内随机获取一个元素
     */
    public T random(int begin,int end,int weightChange) {
        Entry<T>[] sub = entryArray(begin,end);
        return random(sub,weightChange);
    }

    private T random(Entry<T>[] sub,int weightChange) {
        if(sub == null || sub.length == 0)
            return null;
        int i = RandomUtils.nextInt(0,sub.length);
        Entry<T> tEntry = sub[i];
        weightChange(tEntry,weightChange);
        return tEntry.value;
    }


    protected static <T> T entryValue(Entry<T> e) {
        return e == null ? null : e.value;
    }




    /**
     * 节点对象
     */
    private static class Entry<T> {
        T value;
        long index;
        int weight;

        @Override
        public boolean equals(Object o) {
            if(o == null || !(o instanceof Entry))
                return false;
            Entry<T> e = (Entry<T>)o;
            return Objects.equals(e.value,value);
        }

        protected static <T> Entry<T> create(T value) {
            return create(value,0L,1);
        }

        protected static <T> Entry<T> create(T value,long index,int weight) {
            Entry<T> e = new Entry<>();
            e.value = value;
            e.index = index;
            e.weight = weight;
            return e;
        }
    }

}