package cn.mikylin.myths.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * priority runnable blocking queue
 *
 * @author mikylin
 * @date 20200110
 */
public class PriorityRunnableBlockingQueue
        implements BlockingQueue<Runnable>, java.io.Serializable {

    private PriorityBlockingQueue<PriorityRunnableTask> q;

    public PriorityRunnableBlockingQueue(int size) {
        q = new PriorityBlockingQueue<>(size);
    }

    @Override
    public boolean add(Runnable r) {
        return q.offer(PriorityRunnableTask.create(r));
    }

    @Override
    public boolean offer(Runnable r) {
        return q.offer(PriorityRunnableTask.create(r));
    }

    @Override
    public Runnable remove() {
        return q.remove();
    }

    @Override
    public Runnable poll() {
        return q.poll();
    }

    @Override
    public Runnable element() {
        return q.element();
    }

    @Override
    public Runnable peek() {
        return q.peek();
    }

    @Override
    public void put(Runnable r) throws InterruptedException {
        q.put(PriorityRunnableTask.create(r));
    }

    @Override
    public boolean offer(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        return q.offer(PriorityRunnableTask.create(r),timeout,unit);
    }

    @Override
    public Runnable take() throws InterruptedException {
        return q.take();
    }

    @Override
    public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return q.remainingCapacity();
    }

    @Override
    public boolean remove(Object o) {
        if(!(o instanceof Runnable))
            return false;
        return q.remove(PriorityRunnableTask.create((Runnable)o));
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Collection<PriorityRunnableTask> cs = new ArrayList<>(c.size());
        for(Object r : c) {
            if(!(r instanceof Runnable))
                return false;
            cs.add(PriorityRunnableTask.create((Runnable)r));
        }
        return q.containsAll(cs);
    }

    @Override
    public boolean addAll(Collection<? extends Runnable> c) {
        Collection<PriorityRunnableTask> cs = new ArrayList<>(c.size());
        for(Runnable r : c) {
            cs.add(PriorityRunnableTask.create(r));
        }
        return q.addAll(cs);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Collection<PriorityRunnableTask> cs = new ArrayList<>(c.size());
        for(Object r : c) {
            if(!(r instanceof Runnable))
                return false;
            cs.add(PriorityRunnableTask.create((Runnable)r));
        }
        return q.removeAll(cs);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Collection<PriorityRunnableTask> cs = new ArrayList<>(c.size());
        for(Object r : c) {
            if(!(r instanceof Runnable))
                return false;
            cs.add(PriorityRunnableTask.create((Runnable)r));
        }
        return q.retainAll(cs);
    }

    @Override
    public void clear() {
        q.clear();
    }

    @Override
    public int size() {
        return q.size();
    }

    @Override
    public boolean isEmpty() {
        return q.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if(!(o instanceof PriorityRunnableTask))
            return false;
        return q.contains(o);
    }

    @Override
    public Iterator<Runnable> iterator() {
        return new Ite(q);
    }

    private class Ite implements Iterator<Runnable> {

        private int mark;
        private Object[] objs;

        private Ite(PriorityBlockingQueue<PriorityRunnableTask> q) {
            objs = q.toArray();
            mark = 0;
        }

        @Override
        public boolean hasNext() {
            return mark < objs.length;
        }

        @Override
        public Runnable next() {
            return (Runnable) objs[mark ++];
        }
    }

    @Override
    public Object[] toArray() {
        return q.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return q.toArray(a);
    }

    @Override
    public int drainTo(Collection<? super Runnable> c) {
        return q.drainTo(c);
    }

    @Override
    public int drainTo(Collection<? super Runnable> c, int maxElements) {
        return q.drainTo(c,maxElements);
    }
}
