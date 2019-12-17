package cn.mikylin.myths.common;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    private static ThreadLocalRandom random = ThreadLocalRandom.current();

    public static int nextInt(int max) {
        return random.nextInt(max);
    }

    public static int nextInt(int min,int max) {
        return random.nextInt(min,max);
    }

    public static long nextLong(long max) {
        return random.nextLong(max);
    }

    public static long nextLong(long min,long max) {
        return random.nextLong(min,max);
    }

    public static boolean probabilityFor70percent() {
        return nextInt(10) < 7;
    }

    public static boolean probabilityForHalf() {
        return nextInt(10) < 5;
    }

    public static boolean probabilityFor30percent() {
        return nextInt(10) < 3;
    }
}
