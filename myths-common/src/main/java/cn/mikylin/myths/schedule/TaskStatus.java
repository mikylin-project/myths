package cn.mikylin.myths.schedule;

/**
 * 任务状态
 *
 * @author mikylin
 * @date 20200503
 */
public enum TaskStatus {

    /**
     * 被取消
     */
    CANCEL,

    /**
     * 执行一次
     */
    DO_ONCE,


    /**
     * 在任务执行时立刻开始计时下一次执行
     */
    REGIST_AFTER_RUNNING,

    /**
     * 在任务执行结束之后开始计时下一次执行
     */
    REGIST_AFTER_FINISH
}
