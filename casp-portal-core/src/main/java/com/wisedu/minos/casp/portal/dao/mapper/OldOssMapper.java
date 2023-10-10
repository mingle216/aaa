package com.wisedu.minos.casp.portal.dao.mapper;

import com.wisedu.minos.casp.portal.dao.entity.OldOssEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisedu.minos.casp.portal.utils.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * 旧OSS域名记录替换表 Mapper 接口
 * </p>
 *
 * @author wisedu
 * @since 2022-03-25
 */
@CacheNamespace(implementation = MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface OldOssMapper extends BaseMapper<OldOssEntity> {

}
