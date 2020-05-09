package cn.mikylin.myths;

import cn.mikylin.myths.schedule.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class TimeWheelTaskTest {

    @Test
    public void test1() {

        TimeWheel wheel = new SecondTimeWheel(100);


        Schedule s = new DefaultSchedule();
        s.registWheel(wheel);
        s.registTask("haha", new Runnable() {
            @Override
            public void run() {
                System.out.println("111");
            }
        },10L,TimeUnit.SECONDS,TaskStatus.DO_ONCE);

        System.out.println(System.currentTimeMillis());
        wheel.start();

    }
}
