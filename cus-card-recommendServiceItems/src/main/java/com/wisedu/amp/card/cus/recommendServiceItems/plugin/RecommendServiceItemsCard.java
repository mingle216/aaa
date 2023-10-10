package com.wisedu.amp.card.cus.recommendServiceItems.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wisedu.amp.card.cus.recommendServiceItems.model.*;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.CollectionUtil;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.impl.ServiceApiService;
import com.wisedu.minos.casp.portal.service.impl.ServiceItemApiService;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author kaisir
 */
@MinosSPI
public class RecommendServiceItemsCard extends AbstractCard {
    private final static Logger logger = LogManager.getLogger(RecommendServiceItemsCard.class);

    @Override
    public String getCardId() {
        return "CUS_CARD_RECOMMENDSERVICEITEMS";
    }

    @Override
    public ComponentContainer getConfig(CardConfigReq cardConfigReq) {
        ComponentContainer container = cardConfigReq.getComponentContainer();
        String configInfo = ApplicationContextUtil.get(CardUtil.class).getConsoleCardConfigByCardWid(cardConfigReq.getCardWid(),cardConfigReq.getCardId(),cardConfigReq.getPlatformType());
        if (StringUtil.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
            Config config = JSONObject.parseObject(configInfo, Config.class);
            List<String> serviceItemWids = config.getAddServiceItemList();
            if (CollectionUtils.isNotEmpty(serviceItemWids)) {
                String wids = String.join(",", serviceItemWids);
                if (CollectionUtils.isNotEmpty(serviceItemWids)) {
                    QueryAllServiceItemResponse queryAllServiceItem = ApplicationContextUtil.get(ServiceItemApiService.class).queryPageAllServiceItem("1", Global.MAXSIZE.toString(), "", wids);
                    setCompentData(container, serviceItemWids, queryAllServiceItem);
                }
            }
        }
        return container;
    }

    private void setCompentData(ComponentContainer container, List<String> serviceItemWids, QueryAllServiceItemResponse queryAllServiceItem) {
        if(CollectionUtils.isNotEmpty(queryAllServiceItem.getData())){
            Map<String, RecommendServiceItems> serviceItemsMap = queryAllServiceItem.getData().stream().collect(Collectors.toMap(RecommendServiceItems::getItemWid, entity -> entity));
            List<RecommendServiceItems> recommendServiceItems = new ArrayList<>();
            for (String wid: serviceItemWids) {
                if (serviceItemsMap.get(wid) != null){
                    recommendServiceItems.add(serviceItemsMap.get(wid));
                }
             }
            container.setData("addServiceItemList", Global.ComponentParam.VALUE, recommendServiceItems);
        }
    }

    @Override
    public void destroy() {
        logger.debug("destroy..");
    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "renderData":
                result = this.viewData(request);
                break;
            case "favoriteServiceItem":
                result = this.favoriteServiceItem(request);
                break;
            case "queryAllServiceItem":
                result = this.queryAllServiceItem(request);
                break;
            case "getMarkNumber":
                result = 0;
                break;
        }

        return result;
    }

    private QueryAllServiceItemResponse queryAllServiceItem(CardAjaxRequest request) {
        return ApplicationContextUtil.get(ServiceItemApiService.class).queryPageAllServiceItem(request.getParam().get("pageNum"), request.getParam().get("pageSize"), request.getParam().get("searchKey"));
    }


    private List<AllServiceItemResponse.ServiceItemModel> getAllItemList(Config config, String lang) {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        String userId;
        String userAccount;
        if (null == user) {
            userId = "guest";
            userAccount = "guest";
        } else {
            userId = user.getWid();
            userAccount = user.getUserAccount();
        }
        AllServiceItemRequest request = new AllServiceItemRequest();
        request.setUserId(userId);
        request.setUserAccount(userAccount);
        request.setIsRelate( null == config.getIsRelate() ? 0 : config.getIsRelate());
        if(null == user && !StringUtils.isEmpty(lang)){
            request.setLang(lang);
        }
        List<AllServiceItemResponse.ServiceItemModel> allServiceItem = ApplicationContextUtil.get(ServiceItemApiService.class)
                .getAllServiceItem(request);

        return allServiceItem;
    }

    private ResultData favoriteServiceItem(CardAjaxRequest request) {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if (null == user) {
            throw new NoLoginException();
        }
        FavoriteServiceItemRequest favoriteServiceItemRequest = new FavoriteServiceItemRequest();
        favoriteServiceItemRequest.setFlag(request.getParam().get("flag"));
        logger.debug("最近服务事项收藏时flag值:" + request.getParam().get("flag"));
        favoriteServiceItemRequest.setItemId(request.getParam().get("serviceItemId"));
        logger.debug("最近服务事项收藏时item值:" + request.getParam().get("serviceItemId"));
        favoriteServiceItemRequest.setUserId(user.getWid());
        HttpEntity entity = new HttpEntity(favoriteServiceItemRequest);
        AmpBaseResponse response = null;
        try {
            response = RestTemplateUtils.post("/coosk/itemManager/favoriteItem", entity, AmpBaseResponse.class).getBody();
            logger.debug("调用服务事项收藏/取消收藏接口返回结果..." + JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("调用服务事项收藏/取消收藏接口失败...,返回错误信息" + e);
        }
        Assert.isTrue("0".equals(response.getErrcode()), response.getErrmsg());
        ResultData retModel = new ResultData();
        retModel.setErrcode(response.getErrcode());
        retModel.setErrmsg("取消收藏/收藏成功");
        return retModel;
    }


    private List<AllServiceItemResponse.ServiceItemModel> getRecommendServiceItems(Config config, String lang) {
        // 获取所有服务事项
        List<AllServiceItemResponse.ServiceItemModel> serviceItemsBizList = null;
        // 获取配置中保存的推荐服务事项id集合
        List<String> serviceItemWids = config.getAddServiceItemList();
        if (CollectionUtils.isNotEmpty(serviceItemWids)) {
            serviceItemsBizList = getAllItemList(config, lang);
        }
        List<AllServiceItemResponse.ServiceItemModel> recommendServiceItems = null;
        if (CollectionUtils.isNotEmpty(serviceItemWids)) {
            // 处理全部服务
            recommendServiceItems = processingServiceData(serviceItemWids, serviceItemsBizList);
        }
        return recommendServiceItems;
    }

    private Map<String, Object> viewData(CardAjaxRequest request) {
        Map<String, Object> result = new HashMap<>(10);
        String configInfo = ApplicationContextUtil.get(CardUtil.class)
                .getCardConfigByCardWid(request.getCardWid(),request.getCardId());
        if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
        }
        boolean mobileDevice = CommonUtil.isMobileDevice();
        Config config;
        if (mobileDevice) {
            config = getMobileConfig(configInfo);
        } else {
            config = getPcConfig(configInfo);
        }
        //加遊客狀態前台傳入的瀏覽器語言參數
        String lang = "";
        if(!request.getParam().isEmpty() && !StringUtils.isEmpty(request.getParam().get("lang"))){
            lang = request.getParam().get("lang");
        }

        result.put("cardId", getCardId());
        result.put("cardWid", request.getCardWid());
        result.put("config", config);

        // 获取配置中保存的推荐服务事项id集合
        List<String> configItemsWids = config.getAddServiceItemList();

        //推荐服务为空且没有开启自动推荐直接返回空
        if(CollectionUtils.isEmpty(configItemsWids) && "0".equals(config.getAutoRecommend())){
            result.put("recommendAppList", null);
            return result;
        }

        List<AllServiceItemResponse.ServiceItemModel> serviceItemsList = getAllItemList(config, lang);
        Map<String, AllServiceItemResponse.ServiceItemModel> allServiceItemMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(serviceItemsList)) {
            allServiceItemMap = serviceItemsList.stream().collect(Collectors.toMap(AllServiceItemResponse.ServiceItemModel::getItemWid, entity -> entity));
        }

        List<AllServiceItemResponse.ServiceItemModel> recommendServiceItems = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(configItemsWids)){
            for (String wid : configItemsWids) {
                if (allServiceItemMap.get(wid) != null) {
                    recommendServiceItems.add(allServiceItemMap.get(wid));
                }
            }
        }

        // 自动推荐服务事项
        if("1".equals(config.getAutoRecommend())){
            autoRecommend(recommendServiceItems, allServiceItemMap, configItemsWids);
        }

        List<String> grantedServiceList = ApplicationContextUtil.get(ServiceApiService.class).getGrantedServiceList();
        if(CollectionUtils.isNotEmpty(recommendServiceItems)){
            recommendServiceItems.forEach(e->e.setOnlineServiceType(
                    ApplicationContextUtil.get(ServiceApiService.class).getOnlineServiceType(grantedServiceList, e.getServiceList())
            ));
            result.put("serviceItemsInfo", recommendServiceItems);
        }
        return result;
    }

    private void autoRecommend(List<AllServiceItemResponse.ServiceItemModel> recommendServiceItems,
                               Map<String, AllServiceItemResponse.ServiceItemModel> allServiceItemMap,
                               List<String> configItemsWids ){
        List<String> serviceRangeList = Lists.newArrayList(allServiceItemMap.keySet());
        serviceRangeList.removeAll(configItemsWids);
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if(CollectionUtils.isNotEmpty(serviceRangeList) && user != null && org.apache.commons.lang.StringUtils.isNotEmpty(user.getUserAccount())){
            //获取自动推荐的服务
            List<RecommendServiceInfo> recommendServiceListFromAuto = getAutoRecommendServiceItemList(user.getUserAccount(),serviceRangeList);
            if(CollectionUtils.isNotEmpty(recommendServiceListFromAuto)){
                for (RecommendServiceInfo recommendServiceInfo : recommendServiceListFromAuto) {
                    if (allServiceItemMap.get(recommendServiceInfo.getServiceWid()) != null) {
                        AllServiceItemResponse.ServiceItemModel serviceModel = allServiceItemMap.get(recommendServiceInfo.getServiceWid());
                        serviceModel.setServiceRecommendRating(recommendServiceInfo.getRating());
                        recommendServiceItems.add(serviceModel);
                    }
                }
            }
        }
    }

    private Config getMobileConfig(String configInfo) {
        Config config = JSONObject.parseObject(configInfo, Config.class);
        if (null == config) {
            config = new Config();
        }
        return config;
    }

    private Config getPcConfig(String configInfo) {
        Config config = JSONObject.parseObject(configInfo, Config.class);
        if (null == config) {
            config = new Config();
            ContainerType containerType = new ContainerType();
            containerType.setType(0);
            containerType.setValue(500);
            config.setContainerType(containerType);
            config.setShowItem("[]");
            config.setRows(8);
            config.setColumns(4);
            config.setIsRelate(0);
            config.setShowDealOnline(0);
        }
        return config;

    }


    //查询出我收藏的服务
//    private CollectResponse queryFavoriteServiceItem(String userId, String isRelate) {
//        JSONObject param = new JSONObject();
//        param.put("userId", userId);
////			param.put("userId", userId);
//        param.put("isRelate", isRelate);
//        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);
//        CollectResponse collectResponse = null;
//        logger.debug("调用收藏应用查询接口,参数=" + JSON.toJSONString(httpEntity));
//        try {
//            collectResponse = RestTemplateUtils.post("/coosk/userItemFavoriteList", httpEntity, CollectResponse.class).getBody();
//            logger.debug("调用收藏应用查询接口成功，返回结果：" + JSON.toJSONString(collectResponse));
//        } catch (Exception e) {
//            logger.error("调用收藏应用查询接口失败，原因：" + e);
//        }
//        return collectResponse;
//    }

    /**
     * 处理全部服务事项
     */
    private List<AllServiceItemResponse.ServiceItemModel> processingServiceData(List<String> configItemsWids, List<AllServiceItemResponse.ServiceItemModel> appInfoBizList) {
        Map<String, AllServiceItemResponse.ServiceItemModel> allServiceItemMap = appInfoBizList.stream().collect(Collectors.toMap(AllServiceItemResponse.ServiceItemModel::getItemWid, entity -> entity));
        List<AllServiceItemResponse.ServiceItemModel> recommendServiceItems = new ArrayList<>();
        for (String wid : configItemsWids) {
            if (allServiceItemMap.get(wid) != null) {
                recommendServiceItems.add(allServiceItemMap.get(wid));
            }
        }
        return recommendServiceItems;
    }

    /**
     * 获取自动推荐的服务
     * @param userAccount
     * @param serviceRangeList
     * @return
     */
    private List<RecommendServiceInfo> getAutoRecommendServiceItemList(String userAccount, List<String> serviceRangeList) {
        RecommendServiceResponse response = null;

        RecommendServiceReq recommendServiceReq = new RecommendServiceReq();
        recommendServiceReq.setUserAccount(userAccount);
        recommendServiceReq.setServiceRangeList(serviceRangeList);
        HttpEntity entity = new HttpEntity(recommendServiceReq);

        logger.debug("调用获取推荐服务事项接口,参数=" + JSON.toJSONString(entity));
        try {
            response = RestTemplateUtils.post("/casp-expansion/serviceRecommend/getRecommendServiceItem", entity, RecommendServiceResponse.class).getBody();
            logger.debug("调用获取推荐服务事项接口返回结果..." + JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("调用获取推荐服务事项接口失败...,返回错误信息" + e);
        }
        if(response == null){
            return new ArrayList<>();
        }

        return response.getData();
    }
}
