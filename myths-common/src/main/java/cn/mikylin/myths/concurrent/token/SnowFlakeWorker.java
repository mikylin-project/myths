package cn.mikylin.myths.concurrent.token;

/**
 * 基于雪花算法的 id 生成器。
 *
 * 雪花算法产出的 id 本质上是一个长整型的数，一共 64 位：
 * 第一位默认为 0，即 id 必然是正数，不可为负；
 * 后面 41 位是时间戳，最大能表达的时间是 2^41 - 1 毫秒，约 61 年；
 * 后面 5 位是数据中心的 id，能够表示的节点数是 2^5 - 1 = 31 个；
 * 后面 5 位是服务实例 id，能够表示的节点数是 2^5 - 1 = 31 个；
 * 最后 12 位是序列号，在同一毫秒内递增且不重复，最大值 2^12 - 1 = 4095。
 *
 * @author mikylin
 * @date 20200307
 */
public class SnowFlakeWorker {

    private long startTimeStamp;
    private int dataId;
    private int workerId;
    private long lastTimeStamp = -1L;
    private long lastSeq;

    /**
     * 创建一个 id 生成器
     *
     * @param startTimeStamp 生成器的生成时间
     * @param dataId 数据中心 id
     * @param workerId 服务实例 id
     */
    public SnowFlakeWorker(long startTimeStamp,int dataId,int workerId) {

        // 时间戳，验证是否非法或者已经过期
        if(startTimeStamp <= 0L
                || System.currentTimeMillis() - startTimeStamp >= 2175984000000L)
            throw new RuntimeException("start time stamp most be near now.");

        this.startTimeStamp = startTimeStamp; // 开始的时间

        // 数据中心 id 和 服务实例 id 必须在 0 和 31 之间
        if(dataId > 31 || dataId < 0)
            throw new RuntimeException("data id most be 0 to 31.");
        if(workerId > 31 || workerId < 0)
            throw new RuntimeException("worker id most be 0 to 31.");

        this.dataId = dataId << 17;
        this.workerId = workerId << 12;

        // 初始化 sequence 为 0
        this.lastSeq = 0L;
    }

    /**
     * 生成 id
     */
    public long id() {

        long timeSeq; // 时间差的二进制数据转换
        long seq; // 本次的 sequence 编号

        synchronized (this) {

            long now = System.currentTimeMillis(); // 当前时间
            // 如果当前时间小于上一次生成 id 的时间，代表这系统时钟可能存在回退问题
            if(now < lastTimeStamp) {
                throw new RuntimeException(String.format("System time error!"));
            } else if (now == lastTimeStamp) {
                lastSeq = lastSeq + 1L & 4095L;
                if(lastSeq == 0)
                    for(;now <= lastTimeStamp;now = System.currentTimeMillis()) {}
            } else {
                lastSeq = 0;
            }

            seq = lastSeq;
            timeSeq = now - startTimeStamp << 22;
            lastTimeStamp = now;
        }

        return timeSeq | dataId | workerId | seq;
    }



}
