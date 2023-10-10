package com.wisedu.minos.casp.portal.dao.mapper;

import com.wisedu.minos.casp.portal.dao.entity.AttachEntityRelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisedu.minos.casp.portal.utils.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * 附件与其他表关系 Mapper 接口
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
@CacheNamespace(implementation = MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface AttachEntityRelMapper extends BaseMapper<AttachEntityRelEntity> {

}
