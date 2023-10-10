package com.wisedu.minos.casp.portal.dao.mapper;

import com.wisedu.minos.casp.portal.dao.entity.MenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisedu.minos.casp.portal.utils.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * 顶部菜单表 Mapper 接口
 * </p>
 *
 * @author wisedu
 * @since 2022-01-26
 */
@CacheNamespace(implementation = MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface MenuMapper extends BaseMapper<MenuEntity> {

}
