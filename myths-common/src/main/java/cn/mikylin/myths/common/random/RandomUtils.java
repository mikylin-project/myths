package cn.mikylin.myths.common.random;

import cn.mikylin.myths.common.ArrayUtils;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * random utils
 *
 * @author mikylin
 * @date 20200410
 */
public final class RandomUtils {

    private RandomUtils() {}

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final ThreadLocal<Random> rs = new ThreadLocal<>();
    private static final AtomicInteger choose = new AtomicInteger(0);

    private static Random random() {
        Random r = rs.get();
        if(r == null) {
            r = new Random(System.currentTimeMillis());
            rs.set(r);
        }
        return random;
    }

    private static int chooseRandom() {
        return choose.incrementAndGet() % 3;
    }


    private static int randomHash() {
        return new Object().hashCode();
    }

    public static int nextInt(int max) {
        return nextInt(0,max);
    }

    public static int nextInt(int min,int max) {

        if(min >= max)
            return min;

        switch (chooseRandom()) {
            case 0 : return random().nextInt(max - min);
            case 1 : return randomHash() % (max - min) + min;
            default: return random.nextInt(min,max);
        }
    }

    public static double nextDouble(double min,double max) {

        if(min >= max)
            return min;

        switch (chooseRandom()) {
            case 0 : return min + random().nextDouble() * (max - min);
            case 1 : return min + randomHash() / 10d;
            default: return random.nextDouble(min,max);
        }
    }


    public static long nextLong(long max) {
        return nextLong(0,max);
    }

    public static long nextLong(long min,long max) {

        if(min >= max)
            return min;

        switch (chooseRandom()) {
            case 0 :
                long n = max - min;
                long bits, val;
                do {
                    bits = (random().nextLong() << 1) >>> 1;
                    val = bits % n;
                } while (bits - val + (n - 1) < 0L);
                return min + val;
            case 1 :
                return (long)randomHash() << 32 + randomHash();
            default: return random.nextLong(min,max);
        }

    }

    public static int[] rowNumsSafe(int count, int per, int range,
                                 int perMin, int perMax,RandomHandler handler) {
        for(;;) {
            try {
                return rowNums(count,per,range,perMin,perMax,handler);
            } catch (StackOverflowError e) {

            }
        }
    }

    public static int[] rowNumsSafe(int count, int per, int range,
                                    int perMin, int perMax) {
        return rowNumsSafe(count,per,range,perMin,perMax,DEFAULT_HANDLER);
    }


    /**
     * 生成总和一定的一组随机数
     * @param count 总数
     * @param per 份数
     * @param range 极差
     * @param perMin 每份最小数
     * @param perMax 每份最大数
     * @return result
     */
    public static int[] rowNums(int count, int per, int range,
                               int perMin, int perMax,RandomHandler handler) {

        if(count <= 0 || perMin < 0
                || per <= 0 || range <= 0
                || perMax * per < count)
            throw new RuntimeException();

        // 记录的数组
        int[] result = new int[per];


        // 成功标识
        boolean isSuccess = true;
        // row 点最大值
        int max = 0;
        // row 点最小值
        int min = count;
        // 剩余量
        int surplus = count;
        // 最大值
        int perMaxLimit = perMax;

        // 轮询每一份的 row 点
        for(int i = 0 ; i < per ; i ++) {

            if(surplus <= perMin) {
                isSuccess = false;
                break;
            }
            else if(i == per - 1) {
                result[i] = surplus;
                break;
            }
            else if(surplus < perMaxLimit)
                perMaxLimit = surplus;

            int thisPerCount = nextInt(perMin,perMaxLimit);

            // 刷新最大值和最小值
            if(thisPerCount > max) max = thisPerCount;
            if(thisPerCount < min) min = thisPerCount;

            // 检查极差
            if(min * range <= max) {
                int minThisPer = max / range;
                if(perMaxLimit < minThisPer) {
                    isSuccess = false;
                    break;
                }
                thisPerCount = nextInt(minThisPer,perMaxLimit);
                thisPerCount = handler.handler(i,thisPerCount);
            }
            // 刷新剩余量
            surplus = surplus - thisPerCount;
            if(surplus <= 0 && i != per - 1) {
                isSuccess = false;
                break;
            }

            result[i] = thisPerCount;
        }

        // 判断标识条件和去重后的数据
        // 如果去重之后发现数据重复的太多，就重新 row 点
        if(!isSuccess
                || ArrayUtils.distinctArray(result).length <= (per * 4 / 5))
            result = rowNums(count,per,range,perMin,perMax,handler);

        return result;
    }


    private static RandomHandler DEFAULT_HANDLER = (i,perNum) -> perNum;
}
