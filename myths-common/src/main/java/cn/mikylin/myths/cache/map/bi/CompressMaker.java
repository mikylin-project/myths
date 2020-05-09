package cn.mikylin.myths.cache.map.bi;

/**
 * 数字压缩
 * 本质上是将十进制转化为任意的进制数
 *
 * @author mikylin
 * @date 20200509
 */
public class CompressMaker {

    private final BiMap<Long,Character> biMap;

    public CompressMaker() {
        biMap = new SynchronizedBiHashMap<>();
    }

    public void set(Long key,Character s) {
        biMap.set(key,s);
    }

    /**
     * 十进制数字转任意进制
     */
    public String toString(Long number) {

        int size = biMap.size();

        StringBuilder builder = new StringBuilder();

        while(number != 0) {
            Long t = number % size;
            builder.append(biMap.get(t));
            number = number / size;
        }

        return builder.toString();
    }

    /**
     * 任意进制转十进制数字
     */
    public Long toId(String markId) {

        int size = biMap.size();

        Long sum = 0L;

        for(int i = markId.length() - 1 ; i >= 0 ; i --) {
            char markIdC = markId.charAt(i);
            long a = Double.valueOf(Math.pow(size,i)).longValue();
            sum = sum + biMap.revGet(markIdC) * a;
        }
        return sum;
    }

}
