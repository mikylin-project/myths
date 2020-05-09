package cn.mikylin.myths.schedule;

import java.util.concurrent.TimeUnit;

/**
 * 时间轮接口层
 *
 * @author mikylin
 * @date 20200503
 */
public interface TimeWheel extends Runnable {

    void start();


    /**
     * 优雅关闭
     */
    void shutdownGracefully();

    /**
     * 将一个任务注册到时间轮上
     * @param task  任务
     */
    void registTask(TimeWheelTask task);

    /**
     * 时间轮休眠
     *
     * @param milliTime  休眠的毫秒数
     */
    default void tick(final long milliTime) {
        for(long wakeUpTime = System.currentTimeMillis() + milliTime;;) {
            long sleepTime = wakeUpTime - System.currentTimeMillis();
            if(sleepTime > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else
                break;
        }
    }

}
