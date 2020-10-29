package cn.mikylin.myths.concurrent.threadpool;

import java.util.concurrent.ExecutorService;

/**
 * 线程池扩展点
 *
 * @author mikylin
 * @date 202201029
 */
public interface ThreadPoolProcessor {

    void addTask(ExecutorService pool,Runnable r);

    void startTask(Runnable r);

    void successTask(Runnable r);

    void mistakeTask(Throwable e,Runnable r);

}
