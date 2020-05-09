package cn.mikylin.myths.schedule;

/**
 * 创建新线程的时间轮
 *
 * @author mikylin
 * @date 20200508
 */
public abstract class NewThreadTimeWheel implements TimeWheel {

    /**
     * 一轮时间的分段
     */
    private final int segment;

    /**
     * 休眠时间，单位 ms
     */
    private final long intervalTime;


    public NewThreadTimeWheel(int segment, long intervalTime) {
        if(segment <= 0 || intervalTime <= 0L)
            throw new RuntimeException();

        // 分段
        this.segment = segment;
        // 休眠时间
        this.intervalTime = intervalTime;
    }

    int getSegment() {
        return segment;
    }

    long getIntervalTime() {
        return intervalTime;
    }

    abstract void doTickTask();

    /**
     * 执行时间轮
     */
    @Override
    public void run() {

        // 休眠时间轮线程，并定期去唤醒任务执行线程
        for(;;) {
            tick(getIntervalTime());
            doTickTask();
        }
    }

    @Override
    public void start() {
        Thread t = new Thread(this);
        t.setDaemon(false);
        t.start();
    }
}
