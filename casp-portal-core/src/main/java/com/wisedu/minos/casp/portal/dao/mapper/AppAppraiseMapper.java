package com.wisedu.minos.casp.portal.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisedu.minos.casp.portal.dao.entity.AppAppraiseEntity;
import com.wisedu.minos.casp.portal.vo.AppAppraiseSummaryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 应用服务评价 Mapper 接口
 * </p>
 * @author yujiaxin
 * @date 2021/12/13 13:43
 */
public interface AppAppraiseMapper extends BaseMapper<AppAppraiseEntity> {
    AppAppraiseSummaryVo queryAppAppraiseSummary(@Param("appId") String appId);

    List<AppAppraiseSummaryVo> queryAppAppraiseSummaryByPage(@Param("serviceWids") List<String> serviceWids);
}
