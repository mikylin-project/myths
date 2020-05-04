package cn.mikylin.myths.schedule;

/**
 * 工作线程抽象类
 *
 * @author mikylin
 * @date 20200504
 */
public abstract class AbstractTaskThread extends Thread {

    /**
     * 获取分段任务组
     * @param index  角标
     * @return  任务组
     */
    abstract Segment getSegment(int index);

    /**
     * 开始任务
     * @param index  角标
     */
    abstract void doTask(int index);

    /**
     * 注册任务
     * @param task  任务
     */
    abstract void registTask(TimeWheelTask task);

    /**
     * 优雅关闭
     */
    abstract void shutdownGracefully();

}
