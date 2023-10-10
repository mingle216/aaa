package com.wisedu.amp.card.cus.cucRecommendapp.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wisedu.amp.card.cus.cucRecommendapp.model.*;
import com.wisedu.amp.card.cus.cucRecommendapp.model.AmpBaseResponse;
import com.wisedu.amp.card.cus.cucRecommendapp.model.AppInfoResponse;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.impl.ServiceApiService;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;

import java.util.*;
import java.util.stream.Collectors;


@MinosSPI
public class RecommendAppCard extends AbstractCard {
    private final static Logger logger = LogManager.getLogger(RecommendAppCard.class);


    @Override
    public String getCardId() {
        return "CUS_CARD_CUCRECOMMENDAPP";
    }

    @Override
    public void destroy() {
        logger.debug("destroy..");
    }

    @Override
    public ComponentContainer getConfig(CardConfigReq cardConfigReq) {
        ComponentContainer container = cardConfigReq.getComponentContainer();
        String configInfo = ApplicationContextUtil.get(CardUtil.class).getConsoleCardConfigByCardWid(cardConfigReq.getCardWid(),cardConfigReq.getCardId(),cardConfigReq.getPlatformType());
        if (StringUtil.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
            Config config = JSONObject.parseObject(configInfo, Config.class);
            List<String> serviceWids = config.getAddServiceList();
            if (CollectionUtils.isNotEmpty(serviceWids)) {
                QueryAllAppResponse queryAllAppResponse = queryAllService("", "1", Global.MAXSIZE.toString(), "", true);
                if(null != queryAllAppResponse && "0".equals(queryAllAppResponse.getErrcode()) && CollectionUtils.isNotEmpty(queryAllAppResponse.getData())){
                    List<AppInfoBiz> serviceList = queryAllAppResponse.getData();
                    Map<String, AppInfoBiz> serviceMap = serviceList.stream().collect(Collectors.toMap(AppInfoBiz::getServiceWid, entity -> entity));
                    container.setData("addServiceList", Global.ComponentParam.VALUE, getConfigServices(serviceMap,serviceWids));
                }

            }
        }
        return container;
    }

    private List<ConfigService> getConfigServices(Map<String, AppInfoBiz> serviceMap,List<String> serviceWids){
        List<ConfigService> configServices = new ArrayList<>(16);
        for (String wid : serviceWids) {
            if (serviceMap.get(wid) != null){
                ConfigService configService = new ConfigService();
                configService.setServiceWid(serviceMap.get(wid).getServiceWid());
                if(serviceMap.get(wid).getIconLink() != null){
                    configService.setIconLink(serviceMap.get(wid).getIconLink());
                } else {
                    configService.setIconLink(serviceMap.get(wid).getMobileIconLink());
                }
                configService.setServiceName(serviceMap.get(wid).getServiceName());
                configServices.add(configService);
            }

        }
        return configServices;
    }


    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "renderData":
                result = this.renderData(request);
                break;
            case "collectApp":
                result = this.collectApp(request);
                break;
            case "queryAllApp":
                result = this.queryAllApp(request.getParam());
                break;
            case "getMarkNumber":
                result = 0;
                break;
        }

        return result;
    }


    private Object renderData(CardAjaxRequest request) {
        Map<String, Object> data = new HashMap<>();
        logger.debug("RecommendAppCard renderData..");

        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        String configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(),request.getCardId());
        if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
        }

        Config config = JSONObject.parseObject(configInfo, Config.class);
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if (null == config) {
            config = new Config();
            if(!isMobileDevice){
                ContainerType containerType = new ContainerType();
                containerType.setType(0);
                containerType.setValue(500);
                config.setContainerType(containerType);
            }
        }
        config.setAllServiceUrl("/index.html#/apps");


        data.put("config", config);
        data.put("isMobileDevice", isMobileDevice);
        if (null == user) {
            user = new UserInfo();
            user.setWid("");
        }
        String lang = (!request.getParam().isEmpty() && StringUtils.isNotEmpty(request.getParam().get("lang")))
                ? request.getParam().get("lang") : "";
        // 后台配置的推荐服务
        List<String> recommendServiceListFromConfig = config.getAddServiceList();

        //推荐服务为空且没有开启自动推荐直接返回空
        if(CollectionUtils.isEmpty(recommendServiceListFromConfig) && "0".equals(config.getAutoRecommend())){
            data.put("recommendAppList", null);
            return data;
        }
        //处理全部服务
        List<AllServiceResponse.ServiceModel> allServiceList = getAllServiceData(user.getWid(), false, lang);
        Map<String, AllServiceResponse.ServiceModel> allServiceMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(allServiceList)) {
            allServiceMap = allServiceList.stream().collect(Collectors.toMap(AllServiceResponse.ServiceModel::getServiceWid, entity -> entity));
        }

        List<AllServiceResponse.ServiceModel> appInfoBizList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(recommendServiceListFromConfig)) {
            for (String wid : recommendServiceListFromConfig) {
                if (allServiceMap.get(wid) != null) {
                    appInfoBizList.add(allServiceMap.get(wid));
                }
            }
        }

        // 自动推荐服务
        if("1".equals(config.getAutoRecommend())){
            autoRecommend(appInfoBizList, allServiceMap, recommendServiceListFromConfig, user);
        }

        data.put("recommendAppList", appInfoBizList);
        return data;
    }

    private void autoRecommend(List<AllServiceResponse.ServiceModel> appInfoBizList,
                               Map<String, AllServiceResponse.ServiceModel> allServiceMap,
                               List<String> recommendServiceListFromConfig,
                               UserInfo user){
        List<String> serviceRangeList = Lists.newArrayList(allServiceMap.keySet());
        serviceRangeList.removeAll(recommendServiceListFromConfig);

        if(CollectionUtils.isNotEmpty(serviceRangeList) && user != null && StringUtils.isNotEmpty(user.getUserAccount())){
            //获取自动推荐的服务
            List<RecommendServiceInfo> recommendServiceListFromAuto = getAutoRecommendServiceList(user.getUserAccount(),serviceRangeList);
            if(CollectionUtils.isNotEmpty(recommendServiceListFromAuto)){
                for (RecommendServiceInfo recommendServiceInfo : recommendServiceListFromAuto) {
                    if (allServiceMap.get(recommendServiceInfo.getServiceWid()) != null) {
                        AllServiceResponse.ServiceModel serviceModel = allServiceMap.get(recommendServiceInfo.getServiceWid());
                        serviceModel.setServiceRecommendRating(recommendServiceInfo.getRating());
                        appInfoBizList.add(serviceModel);
                    }
                }
            }
        }
    }

    /**
     * 获取自动推荐的服务
     * @param userAccount
     * @param serviceRangeList
     * @return
     */
    private List<RecommendServiceInfo> getAutoRecommendServiceList(String userAccount, List<String> serviceRangeList) {
        RecommendServiceResponse response = null;

        RecommendServiceReq recommendServiceReq = new RecommendServiceReq();
        recommendServiceReq.setUserAccount(userAccount);
        recommendServiceReq.setServiceRangeList(serviceRangeList);
        HttpEntity entity = new HttpEntity(recommendServiceReq);

        logger.debug("调用获取推荐服务接口,参数=" + JSON.toJSONString(entity));
        try {
            response = RestTemplateUtils.post("/casp-expansion/serviceRecommend/getRecommendService", entity, RecommendServiceResponse.class).getBody();
            logger.debug("调用获取推荐服务接口返回结果..." + JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("调用获取推荐服务接口失败...,返回错误信息" + e);
        }
        if(response == null){
            return new ArrayList<>();
        }

        return response.getData();
    }

    private QueryAllAppResponse queryAllApp(Map<String,String> param) {
        QueryAllAppResponse response = queryAllService("", param.get("pageNum"),
                param.get("pageSize"), param.get("searchKey"), true);
        response.setPageNum(Integer.parseInt(param.get("pageNum")));
        response.setSearchValue(param.get("searchKey"));
        return response;
    }


    private AmpBaseResponse collectApp(CardAjaxRequest request) {
        AmpBaseResponse response = null;

        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if (null == user) {
            logger.debug("用户未登录...");
            response.setErrcode("500");
            response.setErrmsg("用户未登录...");
            return response;
        }
        CollectAppRequest collectAppRequest = new CollectAppRequest();
        collectAppRequest.setUserId(user.getWid());
        collectAppRequest.setServiceItemId(request.getParam().get("appId"));
        collectAppRequest.setFlag(request.getParam().get("operate"));
        HttpEntity entity = new HttpEntity(collectAppRequest);

        String collectUrl = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue("collect_app_interface");
        logger.debug("调用应用收藏/取消收藏接口," + collectUrl + "参数=" + JSON.toJSONString(entity));
        try {
            response = RestTemplateUtils.post("/serviceFavorite/favoriteServiceItem", entity, AppInfoResponse.class).getBody();
            logger.debug("调用应用收藏/取消收藏接口返回结果..." + JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("调用应用收藏/取消收藏接口失败...,返回错误信息" + e);
        }

        return response;
    }


    /**
     * 查询出全部服务
     */
    private QueryAllAppResponse queryAllService(String userId, String pageNum, String pageSize, String searchKey, boolean console) {
        QueryAllAppRequest queryAllAppRequest = new QueryAllAppRequest();
        queryAllAppRequest.setUserId(userId);
        queryAllAppRequest.setPageNumber(pageNum);
        queryAllAppRequest.setPageSize(pageSize);
        queryAllAppRequest.setSearchKey(searchKey.trim());
        queryAllAppRequest.setConsole(console);

        HttpEntity entity = new HttpEntity(queryAllAppRequest);
        QueryAllAppResponse response = null;
        logger.debug("调用获取全部应用接口,usr=" + queryAllAppRequest + "参数=" + JSON.toJSONString(entity));
        try {
            response = RestTemplateUtils.post("/groupService/allServiceList", entity, QueryAllAppResponse.class).getBody();
            logger.debug("调用获取全部应用接口成功，返回结果：" + JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("调用获取全部应用接口失败...,返回错误信息" + e);
        }

        return response;
    }


    /**
     * 获取全部服务
     * @param userId
     * @param console
     * @param lang
     * @return
     */
    private List<AllServiceResponse.ServiceModel> getAllServiceData(String userId,boolean console, String lang) {
        AllServiceRequest allServiceListRequest = new AllServiceRequest();
        allServiceListRequest.setUserId(userId);
        if((StringUtils.isEmpty(userId) || "guest".equals(userId)) && StringUtils.isNotEmpty(lang)){
            allServiceListRequest.setLang(lang);
        }
        allServiceListRequest.setPageNumber(1);
        allServiceListRequest.setPageSize(Global.MAXSIZE);
        allServiceListRequest.setConsole(console);
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if(isMobileDevice){
            // 看移动端是否需要展示pc端服务
            boolean isShowPCService = ApplicationContextUtil.get(CardUtil.class).getIsShowPCService();
            if(!isShowPCService){
                allServiceListRequest.setServiceStation("1");
            }
        } else{
            allServiceListRequest.setServiceStation("0");
        }
        return ApplicationContextUtil.get(ServiceApiService.class).getAllService(allServiceListRequest);

//        List<AllServiceResponse.ServiceModel> appInfoBizList = new ArrayList<>();
//        if (CollectionUtils.isNotEmpty(list)) {
//            Map<String, AllServiceResponse.ServiceModel> allServiceMap = list.stream().collect(Collectors.toMap(AllServiceResponse.ServiceModel::getServiceWid, entity -> entity));
//            for (String wid : configAppList) {
//                if (allServiceMap.get(wid) != null) {
//                    appInfoBizList.add(allServiceMap.get(wid));
//                }
//            }
//        }
//        return appInfoBizList;
    }
}
