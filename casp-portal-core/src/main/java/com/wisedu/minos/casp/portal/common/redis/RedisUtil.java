package com.wisedu.minos.casp.portal.common.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：Redis 工具类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title RedisUtil
 * @Author: jcx
 * @Date: 2020/7/21
 */
@Component
public class RedisUtil {
    private static final Logger logger = LogManager.getLogger(RedisUtil.class);

    @Resource(name = "portalRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${system.card.redis.expireTime:5}")
    private Long cardRedisExpireTime;

    /**
     * 格式化时间的string
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */

    public boolean expire (String key, long time) {
        try {
            if ( time > 0 ) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch ( Exception e ) {
            logger.error("指定缓存失效时间：" + e);
            return false;
        }

    }


    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire (String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean isHasKey (String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch ( Exception e ) {
            logger.error("判断是否有当前KEY发生异常：" + e);
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del (String... key) {
        try {
            if ( key != null && key.length > 0 ) {
                if ( key.length == 1 ) {
                    redisTemplate.delete(key[ 0 ]);
                } else {
                    redisTemplate.delete(( Collection<String> ) CollectionUtils.arrayToList(key));
                }
            }
        } catch ( Exception e ) {
            logger.debug("删除失败key 发生异常" + e);
            logger.debug("删除失败key -- {}" + key);
        }
    }

    /**
     * 保存数据到redis
     */
    public void save (String rkey, final Object value) {
        try {
            redisTemplate.opsForValue().set(rkey, serializeValue(value));
        } catch ( Exception e ) {
            logger.error("保存数据到redis发生异常key为【" + rkey + "】：" + e);
        }
    }
//    public void save (String key, String  value) {
//        try {
//            redisTemplate.opsForValue().set(key, value);
//        } catch ( Exception e ) {
//            logger.error("保存数据到redis发生异常key为【" + key + "】：" , e);
//        }
//    }

    /**
     * 保存数据到redis 失效时间 单位秒
     *
     * @param rkey
     * @param time
     * @param value
     */
    public void saveE (String rkey, long time, final Object value) {
        try {
            redisTemplate.opsForValue().set(rkey, serializeValue(value), time, TimeUnit.SECONDS);
        } catch ( Exception e ) {
            logger.error("保存数据到redis发生异常key为【" + rkey + "】：" + e);
        }
    }

//    /**
//     * 返回指定key 所映射的值. 如果redis 中不包含该key 的映射关系，则返回 null
//     */
//    public <T> T read (String rKey, final Class<T> clazz, Class... elementClasses) {
//        try {
//            Object object = redisTemplate.opsForValue().get(rKey);
//            if ( object != null ) {
//                return elementClasses.length < 1 ? deserializeValue(( String ) object, clazz) :
//                        deserializeValue(( String ) object, clazz, elementClasses);
//            }
//        } catch ( Exception e ) {
//            logger.debug("获取redis里数据异常" + e);
//            logger.debug("获取redis里数据异常key为：" + rKey);
//        }
//        return null;
//    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */

    public long incr (String key, long delta) {
        if ( delta < 0 ) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }


    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr (String key, long delta) {
        if ( delta < 0 ) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, - delta);
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */

    public Object hashGet (String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }


    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */

    public Map<Object, Object> hashsGet (String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */

    public boolean hashsSet (String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch ( Exception e ) {
            logger.error("hmset发生异常：" + e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hashsSet (String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if ( time > 0 ) {
                expire(key, time);
            }
            return true;
        } catch ( Exception e ) {
            logger.error("hmset发生异常：" + e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */

    public boolean hashSet (String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch ( Exception e ) {
            logger.error("hset发生异常：" + e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hashSet (String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if ( time > 0 ) {
                expire(key, time);
            }
            return true;
        } catch ( Exception e ) {
            logger.error("hset发生异常：" + e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public Object hashDel (String key, Object... item) {
        return redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * hash键数量
     *
     * @param key
     * @return
     */
    public int hashSize(String key) {
        return redisTemplate.opsForHash().size(key).intValue();
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */

    public boolean isHaveHashKey (String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */

    public double hashIncr (String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hashDecr (String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, - by);
    }


    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove (String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch ( Exception e ) {
            logger.error("lRemove发生异常：" + e);
            return 0;
        }
    }


    /**
     * 移除并返回列表 key 的头元素
     *
     * @param
     * @return value
     */
    public <T> T lPop (String rKey, final Class<T> tClass) {
        try {
            Object object = redisTemplate.opsForList().leftPop(rKey);
            if ( object != null ) {
                return deserializeValue(( String ) object, tClass);
            }
        } catch ( Exception e ) {
            logger.error("从redis队列获取数据异常：" + e);
        }
        return null;
    }

    /**
     * 序列化存储的val
     *
     * @param value
     * @return
     */
    protected String serializeValue (final Object value) {
        return JSON.toJSONString(value);
    }

    /**
     * 反序列化val
     *
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T> T deserializeValue (String value, Class<T> clazz) {
        T r = null;
        if ( StringUtils.isNotBlank(value) ) {

            r = JSON.parseObject(value, clazz);
        }
        return r;
    }

    /**
     * 查找匹配的key
     *
     * @param matchKey
     * @return
     */
    public Set<String> scan(String matchKey) {
        Set<String> keys = redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match("*" + matchKey + "*").count(1000).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });
        return keys;
    }

    /**
     * 批量删除key
     *
     * @param keys
     */
    public void deleteKeys(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public Long getCardRedisExpireTime() {
        return cardRedisExpireTime;
    }
}
