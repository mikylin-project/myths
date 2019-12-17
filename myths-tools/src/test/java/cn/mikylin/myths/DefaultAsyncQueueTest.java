package cn.mikylin.myths;

import cn.mikylin.myths.cache.collection.AsyncQueue;
import cn.mikylin.myths.common.FutureUtils;
import cn.mikylin.myths.common.ThreadUtils;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Future;

public class DefaultAsyncQueueTest {

    @Test
    public void test01() {
        AsyncQueue<Integer> q = AsyncQueue.create();

        for(int i = 0 ; i < 5 ; i ++) {
            final int a = i;
            new Thread(() -> {
                ThreadUtils.sleep(10L);
                q.offer(a);
            }).start();
            ThreadUtils.sleep(1L);
        }

        System.out.println("FINISH");

        Future<Integer> element = q.peek();
        System.out.println(FutureUtils.get(element));

        Future<Integer> poll1 = q.poll();
        System.out.println(FutureUtils.get(poll1));

        Future<Integer> poll2 = q.poll();
        System.out.println(FutureUtils.get(poll2));
    }
}
