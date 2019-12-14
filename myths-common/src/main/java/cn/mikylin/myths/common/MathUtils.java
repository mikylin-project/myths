package cn.mikylin.myths.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MathUtils {


    /**
     * 生成总和一定的一组随机数
     * @param count 总数
     * @param per 份数
     * @param range 极差
     * @param perMin 每份最小数
     * @param perMax 每份最大数
     * @return
     */
    public static List<Integer> rowNum(int count, int per,
                                       int range, int perMin, int perMax) {

        if(count <= 0 || perMin < 0
                || per <= 0 || range <= 0
                || perMax * per < count)
            throw new RuntimeException();

        // 记录的列表
        List<Integer> list = new ArrayList<>(per);
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
        for(int i = 1 ; i <= per ; i ++) {

            if(surplus < perMaxLimit)
                perMaxLimit = surplus;

            int thisPerCount = RandomUtils.nextInt(perMin,perMaxLimit);

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
                thisPerCount = RandomUtils.nextInt(minThisPer,perMaxLimit);
            }
            // 刷新剩余量
            surplus = surplus - thisPerCount;
            if(surplus <= 0 && i != per) {
                isSuccess = false;
                break;
            }

            list.add(thisPerCount);
        }

        // 判断标识条件和去重后的数据
        // 如果去重之后发现数据重复的太多，就重新 row 点
        if(!isSuccess
                || new HashSet<>(list).size() <= (list.size() * 4 / 5))
            list = rowNum(count,per,range,perMin,perMax);

        return list;
    }
}
