package cn.mikylin.myths.concurrent.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.LongAdder;

public class TaskCountProcessorThreadPool extends ProxyModelExecutorService {

    private TaskCountProcessor processor;

    public TaskCountProcessorThreadPool(ExecutorService realPool) {

        processor = new TaskCountProcessor();

        ProcessorThreadPoolExecutor pool
                = new ProcessorThreadPoolExecutor(realPool,processor);
        init(pool);
    }


    public boolean isFinish() {
        return processor.finish();
    }



    private class TaskCountProcessor extends ThreadPoolProcessorAdapter {

        LongAdder addCount = new LongAdder();
        LongAdder finishCount = new LongAdder();

        @Override
        public void addTask(ExecutorService pool, Runnable r) {
            addCount.increment();
        }

        @Override
        public void successTask(Runnable r) {
            finishCount.increment();
        }

        @Override
        public void mistakeTask(Throwable e, Runnable r) {
            finishCount.increment();
        }


        boolean finish() {
            return finishCount.longValue() == addCount.longValue();
        }
    }
}
