package cn.mikylin.utils;

import cn.mikylin.myths.common.lang.ObjectUtils;
import cn.mikylin.myths.common.Constants;
import cn.mikylin.myths.common.lang.StringUtils;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * jedis utils
 *
 * @author mikylin
 * @date 20191112
 */
public final class RedisUtils {


    // 缓存生存时间
    private static final int default_expire = 24 * 60 * 60;

    private static String DEFAULT_REDIS = "DEFAULT_REDIS";

    private static Map<String,JedisPool> pools;

    static {
        pools = new HashMap<>(1);
    }


    public static void initJedisPool(String ip,int port,String key) {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(Constants.System.COMPUTER_CORE);
            config.setMaxIdle(Constants.System.COMPUTER_CORE);
            config.setMaxWaitMillis(1000);
            config.setTestOnBorrow(true);
            JedisPool jedisPool = new JedisPool(config, ip, port,2000);
            pools.put(key,jedisPool);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static void initJedisPool(String ip,int port) {
        initJedisPool(ip,port,DEFAULT_REDIS);
    }

    /**
     * get jedis
     *
     * @return jedis
     */
    private static Jedis getJedis(String redisKey) {
        JedisPool jedisPool = pools.get(redisKey);
        if(jedisPool != null)
            return jedisPool.getResource();
        throw new RuntimeException("jedis can not be find.");
    }

    /**
     * close jedis
     *
     * @param jedis which need close
     */
    private static void close(Jedis jedis) {
        ObjectUtils.close(jedis);
    }




    /**
     * set string value in key
     *
     * @param key  key
     * @param value  value
     * @param redisKey  redis key
     * @param isNx  is set nx in redis
     * @param expireSecond  if this param bigger then zero,set the expire time
     * @return
     */
    public static String set(String key,String value,String redisKey,
                             boolean isNx,int expireSecond) {

        if(StringUtils.isBlank(key)
                || StringUtils.isBlank(value)
                || StringUtils.isBlank(redisKey))
            throw new RuntimeException("key name or value is blank.");

        try (Jedis jedis = getJedis(redisKey)) {
            SetParams params = new SetParams();
            if(isNx) params.nx();
            if(expireSecond > 0) params.ex(expireSecond);

            return jedis.set(key,value,params);
        }
    }

    public static String set(String key,String value) {
        return set(key,value,DEFAULT_REDIS,false,-1);
    }

    public static boolean lock(String key,String redisKey,int expireSecond) {
        String value = String.valueOf(System.currentTimeMillis());
        String set = set(key, value, redisKey, true, expireSecond);
        return StringUtils.isNotBlank(set) && set.equalsIgnoreCase("OK");
    }



    /**
     * get string value in key.
     * if the key not be exist,return null.
     * if the value not be string,do exception.
     *
     * @param key  key
     * @param redisKey  redis key
     * @return value
     */
    public String get(String key,String redisKey) {

        if(StringUtils.isBlank(key)
                || StringUtils.isBlank(redisKey))
            throw new RuntimeException("key can not be blank.");

        try (Jedis jedis = getJedis(redisKey)) {
            return jedis.get(key);
        }
    }

    public String get(String key) {
        return get(key,DEFAULT_REDIS);
    }


    /**
     * set the key's expire time
     *
     * @param key  key
     * @param seconds  expire time
     * @param redisKey redis key
     * @return key's expire time
     */
    public static Long expire(String key,int seconds,String redisKey) {

        if(StringUtils.isBlank(key) || seconds <= 0)
            throw new RuntimeException("key name or seconds param mistake.");

        Jedis jedis = null;
        try {
            jedis = getJedis(redisKey);
            return jedis.expire(key, seconds);
        } finally {
            close(jedis);
        }
    }

    /**
     * set the key's expire time
     *
     * @param key  key
     */
    public static void expire(String key) {
        expire(key,default_expire,DEFAULT_REDIS);
    }

    /**
     * clean all the keys
     *
     * @return string
     */
    public String flushAll(String redisKey) {
        Jedis jedis = null;
        try {
            jedis = getJedis(redisKey);
            return jedis.flushAll();
        }finally {
            close(jedis);
        }
    }

    public String flushAll() {
        return flushAll(DEFAULT_REDIS);
    }








    /**
     * 删除指定的key,也可以传入一个包含key的数组
     *
     * @param keys
     * @return
     */
    public Long del(String... keys) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.del(keys);
    }

    /**
     * 通过key向指定的value值追加值
     *
     * @param key
     * @param str
     * @return
     */
    public Long append(String key, String str) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.append(key, str);
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.exists(key);
    }

    /**
     * 设置key value,如果key已经存在则返回0
     *
     * @param key
     * @param value
     * @return
     */
    public Long setnx(String key, String value) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.setnx(key, value);
    }

    /**
     * 设置key value并指定这个键值的有效期
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public String setex(String key, int seconds, String value) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.setex(key, seconds, value);
    }

    /**
     * 通过key 和offset 从指定的位置开始将原先value替换
     *
     * @param key
     * @param offset
     * @param str
     * @return
     */
    public Long setrange(String key, int offset, String str) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.setrange(key, offset, str);
    }

    /**
     * 通过批量的key获取批量的value
     *
     * @param keys
     * @return
     */
    public List<String> mget(String... keys) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.mget(keys);
    }

    /**
     * 批量的设置key:value,也可以一个
     *
     * @param keysValues
     * @return
     */
    public String mset(String... keysValues) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.mset(keysValues);
    }

    /**
     * 批量的设置key:value,可以一个,如果key已经存在则会失败,操作会回滚
     *
     * @param keysValues
     * @return
     */
    public Long msetnx(String... keysValues) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.msetnx(keysValues);
    }

    /**
     * 设置key的值,并返回一个旧值
     *
     * @param key
     * @param value
     * @return
     */
    public String getSet(String key, String value) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.getSet(key, value);
    }

    /**
     * 通过下标 和key 获取指定下标位置的 value
     *
     * @param key
     * @param startOffset
     * @param endOffset
     * @return
     */
    public String getrange(String key, int startOffset, int endOffset) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.getrange(key, startOffset, endOffset);
    }

    /**
     * 通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1
     *
     * @param key
     * @return
     */
    public Long incr(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.incr(key);
    }

    /**
     * 通过key给指定的value加值,如果key不存在,则这是value为该值
     *
     * @param key
     * @param integer
     * @return
     */
    public Long incrBy(String key, long integer) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.incrBy(key, integer);
    }

    /**
     * 对key的值做减减操作,如果key不存在,则设置key为-1
     *
     * @param key
     * @return
     */
    public Long decr(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.decr(key);
    }

    /**
     * 减去指定的值
     *
     * @param key
     * @param integer
     * @return
     */
    public Long decrBy(String key, long integer) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.decrBy(key, integer);
    }

    /**
     * 通过key获取value值的长度
     *
     * @param key
     * @return
     */
    public Long strLen(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.strlen(key);
    }

    /**
     * 通过key给field设置指定的值,如果key不存在则先创建,如果field已经存在,返回0
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hsetnx(String key, String field, String value) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.hsetnx(key, field, value);
    }

    /**
     * 通过key给field设置指定的值,如果key不存在,则先创建
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hset(String key, String field, String value) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.hset(key, field, value);
    }

    /**
     * 通过key同时设置 hash的多个field
     *
     * @param key
     * @param hash
     * @return
     */
    public String hmset(String key, Map<String, String> hash) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.hmset(key, hash);
    }

    /**
     * 通过key 和 field 获取指定的 value
     *
     * @param key
     * @param failed
     * @return
     */
    public String hget(String key, String failed) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.hget(key, failed);
    }

    /**
     * 设置key的超时时间为seconds
     *
     * @param key
     * @param seconds
     * @return
     */
    public Long expire(String key, int seconds) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.expire(key, seconds);
    }

    /**
     * 通过key 和 fields 获取指定的value 如果没有对应的value则返回null
     *
     * @param key
     * @param fields 可以是 一个String 也可以是 String数组
     * @return
     */
    public List<String> hmget(String key, String... fields) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.hmget(key, fields);
    }

    /**
     * 通过key给指定的field的value加上给定的值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hincrby(String key, String field, Long value) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.hincrBy(key, field, value);
    }

    /**
     * 通过key和field判断是否有指定的value存在
     *
     * @param key
     * @param field
     * @return
     */
    public Boolean hexists(String key, String field) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.hexists(key, field);
    }

    /**
     * 通过key返回field的数量
     *
     * @param key
     * @return
     */
    public Long hlen(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.hlen(key);
    }

    /**
     * 通过key 删除指定的 field
     *
     * @param key
     * @param fields 可以是 一个 field 也可以是 一个数组
     * @return
     */
    public Long hdel(String key, String... fields) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.hdel(key, fields);
    }

    /**
     * 通过key返回所有的field
     *
     * @param key
     * @return
     */
    public Set<String> hkeys(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.hkeys(key);
    }

    /**
     * 通过key返回所有和key有关的value
     *
     * @param key
     * @return
     */
    public List<String> hvals(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.hvals(key);
    }

    /**
     * 通过key获取所有的field和value
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetall(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.hgetAll(key);
    }

    /**
     * 通过key向list头部添加字符串
     *
     * @param key
     * @param strs 可以是一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public Long lpush(String key, String... strs) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.lpush(key, strs);
    }

    /**
     * 通过key向list尾部添加字符串
     *
     * @param key
     * @param strs 可以是一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public Long rpush(String key, String... strs) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.rpush(key, strs);
    }

    /**
     * 通过key在list指定的位置之前或者之后 添加字符串元素
     *
     * @param key
     * @param pivot list里面的value
     * @param value 添加的value
     * @return
     */
    public Long linsertBefore(String key,String pivot,String value) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.linsert(key, ListPosition.BEFORE, pivot, value);
    }

    public Long linsertAfter(String key,String pivot,String value) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.linsert(key, ListPosition.AFTER, pivot, value);
    }

    /**
     * 通过key设置list指定下标位置的value
     * 如果下标超过list里面value的个数则报错
     *
     * @param key
     * @param index 从0开始
     * @param value
     * @return 成功返回OK
     */
    public String lset(String key, Long index, String value) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.lset(key, index, value);
    }

    /**
     * 通过key从对应的list中删除指定的count个 和 value相同的元素
     *
     * @param key
     * @param count 当count为0时删除全部
     * @param value
     * @return 返回被删除的个数
     */
    public Long lrem(String key, long count, String value) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.lrem(key, count, value);
    }

    /**
     * 通过key保留list中从strat下标开始到end下标结束的value值
     *
     * @param key
     * @param start
     * @param end
     * @return 成功返回OK
     */
    public String ltrim(String key, long start, long end) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.ltrim(key, start, end);
    }

    /**
     * 通过key从list的头部删除一个value,并返回该value
     *
     * @param key
     * @return
     */
    public synchronized String lpop(String key) {

        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.lpop(key);
    }

    /**
     * 通过key从list尾部删除一个value,并返回该元素
     *
     * @param key
     * @return
     */
    synchronized public String rpop(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.rpop(key);
    }

    /**
     * 通过key从一个list的尾部删除一个value并添加到另一个list的头部,并返回该value
     * 如果第一个list为空或者不存在则返回null
     *
     * @param srckey
     * @param dstkey
     * @return
     */
    public String rpoplpush(String srckey, String dstkey) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.rpoplpush(srckey, dstkey);
    }

    /**
     * 通过key获取list中指定下标位置的value
     *
     * @param key
     * @param index
     * @return 如果没有返回null
     */
    public String lindex(String key, long index) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.lindex(key, index);
    }

    /**
     * 通过key返回list的长度
     *
     * @param key
     * @return
     */
    public Long llen(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.llen(key);
    }

    /**
     * 通过key获取list指定下标位置的value
     * 如果start 为 0 end 为 -1 则返回全部的list中的value
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.lrange(key, start, end);
    }

    /**
     * 通过key向指定的set中添加value
     *
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 添加成功的个数
     */
    public Long sadd(String key, String... members) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.sadd(key, members);
    }

    /**
     * 通过key删除set中对应的value值
     *
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 删除的个数
     */
    public Long srem(String key, String... members) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.srem(key, members);
    }

    /**
     * 通过key随机删除一个set中的value并返回该值
     *
     * @param key
     * @return
     */
    public String spop(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.spop(key);
    }

    /**
     * 通过key获取set中的差集
     * 以第一个set为标准
     *
     * @param keys 可以 是一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public Set<String> sdiff(String... keys) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.sdiff(keys);
    }

    /**
     * 通过key获取set中的差集并存入到另一个key中
     * 以第一个set为标准
     *
     * @param dstkey 差集存入的key
     * @param keys   可以 是一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public Long sdiffstore(String dstkey, String... keys) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.sdiffstore(dstkey, keys);
    }

    /**
     * 通过key获取指定set中的交集
     *
     * @param keys 可以 是一个string 也可以是一个string数组
     * @return
     */
    public Set<String> sinter(String... keys) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.sinter(keys);
    }

    /**
     * 通过key获取指定set中的交集 并将结果存入新的set中
     *
     * @param dstkey
     * @param keys   可以 是一个string 也可以是一个string数组
     * @return
     */
    public Long sinterstore(String dstkey, String... keys) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.sinterstore(dstkey, keys);
    }

    /**
     * 通过key返回所有set的并集
     *
     * @param keys 可以 是一个string 也可以是一个string数组
     * @return
     */
    public Set<String> sunion(String... keys) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.sunion(keys);
    }

    /**
     * 通过key返回所有set的并集,并存入到新的set中
     *
     * @param dstkey
     * @param keys   可以 是一个string 也可以是一个string数组
     * @return
     */
    public Long sunionstore(String dstkey, String... keys) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.sunionstore(dstkey, keys);
    }

    /**
     * 通过key将set中的value移除并添加到第二个set中
     *
     * @param srckey 需要移除的
     * @param dstkey 添加的
     * @param member set中的value
     * @return
     */
    public Long smove(String srckey, String dstkey, String member) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.smove(srckey, dstkey, member);
    }

    /**
     * 通过key获取set中value的个数
     *
     * @param key
     * @return
     */
    public Long scard(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.scard(key);
    }

    /**
     * 通过key判断value是否是set中的元素
     *
     * @param key
     * @param member
     * @return
     */
    public Boolean sismember(String key, String member) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.sismember(key, member);
    }

    /**
     * 通过key获取set中随机的value,不删除元素
     *
     * @param key
     * @return
     */
    public String srandmember(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.srandmember(key);
    }

    /**
     * 通过key获取set中所有的value
     *
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.smembers(key);
    }


    /**
     * 通过key向zset中添加value,score,其中score就是用来排序的
     * 如果该value已经存在则根据score更新元素
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Long zadd(String key, double score, String member) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zadd(key, score, member);
    }

    /**
     * 通过key删除在zset中指定的value
     *
     * @param key
     * @param members 可以 是一个string 也可以是一个string数组
     * @return
     */
    public Long zrem(String key, String... members) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zrem(key, members);
    }

    /**
     * 通过key增加该zset中value的score的值
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Double zincrby(String key, double score, String member) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zincrby(key, score, member);
    }

    /**
     * 通过key返回zset中value的排名
     * 下标从小到大排序
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrank(String key, String member) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zrank(key, member);
    }

    /**
     * 通过key返回zset中value的排名
     * 下标从大到小排序
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrevrank(String key, String member) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zrevrank(key, member);
    }

    /**
     * 通过key将获取score从start到end中zset的value
     * socre从大到小排序
     * 当start为0 end为-1时返回全部
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zrevrange(key, start, end);
    }

    /**
     * 通过key返回指定score内zset中的value
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrangebyscore(String key, String max, String min) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zrevrangeByScore(key, max, min);
    }

    /**
     * 通过key返回指定score内zset中的value
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrangeByScore(String key, double max, double min) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zrevrangeByScore(key, max, min);
    }

    /**
     * 返回指定区间内zset中value的数量
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(String key, String min, String max) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zcount(key, min, max);
    }

    /**
     * 通过key返回zset中的value个数
     *
     * @param key
     * @return
     */
    public Long zcard(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zcard(key);
    }

    /**
     * 通过key获取zset中value的score值
     *
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key, String member) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zscore(key, member);
    }

    /**
     * 通过key删除给定区间内的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByRank(String key, long start, long end) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zremrangeByRank(key, start, end);
    }

    /**
     * 通过key删除指定score内的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByScore(String key, double start, double end) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.zremrangeByScore(key, start, end);
    }

    /**
     * 返回满足pattern表达式的所有key
     * keys(*)
     * 返回所有的key
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.keys(pattern);
    }

    /**
     * 通过key判断值得类型
     *
     * @param key
     * @return
     */
    public String type(String key) {
        Jedis jedis = getJedis(DEFAULT_REDIS);
        return jedis.type(key);
    }



}