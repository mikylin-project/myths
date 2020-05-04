package cn.mikylin.myths.schedule;

import cn.mikylin.myths.common.lang.TimeUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DefaultSchedule implements Schedule {

    private List<TimeWheel> wheels;
    private Map<String, TimeWheelTask> tasks;

    public DefaultSchedule() {
        this.wheels = new ArrayList<>(1);
        this.tasks = new ConcurrentHashMap<>();
    }


    @Override
    public void registWheel(TimeWheel wheel) {
        if(wheel == null)
            throw new RuntimeException();
        wheels.add(wheel);
    }

    @Override
    public void registTask(String taskName, Runnable task,
                           long time, TimeUnit unit, TaskStatus status) {
        long delayTime = TimeUtils.currentTimeMillis(unit, time);
        registTask(taskName,new TimeWheelTask(task,delayTime,status));
    }

    @Override
    public void registTask(String taskName, TimeWheelTask task) {
        if(wheels.isEmpty())
            throw new RuntimeException();

        for(TimeWheel w : wheels) {
            w.registTask(task);
            tasks.put(taskName,task);
        }
    }


}
