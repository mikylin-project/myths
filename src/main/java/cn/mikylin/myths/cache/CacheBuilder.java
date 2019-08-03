package cn.mikylin.myths.cache;

import cn.mikylin.myths.cache.map.LRUKCacheMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CacheBuilder {


    // LRUKCacheMap 的创建工具类，没做完
    public static <K,V> LRUKMapBuilder<K,V> lurK(){
        return new LRUKMapBuilder<>();
    }

    private static class LRUKMapBuilder<K,V>{

        private static int K_DEFAULT = 3;
        private static long K_TIMES_DEFAULT = 10000l;
        private static int CAP_DEFAULT = 20;
        private static int SOFT_REF_CAP_DEFAULT = CAP_DEFAULT / 2;
        private static long EXPIRE_TIME_DEFAULT = 0L;
        private static boolean DEFENSE_POLICY_DEFAULT = false;

        private int cap;
        private int softRefCap;
        private int k;
        private long kTimes;
        private CacheLoader<K,V> loader;
        private boolean defensePolicy;
        private long expireTime;

        public LRUKMapBuilder<K,V> cap(int cap){
            this.cap = cap;
            return this;
        }

        public LRUKMapBuilder<K,V> softRefCap(int softRefCap){
            this.softRefCap = softRefCap;
            return this;
        }

        public LRUKMapBuilder<K,V> k(int k){
            this.k = k;
            return this;
        }

        public LRUKMapBuilder<K,V> kTimes(int kTimes){
            this.kTimes = kTimes;
            return this;
        }

        public LRUKMapBuilder<K,V> entryLoader(CacheLoader<K,V> loader){
            this.loader = loader;
            return this;
        }

        public LRUKMapBuilder<K,V> defensePolicy(boolean defensePolicy){
            this.defensePolicy = defensePolicy;
            return this;
        }

        public LRUKMapBuilder<K,V> expireTime(long expireTime){
            this.expireTime = expireTime;
            return this;
        }

        public LRUKCacheMap<K,V> build(){

            if(cap <= 0)
                cap = CAP_DEFAULT;
            if(softRefCap <= 0)
                softRefCap = SOFT_REF_CAP_DEFAULT;
            if(k <= 0)
                k = K_DEFAULT;
            if(kTimes <= 0L)
                kTimes = K_TIMES_DEFAULT;
            if(expireTime <= 0L)
                expireTime = EXPIRE_TIME_DEFAULT;

            return new LRUKCacheMap(cap,softRefCap,k,kTimes,
            loader,defensePolicy,expireTime);
        }
    }


    //static ExecutorService pool = Executors.newFixedThreadPool(10);
    public static void main(String[] args) {

        LRUKMapBuilder<Integer,Integer> builder = CacheBuilder.lurK();
        LRUKCacheMap<Integer,Integer> map = builder.build();

        for(int i = 0 ; i < 100 ; i ++){
            final Integer in = i;
            final Integer intt = i + 1;
            System.out.println(in);
            map.put(in,intt);
        }

        map.put(82,85);
        System.out.println(map.get(82));
    }
}
