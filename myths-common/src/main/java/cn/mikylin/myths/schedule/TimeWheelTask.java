package cn.mikylin.myths.schedule;

import static cn.mikylin.myths.schedule.TaskStatus.CANCEL;

/**
 * 任务
 *
 * @author mikylin
 * @date 20200503
 */
public final class TimeWheelTask implements Runnable {

    // 具体的任务
    private final Runnable task;

    // 状态
    private TaskStatus status;

    // 执行间隔时间
    private long delay;

    public TimeWheelTask(Runnable task, long delay, TaskStatus taskStatus) {

        if(taskStatus == null || taskStatus == CANCEL || task == null)
            throw new RuntimeException();

        this.task = task;
        this.status = taskStatus;
        this.delay = delay;
    }

    @Override
    public void run() {
        task.run();
    }



    public TaskStatus getStatus() {
        return status;
    }

    public long delay() {
        return delay;
    }

    public void cancel() {
        synchronized (this) {
            status = CANCEL;
        }
    }

    public boolean notCancel() {
        synchronized (this) {
            return getStatus() != CANCEL;
        }
    }
}
