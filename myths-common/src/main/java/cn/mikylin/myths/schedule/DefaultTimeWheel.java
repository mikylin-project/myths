package cn.mikylin.myths.schedule;

import cn.mikylin.myths.common.lang.ThreadUtils;
import cn.mikylin.myths.common.lang.TimeUtils;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 时间轮默认实现
 *
 * @author mikylin
 * @date 20200503
 */
public final class DefaultTimeWheel extends AbstractTaskThread implements TimeWheel {

    /**
     * 一轮时间的分段
     */
    private final int segment;

    /**
     * 休眠时间，单位 ms
     */
    private final long intervalTime;

    /**
     * 任务执行线程
     */
    private AbstractTaskThread t;

    public DefaultTimeWheel(long time,TimeUnit unit,long intervalTime,ExecutorService taskPool) {
        this(TimeUtils.currentTimeMillis(unit,time),intervalTime,taskPool);
    }

    public DefaultTimeWheel(long time,TimeUnit unit,long intervalTime) {
        this(TimeUtils.currentTimeMillis(unit,time),intervalTime,null);
    }

    /**
     * 构造方法
     * @param milliSecond  一轮的总时间
     * @param intervalTime  休眠时间
     * @param taskPool  执行任务的线程池
     */
    public DefaultTimeWheel(long milliSecond,long intervalTime,ExecutorService taskPool) {

        // 分段
        this.segment = (int) (milliSecond / intervalTime);

        // 休眠时间
        this.intervalTime = intervalTime;

        // 线程池
        ExecutorService pool = taskPool;
        if(pool == null)
            pool = Executors.newSingleThreadExecutor();

        t = new DefaultTaskThread(intervalTime,segment,pool);

        // 设置为非守护线程
        setDaemon(false);
    }

    /**
     * 执行时间轮
     */
    @Override
    public void run() {

        // 启动任务执行线程
        t.start();

        final DefaultTaskThread dtt = (DefaultTaskThread)t;

        // 休眠时间轮线程，并定期去唤醒任务执行线程
        for(;;) {
            ThreadUtils.sleepMilliSecond(intervalTime);
            dtt.notifyTaskThread();
        }
    }

    /**
     * 优雅关闭
     */
    @Override
    public void shutdownGracefully() {
        synchronized (this) {
            if(t == null)
                throw new RuntimeException();
            t.shutdownGracefully();
        }
    }

    @Override
    Segment getSegment(int index) {
        throw new RuntimeException();
    }

    @Override
    void doTask(int index) {
        throw new RuntimeException();
    }

    /**
     * 将一个任务注册到时间轮上
     * @param task  任务
     */
    @Override
    public void registTask(TimeWheelTask task) {
        t.registTask(task);
    }


    /**
     * 工作线程
     */
    static class DefaultTaskThread extends AbstractTaskThread {

        boolean notify = true;
        boolean stop = false;
        int index = 1;
        final int segment;
        final long intervalTime;
        ExecutorService pool;

        private final Segment[] segments;

        DefaultTaskThread(long intervalTime, int segment, ExecutorService pool) {
            this.intervalTime = intervalTime;
            this.segment = segment;
            this.pool = pool;

            setDaemon(true);

            this.segments = new Segment[segment];
            for(int i = 0 ; i < segments.length ; i ++)
                segments[i] = new DefautSegment();
        }

        /**
         * 获取分段任务组
         */
        Segment getSegment(int i) {
            return segments[i];
        }

        /**
         * 线程执行方法
         */
        @Override
        public void run() {
            for(;!stop;) {

                // 休眠当前线程
                waitTaskThread();

                // 检测休止符
                if(stop)
                    break;

                // 检测时间轮的转盘 index
                int i = index;
                if(++ index >= segment)
                    index = 0;

                // 执行任务
                doTask(i);
            }
        }

        /**
         * 开始任务
         *
         * @param index  角标
         */
        void doTask(final int index) {

            synchronized (this) {
                Segment s = segments[index];
                Iterator<TimeWheelTask> iterator = s.iterator();
                for(;iterator.hasNext();) {
                    TimeWheelTask task = iterator.next();
                    iterator.remove();
                    pool.execute(() -> {
                        if(task.notCancel()) {
                            registAfterBeginRunning(task);
                            task.run();
                            registAfterFinish(task);
                        }
                    });
                }
            }
        }

        /**
         * 休眠执行线程
         */
        void waitTaskThread() {
            for(;notify;)
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }


        /**
         * 唤醒执行线程
         */
        void notifyTaskThread() {
            notify = false;
            synchronized (this) {
                this.notify();
            }
        }

        /**
         * 注册 task 到时间轮里
         * @param task  任务
         */
        @Override
        void registTask(TimeWheelTask task) {
            synchronized (this) {
                int i = taskExecuteIndex(task);
                registTask0(task,i);
            }
        }

        void registTask0(TimeWheelTask task,int index) {
            Segment s = getSegment(index);
            s.addTask(task);
        }

        @Override
        void shutdownGracefully() {
            synchronized (this) {
                stop = true;
            }
        }


        void registAfterBeginRunning(TimeWheelTask task) {
            if(task.getStatus() == TaskStatus.REGIST_AFTER_RUNNING)
                registTask(task);
        }

        void registAfterFinish(TimeWheelTask task) {
            if(task.getStatus() == TaskStatus.REGIST_AFTER_FINISH)
                registTask(task);
        }

        int taskExecuteIndex(TimeWheelTask task) {
            return (int)(task.getDelayMilliSecond() / intervalTime) + 1 + index;
        }

    }



    /**
     * 任务组
     */
    static class DefautSegment implements Segment {
        ConcurrentLinkedQueue<TimeWheelTask> tasks = new ConcurrentLinkedQueue<>();

        @Override
        public void addTask(TimeWheelTask t) {
            tasks.add(t);
        }

        @Override
        public Iterator<TimeWheelTask> iterator() {
            return tasks.iterator();
        }
    }

}
