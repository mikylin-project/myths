package cn.mikylin.myths.concurrent.threadpool;

import java.util.concurrent.*;

/**
 * can retry thread pool.
 *
 * @author mikylin
 * @date 20201030
 */
public final class ReTryThreadPoolExecutor extends ProxyModelExecutorService {

    private int retry = 1;
    private boolean isFair = false;

    public ReTryThreadPoolExecutor(ExecutorService realPool,
                                   int retry,boolean isFair) {
        init(realPool);

        if (retry > 1)
            this.retry = retry;

        this.isFair = isFair;
    }

    public ReTryThreadPoolExecutor(ExecutorService realPool) {
        init(realPool);
    }

    @Override
    public void execute(Runnable command) {
        if (command instanceof ReTryRunnable)
            realPool.execute(command);
        else
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
            while (!isShutdown() && !isTerminated()) {
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
