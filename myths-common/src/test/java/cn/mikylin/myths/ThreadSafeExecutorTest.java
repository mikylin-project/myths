package cn.mikylin.myths;

import cn.mikylin.myths.common.concurrent.NonBlockingExecutor;
import cn.mikylin.myths.common.concurrent.ThreadSafeExecutor;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadSafeExecutorTest {

    ExecutorService pool = Executors.newFixedThreadPool(10);

    @Test
    public void test() {

        ThreadSafeExecutor t = new NonBlockingExecutorTester();

        for(int i = 0 ; i < 1000 ; i ++) {
            pool.submit(() -> t.doSafeExecute(1));
        }

    }


    private static class NonBlockingExecutorTester implements NonBlockingExecutor<Integer,Integer> {

        private int i = 0;

        @Override
        public Integer doExecute(Integer integer) {
            i = i + integer;
            System.out.println(i);
            return i;
        }
    }
}
