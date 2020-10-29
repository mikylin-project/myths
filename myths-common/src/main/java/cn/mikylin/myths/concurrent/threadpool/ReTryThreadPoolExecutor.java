package cn.mikylin.myths.concurrent.threadpool;

import java.util.concurrent.*;

public class ReTryThreadPoolExecutor extends ProxyModelExecutorService {

    private int retry = 1;
    private boolean isFair;

    public ReTryThreadPoolExecutor(ExecutorService realPool,
                                   int retry,boolean isFair) {
        super(realPool);
        if(retry < 0)
            throw new IllegalArgumentException("param exception");
        if (retry > 1)
            this.retry = retry;
        this.isFair = isFair;
    }

    public ReTryThreadPoolExecutor(ExecutorService realPool) {
        super(realPool);
    }

    @Override
    public void execute(Runnable command) {
        realPool.execute(new ReTryRunnable(command));
    }


    private final class ReTryRunnable implements Runnable {

        final Runnable r;

        int reallyDo = 0;

        ReTryRunnable(Runnable r) {
            this.r = r;
        }

        @Override
        public void run() {
            for (;;) {
                try {
                    r.run();
                } catch (Throwable e) {
                    if (++ reallyDo >= retry)
                        throw e;

                    if (isFair)
                        realPool.execute(this);
                    else
                        continue;
                }
                break;
            }
        }
    }


}
