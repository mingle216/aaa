package com.wisedu.amp.card.cus.cucFavoriteApp.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.wisedu.amp.card.cus.cucFavoriteApp.model.*;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.MinosCommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;

import java.util.*;

/**
 * @Auther:hlchen02
 * @Date:2020/8/17
 * @Description:com.wisedu.amp.card.cus.cucFavoriteApp.plugin
 * @Version:1.0
 */
@MinosSPI
public class FavoriteAppCard extends AbstractCard {
    private final static Logger logger = LogManager.getLogger(FavoriteAppCard.class);

    @Override
    public String getCardId () {
        return "CUS_CARD_CUCFAVORITEAPP";
    }


    @Override
    public void destroy () {
        logger.info("destroy..");
    }

    @Override
    public Object execDispatcher (CardAjaxRequest request) {
        Object result = null;
        if(!checkLogin()){
            return result;
        }
        switch ( request.getMethod() ) {
            case "renderData":
                result = this.renderData(request);
                break;
            case "collectApp":
                result = this.collectApp(request);
                break;
            case "sort":
                result = this.sort(request);
                break;
            case "getMarkNumber":
                result = 0;
                break;
            case "batchFavoriteService":
                result = this.batchFavoriteService(request);
                break;

        }

        return result;
    }

    private Object batchFavoriteService (CardAjaxRequest request) {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        AppSortRequest appSortRequest = new AppSortRequest();
        appSortRequest.setUserId(user.getWid());
        appSortRequest.setServiceItemIds(request.getParam().get("serviceWid").split(","));
        HttpEntity entity = new HttpEntity(appSortRequest);

        AmpBaseResponse response = null;

        try {
            response = RestTemplateUtils.post("/serviceFavorite/batchFavoriteService", entity, AmpBaseResponse.class).getBody();
        } catch ( Exception e ) {
            logger.error("调用批量处理收藏接口失败...,返回错误信息" + e);
        }

        return response.getErrcode();
    }


    private Object renderData(CardAjaxRequest request) {
        Map<String,Object> renderData = new HashMap<>();

        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if ( null == user || StringUtil.isEmpty(user.getUserAccount()) ) {
            user = new UserInfo();
            user.setWid("");
        }

        AppInfoRequest appInfoRequest = getAppInfoRequest(user);

        HttpEntity entity = new HttpEntity(appInfoRequest);

        AppInfoResponse response = null;
        try {
            response = RestTemplateUtils.post("/serviceFavorite/userServiceFavoriteList", entity, AppInfoResponse.class).getBody();
        } catch ( Exception e ) {
            logger.error("调用收藏应用查询接口失败，原因：" + e);
        }

        String configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(),request.getCardId());
        if (StringUtil.isNotEmpty(configInfo) ) {
            configInfo = JSON.parse(configInfo).toString();
        }
        Config config = JSONObject.parseObject(configInfo, Config.class);
        if ( null == config ) {
            config = new Config();
            ContainerType containerType = new ContainerType();
            containerType.setType(0);
            containerType.setValue(500);
            config.setContainerType(containerType);
            config.setColumns(4);
        }

        List<AppInfo> data = null;
        if ( null != response && null != response.getData() ) {
            data = response.getData();
        }
        renderData.put("appList", data);
        renderData.put("config", config);
        renderData.put("isMobileDevice",CommonUtil.isMobileDevice());
        return renderData;

    }

    private AppInfoRequest getAppInfoRequest(UserInfo user) {
        AppInfoRequest appInfoRequest = new AppInfoRequest();
        appInfoRequest.setUserId(user.getWid());
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if(isMobileDevice){
            // 看移动端是否需要展示pc端服务
            boolean isShowPCService = ApplicationContextUtil.get(CardUtil.class).getIsShowPCService();
            if(!isShowPCService){
                appInfoRequest.setServiceStation("1");
            }
        } else{
            appInfoRequest.setServiceStation("0");
        }
        return appInfoRequest;
    }

    private AmpBaseResponse collectApp (CardAjaxRequest request) {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        CollectAppRequest collectAppRequest = new CollectAppRequest();
        collectAppRequest.setUserId(user.getWid());
        collectAppRequest.setServiceItemId(request.getParam().get("appId"));
        collectAppRequest.setFlag(request.getParam().get("operate"));
        HttpEntity entity = new HttpEntity(collectAppRequest);

        AmpBaseResponse response = null;
        //获取收藏或取消收藏应用接口地址
        logger.debug("调用应用收藏/取消收藏接口,参数=" + MinosCommonUtil.getSafetyLog(entity.toString()));
        try {
            response = RestTemplateUtils.post("/serviceFavorite/favoriteServiceItem", entity, AppInfoResponse.class).getBody();
        } catch ( Exception e ) {
            logger.error("调用应用收藏/取消收藏接口失败...,返回错误信息" + e);
        }

        return response;
    }


    private AmpBaseResponse sort (CardAjaxRequest request) {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        CollectServicesRequest collectAppRequest = new CollectServicesRequest();
        collectAppRequest.setUserId(user.getWid());
        if(null != request.getParam()){
           // 前端传值，逗号分隔
            String widsStr = request.getParam().get("serviceItemIds");
            List<String> serviceWids = widsStr == null ? Collections.emptyList()
                    : Splitter.on(",").trimResults().omitEmptyStrings().splitToList(widsStr);
            collectAppRequest.setServiceItemIds(serviceWids);
        }
        HttpEntity entity = new HttpEntity(collectAppRequest);
        AmpBaseResponse response = null;
        // 获取收藏或取消收藏应用接口地址
        try {
            response = RestTemplateUtils.post("/serviceFavorite/batchFavoriteService", entity, AppInfoResponse.class).getBody();
        } catch ( Exception e ) {
            logger.error("调用批量取消服务收藏接口失败...,返回错误信息" + e);
        }

        return response;
    }
}
