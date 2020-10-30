package cn.mikylin.myths;

import cn.mikylin.myths.concurrent.token.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * simple test for {@link cn.mikylin.myths.concurrent.token.SnowFlakeWorker}
 *
 * @author mikylin
 * @date 20200222
 */
public class SnowFlakeTest {

    @Test
    public void test() {
        SnowFlakeWorker worker = new SnowFlakeWorker(System.currentTimeMillis(),1,1);

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(10000000);

        long s = System.currentTimeMillis();
        for(int i = 0 ; i <= 10000000 ; i ++) {
            executorService.execute(() -> {
                worker.id();
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long b = System.currentTimeMillis();

        System.out.println(b - s);
    }
}
