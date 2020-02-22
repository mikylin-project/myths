package cn.mikylin.myths;

import cn.mikylin.myths.common.concurrent.RateLimiter;
import org.junit.jupiter.api.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * simple test for {@link cn.mikylin.myths.common.concurrent.RateLimiter}
 *
 * @author mikylin
 * @date 20200222
 */
public class RateLimitTest {

    @Test
    public void test() {

        RateLimiter rl = new RateLimiter(50,30);
        ExecutorService s = Executors.newFixedThreadPool(100);
        AtomicInteger aa = new AtomicInteger(0);
        for(int i = 0;i <= 10000; i++) {
            s.execute(() -> {
                rl.acquire();
                System.out.println(
                        aa.getAndAdd(1) + " "
                                + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            });
        }
    }
}
