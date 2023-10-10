package com.wisedu.minos.casp.portal.utils;

import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import lombok.Setter;
import org.apache.ibatis.cache.Cache;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title MybatisRedisCache
 * @Author: 01116267
 * @Date: 2021/11/15
 */
@Component
public class MybatisRedisCache implements Cache {

    private RedisUtil getRedis(){
        return ApplicationContextUtil.get(RedisUtil.class);
    }

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * 缓存刷新时间（秒）
     */
    @Setter
    private long flushInterval = 0L;

    private String id;

    public MybatisRedisCache() {}

    public MybatisRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object o, Object o1) {
        // 分页查询不走缓存
        if( !StringUtils.isEmpty(o.toString()) && o.toString().contains("selectPage") ){
            return;
        }
        getRedis().hashSet(getId(), o.toString(), o1);

        if (flushInterval > 0L) {
            getRedis().expire(getId(), flushInterval);
        }
    }

    @Override
    public Object getObject(Object o) {
        // 分页查询不走缓存
        if( !StringUtils.isEmpty(o.toString()) && o.toString().contains("selectPage") ){
            return null;
        }
        return getRedis().hashGet(getId(), o.toString());
    }

    @Override
    public Object removeObject(Object o) {
        return getRedis().hashDel(getId(), o);
    }

    @Override
    public void clear() {
        getRedis().del(getId());
    }

    @Override
    public int getSize() {
        return getRedis().hashSize(getId());
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}