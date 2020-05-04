package cn.mikylin.myths.schedule;

/**
 * 时间轮接口层
 *
 * @author mikylin
 * @date 20200503
 */
public interface TimeWheel {

    /**
     * 开启时间轮
     */
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

}
