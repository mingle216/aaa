package com.wisedu.minos.casp.portal.dao.mapper;

import com.wisedu.minos.casp.portal.dao.entity.UserCalCardRelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户-日程卡片关联表 Mapper 接口
 * </p>
 *
 * @author wisedu
 * @since 2022-03-15
 */
public interface UserCalCardRelMapper extends BaseMapper<UserCalCardRelEntity> {

    List<UserCalCardRelEntity> queryUserCalList(@Param("week") String week, @Param("hour") String hour);
}
