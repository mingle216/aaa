package com.wisedu.minos.casp.portal.dao.mapper;

import com.wisedu.minos.casp.portal.dao.entity.SiteEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisedu.minos.casp.portal.utils.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * 站点表 Mapper 接口
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@CacheNamespace(implementation = MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface SiteMapper extends BaseMapper<SiteEntity> {

}
