package cn.mikylin.myths;

import cn.mikylin.myths.concurrent.sync.SpanExecutor;
import cn.mikylin.myths.concurrent.sync.ThreadSafeExecutor;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * simple test for {@link cn.mikylin.myths.concurrent.sync.SpanExecutor}
 *
 * @author mikylin
 * @date 20200222
 */
public class ThreadSafeExecutorTest {

    ExecutorService pool = Executors.newFixedThreadPool(10);

    @Test
    public void test() {

        ThreadSafeExecutor t = new SpanExecutorTester();

        for(int i = 0 ; i < 1000 ; i ++) {
            pool.submit(() -> t.safeExecutor(1));
        }

    }


    private static class SpanExecutorTester implements SpanExecutor<Integer,Integer> {

        private int i = 0;

        @Override
        public Integer doExecute(Integer integer) {
            i = i + integer;
            System.out.println(i);
            return i;
        }
    }
}
