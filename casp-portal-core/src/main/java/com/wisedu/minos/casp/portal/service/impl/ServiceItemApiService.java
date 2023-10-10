package com.wisedu.minos.casp.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisedu.casp.sim.api.DubboServiceItemService;
import com.wisedu.casp.sim.model.DubboCaspItemJump;
import com.wisedu.casp.sim.model.DubboCaspItemJumpReq;
import com.wisedu.minos.api.auth.AppServiceGrantService;
import com.wisedu.minos.api.data.AppServiceService;
import com.wisedu.minos.api.data.FavoriteService;
import com.wisedu.minos.api.model.*;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.CollectionUtil;
import com.wisedu.minos.casp.portal.common.utils.ObjectUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.utils.CardDubboUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @title 事项相关
 * @Author: gulong
 */
@Service
@Slf4j
public class ServiceItemApiService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserUtil userUtil;

    @Value("${system.card.redis.expireTime:5}")
    private Long redisExpireTime;


    @DubboReference
    private AppServiceGrantService appServiceGrantService;

    @DubboReference
    private FavoriteService favoriteService;

    @DubboReference
    private DubboServiceItemService dubboServiceItemService;
    @DubboReference
    private AppServiceService appServiceService;

    /**
     * 获取全部事项
     *
     * @param allServiceItemRequest
     * @return
     */
    public List<AllServiceItemResponse.ServiceItemModel> getAllServiceItem (AllServiceItemRequest allServiceItemRequest) {
        // 是否是条件查询，如果是条件查询则不走缓存直接查询调用接口查询
        allServiceItemRequest.setPageNumber(1);
        allServiceItemRequest.setPageSize(Global.MAXSIZE);
//        boolean isConditionSearch = StringUtil.isNotEmpty(allServiceItemRequest.getSearchKey())
//                || StringUtil.isNotEmpty(allServiceItemRequest.getCategoryWids())
//                || StringUtil.isNotEmpty(allServiceItemRequest.getDeptWids())
//                || StringUtil.isNotEmpty(allServiceItemRequest.getRoleWids())
//                || StringUtil.isNotEmpty(allServiceItemRequest.getDimensions())
//                || StringUtil.isNotEmpty(allServiceItemRequest.getGroupWid());
//        if ( isConditionSearch ) {
        return getServiceItemModels(allServiceItemRequest).getData();
//        }
//        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
//        String key = "allServiceItem:userId:" + (null == user ? "guest" : user.getUserAccount());
//        Object result = redisUtil.get(key);
//        if ( result == null ) {
//            List<AllServiceItemResponse.ServiceItemModel> data = getServiceItemModels(allServiceItemRequest).getData();
//            redisUtil.set(key, data, redisExpireTime, TimeUnit.SECONDS);
//            return data;
//        } else {
//            return ( List<AllServiceItemResponse.ServiceItemModel> ) result;
//        }
    }

    /**
     * 接口调用全部服务接口
     *
     * @param allServiceItemRequest
     * @return
     */
    public AllServiceItemResponse getServiceItemModels (AllServiceItemRequest allServiceItemRequest) {
        try {
            HttpEntity<AllServiceItemRequest> httpEntity = new HttpEntity<>(allServiceItemRequest);
            LOGGER.debug("全部服务事项接口请求：" + JSON.toJSONString(allServiceItemRequest));
            AllServiceItemResponse allServiceResponse = RestTemplateUtils.postForObject("/coosk/itemManager/queryItemList", httpEntity, AllServiceItemResponse.class);
            LOGGER.debug("全部服务事项接口返回：" + JSON.toJSONString(allServiceResponse));
            if ( null != allServiceResponse && "0".equals(allServiceResponse.getErrcode()) && allServiceResponse.getData() != null ) {
                return allServiceResponse;
            }
        } catch ( Exception e ) {
            LOGGER.error("获取全部服务事项接口失败", e);
        }
        AllServiceItemResponse allServiceItemResponse = new AllServiceItemResponse();
        allServiceItemResponse.setData(Collections.emptyList());
        allServiceItemResponse.setPage(new Page());
        return allServiceItemResponse;
    }


    /**
     * visitServiceItem
     * <p/> 请求一次访问计数+1
     *
     * @param
     * @return void
     * @throws
     * @date 2020/10/22 17:32
     */
    public BaseResponse visitServiceItem (VisitServiceItemRequest serviceRequest) {
        try {
            HttpEntity<VisitServiceItemRequest> httpEntity = new HttpEntity<>(serviceRequest);
            BaseResponse baseResponse = RestTemplateUtils.postForObject("/coosk/itemManager/visitItem", httpEntity, BaseResponse.class);
            return baseResponse;
        } catch ( Exception e ) {
            LOGGER.error("访问服务事项计数接口失败", e);
            BaseResponse visitServiceResponse = new BaseResponse();
            visitServiceResponse.setErrcode("500").setErrmsg(e.getMessage());
            return visitServiceResponse;
        }
    }

    public ServiceRelServiceResponse.ServiceItemAttr getServiceItemRelService (ServiceItemRelServiceRequest serviceRequest) {
        ServiceRelServiceResponse.ServiceItemAttr response = new ServiceRelServiceResponse.ServiceItemAttr();
        List<ServiceRelServiceResponse.ServiceModel> serviceModelList;
        DubboCaspItemJumpReq dubboCaspItemJumpReq = new DubboCaspItemJumpReq();
        dubboCaspItemJumpReq.setItemWid(serviceRequest.getServiceItemWid());
        dubboCaspItemJumpReq.setLang(serviceRequest.getLangCountry());
        com.wisedu.casp.sim.model.DubboResponse<DubboCaspItemJump> result = dubboServiceItemService.queryItemForCaspJump(dubboCaspItemJumpReq);
        if ( "0".equals(result.getErrcode()) ) {
            DubboCaspItemJump data = result.getData();
            if ( data == null ) {
                return response;
            }
            response.setIsShow(data.getIsShow());
            response.setItemName(data.getItemName());
            response.setIsEnabled(data.getIsEnabled());
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            List<String> linkService = data.getLinkServiceWids();
            if ( CollectionUtils.isNotEmpty(linkService) ) {
                ServiceByUserAndWidsReq req = new ServiceByUserAndWidsReq();
                req.setUserWid(null!=user?user.getWid():"");
                req.setData(linkService);
                DubboServiceRes dubboServiceRes = appServiceService.queryServiceByUserAndWids(req);
                if ( "0".equals(dubboServiceRes.getErrcode()) && CollectionUtils.isNotEmpty(dubboServiceRes.getData()) ) {
                    List<DubboServiceData> dubboServiceData = dubboServiceRes.getData();
//                    serviceModelList = ObjectUtil.copyListProperties(dubboServiceData, ServiceRelServiceResponse.ServiceModel::new);
                    serviceModelList = ObjectUtil.copyListProperties(dubboServiceData,ServiceRelServiceResponse.ServiceModel::new,(dubboDto, model) ->{
                        model.setServiceNameLangKeys(ObjectUtil.copyListProperties(model.getServiceNameLangKeys(), MinosMultiLangData::new));
                    });
                    //按端筛选展示服务
                    response.setServiceList(getServiceListByPlatform(serviceModelList, linkService));
                    handlerLangCountry(response.getServiceList(), serviceRequest.getLangCountry());
                }
            }
        } else {
            throw new BusinessException("查询服务事项详情失败:" + result.getErrcode() + result.getErrmsg());
        }
        return response;
    }

    public List<ServiceRelServiceResponse.ServiceModel> getServiceListByPlatform (List<ServiceRelServiceResponse.ServiceModel> serviceModelList, List<String> linkService) {
        if ( CollectionUtils.isNotEmpty(serviceModelList) ) {
            // dubbo 获取用户授权的服务列表然后取交集
            List<String> canUseServiceWids = getCanUseServiceWids(serviceModelList);
            List<String> canVisitedServiceWids = getCanVisitedServiceWids(serviceModelList);
            Map<String, ServiceRelServiceResponse.ServiceModel> linkInfoMap = serviceModelList.stream().collect(Collectors.toMap(ServiceRelServiceResponse.ServiceModel::getWid,
                    serviceModel -> serviceModel));
            serviceModelList.clear();
            // 根据详情返回的服务列表排序
            for ( String wid : linkService ) {
                ServiceRelServiceResponse.ServiceModel serviceModel = linkInfoMap.get(wid);
                // 按端展示
                handleService(serviceModelList, canUseServiceWids, canVisitedServiceWids, serviceModel);
            }
        }
        return serviceModelList;
    }

    private void handleService (List<ServiceRelServiceResponse.ServiceModel> serviceModelList, List<String> canUseServiceWids,
                                List<String> canVisitedServiceWids, ServiceRelServiceResponse.ServiceModel serviceModel) {
        if ( serviceModel != null ) {
            serviceModel.setIconLink(serviceModel.getPcIconUrl());
            serviceModel.setMobileIconLink(serviceModel.getMobileIconUrl());
            serviceModel.setPermission(0);
            if ( CollectionUtils.isNotEmpty(canUseServiceWids) ) {
                if ( canUseServiceWids.contains(serviceModel.getWid()) ) {
                    serviceModel.setPermission(1);
                } else {
                    serviceModel.setPermission(0);
                }
            }
            if ( CollectionUtils.isNotEmpty(canVisitedServiceWids) && canVisitedServiceWids.contains(serviceModel.getWid()) ) {
                serviceModelList.add(serviceModel);
            }

        }
    }

    /***
     * @Author jcx
     * @Description 处理服务名称多语言
     * @Date 18:43 2021/7/30
     * @Param serviceModelList:
     * @Param lang:
     * @return void
     **/
    private void handlerLangCountry (List<ServiceRelServiceResponse.ServiceModel> serviceModelList, String lang) {
        if ( CollectionUtils.isEmpty(serviceModelList) ) {
            return;
        }
        serviceModelList.forEach(serviceModel -> {
            List<MinosMultiLangData> serviceNameLangKeys = serviceModel.getServiceNameLangKeys();
            if ( CollectionUtils.isNotEmpty(serviceNameLangKeys) ) {
                List<MinosMultiLangData> itemLangNames = serviceNameLangKeys.stream().filter(e -> e.getLangCountry().equals(lang)).collect(Collectors.toList());
                if ( CollectionUtils.isNotEmpty(itemLangNames) ) {
                    serviceModel.setServiceName(itemLangNames.get(0).getLangValue());
                }
            }
        });
    }

    /**
     * 根据用户查询可用服务
     *
     * @param serviceModelList
     * @return
     */
    private List<String> getCanUseServiceWids (List<ServiceRelServiceResponse.ServiceModel> serviceModelList) {
        List<String> canUseWids = null;
        List<String> grantedWids;
        if ( userUtil.getUserAccount() == null ) {
            grantedWids = appServiceGrantService.queryGrantedServiceForGuest();
        } else {
            grantedWids = appServiceGrantService.checkServiceGrant(userUtil.getUserAccount());
        }
        if ( CollectionUtils.isNotEmpty(grantedWids) ) {
            List<String> serviceModeWids = serviceModelList.stream().map(ServiceRelServiceResponse.ServiceModel::getWid).collect(Collectors.toList());
            canUseWids = CollectionUtil.intersection(serviceModeWids, grantedWids);
        }
        return canUseWids;
    }

    /**
     * 根据用户查询可见服务
     *
     * @param serviceModelList
     * @return
     */
    private List<String> getCanVisitedServiceWids (List<ServiceRelServiceResponse.ServiceModel> serviceModelList) {
        List<String> canVisitedWids = null;
        List<String> visitedWids;
        if ( userUtil.getUserAccount() == null ) {
            visitedWids = appServiceGrantService.queryVisitedServiceForGuest();
        } else {
            visitedWids = appServiceGrantService.queryVisitedService(userUtil.getUserAccount());
        }
        if ( CollectionUtils.isNotEmpty(visitedWids) ) {
            List<String> serviceModeWids = serviceModelList.stream().map(ServiceRelServiceResponse.ServiceModel::getWid).collect(Collectors.toList());
            canVisitedWids = CollectionUtil.intersection(serviceModeWids, visitedWids);
        }
        return canVisitedWids;
    }

    /***
     * getAllServiceItem
     * <p/>
     *
     * @param
     * @throws
     * @return com.wisedu.minos.casp.portal.model.AllServiceItemResponse
     * @date 2021/6/8 15:53
     */
    public AllServiceItemResponse getAllServiceItemPage (AllServiceItemRequest allServiceItemRequest) {
        // 是否是条件查询，如果是条件查询则不走缓存直接查询调用接口查询
        boolean isConditionSearch = StringUtil.isNotEmpty(allServiceItemRequest.getSearchKey())
                || StringUtil.isNotEmpty(allServiceItemRequest.getCategoryWids())
                || StringUtil.isNotEmpty(allServiceItemRequest.getDeptWids())
                || StringUtil.isNotEmpty(allServiceItemRequest.getRoleWids())
                || StringUtil.isNotEmpty(allServiceItemRequest.getDimensions())
                || StringUtil.isNotEmpty(allServiceItemRequest.getGroupWid())
                || allServiceItemRequest.getAvailableOnline()
                || allServiceItemRequest.getOrderByVisitCount()
                || (allServiceItemRequest.getPageNumber() != 1 || allServiceItemRequest.getPageSize() != 2000);
        if ( isConditionSearch ) {
            return getServiceItemModels(allServiceItemRequest);
        }
        String userId = allServiceItemRequest.getUserId();
        String key = "allServiceItemPage:userId:" + userId;
        Object result = redisUtil.get(key);
        if ( result == null ) {
            AllServiceItemResponse serviceItemModels = getServiceItemModels(allServiceItemRequest);
            redisUtil.set(key, serviceItemModels, redisExpireTime, TimeUnit.SECONDS);
            return serviceItemModels;
        } else {
            return ( AllServiceItemResponse ) result;
        }
    }


    /**
     * 查询出全部服务事项(无权限，管控台用)
     */
    public QueryAllServiceItemResponse queryPageAllServiceItem (String... parms) {
        QueryAllServiceItemRequest queryAllServiceItemRequest = new QueryAllServiceItemRequest();
        queryAllServiceItemRequest.setPageNumber(parms[ 0 ]);
        queryAllServiceItemRequest.setPageSize(parms[ 1 ]);
        queryAllServiceItemRequest.setSearchKey(parms[ 2 ].trim());
        if ( parms.length > 3 ) {
            queryAllServiceItemRequest.setWids(parms[ 3 ]);
        }
        HttpEntity entity = new HttpEntity(queryAllServiceItemRequest);
        QueryAllServiceItemResponse response = null;
        LOGGER.debug("调用获取全部服务事项(,usr=" + queryAllServiceItemRequest + "参数=" + JSON.toJSONString(entity));
        try {
            response = RestTemplateUtils.post("/coosk/itemManager/getQueryItemPageList", entity, QueryAllServiceItemResponse.class).getBody();
            LOGGER.debug("调用获取全部服务事项接口成功，返回结果：" + JSON.toJSONString(response));
        } catch ( Exception e ) {
            LOGGER.error("调用获取全部服务事项接口失败...,返回错误信息" + e);
        }
        if ( response != null && response.getTotalSize() != null ) {
            response.setTotal(response.getTotalSize());
        }
        return response;
    }

    /**
     * 查询出全部服务事项(运营中心调用接口获取服务事项信息用)
     */
    public QueryItemPageListForCaspRes getQueryItemPageListForCasp (String markId, String pageSize) {
        QueryAllServiceItemRequest queryAllServiceItemRequest = new QueryAllServiceItemRequest();
        queryAllServiceItemRequest.setWids(markId);
        queryAllServiceItemRequest.setPageSize(pageSize);
        HttpEntity entity = new HttpEntity(queryAllServiceItemRequest);
        QueryItemPageListForCaspRes response = null;
        try {
            response = RestTemplateUtils.post("/coosk/itemManager/getQueryItemPageListForCasp", entity, QueryItemPageListForCaspRes.class).getBody();
        } catch ( Exception e ) {
            LOGGER.info("调用获取全部服务事项接口失败...,返回错误信息", e);
        }
        if ( response != null && "0".equals(response.getErrcode()) ) {
            return response;
        }
        return response;
    }

    /**
     * visitService
     * <p/>
     * 访问服务计数
     *
     * @param requestParam
     * @param request
     * @return java.lang.String
     * @throws
     * @date 2020/10/22 17:31
     */
    public String visitService(Map<String, String> requestParam, TemplateAjaxRequest request) {
        String id = requestParam.get("id");//getKeywords(requestParam, "id");
        String type = requestParam.get("type");//getKeywords(requestParam, "type");
        String userWid="guest";
        UserInfo currentUser =null;
        if(null!=request&&null!=request.getCurrentUser()){
            currentUser=request.getCurrentUser();
            userWid=currentUser.getWid();
        }else{
            currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            if(null!=currentUser){
                userWid=currentUser.getWid();
            }
        }
        if ( Global.VisitType.SERVICE.getType().equals(type)) {
            recommendRecordVisits(currentUser, id, 1);
            return ApplicationContextUtil.get(ServiceApiService.class)
                    .visitService(new VisitServiceRequest().setServiceWid(id).setUserWid(userWid)).getErrmsg();
        } else if (Global.VisitType.ITEM.getType().equals(type)) {
            recommendRecordVisits(currentUser, id, 2);
            return ApplicationContextUtil.get(ServiceItemApiService.class)
                    .visitServiceItem(new VisitServiceItemRequest().setItemWid(id).setUserWid(userWid)).getErrmsg();
        } else if (Global.VisitType.ONETHING.getType().equals(type)) {
            recommendRecordVisits(currentUser, id, 3);
            try {
                ApplicationContextUtil.get(CardDubboUtil.class)
                        .getDubboOneThingService().visitOneThing(id, userWid);
                return "success";
            }catch (Exception e){
                LOGGER.warn("记录一件事访问量异常", e);
                return e.getMessage();
            }
        } else {
            return "ok";
        }
    }

    public void recommendRecordVisits(UserInfo currentUser, String id, int type){
        //记录访问次数
        if (currentUser != null && StringUtils.isNotBlank(currentUser.getUserAccount())) {
            JSONObject param = new JSONObject();
            param.put("userAccount", currentUser.getUserAccount());
            param.put("serviceWid", id);
            param.put("type", type);
            HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);
            try {
                ResponseEntity<JSONObject> post = RestTemplateUtils.post("/casp-expansion/serviceRecommend/recordVisits", httpEntity, JSONObject.class);
                if (!post.getStatusCode().equals(HttpStatus.OK) || post.getBody() == null || !"success".equals(post.getBody().get("errmsg"))) {
                    LOGGER.error("调用记录访问次数接口失败:{}", post.toString());
                }
            } catch (Exception e) {
                LOGGER.error("调用记录访问次数接口失败", e);
            }
        }
    }

}
