package cn.mikylin.myths;

import cn.mikylin.myths.common.concurrent.TokenLimiter;
import org.junit.jupiter.api.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * simple test for {@link TokenLimiter}
 *
 * @author mikylin
 * @date 20200222
 */
public class RateLimitTest {

    @Test
    public void test() {

        TokenLimiter rl = new TokenLimiter(50,30);
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
