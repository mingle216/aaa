package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wisedu.minos.casp.portal.dao.entity.AppAppraiseEntity;
import com.wisedu.minos.casp.portal.dao.mapper.AppAppraiseMapper;
import com.wisedu.minos.casp.portal.vo.AppAppraiseSummaryVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppAppraiseService extends ServiceImpl<AppAppraiseMapper, AppAppraiseEntity> {

    public boolean exists(String appId, String userId) {
        return this.count(this.lambdaQuery().eq(AppAppraiseEntity::getAppId, appId).eq(AppAppraiseEntity::getUserId, userId).getWrapper()) > 0;
    }

    public IPage<AppAppraiseEntity> queryAppAppraises(String appId, int scoreLevel, boolean showDeleted, int pageNum, int pageSize, boolean showMyself, String userId) {
        Page<AppAppraiseEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryChainWrapper<AppAppraiseEntity> queryChainWrapper = getWrapper(appId, scoreLevel, showDeleted, showMyself, userId);
        queryChainWrapper.last("order by wid desc");
        return this.page(page, queryChainWrapper.getWrapper());
    }

    public int countAppAppraises(String appId, int scoreLevel, boolean showDeleted, boolean showMyself, String userId) {
        return this.count(getWrapper(appId, scoreLevel, showDeleted, showMyself, userId).getWrapper());
    }

    private LambdaQueryChainWrapper<AppAppraiseEntity> getWrapper(String appId, int scoreLevel, boolean showDeleted, boolean showMyself, String userId){
        double minScore = 0;
        double maxScore = 5.1;
        switch (scoreLevel){
            case 1:
                minScore = 4;
                maxScore = 5.1;
                break;
            case 2:
                minScore = 3;
                maxScore = 4;
                break;
            case 3:
                minScore = 0;
                maxScore = 3;
                break;
        }

        LambdaQueryChainWrapper<AppAppraiseEntity> queryWrapper = this.lambdaQuery();
        queryWrapper.eq(AppAppraiseEntity::getAppId, appId)
                .ge(AppAppraiseEntity::getScore, minScore)
                .lt(AppAppraiseEntity::getScore, maxScore)
                .eq(!showDeleted, AppAppraiseEntity::getIsDeleted, '0');
        if(showMyself){
            queryWrapper.eq(AppAppraiseEntity::getUserId, userId);
        }
        return queryWrapper;
    }

    public AppAppraiseSummaryVo queryAppAppraiseSummary(String appId) {
        return this.baseMapper.queryAppAppraiseSummary(appId);
    }


    public List<AppAppraiseSummaryVo> queryAppAppraiseSummary(List<String> serviceWids) {
        List<List<String>> partition = Lists.partition(serviceWids, 1000);
        List<AppAppraiseSummaryVo> data = Lists.newArrayList();
        partition.forEach(str -> {
            List<AppAppraiseSummaryVo> appAppraiseSummaryVos = this.baseMapper.queryAppAppraiseSummaryByPage(str);
            if( CollectionUtils.isNotEmpty(appAppraiseSummaryVos) ){
                data.addAll(appAppraiseSummaryVos);
            }
        });
        return data;
    }


}
