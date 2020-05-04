package cn.mikylin.myths.schedule;

/**
 * 时间轮任务组
 *
 * @author mikylin
 * @date 20200504
 */
public interface Segment extends Iterable<TimeWheelTask> {

    void addTask(TimeWheelTask task);
}
