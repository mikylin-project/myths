package cn.mikylin.myths.cache.map;

import cn.mikylin.myths.cache.map.bi.BiHashMap;
import cn.mikylin.myths.cache.map.lruk.LRUKCacheMap;
import cn.mikylin.myths.common.MapUtils;
import java.util.Map;

/**
 * cache builder.
 *
 * @author mikylin
 * @date 20190801
 */
public final class CacheBuilder {

    /**
     * quick to create lru-k map
     */

    public static <K,V> LRUKMapBuilder<K,V> lurK() {
        return new LRUKMapBuilder<>();
    }

    public static class LRUKMapBuilder<K,V> {

        private static int K_DEFAULT = 3;
        private static long K_TIMES_DEFAULT = 10000l;
        private static int CAP_DEFAULT = 20;
        private static int SOFT_REF_CAP_DEFAULT = CAP_DEFAULT / 2;
        private static long EXPIRE_TIME_DEFAULT = 0L;

        private int cap;
        private int softRefCap;
        private int k;
        private long kTimes;
        private CacheLoader<K,V> loader;
        private boolean defensePolicy;
        private long expireTime;

        private boolean finished;

        public LRUKMapBuilder<K,V> cap(int cap){
            this.cap = cap;
            return this;
        }

        public LRUKMapBuilder<K,V> softRefCap(int softRefCap) {
            this.softRefCap = softRefCap;
            return this;
        }

        public LRUKMapBuilder<K,V> k(int k){
            this.k = k;
            return this;
        }

        public LRUKMapBuilder<K,V> kTimes(int kTimes) {
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

            synchronized (this){
                if(finished)
                    throw new RuntimeException("builder just closed.");

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

                finished = true;

                return new LRUKCacheMap(cap,k,kTimes,loader,expireTime);
            }
        }
    }

    /**
     * quick to create bi map
     */

    public static <K,V> BiHashMap<K,V> bi(Map<K,V> normal, Map<V,K> rever){
        return new BiHashMap<>(normal,rever);
    }

    public static <K,V> BiHashMap<K,V> bi(){
        return bi(MapUtils.newConcurrentMap(),MapUtils.newConcurrentMap());
    }

}
