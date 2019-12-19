package cn.mikylin.utils;

import cn.mikylin.myths.common.ObjectUtils;
import cn.mikylin.myths.common.Constants;
import cn.mikylin.myths.common.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * jedis utils
 *
 * @author mikylin
 * @date 20191112
 */
public class RedisUtils {

    private static boolean init = false;

    // 缓存生存时间
    private static final int default_expire = 24 * 60 * 60;

    // pool 对象
    private static JedisPool jedisPool = null;

    public static void defaultInitJedisPool(String ip,int port) {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(Constants.System.COMPUTER_CORE);
            config.setMaxIdle(Constants.System.COMPUTER_CORE);
            config.setMaxWaitMillis(1000);
            config.setTestOnBorrow(true);
            jedisPool = new JedisPool(config, ip, port,2000);
            init = true;
        } catch (Exception e) {

        }
    }

    /**
     * get jedis
     *
     * @return jedis
     */
    private static Jedis getJedis() {
        if(!init || jedisPool == null)
            throw new RuntimeException("init the redis utils before use it.");
        return jedisPool.getResource();
    }

    /**
     * close jedis
     *
     * @param jedis which need close
     */
    private static void close(Jedis jedis) {
        if(jedis != null) {
            ObjectUtils.close(jedis);
        }
    }

    /**
     * set the key's expire time
     *
     * @param key  key
     * @param seconds  expire time
     * @return key's expire time
     */
    public static Long expire(String key,int seconds){

        if(StringUtils.isBlank(key) || seconds <= 0)
            throw new RuntimeException("key name or seconds param mistake.");

        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.expire(key, seconds);
        }finally {
            close(jedis);
        }
    }

    /**
     * set the key's expire time
     *
     * @param key  key
     */
    public static void expire(String key){
        expire(key,default_expire);
    }

    /**
     * clean all the keys
     *
     * @return string
     */
    public String flushAll(){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.flushAll();
        }finally {
            close(jedis);
        }
    }


}