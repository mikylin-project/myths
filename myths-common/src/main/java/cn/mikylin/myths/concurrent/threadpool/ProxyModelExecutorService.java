package cn.mikylin.myths.concurrent.threadpool;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public abstract class ProxyModelExecutorService implements ExecutorService {

    protected ExecutorService realPool;

    public ProxyModelExecutorService(ExecutorService realPool) {
        this.realPool = realPool;
    }


    @Override
    public void shutdown() {
        realPool.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return realPool.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return realPool.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return realPool.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return realPool.awaitTermination(timeout,unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return realPool.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return realPool.submit(task,result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return realPool.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return realPool.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return realPool.invokeAll(tasks,timeout,unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return realPool.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return realPool.invokeAny(tasks,timeout,unit);
    }

    @Override
    public void execute(Runnable command) {
        realPool.execute(command);
    }
}
