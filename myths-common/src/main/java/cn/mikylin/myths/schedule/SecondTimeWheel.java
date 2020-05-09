package cn.mikylin.myths.schedule;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 时间轮默认实现
 *
 * @author mikylin
 * @date 20200503
 */
public final class SecondTimeWheel extends NewThreadTimeWheel {

    /**
     * 任务执行线程
     */
    private final Thread t;
    private final TaskThreadRunner taskRunner;

    /**
     * 构造方法
     * @param segment  一轮的总时间
     * @param taskPool  执行任务的线程池
     */
    public SecondTimeWheel(int segment,ExecutorService taskPool) {

        super(segment,1000L);

        // 线程池
        ExecutorService pool = taskPool;
        if(pool == null)
            pool = Executors.newSingleThreadExecutor();

        taskRunner = new TaskThreadRunner(1000,segment,pool);
        t = new Thread(taskRunner);
        t.start();
    }

    public SecondTimeWheel(int segment) {
        this(segment,Executors.newSingleThreadExecutor());
    }

    /**
     * 执行时间轮
     */
    @Override
    void doTickTask() {
        taskRunner.notifyTaskThread();
    }



    /**
     * 优雅关闭
     */
    @Override
    public void shutdownGracefully() {
        synchronized (this) {
            if(t == null)
                throw new RuntimeException();
            taskRunner.shutdownGracefully();
        }
    }

    /**
     * 将一个任务注册到时间轮上
     * @param task  任务
     */
    @Override
    public void registTask(TimeWheelTask task) {
        taskRunner.registTask(task);
    }


    /**
     * 工作线程
     */
    static class TaskThreadRunner implements Runnable {

        boolean notify = true;
        boolean stop = false;
        int index = 0;
        final int segment;
        final long intervalTime;
        ExecutorService pool;

        private final Segment[] segments;

        TaskThreadRunner(long intervalTime, int segment, ExecutorService pool) {
            this.intervalTime = intervalTime;
            this.segment = segment;
            this.pool = pool;

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
                            System.out.println(System.currentTimeMillis());
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
            for(;notify;) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            notify = true;
        }


        /**
         * 唤醒执行线程
         */
        void notifyTaskThread() {
            notify = false;
            synchronized (this) {
                notifyAll();
            }
        }

        /**
         * 注册 task 到时间轮里
         * @param task  任务
         */
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

        synchronized void shutdownGracefully() {
            stop = true;
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
            long delayMilliSecond = task.getDelayMilliSecond();

            int delay = 0;
            if(delayMilliSecond % intervalTime != 0L) {
                delay = 1;
            }

            int i = (int)(delayMilliSecond / intervalTime);
            return i + index + delay;
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
