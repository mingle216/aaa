package com.wisedu.casp.portal.template.cus.official.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.dao.entity.ServiceLatestUseEntity;
import com.wisedu.minos.casp.portal.dao.mapper.ServiceLatestUseMapper;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * ServiceApi
 * <p/>
 * 服务组合接口
 *
 * @author hyluan
 * @date 2020/10/15 17:32
 * Copyright (c) 2018 wisedu
 */
//@Service
@Slf4j
public class ServiceApi {
    @Autowired
    private ServiceLatestUseMapper serviceLatestUseMapper;

    /**
     * visitService
     * <p/> 请求一次访问计数+1
     *
     * @param
     * @return void
     * @throws
     * @date 2020/10/22 17:32
     */
    public BaseResponse visitService(VisitServiceRequest serviceRequest) {
        try {
            HttpEntity<VisitServiceRequest> httpEntity = new HttpEntity<>(serviceRequest);
            LOGGER.debug("访问服务计数接口请求：" + JSON.toJSONString(serviceRequest));
            BaseResponse baseResponse = RestTemplateUtils.postForObject("/groupService/visitServiceItem", httpEntity, BaseResponse.class);
            LOGGER.debug("访问服务计数接口返回：" + JSON.toJSONString(baseResponse));
            handleServiceVisitTime(serviceRequest);
            return baseResponse;
        } catch (Exception e) {
            LOGGER.error("访问服务接口失败", e);
            BaseResponse visitServiceResponse = new BaseResponse();
            visitServiceResponse.setErrcode("500").setErrmsg(e.getMessage());
            return visitServiceResponse;
        }
    }

    /**
     * 处理访问服务时间
     * @param serviceRequest
     */
    private void handleServiceVisitTime(VisitServiceRequest serviceRequest) {
        String serviceWid = serviceRequest.getServiceWid();
        UserUtil userUtil = ApplicationContextUtil.get(UserUtil.class);
        UserInfo currentUser = userUtil.getCurrentUser();
        if(currentUser == null){
            return;
        }
        QueryWrapper<ServiceLatestUseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("SERVICE_WID", serviceWid)
                .eq("USER_ID", currentUser.getWid());
        ServiceLatestUseEntity serviceLatestUseEntity = serviceLatestUseMapper.selectOne(wrapper);
        if(serviceLatestUseEntity == null){
            serviceLatestUseEntity = new ServiceLatestUseEntity();
            serviceLatestUseEntity.setServiceWid(serviceWid);
            serviceLatestUseEntity.setUserId(currentUser.getWid());
            serviceLatestUseEntity.setUseTime(new Date());
            serviceLatestUseMapper.insert(serviceLatestUseEntity);
        } else {
            serviceLatestUseEntity.setUseTime(new Date());
            UpdateWrapper<ServiceLatestUseEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("SERVICE_WID", serviceWid)
                    .eq("USER_ID", currentUser.getWid());
            serviceLatestUseMapper.update(serviceLatestUseEntity,updateWrapper);
        }
    }



}
