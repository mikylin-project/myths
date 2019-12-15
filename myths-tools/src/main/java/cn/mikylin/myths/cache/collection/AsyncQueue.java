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

    public AsyncQueue() {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.futureQueue = new LinkedBlockingQueue<>();
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
            if(status == AsyncFuture.ELEMENT)
                firstFuture.set(t);
            else if(status == AsyncFuture.POLL) {
                firstFuture.set(t);
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
        }
    }

    public int waiterSize() {
        return futureQueue.size();
    }





    private static <T> AsyncFuture<T> pollFuture(T t) {
        return new AsyncFuture<>(t,AsyncFuture.POLL);
    }

    private static <T> AsyncFuture peekFuture(T t) {
        return new AsyncFuture(t,AsyncFuture.ELEMENT);
    }






    private static class AsyncFuture<T> implements Future<T> {

        private static final int POLL = 0;
        private static final int ELEMENT = 1;
        private static final int CANCELLED = -1;

        private T result;
        private volatile int status; // -1 - cancelled ; 0 - poll ; 1 - peek

        private AsyncFuture() {}

        private AsyncFuture(T result,int status) {
            this.result = result;
            this.status = status;
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
            for(;;) {
                if(result != null && status >= 0)
                    return result;
                else if(status == -1)
                    break;
            }
            return null;
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
                boolean offer = q.offer(a);
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
