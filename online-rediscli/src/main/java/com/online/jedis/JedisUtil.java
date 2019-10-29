package com.online.jedis;


import com.online.common.util.SerializeUtil;
import com.online.lock.JedisLock;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.util.JedisClusterCRC16;
import redis.clients.jedis.util.SafeEncoder;


import java.util.*;

/***
 * jedis工具类
 */
public class JedisUtil {
    /*缓存生存时间  */
    private static final int expire = 60000;

    /***
     * 设置过期时间
     * @param key 键
     * @param seconds 过期时间
     */
    public static void expire(String key,int seconds){
        if(seconds<0){
            return;
        }
        Jedis jedis = JedisPoolClient.getJedis();
        jedis.expire(key, seconds);
        JedisPoolClient.returnJedis(jedis);
    }

    /*
     * 设置默认过期时间 默认60S
     * @param key 键
     */
    public static void expire(String key){
        expire(key,expire);
    }


    /**
     * 更改key 返回值是状态码
     * @param oldkey 旧的名字
     * @param newkey 新的名字
     * @return
     */

    public static String rename(String oldkey,String newkey){
        return rename(SafeEncoder.encode(oldkey),SafeEncoder.encode(newkey));
    }

    /*
     * 更改key，仅当新key不存在时才执行
     * @param oldkey 旧的名字
     * @param newkey 新的名字
     * @return
     */
    public static long renamenx(String oldkey,String newkey){
        Jedis jedis = JedisPoolClient.getJedis();
        long status = jedis.renamenx(oldkey, newkey);
        JedisPoolClient.returnJedis(jedis);
        return status;

    }

    /*
     * 更改key，仅当新key不存在时才执行
     * @param oldkey 旧的名字
     * @param newkey 新的名字
     * @return
     */
    public static String rename(byte[] oldkey,byte[] newkey){
        Jedis jedis = JedisPoolClient.getJedis();
        String status = jedis.rename(oldkey,newkey);
        JedisPoolClient.returnJedis(jedis);
        return status;
    }

    public static void persistObject(String key,Object obj){
        if(obj == null){
            return ;
        }

        Jedis jedis = JedisPoolClient.getJedis();
        jedis.set(key.getBytes(),SerializeUtil.serialize(obj));
        JedisPoolClient.returnJedis(jedis);
    }

    /***
     * 设置key的缓存时间
     * @param key
     * @param obj
     * @param expire
     */
    public static void persistObjectWithExpire(String key,Object obj,int expire){
        if(obj == null){
            return ;
        }
        Jedis jedis = JedisPoolClient.getJedis();
        jedis.set(key.getBytes(), SerializeUtil.serialize(obj));
        jedis.expire(key,expire);
        JedisPoolClient.returnJedis(jedis);
    }
    public static Object getObject(String key){
        Jedis jedis = JedisPoolClient.getJedis();
        byte bytes[]  = jedis.get(key.getBytes());
        JedisPoolClient.returnJedis(jedis);
        if(bytes==null||bytes.length<=0){
            return null;
        }

        Object obj = SerializeUtil.unserialize(bytes);

        return obj;
    }
    /*
     * 设置key的过期时间，以秒为单位
     * aram key 键名@p
     * @param sencons 失效时间
     * @reutrn 返回值是影响的记录数
     */
    public static long expired(String key,int seconds){
        Jedis jedis = JedisPoolClient.getJedis();
        long count = jedis.expire(key, seconds);
        JedisPoolClient.returnJedis(jedis);
        return count;
    }

    /*
     * 设置key的过期时间，它是距历元（即格林威治标准时间 1970年1月1日的00:00:00,格里高利历)的偏移量
     * @param key 键名
     *  @param sencons 失效时间点
     */
    public static long expireAt(String key,long timestamp){
        Jedis jedis = JedisPoolClient.getJedis();
        long count = jedis.expireAt(key, timestamp);
        JedisPoolClient.returnJedis(jedis);
        return count;
    }
    /*
     * 查询key的过期时间
     *      以秒为单位的时间表示返回的是指定key的剩余的生存时间
     * @param key 键名
     * @return 以秒为单位返回剩余时间
     */
    public static long ttl(String key){
        Jedis jedis = JedisPoolClient.getJedis();
        long len = jedis.ttl(key);
        JedisPoolClient.returnJedis(jedis);
        return len;

    }
    /*
     * 取消对key过期时间的设置
     *      将带生存时间的转换成一个永不过期的key
     *@param key 键名
     *  @retrurn    当移除成功时返回1，key不存在或者移除不成功时返回0
     */
    public static long persist(String key){
        Jedis jedis = JedisPoolClient.getJedis();
        long count = jedis.persist(key);
        JedisPoolClient.returnJedis(jedis);
        return count;
    }

    /*
     * 删除keys对应的记录，可以是多个key
     *  @param keys 键名,可变的数组
     *  @return 返回值是被删除的数量
     */
    public static long del(String...keys){
        Jedis jedis = JedisPoolClient.getJedis();
        long count = jedis.del(keys);
        JedisPoolClient.returnJedis(jedis) ;
        return count;
    }

    /*
     * 删除keys对应的记录，可以是多个key
     * @param keys 键名,可变的数组
     * @return 返回删除的高数
     */
    public static long del(byte[]...keys){
        Jedis jedis = JedisPoolClient.getJedis();
        long count = jedis.del(keys);
        JedisPoolClient.returnJedis(jedis);
        return count;
    }
    /*
     * 删除keys对应的记录，可以是多个key
     * @param keys 键名,可变的数组
     * @return 返回删除的高数
     */
    public static long delObject(String key){
        Jedis jedis = JedisPoolClient.getJedis();
        long count = jedis.del(SerializeUtil.serialize(key));
        JedisPoolClient.returnJedis(jedis);
        return count;
    }
    /*
     * 判断key是否存在
     * @param key
     * @return true:存在  false:不存在
     */
    public static boolean exists(String key){
        Jedis jedis = JedisPoolClient.getJedis();
        boolean exists = jedis.exists(key);
        JedisPoolClient.returnJedis(jedis);
        return exists;
    }

    /**
     * 对List，set，SortSet 进行排序，如果集合数据较大应避免使用这个方法
     * @param key 键
     * @return 返回排序后的结果，默认升序 sort key Desc为降序
     */
    public static List<String> sort(String key){
        Jedis jedis = JedisPoolClient.getJedis();
        List<String> list = jedis.sort(key);

        JedisPoolClient.returnJedis(jedis);
        return list;
    }

    /*
     * 对List，set，SortSet 进行排序，如果集合数据较大应避免使用这个方法
     *
     * @return 返回排序后的结果，默认升序 sort key Desc为降序
     */
    public static List<String> sort(String key,SortingParams param){
        Jedis jedis = JedisPoolClient.getJedis();
        List<String> list = jedis.sort(key,param);
        JedisPoolClient.returnJedis(jedis);
        return list;
    }

    /***
     * 返回指定key的存储类型
      * @param key 键名
     * @return 返回键的类型
     */
    public static String type(String key){
        Jedis jedis = JedisPoolClient.getJedis();
        String type = jedis.type(key);
        JedisPoolClient.returnJedis(jedis);
        return type;
    }

    /*
     *
     *
     *
     */

    /***
     * 查找所有匹配模式的键
     * @param pattern  *代表任意多个 ？代表一个
     * @return
     */
    public static Set<String> Keys(String pattern){
        Jedis jedis = JedisPoolClient.getJedis();
        Set<String> set = jedis.keys(pattern);

        JedisPoolClient.returnJedis(jedis);
        return set;
    }

    /*************************set部分*******************************/
    public static class JSetUtil{
        /***
         *  向set添加一条记录
         * @param key 键名
         * @param member 添加的元素
         * @return 如果member已经存在则返回0，否则返回1
         */
        public static long sadd(String key,String member){
            Jedis jedis = JedisPoolClient.getJedis();
            Long s = jedis.sadd(key, member);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

        /**
         *      *  向set添加一条记录
         * @param key 键名
         * @param member 添加的元素
         * @return 如果member已经存在则返回0，否则返回1
         */
        public static long sadd(byte[] key,byte[] member){
            Jedis jedis = JedisPoolClient.getJedis();
            Long s = jedis.sadd(key, member);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

        /**
         * 获取给定key中元素个数
         * @param key
         @return 元素个数
         */
        public static long scard(String key){
            Jedis jedis = JedisPoolClient.getJedis();
            Long count = jedis.scard(key);
            JedisPoolClient.returnJedis(jedis);
            return count;
        }

        /*
         * 返回从第一组和所有的给定集合之间的有差异的成员
         *
         * @return 有差异成员的集合
         */
        public static Set<String> sdiff(String...keys){
            Jedis jedis = JedisPoolClient.getJedis();
            Set<String> set = jedis.sdiff(keys);
            JedisPoolClient.returnJedis(jedis);
            return set;
        }

        /*
         * 这个命令的作用和 SDIFF 类似，但它将结果保存到 destination 集合，而不是简单地返回结果集,如果目标已存在，则覆盖
         *
         * @return  新集合的记录数
         */
        public static long sdiffstore(String newkey,String...keys){
            Jedis jedis = JedisPoolClient.getJedis();
            Long count = jedis.sdiffstore(newkey, keys);
            JedisPoolClient.returnJedis(jedis);
            return count;
        }

        /*
         * sinter 返回给定集合交集成员，如果其中一个集合为不存在或为空，则返回空set
         * @return 交集成员的集合
         */
        public static Set<String> sinter(String...keys){
            Jedis jedis = JedisPoolClient.getJedis();
            Set<String> set = jedis.sinter(keys);
            JedisPoolClient.returnJedis(jedis);
            return set;
        }
        /*
         * sinterstore 这个命令类似于 SINTER 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集。
         * 如果 destination 集合已经存在，则将其覆盖。destination 可以是 key 本身
         *
         * @return 新集合的记录数
         */
        public static long sinterstore(String dstkey,String...keys){
            Jedis jedis = JedisPoolClient.getJedis();
            long count = jedis.sinterstore(dstkey, keys);
            JedisPoolClient.returnJedis(jedis);
            return count;
        }
        /*
         * sismember 确定一个给定的值是否存在
         * @param String member 要判断的值
         * @return 存在返回1，不存在返回0
         */
        public static boolean sismember(String key,String member){
            Jedis jedis = JedisPoolClient.getJedis();
            Boolean s = jedis.sismember(key,member);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }
        /*
         * smembers 返回集合中的所有成员
         * @return 成员集合
         */

        public static Set<String> smembers(String key){
            Jedis jedis = JedisPoolClient.getJedis();
            Set<String> set = jedis.smembers(key);
            JedisPoolClient.returnJedis(jedis);
            return set;
        }

        public static Set<byte[]> smembers(byte[] key){
            Jedis jedis = JedisPoolClient.getJedis();
            Set<byte[]> set = jedis.smembers(key);
            JedisPoolClient.returnJedis(jedis);
            return set;
        }

        /*
         * smove 将成员从源集合移除放入目标集合 </br>
         * 如果源集合不存在或不包含指定成员，不进行任何操作，返回0</br>
         * 否则该成员从源集合上删除，并添加到目标集合，如果目标集合成员以存在，则只在源集合进行删除
         * @param srckey 源集合  dstkey目标集合   member源集合中的成员
         *
         * @return 状态码 1成功 0失败
         */
        public static long smove(String srckey,String dstkey,String member){
            Jedis jedis = JedisPoolClient.getJedis();
            Long s = jedis.smove(srckey, dstkey, member);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }
        /*
         * spop 从集合中删除成员  移除并返回集合中的一个随机元素。
         *
         * @return 被删除的随机成员
         */
        public static String spop(String key){
            Jedis jedis = JedisPoolClient.getJedis();
            String s = jedis.spop(key); //s 被移除的随机成员
            JedisPoolClient.returnJedis(jedis);
            return s;
        }
        /*
         * 从集合中删除指定成员
         * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。当 key 不是集合类型，返回一个错误。
         *
         * @param key member要删除的成员
         * @return 状态码 成功返回1，成员不存在返回0
         */
        public static long srem(String key,String member){
            Jedis jedis = JedisPoolClient.getJedis();
            Long s = jedis.srem(key, member);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

        /*
         * sunion 合并多个集合并将合并后的结果集保存在指定的新集合中，如果新集合已经存在则覆盖
         */
        public static Set<String> sunion(String...keys){
            Jedis jedis = JedisPoolClient.getJedis();
            Set<String> set = jedis.sunion(keys);
            JedisPoolClient.returnJedis(jedis);
            return set;
        }

        /*
         *这个命令类似于 SUNION 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集。
         *如果 destination 已经存在，则将其覆盖。  destination 可以是 key 本身
         */
        public static long sunionstore(String dstkey,String...keys){
            Jedis jedis = JedisPoolClient.getJedis();
            Long s = jedis.sunionstore(dstkey, keys);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }


    }



    /******************************SortSet******************************/

    public static class JSortSetUtil{
        /*
         * zadd 向集合中增加一条记录，如果这个值已经存在，这个值对应的权重将被置为新的权重
         *
         * @param double score 权重 member要加入的值
         * @return 状态码 1成功 0已经存在member值
         */

        public static long zadd(String key,double score,String member){
            Jedis jedis = JedisPoolClient.getJedis();
            long s = jedis.zadd(key, score,member);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }
        /*
         * 获取集合中元素的数量
         * @param String key
         * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0 。
         */
        public static long zcard(String key){
            Jedis jedis = JedisPoolClient.getJedis();
            long count= jedis.zcard(key);
            JedisPoolClient.returnJedis(jedis);
            return count;
        }
        /*
         * zcount 获取指定权重区间内的集合数量
         *
         * @param double min最小排序位置   max最大排序位置
         */
        public static long zcount(String key,double min,double max){
            Jedis jedis = JedisPoolClient.getJedis();
            long count = jedis.zcount(key,min,max);
            JedisPoolClient.returnJedis(jedis);
            return count;
        }
        /*
         * zrange 返回有序集合key中，指定区间的成员0，-1指的是整个区间的成员
         */
        public static Set<String> zrange(String key,int start,int end){

            Jedis jedis = JedisPoolClient.getJedis();
            Set<String> set = jedis.zrange(key,0,-1);
            JedisPoolClient.returnJedis(jedis);
            return set;
        }



        /*
         * zrevrange  返回有序集 key 中，指定区间内的成员。其中成员的位置按 score 值递减(从大到小)来排列
         */
        public static Set<String> zrevrange(String key, int start, int end) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = JedisPoolClient.getJedis();
            Set<String> set = sjedis.zrevrange(key, start, end);
            JedisPoolClient.returnJedis(sjedis);
            return set;
        }

        /*
         * zrangeByScore  根据上下权重查询集合
         */
        public static Set<String> zrangeByScore(String key,double min,double max){
            Jedis jedis = JedisPoolClient.getJedis();
            Set<String> set = jedis.zrangeByScore(key, min, max);
            JedisPoolClient.returnJedis(jedis);
            return set;
        }
        /*
         * 接上面方法，获取有序集合长度
         */
        public static long zlength(String key){
            long len = 0;
            Set<String> set = zrange(key,0,-1);
            len = set.size();
            return len;
        }

        /*
         * zincrby  为有序集 key 的成员 member 的 score 值加上增量 increment
         *
         * @return member 成员的新 score 值，以字符串形式表示
         */

        public static double zincrby(String key,double score,String member){
            Jedis jedis = JedisPoolClient.getJedis();
            double s = jedis.zincrby(key, score, member);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }
        /*
         * zrank 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列
         */

        public static long zrank(String key,String member){
            Jedis jedis = JedisPoolClient.getJedis();
            long index = jedis.zrank(key, member);
            JedisPoolClient.returnJedis(jedis);
            return index;
        }

        /*
         *zrevrank   返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。
         */
        public static long zrevrank(String key,String member){
            Jedis jedis = JedisPoolClient.getJedis();
            long index = jedis.zrevrank(key, member);
            JedisPoolClient.returnJedis(jedis);
            return index;
        }

        /*
         * zrem 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。当 key 存在但不是有序集类型时，返回一个错误。在 Redis 2.4 版本以前， ZREM 每次只能删除一个元素。
         * @return 被成功移除的成员的数量，不包括被忽略的成员
         */
        public static long zrem(String key,String member){
            Jedis jedis = JedisPoolClient.getJedis();
            long count= jedis.zrem(key, member);
            JedisPoolClient.returnJedis(jedis);
            return count;

        }

        /*
         *zremrangebyrank 移除有序集 key 中，指定排名(rank)区间内的所有成员。
         *@return 被移除成员的数量
         */
        public static long zremrangeByRank(String key,int start,int end){
            Jedis jedis = JedisPoolClient.getJedis();
            long count= jedis.zremrangeByRank(key, start,end);
            JedisPoolClient.returnJedis(jedis);
            return count;

        }


        /*
         * zremrangeByScore  删除指定权重区间的元素
         */
        public static long zremrangeByScore(String key, double min, double max) {
            Jedis jedis = JedisPoolClient.getJedis();
            long count = jedis.zremrangeByScore(key, min, max);
            JedisPoolClient.returnJedis(jedis);
            return count;
        }


        /*
         * 获取给定值在集合中的权重
         */
        public static double zscore(String key,String member){
            Jedis jedis = JedisPoolClient.getJedis();
            Double score = jedis.zscore(key, member);
            JedisPoolClient.returnJedis(jedis);
            if(score !=null){
                return score;
            }
            return 0;
        }




    }



    /*******************************hash***********************************/
    public static class JHashUtil{
        /**
         * 从hash中删除指定的存储
         *
         * @param key 键名
         * @param fieid 存储的名字
         * @return 状态码，1成功，0失败
         * */
        public static long hdel(String key, String fieid) {
            Jedis jedis = JedisPoolClient.getJedis();
            long s = jedis.hdel(key, fieid);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

        /***
         * 删除某个key
         * @param key
         * @return
         */
        public static long hdel(String key) {
            Jedis jedis = JedisPoolClient.getJedis();
            long s = jedis.del(key);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

        /**
         * 测试hash中指定的存储是否存在
         *
         * @param key 键名
         * @param fieid 存储的名字
         * @return 1存在，0不存在
         * */
        public static boolean hexists(String key, String fieid) {
            Jedis jedis = JedisPoolClient.getJedis();
            boolean s = jedis.hexists(key, fieid);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

        /**
         * 返回hash中指定存储位置的值
         *
         * @param key
         * @param fieid 存储的名字
         * @return 存储对应的值
         * */
        public static String hget(String key, String fieid) {
            Jedis jedis = JedisPoolClient.getJedis();
            String s = jedis.hget(key, fieid);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

        /***
         * 获取key中某个属性的志
         * @param key hash对应的key
         * @param fieid hash里的key
         * @return
         */
        public static  byte[] hget(byte[] key, byte[] fieid) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis jedis = JedisPoolClient.getJedis();
            byte[] s = jedis.hget(key, fieid);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

        /**
         * 以Map的形式返回hash中的存储和值
         *
         * @param key
         * @return Map<Strinig,String>
         * */
        public static Map<String, String> hgetAll(String key) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis jedis = JedisPoolClient.getJedis();
            Map<String, String> map = jedis.hgetAll(key);
            JedisPoolClient.returnJedis(jedis);
            return map;
        }

        /**
         * 添加一个对应关系
         *
         * @param key hash表的键
         * @param fieid  hash表中元素的键
         * @param value  hahs表中元素的值
         * @return 状态码 1成功，0失败，fieid已存在将更新，也返回0
         * **/
        public static long hset(String key, String fieid, String value) {
            Jedis jedis = JedisPoolClient.getJedis();
            long s = jedis.hset(key, fieid, value);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }
        /**
         * 添加一个对应关系
         *
         * @param key hash表的键
         * @param fieid  hash表中元素的键
         * @param value  hahs表中元素的值
         * @return 状态码 1成功，0失败，fieid已存在将更新，也返回0
         * **/
        public static long hset(String key, String fieid, byte[] value) {
            Jedis jedis = JedisPoolClient.getJedis();
            long s = jedis.hset(key.getBytes(), fieid.getBytes(), value);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

        /**
         * 添加对应关系，只有在fieid不存在时才执行
         *
         * @param key
         * @param fieid
         * @param value
         * @return 状态码 1成功，0失败fieid已存
         * **/
        public static long hsetnx(String key, String fieid, String value) {
            Jedis jedis = JedisPoolClient.getJedis();
            long s = jedis.hsetnx(key, fieid, value);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

        /**
         * 获取hash中value的集合
         *
         * @param key 键
         * @return List<String>
         * */
        public static List<String> hvals(String key) {
            Jedis sjedis = JedisPoolClient.getJedis();
            List<String> list = sjedis.hvals(key);
            JedisPoolClient.returnJedis(sjedis);
            return list;
        }

        /**
         * 在指定的存储位置加上指定的数字，存储位置的值必须可转为数字类型
         *
         * @param
         *            key 键
         * @param
         *            fieid 存储位置
         * @param
         *            value 要增加的值,可以是负数
         * @return 增加指定数字后，存储位置的值
         * */
        public static long hincrby(String key, String fieid, long value) {
            Jedis jedis = JedisPoolClient.getJedis();
            long s = jedis.hincrBy(key, fieid, value);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

        /**
         * 返回指定hash中的所有存储名字,类似Map中的keySet方法
         *
         * @param
         *            key 键
         * @return Set<String> 存储名称的集合
         * */
        public static Set<String> hkeys(String key) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = JedisPoolClient.getJedis();
            Set<String> set = sjedis.hkeys(key);
            JedisPoolClient.returnJedis(sjedis);
            return set;
        }

        /**
         * 获取hash中存储的个数，类似Map中size方法
         *
         * @param
         *            key 键
         * @return long 存储的个数
         * */
        public static long hlen(String key) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = JedisPoolClient.getJedis();
            long len = sjedis.hlen(key);
            JedisPoolClient.returnJedis(sjedis);
            return len;
        }

        /**
         * 根据多个key，获取对应的value，返回List,如果指定的key不存在,List对应位置为null
         *
         * @param
         *            key 键
         * @param
         *             fieids 存储位置
         * @return List<String>
         * */
        public static List<String> hmget(String key, String... fieids) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = JedisPoolClient.getJedis();
            List<String> list = sjedis.hmget(key, fieids);
            JedisPoolClient.returnJedis(sjedis);
            return list;
        }

        public static List<byte[]> hmget(byte[] key, byte[]... fieids) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = JedisPoolClient.getJedis();
            List<byte[]> list = sjedis.hmget(key, fieids);
            JedisPoolClient.returnJedis(sjedis);
            return list;
        }

        /**
         * 添加对应关系，如果对应关系已存在，则覆盖
         *
         * @param
         *            key 键
         * @param
         *            <String,String> 对应关系
         * @return 状态，成功返回OK
         * */
        public static String hmset(String key, Map<String, String> map) {
            Jedis jedis = JedisPoolClient.getJedis();
            String s = jedis.hmset(key, map);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

        /**
         * 添加对应关系，如果对应关系已存在，则覆盖
         *
         * @param
         *            key 键
         * @param
         *            <String,String> 对应关系
         * @return 状态，成功返回OK
         * */
        public static String hmset(byte[] key, Map<byte[], byte[]> map) {
            Jedis jedis = JedisPoolClient.getJedis();
            String s = jedis.hmset(key, map);
            JedisPoolClient.returnJedis(jedis);
            return s;
        }

    }


    // *******************************************Strings*******************************************//

   public static class JStringUtil{
       /**
        * 根据key获取记录
        *
        * @param
        *            key 键
        * @return 值
        * */
       public static String get(String key) {
           // ShardedJedis sjedis = getShardedJedis();
           Jedis sjedis = JedisPoolClient.getJedis();
           String value = sjedis.get(key);
           JedisPoolClient.returnJedis(sjedis);
           return value;
       }

       /**
        * 根据key获取记录
        *
        * @param  key 键
        * @return 值
        * */
       public static byte[] get(byte[] key) {
           // ShardedJedis sjedis = getShardedJedis();
           Jedis sjedis = JedisPoolClient.getJedis();
           byte[] value = sjedis.get(key);
           JedisPoolClient.returnJedis(sjedis);
           return value;
       }

       /**
        * 添加有过期时间的记录
        *
        * @param
        *            key 键
        * @param
        *            seconds 过期时间，以秒为单位
        * @param
        *            value
        * @return String 操作状态
        * */
       public static String setEx(String key, int seconds, String value) {
           Jedis jedis = JedisPoolClient.getJedis();
           String str = jedis.setex(key, seconds, value);
           JedisPoolClient.returnJedis(jedis);
           return str;
       }

       /**
        * 添加有过期时间的记录
        *
        * @param
        *            key 键
        * @param     seconds 过期时间，以秒为单位
        * @param
        *            value 值
        * @return String 操作状态
        * */
       public static String setEx(byte[] key, int seconds, byte[] value) {
           Jedis jedis = JedisPoolClient.getJedis();
           String str = jedis.setex(key, seconds, value);
           JedisPoolClient.returnJedis(jedis);
           return str;
       }

       /**
        * 添加一条记录，仅当给定的key不存在时才插入
        *
        * @param
        *            key 键
        * @param
        *            value 值
        * @return long 状态码，1插入成功且key不存在，0未插入，key存在
        * */
       public static long setnx(String key, String value) {
           Jedis jedis = JedisPoolClient.getJedis();
           long str = jedis.setnx(key, value);
           JedisPoolClient.returnJedis(jedis);
           return str;
       }

       /**
        * 添加记录,如果记录已存在将覆盖原有的value
        *
        * @param
        *            key 键
        * @param
        *            value 值
        * @return 状态码
        * */
       public static String set(String key, String value) {
           return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
       }

       /**
        * 添加记录,如果记录已存在将覆盖原有的value
        *
        * @param
        *            key 键
        * @param
        *            value 值
        * @return 状态码
        * */
       public static String set(String key, byte[] value) {
           return set(SafeEncoder.encode(key), value);
       }

       /**
        * 添加记录,如果记录已存在将覆盖原有的value
        *
        * @param  key 键
        * @param  value 值
        * @return 状态码
        * */
       public static String set(byte[] key, byte[] value) {
           Jedis jedis = JedisPoolClient.getJedis();
           String status = jedis.set(key, value);
           JedisPoolClient.returnJedis(jedis);
           return status;
       }

       /**
        * 从指定位置开始插入数据，插入的数据会覆盖指定位置以后的数据<br/>
        * 例:String str1="123456789";<br/>
        * 对str1操作后setRange(key,4,0000)，str1="123400009";
        *
        * @param key 键
        * @param offset 位置
        * @param value 值
        * @return value的长度
        * */
       public static long setRange(String key, long offset, String value) {
           Jedis jedis = JedisPoolClient.getJedis();
           long len = jedis.setrange(key, offset, value);
           JedisPoolClient.returnJedis(jedis);
           return len;
       }

       /**
        * 在指定的key中追加value
        *
        * @param key 键
        * @param value 值
        * @return long 追加后value的长度
        * **/
       public static long append(String key, String value) {
           Jedis jedis = JedisPoolClient.getJedis();
           long len = jedis.append(key, value);
           JedisPoolClient.returnJedis(jedis);
           return len;
       }

       /**
        * 将key对应的value减去指定的值，只有value可以转为数字时该方法才可用
        *
        * @param key 键
        * @param number 要减去的值
        * @return long 减指定值后的值
        * */
       public static long decrBy(String key, long number) {
           Jedis jedis = JedisPoolClient.getJedis();
           long len = jedis.decrBy(key, number);
           JedisPoolClient.returnJedis(jedis);
           return len;
       }

       /**
        * <b>可以作为获取唯一id的方法</b><br/>
        * 将key对应的value加上指定的值，只有value可以转为数字时该方法才可用
        *
        * @param key 键
        * @param number 要减去的值
        * @return long 相加后的值
        * */
       public static long incrBy(String key, long number) {
           Jedis jedis = JedisPoolClient.getJedis();
           long len = jedis.incrBy(key, number);
           JedisPoolClient.returnJedis(jedis);
           return len;
       }

       /**
        * 对指定key对应的value进行截取
        *
        * @param key 键
        * @param startOffset 开始位置(包含)
        * @param endOffset 结束位置(包含)
        * @return String 截取的值
        * */
       public static String getrange(String key, long startOffset, long endOffset) {
           Jedis sjedis = JedisPoolClient.getJedis();
           String value = sjedis.getrange(key, startOffset, endOffset);
           JedisPoolClient.returnJedis(sjedis);
           return value;
       }

       /**
        * 获取并设置指定key对应的value<br/>
        * 如果key存在返回之前的value,否则返回null
        *
        * @param key 键
        * @param value 值
        *
        * @return String 原始value或null
        * */
       public static String getSet(String key, String value) {
           Jedis jedis = JedisPoolClient.getJedis();
           String str = jedis.getSet(key, value);
           JedisPoolClient.returnJedis(jedis);
           return str;
       }

       /**
        * 批量获取记录,如果指定的key不存在返回List的对应位置将是null
        *
        * @param  keys 键列表
        * @return List<String> 值得集合
        * */
       public static List<String> mget(String... keys) {
           Jedis jedis = JedisPoolClient.getJedis();
           List<String> str = jedis.mget(keys);
           JedisPoolClient.returnJedis(jedis);
           return str;
       }

       /**
        * 批量存储记录
        *
        * @param keysvalues 例:keysvalues="key1","value1","key2","value2";
        * @return String 状态码
        * */
       public static String mset(String... keysvalues) {
           Jedis jedis = JedisPoolClient.getJedis();
           String str = jedis.mset(keysvalues);
           JedisPoolClient.returnJedis(jedis);
           return str;
       }

       /**
        * 获取key对应的值的长度
        *
        * @param key 键
        * @return value值得长度
        * */
       public static long strlen(String key) {
           Jedis jedis = JedisPoolClient.getJedis();
           long len = jedis.strlen(key);
           JedisPoolClient.returnJedis(jedis);
           return len;
       }
   }


    // *******************************************Lists*******************************************//
    public static class JListUtil{
        /**
         * List长度
         *
         * @param  key 键
         * @return 长度
         * */
        public static long llen(String key) {
            return llen(SafeEncoder.encode(key));
        }

        /**
         * List长度
         *
         * @param  key 键
         * @return 长度
         * */
        public static long llen(byte[] key) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = JedisPoolClient.getJedis();
            long count = sjedis.llen(key);
            JedisPoolClient.returnJedis(sjedis);
            return count;
        }

        /**
         * 覆盖操作,将覆盖List中指定位置的值
         *
         * @param  key 键
         * @param  index 位置
         * @param  value 值
         * @return 状态码
         * */
        public static String lset(byte[] key, int index, byte[] value) {
            Jedis jedis = JedisPoolClient.getJedis();
            String status = jedis.lset(key, index, value);
            JedisPoolClient.returnJedis(jedis);
            return status;
        }

        /**
         * 覆盖操作,将覆盖List中指定位置的值
         *
         * @param key 键
         * @param index 位置
         * @param value 值
         * @return 状态码
         * */
        public static String lset(String key, int index, String value) {
            return lset(SafeEncoder.encode(key), index, SafeEncoder.encode(value));
        }


        /**
         * 获取List中指定位置的值
         *
         * @param  key 键
         * @param  index 位置
         * @return 值
         * **/
        public static String lindex(String key, int index) {
            return SafeEncoder.encode(lindex(SafeEncoder.encode(key), index));
        }

        /**
         * 获取List中指定位置的值
         *
         * @param  key 键
         * @param  index 位置
         * @return 值
         * **/
        public static byte[] lindex(byte[] key, int index) {
            Jedis jedis = JedisPoolClient.getJedis();
            byte[] value = jedis.lindex(key, index);
            JedisPoolClient.returnJedis(jedis);
            return value;
        }

        /**
         * 将List中的第一条记录移出List
         *
         * @param key 键
         * @return 移出的记录
         * */
        public static String lpop(String key) {
            return SafeEncoder.encode(lpop(SafeEncoder.encode(key)));
        }

        /**
         * 将List中的第一条记录移出List
         *
         * @param key 键
         * @return 移出的记录
         * */
        public static byte[] lpop(byte[] key) {
            Jedis jedis = JedisPoolClient.getJedis();
            byte[] value = jedis.lpop(key);
            JedisPoolClient.returnJedis(jedis);
            return value;
        }

        /**
         * 将List中最后第一条记录移出List
         *
         * @param  key 键
         * @return 移出的记录
         * */
        public static String rpop(String key) {
            Jedis jedis = JedisPoolClient.getJedis();
            String value = jedis.rpop(key);
            JedisPoolClient.returnJedis(jedis);
            return value;
        }

        /**
         * 向List尾部追加记录
         *
         * @param key 键
         * @param value 值
         * @return 记录总数
         * */
        public static long lpush(String key, String value) {
            return lpush(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }

        /**
         * 向List头部追加记录
         *
         * @param key 键
         * @param value 值
         * @return 记录总数
         * */
        public static long rpush(String key, String value) {
            Jedis jedis = JedisPoolClient.getJedis();
            long count = jedis.rpush(key, value);
            JedisPoolClient.returnJedis(jedis);
            return count;
        }

        /**
         * 向List头部追加记录
         * @param key 键
         * @param value 值
         * @return 记录总数
         * */
        public static long rpush(byte[] key, byte[] value) {
            Jedis jedis = JedisPoolClient.getJedis();
            long count = jedis.rpush(key, value);
            JedisPoolClient.returnJedis(jedis);
            return count;
        }

        /**
         * 向List中追加记录
         *
         * @param  key 键
         * @param value 值
         * @return 记录总数
         * */
        public static long lpush(byte[] key, byte[] value) {
            Jedis jedis = JedisPoolClient.getJedis();
            long count = jedis.lpush(key, value);
            JedisPoolClient.returnJedis(jedis);
            return count;
        }

        /**
         * 获取指定范围的记录，可以做为分页使用
         *
         * @param  key 键
         * @param  start 开始位置
         * @param  end 结束位置
         * @return List
         * */
        public static List<String> lrange(String key, long start, long end) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = JedisPoolClient.getJedis();
            List<String> list = sjedis.lrange(key, start, end);
            JedisPoolClient.returnJedis(sjedis);
            return list;
        }

        /**
         * 获取指定范围的记录，可以做为分页使用
         *
         * @param  key 键
         * @param  start 开始位置
         * @param  end 如果为负数，则尾部开始计算
         * @return List
         * */
        public static List<byte[]> lrange(byte[] key, int start, int end) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = JedisPoolClient.getJedis();
            List<byte[]> list = sjedis.lrange(key, start, end);
            JedisPoolClient.returnJedis(sjedis);
            return list;
        }

        /**
         * 删除List中c条记录，被删除的记录值为value
         *
         * @param  key 键
         * @param  c 要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
         * @param  value 要匹配的值
         * @return 删除后的List中的记录数
         * */
        public static long lrem(byte[] key, int c, byte[] value) {
            Jedis jedis = JedisPoolClient.getJedis();
            long count = jedis.lrem(key, c, value);
            JedisPoolClient.returnJedis(jedis);
            return count;
        }

        /**
         * 删除List中c条记录，被删除的记录值为value
         *
         * @param key 键
         * @param c 要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
         * @param value 要匹配的值
         * @return 删除后的List中的记录数
         * */
        public static long lrem(String key, int c, String value) {
            return lrem(SafeEncoder.encode(key), c, SafeEncoder.encode(value));
        }

        /**
         * 算是删除吧，只保留start与end之间的记录
         *
         * @param  key 键
         * @param  start 记录的开始位置(0表示第一条记录)
         * @param  end 记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
         * @return 执行状态码
         * */
        public static String ltrim(byte[] key, int start, int end) {
            Jedis jedis = JedisPoolClient.getJedis();
            String str = jedis.ltrim(key, start, end);
            JedisPoolClient.returnJedis(jedis);
            return str;
        }

        /**
         * 算是删除吧，只保留start与end之间的记录
         *
         * @param key
         * @param start 记录的开始位置(0表示第一条记录)
         * @param end 记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
         * @return 执行状态码
         * */
        public static String ltrim(String key, int start, int end) {
            return ltrim(SafeEncoder.encode(key), start, end);
        }

        /***
         * 订阅频道
         * @param channel
         */
        public static void subscribe(String channel){
            Jedis jedis = JedisPoolClient.getJedis();
            JedisPubSub pub = new RedisSubPubListener();
            jedis.subscribe(pub,channel);
        }

        public static void pubish(String channel,String message){
            Jedis jedis = JedisPoolClient.getJedis();
            jedis.publish(channel,message);
            JedisPoolClient.returnJedis(jedis);
        }
    }

    public static Map<String,String> serialIOMget(List<String> keys){
        Map<String,String> keyvalueMap = new Hashtable <>();

        Map<Jedis,List<String>> nodeListMap = new HashMap <>();
        //循环遍历key
        for(String key: keys){
            //获得key所属的slot槽
            int slot = JedisClusterCRC16.getSlot(key);
            Jedis jedis = JedisPoolClient.getJedisCluster().getConnectionFromSlot(slot);
            if(nodeListMap.containsKey(jedis)){
                nodeListMap.get(jedis).add(key);
            }else{
                List<String>list = new ArrayList <String>();
                list.add(key);
                nodeListMap.put(jedis,list);
            }
        }
        for(Map.Entry<Jedis,List<String> > entry:nodeListMap.entrySet()) {
            Jedis jedis = entry.getKey();
            List<String> nodeKeyList = entry.getValue();

            String[] nodeKeyArray = nodeKeyList.toArray(new String[nodeKeyList.size()]);
            List<String> nodeValueList = jedis.mget(nodeKeyArray);

            for( int i = 0; i <nodeKeyList.size();i++){
                keyvalueMap.put(nodeKeyList.get(i),nodeValueList.get(i));
            }
        }

        return keyvalueMap;
    }


    public static void getHotKey(String key,String uuid){
        String value = JedisUtil.JStringUtil.get(key);
        Jedis jedis = JedisPoolClient.getJedis();
        if(value ==null){
            String muiltyKey = "mulity:key"+key;
            if(JedisLock.tryGetDistributedLock(jedis,muiltyKey,uuid,180)){//加锁成功
               value = JedisUtil.JStringUtil.get(key);//从数据源中获取数据
                //将数据放到缓存里
                jedis.setex(key,5000,value);
                //删除分布式锁
                jedis.del(muiltyKey);

            }else{
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getHotKey(key,uuid);
            }
        }
    }

}
