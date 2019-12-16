package cn.mikylin.myths.cache.collection;

import cn.mikylin.myths.common.FutureUtils;
import cn.mikylin.myths.common.ThreadUtils;
import cn.mikylin.myths.common.TimeUtils;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 异步队列
 *
 * @author mikylin
 * @date 20191215
 */
public class AsyncQueue<T> {

    private Queue<T> taskQueue;
    private Queue<AsyncFuture<T>> futureQueue;
    private int spin;

    public AsyncQueue(int spin) {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.futureQueue = new LinkedBlockingQueue<>();
        this.spin = spin;
    }

    public AsyncQueue() {
        this(20);
    }

    /**
     * 向队列中添加元素
     */
    public boolean offer(T t) {
        for(;;) {
            AsyncFuture<T> firstFuture = futureQueue.poll();
            if(firstFuture == null)
                break;
            int status = firstFuture.status;
            if(status == AsyncFuture.ELEMENT) {
                firstFuture.set(t);
                tryUnpark(firstFuture);
            }
            else if(status == AsyncFuture.POLL) {
                firstFuture.set(t);
                tryUnpark(firstFuture);
                return true;
            }
            else if(status == AsyncFuture.CANCELLED)
                continue;
        }
        return taskQueue.offer(t);
    }

    /**
     * 返回地一个元素，并从队列中删除
     */
    public Future<T> poll() {
        T task = taskQueue.poll();
        AsyncFuture<T> f = pollFuture(task);
        if(task == null)
            futureQueue.offer(f);
        return f;
    }

    /**
     * 返回地一个元素，不从队列中删除
     */
    public Future<T> peek() {
        T task = taskQueue.peek();
        AsyncFuture<T> f = peekFuture(task);
        if(task == null)
            futureQueue.offer(f);
        return f;
    }

    public void clear() {
        taskQueue.clear();
    }

    public int size() {
        return taskQueue.size();
    }

    public void clearWaiter() {
        for(;;) {
            AsyncFuture<T> poll = futureQueue.poll();
            if(poll == null)
                break;
            poll.cancel(true);
            tryUnpark(poll);
        }
    }

    public int waiterSize() {
        return futureQueue.size();
    }


    private AsyncFuture<T> pollFuture(T t) {
        return new AsyncFuture<>(t,AsyncFuture.POLL,this);
    }

    private AsyncFuture peekFuture(T t) {
        return new AsyncFuture(t,AsyncFuture.ELEMENT,this);
    }

    private static void tryUnpark(AsyncFuture f) {
        System.out.println("unpark");

        synchronized (f.locker) {
            f.locker.notifyAll();
        }
    }


    private static class AsyncFuture<T> implements Future<T> {

        private static final int POLL = 0;
        private static final int ELEMENT = 1;
        private static final int CANCELLED = -1;

        private T result;
        private volatile int status; // -1 - cancelled ; 0 - poll ; 1 - peek
        private AsyncQueue<T> queue;
        private Object locker = new Object();


        private AsyncFuture(T result,int status,AsyncQueue<T> queue) {
            this.result = result;
            this.status = status;
            this.queue = queue;
        }

        /**
         * 阻塞方法
         */
        private void park() {
            System.out.println("begin park");

            synchronized (locker) {
                try {
                    locker.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void set(T t) {
            result = t;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            if(mayInterruptIfRunning)
                status = -1;
            return true;
        }

        @Override
        public boolean isCancelled() {
            return status == -1;
        }

        @Override
        public boolean isDone() {
            return result != null;
        }

        @Override
        public T get()
                throws InterruptedException, ExecutionException {
            for(int i = 0; i < queue.spin ; i ++) {
                if(result != null && status >= 0)
                    return result;
                else if(status == -1)
                    return null;
            }
            park();
            return result;
        }

        @Override
        public T get(long timeout, TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {

            long out = TimeUtils.currentTimeMillis(unit, timeout);

            long beginTime = System.currentTimeMillis();
            long endTime = beginTime + out;

            for(;System.currentTimeMillis() <= endTime;) {
                if(result != null && status >= 0)
                    return result;
            }

            return null;
        }
    }


    public static void main(String[] args) {
        AsyncQueue<Integer> q = new AsyncQueue<>();

        for(int i = 0 ; i < 5 ; i ++) {
            final int a = i;
            new Thread(() -> {
                ThreadUtils.sleep(10L);
                q.offer(a);
            }).start();
            ThreadUtils.sleep(1L);
        }

        System.out.println("FINISH");

        Future<Integer> element = q.peek();
        System.out.println(FutureUtils.get(element));

        Future<Integer> poll1 = q.poll();
        System.out.println(FutureUtils.get(poll1));

        Future<Integer> poll2 = q.poll();
        System.out.println(FutureUtils.get(poll2));

    }

}
