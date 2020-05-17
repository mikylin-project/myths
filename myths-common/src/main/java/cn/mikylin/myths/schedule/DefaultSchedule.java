package cn.mikylin.myths.schedule;

import cn.mikylin.myths.common.lang.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DefaultSchedule implements Schedule {

    private TimeWheel wheel;
    private Map<String, TimeWheelTask> tasks;

    public DefaultSchedule(TimeWheel wheel) {
        registWheel(wheel);
        this.tasks = new ConcurrentHashMap<>();
    }


    @Override
    public synchronized void registWheel(TimeWheel wheel) {
        if(wheel == null)
            throw new IllegalArgumentException();
        this.wheel = wheel;
    }

    @Override
    public synchronized void registTask(String taskName, Runnable task,
                           long time, TimeUnit unit, TaskStatus status) {
        long delayTime = TimeUtils.currentTimeMillis(unit,time);
        registTask(taskName,new TimeWheelTask(task,delayTime,status));
    }

    @Override
    public synchronized void registTask(String taskName, TimeWheelTask task) {
        if(wheel == null)
            throw new RuntimeException();

        wheel.registTask(task);
        tasks.put(taskName,task);
    }


}
