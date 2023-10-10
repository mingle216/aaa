package com.wisedu.amp.card.cus.hotApp.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wisedu.amp.card.cus.hotApp.model.*;
import com.wisedu.amp.card.cus.hotApp.util.ResultUtil;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
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
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Template;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther:hlchen02
 * @Date:2020/8/17
 * @Description:com.wisedu.amp.card.cus.hotApp.plugin
 * @Version:1.0
 */
@MinosSPI
public class HotAppCard extends AbstractCard {
    private final static Logger logger = LogManager.getLogger(HotAppCard.class);

    @Override
    public String getCardId () {
        return "CUS_CARD_HOTAPP";
    }

    @Override
    public void destroy () {
        logger.debug("destroy..");
    }

    @Override
    public Object execDispatcher (CardAjaxRequest request) {
        Object result = null;
        switch ( request.getMethod() ) {
            case "renderData":
                result = this.renderData(request);
                break;
            case "config":
                result = this.config(request);
                break;
            case "collectApp":
                result = this.collectApp(request);
                break;
            case "getMarkNumber":
                result = 0;
                break;
        }
        return result;
    }

    private Object renderData(CardAjaxRequest request) {
        Map<String,Object> data = new HashMap<>();
        logger.debug("doAjax...HotAppCard...");
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();


        //查询配置信息
        String configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(),request.getCardId());
        if ( org.apache.commons.lang.StringUtils.isNotEmpty(configInfo) ) {
            configInfo = JSON.parse(configInfo).toString();
        }
        Config config = JSONObject.parseObject(configInfo, Config.class);
        if ( null == config ) {
            config = new Config();
            ContainerType containerType = new ContainerType();
            containerType.setType(0);
            containerType.setValue(500);
            config.setContainerType(containerType);
            config.setRows(8);
            config.setColumns(4);
            config.setHotServiceTimeRadio(0);
        }
        data.put("config", config);
        if ( null == user || StringUtil.isEmpty(user.getWid()) ) {
          user = new UserInfo();
          user.setWid("");
        }

        String lang = (!request.getParam().isEmpty() && StringUtils.isNotEmpty(request.getParam().get("lang")))
                ? request.getParam().get("lang") : "";

        HotAppRequest hotAppRequest = getHotAppRequest(user, config, lang);
        HttpEntity entity = new HttpEntity(hotAppRequest);

        HotAppRespose hotAppRespose = null;
        //获取热门应用查询接口地址
        logger.debug("调用热门应用查询接口,参数=" + JSON.toJSONString(entity));
        try {
            hotAppRespose = RestTemplateUtils.post("/groupService/hotServiceList", entity,
                    HotAppRespose.class).getBody();
            logger.debug("调用热门应用查询接口成功，返回结果：" + JSON.toJSONString(hotAppRespose));
        } catch ( Exception e ) {
            logger.error("调用热门应用查询接口失败，原因：" + e);
        }

        if ( null != hotAppRespose && null != hotAppRespose.getData()) {
            // 权限处理
            List<HotAppInfo> hotAppData = hotAppRespose.getData();
            data.put("appList", hotAppData);
        }
        data.put("config", config);
        data.put("isMobileDevice", CommonUtil.isMobileDevice());
        return data;
    }

    private HotAppRequest getHotAppRequest(UserInfo user, Config config, String lang) {
        HotAppRequest hotAppRequest = new HotAppRequest();

        hotAppRequest.setUserId(user.getWid());
        if(StringUtils.isEmpty(user.getWid()) && StringUtils.isNotEmpty(lang)){
            hotAppRequest.setLang(lang);
        }

        if ( config.getRows()!=null ) {
            hotAppRequest.setAmount(config.getRows());
        } else {
            hotAppRequest.setAmount(8);
        }
        //设置统计类型 1：7天,2: 近一个月,3:近6个月，4:近一年
        if(config.getHotServiceTimeRadio() != null){
            hotAppRequest.setTimeType(String.valueOf(config.getHotServiceTimeRadio() + 1));
        } else {
            hotAppRequest.setTimeType("1");
        }
        //判断是PC OR MOBILE
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if(isMobileDevice){
            // 看移动端是否需要展示pc端服务
            boolean isShowPCService = ApplicationContextUtil.get(CardUtil.class).getIsShowPCService();
            if(!isShowPCService){
                hotAppRequest.setServiceStation("1");
            }
        } else{
            hotAppRequest.setServiceStation("0");
        }
        return hotAppRequest;
    }


    private AmpBaseResponse collectApp (CardAjaxRequest request) {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if(null == user){
            throw new NoLoginException();
        }
        CollectAppRequest collectAppRequest = new CollectAppRequest();
        collectAppRequest.setUserId(user.getWid());
        collectAppRequest.setServiceItemId(request.getParam().get("appId"));
        collectAppRequest.setFlag(request.getParam().get("operate"));
        HttpEntity entity = new HttpEntity(collectAppRequest);
        AmpBaseResponse response = null;
        //获取收藏或取消收藏应用接口地址
        logger.debug("调用应用收藏/取消收藏接口,参数=" + JSON.toJSONString(entity));
        try {
            response = RestTemplateUtils.post("/serviceFavorite/favoriteServiceItem", entity, AppInfoResponse.class).getBody();
            logger.debug("调用应用收藏/取消收藏接口返回结果..." + JSON.toJSONString(response));
        } catch ( Exception e ) {
            logger.error("调用应用收藏/取消收藏接口失败...,返回错误信息" + e);
        }

        return response;
    }

    private String config (CardAjaxRequest request) {
        logger.debug("doConfig...HotAppCard...");

        Template t = getGroupTemplate().getTemplate("/" + this.getCardId() + "/hotConfig.html");
        //查询配置信息
        //先查询传参，如果传参没有则需要查询库，在没有则初始化默认
        String configInfo;
        if ( null != request.getParam() && null != request.getParam().get("config") ) {
            configInfo = request.getParam().get("config");
        } else {
            configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(),request.getCardId());
            if ( org.apache.commons.lang.StringUtils.isNotEmpty(configInfo) ) {
                configInfo = JSON.parse(configInfo).toString();
            }
        }
        ConfigInfo config = JSONObject.parseObject(configInfo, ConfigInfo.class);
        if ( null == config ) {
            config = new ConfigInfo();
        }
        //设置单选框
        handleRadioData(t, config, request.getCardWid());
        //设置卡片高度
        t.binding("num", config.getNum());
        t.binding("height", config.getHeight());
        t.binding("cardId", getCardId());
        t.binding("cardWid", request.getCardWid());
        return t.render();
    }

    /*
     *处理config初始化的单选框值
     */
    private void handleRadioData(Template t, ConfigInfo configInfo, String cardWid) {
        //设置卡片高度单选框
        if (null != configInfo.getHeightFlag()) {
            if (ResultUtil.HeightType.NO_MAXIMUM_LIMIT.getIndex().equals(configInfo.getHeightFlag())) {
                t.binding(ResultUtil.HeightType.NO_MAXIMUM_LIMIT.getName(), ResultUtil.CHECKED);
                t.binding(ResultUtil.HeightType.MAXIMUM_LIMIT.getName(), ResultUtil.EMPTY);
                t.binding(ResultUtil.HeightType.FIXED_HEIGHT.getName(), ResultUtil.EMPTY);

                t.binding(ResultUtil.HeightInputShowType.MAXIMUM_LIMIT.getName(), "card-" + cardWid + "-hide");
                t.binding(ResultUtil.HeightInputShowType.FIXED_HEIGHT.getName(), "card-" + cardWid + "-hide");
            } else if (ResultUtil.HeightType.MAXIMUM_LIMIT.getIndex().equals(configInfo.getHeightFlag())) {
                t.binding(ResultUtil.HeightType.NO_MAXIMUM_LIMIT.getName(), ResultUtil.EMPTY);
                t.binding(ResultUtil.HeightType.MAXIMUM_LIMIT.getName(), ResultUtil.CHECKED);
                t.binding(ResultUtil.HeightType.FIXED_HEIGHT.getName(), ResultUtil.EMPTY);

                t.binding(ResultUtil.HeightInputShowType.MAXIMUM_LIMIT.getName(), ResultUtil.EMPTY);
                t.binding(ResultUtil.HeightInputShowType.FIXED_HEIGHT.getName(), "card-" + cardWid + "-hide");
            } else if (ResultUtil.HeightType.FIXED_HEIGHT.getIndex().equals(configInfo.getHeightFlag())) {
                t.binding(ResultUtil.HeightType.NO_MAXIMUM_LIMIT.getName(), ResultUtil.EMPTY);
                t.binding(ResultUtil.HeightType.MAXIMUM_LIMIT.getName(), ResultUtil.EMPTY);
                t.binding(ResultUtil.HeightType.FIXED_HEIGHT.getName(), ResultUtil.CHECKED);

                t.binding(ResultUtil.HeightInputShowType.MAXIMUM_LIMIT.getName(), "card-" + cardWid + "-hide");
                t.binding(ResultUtil.HeightInputShowType.FIXED_HEIGHT.getName(), ResultUtil.EMPTY);
            } else {
                t.binding(ResultUtil.HeightType.NO_MAXIMUM_LIMIT.getName(), ResultUtil.EMPTY);
                t.binding(ResultUtil.HeightType.MAXIMUM_LIMIT.getName(), ResultUtil.EMPTY);
                t.binding(ResultUtil.HeightType.FIXED_HEIGHT.getName(), ResultUtil.EMPTY);

                t.binding(ResultUtil.HeightInputShowType.MAXIMUM_LIMIT.getName(), "card-" + cardWid + "-hide");
                t.binding(ResultUtil.HeightInputShowType.FIXED_HEIGHT.getName(), "card-" + cardWid + "-hide");
            }

        } else {
            t.binding(ResultUtil.HeightType.NO_MAXIMUM_LIMIT.getName(), ResultUtil.EMPTY);
            t.binding(ResultUtil.HeightType.MAXIMUM_LIMIT.getName(), ResultUtil.EMPTY);
            t.binding(ResultUtil.HeightType.FIXED_HEIGHT.getName(), ResultUtil.EMPTY);

            t.binding(ResultUtil.HeightInputShowType.MAXIMUM_LIMIT.getName(), "card-" + cardWid + "-hide");
            t.binding(ResultUtil.HeightInputShowType.FIXED_HEIGHT.getName(), "card-" + cardWid + "-hide");
        }
    }

}
