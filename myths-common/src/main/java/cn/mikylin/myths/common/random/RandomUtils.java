package cn.mikylin.myths.common.random;

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

}
