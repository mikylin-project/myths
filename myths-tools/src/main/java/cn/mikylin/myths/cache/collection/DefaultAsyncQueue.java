package cn.mikylin.myths.cache.collection;

import cn.mikylin.myths.common.lang.ObjectUtils;
import cn.mikylin.myths.common.TimeUtils;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * default implement with async queue.
 *
 * @author mikylin
 * @date 20191217
 */
public class DefaultAsyncQueue<T> implements AsyncQueue<T> {

    // task queue
    private Queue<T> taskQueue;

    // future waiter queue
    private Queue<WeakReference<Future<T>>> waiterQueue;

    // spin number
    private int spin;

    DefaultAsyncQueue(QueueFactory<T> taskQueueFactory,
                      QueueFactory<WeakReference<Future<T>>> futureQueueFactory,
                      int spin) {
        if(spin < 0)
            throw new RuntimeException("spin can not be negative number.");
        if(spin == 0)
            spin = 1;

        this.taskQueue = taskQueueFactory.getQueue();
        this.waiterQueue = futureQueueFactory.getQueue();
        this.spin = spin;
    }


    @Override
    public boolean offer(T t) {
        return consume(t) ? true : taskQueue.offer(t);
    }

    /**
     * consume function.
     * @param t  element
     * @return consume success or not.
     *      if element is poll by any future,return true,
     *      if element not poll any one,return false and offer in task queue.
     */
    private boolean consume(T t) {

        if(t == null)
            return true;

        for(;;) {
            AsyncFuture<T> firstFuture = pollWaiter();

            if(firstFuture == null)
                break;

            if(firstFuture.status == AsyncFuture.DEFAULT) {

                int type = firstFuture.type;

                if(type == AsyncFuture.ELEMENT)
                    firstFuture.set(t);
                else if(type == AsyncFuture.POLL
                        && firstFuture.set(t))
                    return true;
            }
        }
        return false;
    }


    @Override
    public Future<T> poll() {
        T element = pollTask();
        AsyncFuture<T> f = pollFuture();
        if(element == null) {
            offerWaiter(f);
            consume(pollTask());
        } else
            f.set(element);
        return f;
    }

    @Override
    public Future<T> peek() {
        T element = peekTask();
        AsyncFuture<T> f = peekFuture();
        if(element == null) {
            offerWaiter(f);
            consume(peekTask());
        } else
            f.set(element);
        return f;
    }

    /**
     * offer a future to future waiter queue.
     *
     * @param f  future
     */
    private void offerWaiter(AsyncFuture<T> f) {
        WeakReference<Future<T>> weakFuture = new WeakReference<>(f);
        waiterQueue().offer(weakFuture);
    }

    /**
     * poll a future waiter.
     *
     * @return future
     */
    private AsyncFuture<T> pollWaiter() {
        return  (AsyncFuture<T>)waiterQueue().poll().get();
    }

    private T pollTask() {
        return taskQueue().poll();
    }

    private T peekTask() {
        return taskQueue().peek();
    }

    @Override
    public void clearTask() {
        taskQueue().clear();
    }


    @Override
    public int taskSize() {
        return taskQueue().size();
    }


    @Override
    public void clearWaiter() {
        for(;;) {
            AsyncFuture<T> poll = pollWaiter();
            if(poll == null)
                break;
            poll.cancel(true);
        }
    }

    @Override
    public int waiterSize() {
        return waiterQueue.size();
    }

    /**
     * get task queue.
     *
     * @return task queue
     */
    private Queue<T> taskQueue() {
        return taskQueue;
    }

    /**
     * get waiter queue.
     *
     * @return waiter queue
     */
    private Queue<WeakReference<Future<T>>> waiterQueue() {
        return waiterQueue;
    }

    /**
     * poll-type future create.
     *
     * @return poll async future
     */
    private AsyncFuture<T> pollFuture() {
        return new AsyncFuture<>(AsyncFuture.POLL,this);
    }

    /**
     * peek-type future create.
     *
     * @return poll async future
     */
    private AsyncFuture peekFuture() {
        return new AsyncFuture(AsyncFuture.ELEMENT,this);
    }

    /**
     * async future
     *
     * @author mikylin
     * @date 20191217
     */
    private static class AsyncFuture<T> implements Future<T> {

        /**
         * future type
         */
        private static final int POLL = 1;
        private static final int ELEMENT = 2;

        /**
         * future status
         */
        private static final int FINISH = 1;
        private static final int DEFAULT = 0;
        private static final int CANCELLED = -1;

        private T result;
        private AsyncQueue<T> queue; // queue owner the future
        private volatile int type; // 1 - poll ; 2 - peek

        private Queue<Thread> tq; // wait threads


        private AsyncFuture(T result, int type, DefaultAsyncQueue<T> queue) {
            this.result = result;
            this.type = type;
            this.queue = queue;
            this.status = 0;
        }

        private AsyncFuture(int status, DefaultAsyncQueue<T> queue) {
            this(null,status,queue);
        }


        private void waitThread() {
            if(tq == null)
                synchronized (this) {
                    if(tq == null)
                        tq = new LinkedBlockingQueue<>();
                }

            tq.add(Thread.currentThread());
        }

        /**
         * wait the thread.
         *
         * @param untilTime  park until time
         */
        private void park(long untilTime) throws InterruptedException {

            if(untilTime == 0)
                for(; status == DEFAULT ;)
                    LockSupport.park();

            else if(System.currentTimeMillis() < untilTime)
                for(; status == DEFAULT && System.currentTimeMillis() < untilTime ;)
                    LockSupport.parkUntil(untilTime);

            unpark();

            if(status < 0)
                throw new InterruptedException();
        }


        /**
         * notify the thread.
         */
        private void unpark() {
            Thread t;
            if(status != DEFAULT && (t = tq.poll()) != null)
                LockSupport.unpark(t);
        }

        /**
         * set the value in future.
         */
        private boolean set(T t) {
            if(casStatus(DEFAULT,FINISH)) {
                result = t;
                unpark();
                return true;
            }
            return false;
        }

        /**
         * cancel the future
         */
        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            if(mayInterruptIfRunning
                    && casStatus(DEFAULT,CANCELLED)) {
                unpark();
                return true;
            }
            return false;
        }

        @Override
        public boolean isCancelled() {
            return status == CANCELLED;
        }

        @Override
        public boolean isDone() {
            return status == FINISH;
        }

        @Override
        public T get()
                throws InterruptedException, ExecutionException {
            DefaultAsyncQueue<T> q = (DefaultAsyncQueue<T>)queue;
            for(int i = 0; i < q.spin ; i ++) {
                if(result != null && status >= 0)
                    return result;
                else if(status < 0)
                    return null;
            }

            park(0);

            return result;
        }

        @Override
        public T get(long timeout, TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {

            if(result != null && status >= 0)
                return result;

            long out = TimeUtils.currentTimeMillis(unit,timeout);
            long beginTime = System.currentTimeMillis();
            long endTime = beginTime + out;

            park(endTime);

            if(status == DEFAULT)
                throw new TimeoutException();

            return result;
        }

        /**
         * cas utils for future status.
         *
         * -1 - cancelled
         * 0 - default
         * 1 - finish
         */
        private volatile int status;
        private static VarHandle STATUS;
        static {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            try {
                STATUS = lookup.findVarHandle(AsyncFuture.class, "status", int.class);
            } catch (Exception e) {
                throw new RuntimeException("status init exception.");
            }
        }
        private boolean casStatus(int begin,int after) {
            return STATUS.compareAndSet(this,begin,after);
        }
    }

}
