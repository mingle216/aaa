package com.wisedu.minos.casp.portal.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisedu.minos.casp.portal.dao.entity.UserCalCardRelEntity;
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

    IPage<UserCalCardRelEntity> queryUserCalListByPage(IPage<UserCalCardRelEntity> page , @Param("week") String week, @Param("hour") String hour);


}
