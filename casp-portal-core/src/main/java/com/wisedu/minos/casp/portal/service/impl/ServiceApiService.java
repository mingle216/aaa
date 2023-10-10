package com.wisedu.minos.casp.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wisedu.minos.api.auth.AppServiceGrantService;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.ServiceLatestUseEntity;
import com.wisedu.minos.casp.portal.dao.mapper.ServiceLatestUseMapper;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.service.QueryUserGrantedServiceResponse;
import com.wisedu.minos.casp.portal.model.service.ServiceDetail;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @title 服务相关
 * @Author: gulong
 */
@Service
@Slf4j
public class ServiceApiService{

    @DubboReference
    private AppServiceGrantService appServiceGrantService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserUtil userUtil;

    @Value("${system.card.redis.expireTime:5}")
    private Long redisExpireTime;

    @Autowired
    private ServiceLatestUseMapper serviceLatestUseMapper;
    /**
     * 功能描述：根据用户查询可用服务列表
     *
     * @title getGrantServices
     * @Author: gulong
     */
    public List<String> getGrantServices(String userId) {
        if(StringUtil.isNotEmpty(userId)){
            return appServiceGrantService.checkServiceGrant(userId);
        } else {
            return appServiceGrantService.queryGrantedServiceForGuest();
        }
    }

    /**
     * 功能描述：根据用户查询可见服务列表
     *
     * @title getLatestServices
     * @Author: gulong
     */
    public List<String> getVisitedServices(String userId) {
        if(StringUtil.isNotEmpty(userId)){
            return appServiceGrantService.queryVisitedService(userId);
        } else {
            return appServiceGrantService.queryVisitedServiceForGuest();
        }
    }


    /**
     * 获取全部服务
     * @param allServiceRequest
     * @return
     */
    public List<AllServiceResponse.ServiceModel> getAllService(AllServiceRequest allServiceRequest) {
        // 是否是条件查询，如果是条件查询则不走缓存直接查询调用接口查询
        boolean isConditionSearch = StringUtil.isNotEmpty(allServiceRequest.getSearchKey()) || CollectionUtils.isNotEmpty(allServiceRequest.getClassifyList());
        if(isConditionSearch){
            return getServiceModels(allServiceRequest);
        }
        String userId = StringUtil.isNotEmpty(userUtil.getUserAccount()) ? userUtil.getUserAccount():"guest";
        String key = "allService:userId:" + userId +"platform:" + allServiceRequest.getServiceStation();
        Object result = redisUtil.get(key);
        if (result == null ){
            List<AllServiceResponse.ServiceModel> data = getServiceModels(allServiceRequest);
            redisUtil.set(key, data, redisExpireTime, TimeUnit.SECONDS);
            return data;
        } else {
            return (List<AllServiceResponse.ServiceModel>) result;
        }
    }

    /**
     *
     * 接口调用全部服务接口
     * @param allServiceRequest
     * @return
     */
    private List<AllServiceResponse.ServiceModel> getServiceModels(AllServiceRequest allServiceRequest) {
        try {
            HttpEntity<AllServiceRequest> httpEntity = new HttpEntity<>(allServiceRequest);
            LOGGER.debug("全部服务接口请求：" + JSON.toJSONString(allServiceRequest));
            AllServiceResponse allServiceResponse = RestTemplateUtils.postForObject("/groupService/allServiceList", httpEntity, AllServiceResponse.class);
            LOGGER.debug("全部服务接口返回：" + JSON.toJSONString(allServiceResponse));
            if (null != allServiceResponse && "0".equals(allServiceResponse.getErrcode())) {
                List<AllServiceResponse.ServiceModel> data = allServiceResponse.getData();
                if (data != null) {
                    return data;
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取获取全部服务接口失败", e);
        }
        return Collections.emptyList();
    }

    /***
     * @Author jcx
     * @Description 门户用提供给运营中心服务接口
     * @Date 10:44 2021/7/28
     * @Param serviceForCaspReq:
     * @return List<AppInfoForCasp>
     **/
    public List<AppInfoForCasp> allServiceListForCasp(ServiceForCaspReq serviceForCaspReq) {
        try {
            HttpEntity<ServiceForCaspReq> httpEntity = new HttpEntity<>(serviceForCaspReq);
            LOGGER.info("门户用提供给运营中心服务接口请求：" + JSON.toJSONString(serviceForCaspReq));
            AppInfoForCaspRes appInfoForCaspRes = RestTemplateUtils.postForObject("/groupService/allServiceListForCasp", httpEntity, AppInfoForCaspRes.class);
            LOGGER.info("门户用提供给运营中心服务接口返回：" + JSON.toJSONString(appInfoForCaspRes));
            if (null != appInfoForCaspRes && "0".equals(appInfoForCaspRes.getErrcode())) {
                List<AppInfoForCasp> data = appInfoForCaspRes.getData();
                if (data != null) {
                    return data;
                }
            }
        } catch (Exception e) {
            LOGGER.error("门户用提供给运营中心服务接口失败", e);
        }
        return new ArrayList<AppInfoForCasp>();
    }


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
            LOGGER.info("访问服务计数接口请求：" + JSON.toJSONString(serviceRequest));
            BaseResponse baseResponse = RestTemplateUtils.postForObject("/groupService/visitServiceItem", httpEntity, BaseResponse.class);
            LOGGER.info("访问服务计数接口返回：" + JSON.toJSONString(baseResponse));
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
    public List<String> getGrantedServiceList() {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        List<String> list = new ArrayList<>();
        if (null == user || org.apache.commons.lang3.StringUtils.isBlank(user.getUserAccount())) {
            return ApplicationContextUtil.get(ServiceApiService.class).getGrantServices(null);
        }
        Map<String, Object> userQueryReq = new HashMap<>(1);
        userQueryReq.put("userId", user.getUserAccount());
        QueryUserGrantedServiceResponse queryUserGrantedServiceResponse = null;
        try {
            queryUserGrantedServiceResponse = RestTemplateUtils.post("/service/queryUserGrantedService", userQueryReq, QueryUserGrantedServiceResponse.class).getBody();
            LOGGER.debug("调用获取用户授权的服务列表接口成功..., 返回结果 = " + JSON.toJSONString(queryUserGrantedServiceResponse));
        } catch (Exception e) {
            LOGGER.debug("调用获取用户授权的服务列表接口成功..., 返回结果 = " + e);
        }
        if(null != queryUserGrantedServiceResponse && null != queryUserGrantedServiceResponse.getData()){
            List<ServiceDetail> serviceDetailList = queryUserGrantedServiceResponse.getData();
            list = serviceDetailList.stream().map(ServiceDetail::getWid).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 0 不显示，1 禁用 2 显示
     */
    public Integer getOnlineServiceType(List<String> grantServiceList, List<String> serviceList) {
        if (org.springframework.util.CollectionUtils.isEmpty(serviceList)) {
            return 0;
        }
        if (!org.springframework.util.CollectionUtils.containsAny(serviceList, grantServiceList)) {
            return 1;
        }
        return 2;
    }
}
