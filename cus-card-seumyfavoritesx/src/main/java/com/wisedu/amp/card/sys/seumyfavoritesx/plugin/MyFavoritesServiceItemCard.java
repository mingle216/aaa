package com.wisedu.amp.card.sys.seumyfavoritesx.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.wisedu.amp.card.sys.seumyfavoritesx.model.AmpBaseResponse;
import com.wisedu.amp.card.sys.seumyfavoritesx.model.CollectServiceItemsRequest;
import com.wisedu.amp.card.sys.seumyfavoritesx.model.Config;
import com.wisedu.amp.card.sys.seumyfavoritesx.model.ConfigH5;
import com.wisedu.amp.card.sys.seumyfavoritesx.model.ConfigInfo;
import com.wisedu.amp.card.sys.seumyfavoritesx.model.ContainerType;
import com.wisedu.amp.card.sys.seumyfavoritesx.model.FavoriteServiceItemRequest;
import com.wisedu.amp.card.sys.seumyfavoritesx.model.MyFavouritesServiceitemResponse;
import com.wisedu.amp.card.sys.seumyfavoritesx.model.ResultUtil;
import com.wisedu.amp.card.sys.seumyfavoritesx.model.UserFavoriteServiceItems;
import com.wisedu.amp.card.sys.seumyfavoritesx.model.UserFavoriteServiceItemsRequest;
import com.wisedu.amp.card.sys.seumyfavoritesx.util.RestTemplateUtil;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Template;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

@MinosSPI
public class MyFavoritesServiceItemCard extends AbstractCard {
    private final static Logger logger = LogManager.getLogger(MyFavoritesServiceItemCard.class);

    @Override
    public String getCardId() {
        return "CUS_CARD_SEUMYFAVORITESX";
    }

    @Override
    public void destroy() {
        logger.debug("destroy..");
    }

    @Override
    public Object execDispatcher(CardAjaxRequest cardAjaxRequest) {
        Object result = null;
        switch (cardAjaxRequest.getMethod()) {
            case "render":
                result = this.view(cardAjaxRequest);
                break;
            case "renderData":
                result = this.viewData(cardAjaxRequest);
                break;
            case "config":
                result = this.config(cardAjaxRequest);
                break;
            case "favoriteServiceItem":
                result = this.favoriteServiceItem(cardAjaxRequest);
                break;
            case "getMarkNumber":
                result = 0;
                break;
            case "sort":
                result = sort(cardAjaxRequest);
                break;
        }

        return result;
    }

    /**
     * 取消收藏
     *
     * @param request
     * @return
     */
    private ResultData favoriteServiceItem(CardAjaxRequest request) {
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            FavoriteServiceItemRequest favoriteServiceItemRequest = new FavoriteServiceItemRequest();
            favoriteServiceItemRequest.setFlag("0");
            favoriteServiceItemRequest.setItemId(request.getParam().get("serviceItemId"));
            favoriteServiceItemRequest.setUserId(user.getWid());
            HttpEntity entity = new HttpEntity(favoriteServiceItemRequest);
            ResultData<String> result = RestTemplateUtil.httpRequestForObject(entity, "/casp-sim/itemManager/favoriteItem", HttpMethod.POST, ResultData.class, String.class);
            Assert.notNull(result, "取消收藏" + request.getParam().get("serviceItemId") + "失败！");
            Assert.isTrue("0".equals(result.getErrcode()), result.getErrmsg());
            ResultData retModel = new ResultData();
            retModel.setErrcode("0");
            retModel.setErrmsg("取消收藏成功");
            return retModel;
        } catch (Exception e) {
            logger.error("取消收藏失败...,返回错误信息" , e);
            return null;
        }
    }

    /**
     * 渲染我收藏的服务事项页面
     *
     * @param request
     * @return
     */
    private String view(CardAjaxRequest request) {
        try {
            logger.debug("进入渲染我收藏的服务事项页面..");
            Template t = getGroupTemplate().getTemplate("/" + this.getCardId() + "/view.html");
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            String configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(),request.getCardId());
            if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
                configInfo = JSON.parse(configInfo).toString();
            }
            ConfigInfo config = (ConfigInfo) JSONObject.parseObject(configInfo, ConfigInfo.class);
            List<UserFavoriteServiceItems> list = new ArrayList<>();
            if (null == config) {
                config = new ConfigInfo();
            }
            if (null == user || StringUtil.isEmpty(user.getWid())) {
                logger.error("未登录....");
            } else {
                UserFavoriteServiceItemsRequest userFavoriteServiceItemsRequest = new UserFavoriteServiceItemsRequest();
                userFavoriteServiceItemsRequest.setIsRelate(0);
                userFavoriteServiceItemsRequest.setUserId(user.getWid());
                HttpEntity entity = new HttpEntity(userFavoriteServiceItemsRequest);
                MyFavouritesServiceitemResponse response = null;
                try {
                    logger.debug("调用获取事项收藏列表接口请求参数：" + JSON.toJSONString(userFavoriteServiceItemsRequest));
                    response = RestTemplateUtils.post("/casp-sim/userItemFavoriteList", entity, MyFavouritesServiceitemResponse.class).getBody();
                    logger.debug("调用获取事项收藏列表接口成功，返回结果：" + JSON.toJSONString(response));
                } catch (Exception e) {
                    logger.error("调用获取事项收藏列表接口失败" , e);
                }
                if (null != response && ResultUtil.ResponseStatus.SUCCESS.getIndex().equals(response.getErrcode())) {
                    list = response.getData();
                }
            }
            //收藏的服务事项列表
            t.binding("serviceItemsInfo", list);
            t.binding("cardId", getCardId());
            t.binding("cardWid", request.getCardWid());
            t.binding("config", config);
            t.binding("heightFlag", config.getHeightFlag());
            t.binding("height", config.getHeight());
            handleCheckBoxData(t, config);
            String str = t.render();
            return str;
        } catch (Exception e) {
            logger.debug("渲染我收藏的服务事项页面失败...,返回错误信息" , e);
            return null;
        }
    }

    /**
     * 渲染我收藏的服务事项页面
     *
     * @param request
     * @return
     */
    private Map<String, Object> viewData(CardAjaxRequest request) {
        try {
            logger.debug("进入渲染我收藏的服务事项页面..");
            Map<String, Object> result = new HashMap<>(10);
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            String configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(),request.getCardId());
            if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
                configInfo = JSON.parse(configInfo).toString();
            }
            Config config =null;
            if(!CommonUtil.isMobileDevice()) {
                config = JSONObject.parseObject(configInfo, Config.class);
                if (null == config) {
                    config = new Config();
                    ContainerType containerType = new ContainerType();
                    containerType.setType(0);
                    containerType.setValue(500);
                    config.setContainerType(containerType);
                    config.setShowItem("[]");
                    config.setRows(8);
                    config.setColumns(4);
                    config.setShowManage(0);
                }
            }
            ConfigH5 configH5 =null;
            if(CommonUtil.isMobileDevice()) {
                configH5 = JSONObject.parseObject(configInfo, ConfigH5.class);
                if (null == configH5) {
                    configH5 = new ConfigH5();
                   configH5.setIconShow("1");
                   configH5.setColumns("5");
                }
            }
            List<UserFavoriteServiceItems> list = new ArrayList<>();
            if (null == user || StringUtil.isEmpty(user.getWid())) {
                logger.error("未登录....");
            } else {
                UserFavoriteServiceItemsRequest userFavoriteServiceItemsRequest = new UserFavoriteServiceItemsRequest();
                userFavoriteServiceItemsRequest.setIsRelate(0);
                userFavoriteServiceItemsRequest.setUserId(user.getWid());
                HttpEntity entity = new HttpEntity(userFavoriteServiceItemsRequest);
                MyFavouritesServiceitemResponse response = null;
                try {
                    logger.debug("调用获取事项收藏列表接口请求参数：" + JSON.toJSONString(userFavoriteServiceItemsRequest));
                    response = RestTemplateUtils.post("/casp-sim/userItemFavoriteList", entity, MyFavouritesServiceitemResponse.class).getBody();
                    logger.debug("调用获取事项收藏列表接口成功，返回结果：" + JSON.toJSONString(response));
                } catch (Exception e) {
                    logger.error("调用获取事项收藏列表接口失败" , e);
                }
                if (null != response && ResultUtil.ResponseStatus.SUCCESS.getIndex().equals(response.getErrcode())) {
                    list = response.getData();
                }
            }
            //收藏的服务事项列表
            result.put("serviceItemsInfo", list);
            result.put("cardId", getCardId());
            result.put("cardWid", request.getCardWid());
            if(!CommonUtil.isMobileDevice()){
                result.put("config", config);
                result.put("heightFlag", config.getContainerType().getType());
                result.put("height", config.getContainerType().getValue());
                result.put("columns", config.getColumns());
                handleCheckBoxDataMap(result, config);

            }else{
                result.put("config", configH5);
                result.put("columns", configH5.getColumns());

            }
            return result;
        } catch (Exception e) {
            logger.debug("渲染我收藏的服务事项页面失败...,返回错误信息" , e);
            return null;
        }
    }

    /**
     * 我收藏的服务事项配置页面
     *
     * @param request
     * @return
     */
    private String config(CardAjaxRequest request) {
        try {
            logger.debug("doConfig..");
            Template t = getGroupTemplate().getTemplate("/" + this.getCardId() + "/config.html");
            //先查询传参，如果传参没有则需要查询库，在没有则初始化默认
            String configInfo = "";
            if (null != request.getParam() && null != request.getParam().get("config")) {
                configInfo = request.getParam().get("config");
            } else {
                configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(),request.getCardId());
                if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
                    configInfo = JSON.parse(configInfo).toString();
                }
            }
            ConfigInfo config = (ConfigInfo) JSONObject.parseObject(configInfo, ConfigInfo.class);
            if (null == config) {
                config = new ConfigInfo();
            }
            handleRadioData(t, config, request.getCardWid());
            t.binding("height", config.getHeight());
            t.binding("configure", config.getConfigure());
            t.binding("cardId", getCardId());
            t.binding("cardWid", request.getCardWid());
            // 设置复选框
            handleCheckBoxData(t, config);
            String str = t.render();
            return str;
        } catch (Exception e) {
            logger.debug("我收藏的服务事项配置页面失败...,返回错误信息" , e);
            return null;
        }
    }

    /*
     * 处理config初始化的复选框值
     */
    private void handleCheckBoxData(Template t, ConfigInfo config) {
        if(null != config) {
            if (null != config.getConfigure()) {
                if (config.getConfigure().contains(ResultUtil.CHECKED_FLAG_OLD.FWZT)) {
                    t.binding("configure0", "checked");
                } else {
                    t.binding("configure0", "");
                }

                if (config.getConfigure().contains(ResultUtil.CHECKED_FLAG_OLD.ZRBM)) {
                    t.binding("configure1", "checked");
                } else {
                    t.binding("configure1", "");
                }
                if (config.getConfigure().contains(ResultUtil.CHECKED_FLAG_OLD.FWSXTB)) {
                    t.binding("configure2", "checked");
                } else {
                    t.binding("configure2", "");
                }

            } else {
                t.binding("configure0", "");
                t.binding("configure1", "");
                t.binding("configure2", "");
            }
        }else {
            t.binding("configure0", "");
            t.binding("configure1", "");
            t.binding("configure2", "");
        }
    }

    /*
     * 处理config初始化的复选框值
     */
    private void handleCheckBoxDataMap(Map<String ,Object> t, Config config) {
        if(null != config) {
            if (null != config.getShowItem()) {
                if (config.getShowItem().contains(ResultUtil.CHECKED_FLAG.ICON)) {
                    t.put("configure0", "checked");
                } else {
                    t.put("configure0", "");
                }

                if (config.getShowItem().contains(ResultUtil.CHECKED_FLAG.SERVICE)) {
                    t.put("configure1", "checked");
                } else {
                    t.put("configure1", "");
                }
                if (config.getShowItem().contains(ResultUtil.CHECKED_FLAG.DEPT)) {
                    t.put("configure2", "checked");
                } else {
                    t.put("configure2", "");
                }

            } else {
                t.put("configure0", "");
                t.put("configure1", "");
                t.put("configure2", "");
            }
        }else {
            t.put("configure0", "");
            t.put("configure1", "");
            t.put("configure2", "");
        }
    }


    /*
     * 处理config初始化的单选框值
     */
    private void handleRadioData(Template t, ConfigInfo configInfo, String cardWid) {
        // 设置卡片高度单选框
        if (null != configInfo.getHeightFlag()) {
            if (ResultUtil.HEIGHT_TYPE.ZSYWZDXZ.equals(configInfo.getHeightFlag())) {
                t.binding("heightFlag1", "checked");
                t.binding("heightFlag2", "");
                t.binding("heightFlag3", "");

                t.binding("hideClass1", "card-" + cardWid + "-hide");
                t.binding("hideClass2", "card-" + cardWid + "-hide");
            } else if (ResultUtil.HEIGHT_TYPE.ZSYYZDXZ.equals(configInfo.getHeightFlag())) {
                t.binding("heightFlag1", "");
                t.binding("heightFlag2", "checked");
                t.binding("heightFlag3", "");

                t.binding("hideClass1", "");
                t.binding("hideClass2", "card-" + cardWid + "-hide");
            } else if (ResultUtil.HEIGHT_TYPE.GDGD.equals(configInfo.getHeightFlag())) {
                t.binding("heightFlag1", "");
                t.binding("heightFlag2", "");
                t.binding("heightFlag3", "checked");

                t.binding("hideClass1", "card-" + cardWid + "-hide");
                t.binding("hideClass2", "");
            } else {
                t.binding("heightFlag1", "");
                t.binding("heightFlag2", "");
                t.binding("heightFlag3", "");

                t.binding("hideClass1", "card-" + cardWid + "-hide");
                t.binding("hideClass2", "card-" + cardWid + "-hide");
            }

        } else {
            t.binding("heightFlag1", "");
            t.binding("heightFlag2", "");
            t.binding("heightFlag3", "");
        }
    }

    public Object sort(CardAjaxRequest request){
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        CollectServiceItemsRequest collectAppRequest = new CollectServiceItemsRequest();
        collectAppRequest.setUserId(user.getWid());
        if(null != request.getParam()){
            // 前端传值，逗号分隔
            String widsStr = request.getParam().get("itemIds");
            List<String> serviceWids = widsStr == null ? Collections.emptyList()
                : Splitter.on(",").trimResults().omitEmptyStrings().splitToList(widsStr);
            collectAppRequest.setItemIds(serviceWids);
        }
        HttpEntity entity = new HttpEntity(collectAppRequest);
        AmpBaseResponse response = null;
        // 获取收藏或取消收藏应用接口地址
        logger.info("调用批量取消服务事项收藏接口,参数=" + JSON.toJSONString(entity));
        try {
            response = RestTemplateUtils.post("/casp-sim/itemManager/batchFavoriteItem", entity, AmpBaseResponse.class).getBody();
            logger.debug("调用批量取消服务事项收藏接口返回结果..." + JSON.toJSONString(response));
        } catch ( Exception e ) {
            logger.error("调用批量取消服务事项收藏接口失败...,返回错误信息" + e);

        }
        if(response==null || !"0".equals(response.getErrcode())){
            throw new BusinessException("批量取消服务事项失败");
        }
        return response;
    }
}
