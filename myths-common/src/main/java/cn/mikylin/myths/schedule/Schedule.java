package cn.mikylin.myths.schedule;

import java.util.concurrent.TimeUnit;

/**
 * 任务管理器
 *
 * @author mikylin
 * @date 20200503
 */
public interface Schedule {

    void registWheel(TimeWheel wheel);
    void registTask(String taskName,Runnable task,long time,TimeUnit unit,TaskStatus status);
    void registTask(String taskName,TimeWheelTask task);

}
