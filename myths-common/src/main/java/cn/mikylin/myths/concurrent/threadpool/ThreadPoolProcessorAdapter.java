package cn.mikylin.myths.concurrent.threadpool;

import java.util.concurrent.ExecutorService;

public class ThreadPoolProcessorAdapter implements ThreadPoolProcessor {

    @Override
    public void addTask(ExecutorService pool, Runnable r) {

    }

    @Override
    public void startTask(Runnable r) {

    }

    @Override
    public void successTask(Runnable r) {

    }

    @Override
    public void mistakeTask(Throwable e, Runnable r) {

    }
}
