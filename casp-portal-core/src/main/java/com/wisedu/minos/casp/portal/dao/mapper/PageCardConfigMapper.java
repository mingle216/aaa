package com.wisedu.minos.casp.portal.dao.mapper;

import com.wisedu.minos.casp.portal.dao.entity.PageCardConfigEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisedu.minos.casp.portal.utils.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 页面卡片配置表 Mapper 接口
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@CacheNamespace(implementation = MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface PageCardConfigMapper extends BaseMapper<PageCardConfigEntity> {

    String checkCardByCardWid(@Param("cardWid")String cardWid);
}
