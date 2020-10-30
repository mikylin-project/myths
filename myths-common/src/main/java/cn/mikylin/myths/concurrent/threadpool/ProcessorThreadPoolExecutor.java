package cn.mikylin.myths.concurrent.threadpool;

import java.util.concurrent.ExecutorService;

public class ProcessorThreadPoolExecutor extends ProxyModelExecutorService {

    private ThreadPoolProcessor processor;

    public ProcessorThreadPoolExecutor(ExecutorService realPool,
                                       ThreadPoolProcessor processor) {
        init(realPool);
        this.processor = processor;
    }

    @Override
    public void execute(Runnable command) {
        processor.addTask(realPool,command);
        realPool.execute(new ProcessorRunnable(command));
    }


    private final class ProcessorRunnable implements Runnable {

        final Runnable r;

        ProcessorRunnable(Runnable r) {
            this.r = r;
        }

        @Override
        public void run() {
            processor.startTask(r);
            try {
                r.run();
            } catch (Throwable e) {
                processor.mistakeTask(e,r);
                throw e;
            }
            processor.successTask(r);
        }
    }

}
