package cn.mikylin.myths;

import cn.mikylin.myths.cache.collection.RandomCollection;
import org.junit.jupiter.api.Test;

public class RandomCollectionTest {

    @Test
    public void test01() {
        RandomCollection.Set<Integer> l = new RandomCollection.Set<>();
        l.add(1,2);
        l.add(2,1);
        l.add(3,1);
        l.add(4,1);
        l.add(5,1);
        l.add(6,1);


        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        int num4 = 0;
        int num5 = 0;
        int num6 = 0;

        for(int i = 0 ; i < 700000 ; i ++) {
            Integer r = l.random();
            if(r == 1) num1 ++;
            if(r == 2) num2 ++;
            if(r == 3) num3 ++;
            if(r == 4) num4 ++;
            if(r == 5) num5 ++;
            if(r == 6) num6 ++;
        }

        System.out.println(num1);
        System.out.println(num2);
        System.out.println(num3);
        System.out.println(num4);
        System.out.println(num5);
        System.out.println(num6);

        System.out.println(num1 + num2 + num3 + num4 + num5 + num6);

        System.out.println(l.get(2));
    }
}
