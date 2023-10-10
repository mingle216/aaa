package com.wisedu.minos.casp.portal.dao.mapper;

import com.wisedu.minos.casp.portal.dao.entity.ServiceLatestUseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisedu.minos.casp.portal.utils.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * <p>
 * 用户最近使用服务表 Mapper 接口
 * </p>
 *
 * @author wisedu
 * @since 2021-11-29
 */
public interface ServiceLatestUseMapper extends BaseMapper<ServiceLatestUseEntity> {

}
