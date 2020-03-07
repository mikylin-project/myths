package cn.mikylin.myths.concurrent;

import java.util.concurrent.*;

/**
 * thread pool for priority
 *
 * @author mikylin
 * @date 20200110
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                      long keepAliveTime, TimeUnit unit,
                                      int queueSize, ThreadFactory threadFactory,
                                      RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime,
                unit,new PriorityRunnableBlockingQueue(queueSize),
                threadFactory,handler);
    }

    public void execute(Runnable r,int priority) {
        PriorityRunnableTask priorityRunnableTask = PriorityRunnableTask.create(r, priority);
        execute(priorityRunnableTask);
    }

}
