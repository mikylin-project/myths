package cn.mikylin.myths.cache.collection;

import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * async queue interface.
 *
 * @author mikylin
 * @date 20191217
 */
public interface AsyncQueue<T> {

    /**
     * add the element in task queue.
     * element can not be null.
     *
     * @param t  element
     * @return success or not
     */
    boolean offer(T t);

    /**
     * get and delete the first element in task queue.
     *
     * @return future with the element
     */
    Future<T> poll();

    /**
     * get the first element in task queue.
     *
     * @return future with the element
     */
    Future<T> peek();

    /**
     * delete all task in queue.
     */
    void clearTask();

    /**
     * task size.
     *
     * @return task size
     */
    int taskSize();

    /**
     * delete all the future waiter.
     */
    void clearWaiter();

    /**
     * future waiter size.
     *
     * @return waiter size
     */
    int waiterSize();

    /**
     * get task queue.
     *
     * @return task queue
     */
    Queue<T> taskQueue();

    /**
     * get waiter queue.
     *
     * @return waiter queue
     */
    Queue<Future<T>> waiterQueue();

    /**
     * queue create factory interface.
     */
    interface QueueFactory<T> {
        Queue<T> getQueue();
    }


    static <T> AsyncQueue<T> create() {
        return create (
                () -> new LinkedBlockingQueue<>(),
                () -> new LinkedBlockingQueue<>(),
                20);
    }

    static <T> AsyncQueue<T> create(QueueFactory<T> taskQueueFactory,
                                    QueueFactory<Future<T>> futureQueueFactory,
                                    int spin) {
        return new DefaultAsyncQueue (taskQueueFactory, futureQueueFactory, spin);
    }

}
