package cn.mikylin.myths.common.lang;

import java.util.Collection;

/**
 * int utils.
 *
 * @author mikylin
 * @date 20201126
 */
public final class Int {

    /**
     * python : 循环 0 - 10
     * for i in range(len(array)):
     *    // ...
     *
     * like it :
     * int[] is = new int[10];
     * for (int i : Int.range(is)) {
     *   // ...
     * }
     *
     * for (int i : Int.range(0,10)) {
     *    // ...
     * }
     */
    public static int[] range(Collection coll) {
        if (coll == null || coll.size() == 0)
            return new int[0];
        return range(0,coll.size() - 1);
    }

    public static int[] range(int[] is) {
        if (is == null || is.length == 0)
            return new int[0];
        return range(0,is.length - 1);
    }

    public static int[] range(int end) {
        return range(0,end);
    }


    public static int[] range(int begin,int end) {
        if (end < begin)
            return new int[0];
        else if (end == begin)
            return new int[]{begin};

        int size = end - begin + 1;
        int[] is = new int[size];
        int j = 0;
        for (int i = begin ; i <= end ; i ++) {
            is[j ++] = i;
        }
        return is;
    }

    /**
     * 十进制 -> 二进制
     */
    public static String toBinaryString(int n) {
        StringBuilder b = new StringBuilder();
        for (int i = 0 ; i < 32 ; i ++) {
            n = n >>> 1;
            b.append(n & 1);
        }

        return b.toString();
    }


}
