package cn.mikylin.myths.cache.collection;

import cn.mikylin.myths.common.RandomUtils;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.*;

/**
 * 随机集合
 *
 * @author mikylin
 * @date 20191214
 */
public abstract class RandomCollection<T> {


    public static class Set<T> extends RandomCollection<T> {
        public Set() {
            col = new TreeSet<>();
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

    public static class List<T> extends RandomCollection<T> {
        public List() {
            col = new LinkedList<>();
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

    protected Collection<Entry<T>> col;

    private RandomCollection() { }



    /**
     * 存值方法，输入 value 和权重，返回　标号
     */
    public long add(T t, int weight) {
        Objects.requireNonNull(col);
        long andAdd = getIndex();
        Entry<T> tEntry = Entry.create(t,andAdd,weight);
        col.add(tEntry);
        return andAdd;
    }


    /**
     * 获取指定 index 的元素
     */
    public T get(long index) {
        Objects.requireNonNull(col);
        for(Entry<T> e : col)
            if(e.index == index)
                return e.value;
        return null;
    }

    /**
     * 删除指定元素
     */
    public boolean remove(T o) {
        Objects.requireNonNull(col);
        Entry<T> tEntry = Entry.create(o);
        return col.remove(tEntry);
    }


    /**
     * 删除指定 index 的元素
     */
    public T remove(long index) {
        Objects.requireNonNull(col);
        for(Entry<T> e : col)
            if(e.index == index) {
                col.remove(e);
                return e.value;
            }
        return null;
    }

    /**
     * 列表 size
     */
    public int size() {
        Objects.requireNonNull(col);
        return col.size();
    }

    /**
     * 清空列表
     */
    public void clear() {
        Objects.requireNonNull(col);
        col.clear();
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

    /**
     * 在指定范围内随机获取一个元素
     */
    public T random(int begin,int end) {

        end = end >= size() ? size() - 1 : end;
        Entry<T>[] sub = new Entry[end - begin + 1];
        for(int i = begin ; i <= end ; i ++)
            sub[i - begin] = getEntry(i);

        java.util.List<Long> l = new LinkedList<>();
        for(Entry<T> e : sub)
            for(int i = 0 ; i < e.weight ; i ++)
                l.add(e.index);

        int i = RandomUtils.nextInt(0,l.size());
        long index = l.get(i).longValue();
        return get(index);
    }

    /**
     * 随机获取一个元素
     */
    public T random() {
        return random(0,size() - 1);
    }


    protected static <T> T entryValue(Entry<T> e) {
        return e == null ? null : e.value;
    }

    private long getIndex() {
        return (long)INDEX_LONG.getAndAdd(this,1L);
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

    // 测试
    public static void main(String[] args) {
        RandomCollection.List<Integer> l = new RandomCollection.List<>();
        l.add(1,2);
        l.add(2,1);
        l.add(3,1);
        l.add(4,1);
        l.add(5,1);
        l.add(6,1);


        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        int num4 = 0;
        int num5 = 0;
        int num6 = 0;

        for(int i = 0 ; i < 700000 ; i ++) {
            Integer r = l.random();
            if(r == 1) num1 ++;
            if(r == 2) num2 ++;
            if(r == 3) num3 ++;
            if(r == 4) num4 ++;
            if(r == 5) num5 ++;
            if(r == 6) num6 ++;
        }

        System.out.println(num1);
        System.out.println(num2);
        System.out.println(num3);
        System.out.println(num4);
        System.out.println(num5);
        System.out.println(num6);

        System.out.println(num1 + num2 + num3 + num4 + num5 + num6);

        System.out.println(l.get(0L));
    }

}
