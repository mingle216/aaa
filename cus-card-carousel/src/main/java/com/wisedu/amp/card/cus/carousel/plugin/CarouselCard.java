package com.wisedu.amp.card.cus.carousel.plugin;

import com.alibaba.fastjson.JSON;
import com.wisedu.minos.casp.portal.common.baseservice.CaspSafetyService;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author dali
 */
@MinosSPI
public class CarouselCard extends AbstractCard {

    private final static Logger logger = LogManager.getLogger(CarouselCard.class);

    /**
     * 卡片高度类型：1自适应 2.自适应（高度限制） 3固定高度
     */
    /*private final static String CARD_HEIGHT_TYPE = "CARD_HEIGHT_TYPE";
    *//**
     * 是否显示标题（默认1） ： 1显示 0不显示
     *//*
    private final static String CARD_SHOW_TITLE = "CARD_SHOW_TITLE";
    *//**
     * 是否显示摘要（默认1） ： 1显示 0不显示
     *//*
    private final static String CARD_SHOW_SUMMARY = "CARD_SHOW_SUMMARY";
    *//**
     * 自适应最大高度
     *//*
    private final static String FIXED_HEIGHT = "FIXED_HEIGHT";

    *//**
     * 固定高度
     *//*
    private final static String MAX_HEIGHT = "MAX_HEIGHT";


    private final static String CAROUSEL_LIST = "carouselList";*/

    public static final String BRACKETS = "[]";

    /**
     * 轮播图的配置项
     */
    private final static String ID = "ID";
    private final static String SORT_VALUE = "SORT_VALUE";
    private final static String TITLE = "TITLE";
    private final static String SUMMARY = "SUMMARY";
    private final static String URL = "URL";
    private final static String PROTOCOL = "PROTOCOL";
    private final static String IMAGE = "IMAGE";
    private final static String DEFAULTACTIVE = "defaultActive";

    private final static String HTTP = "http://";
    /*private final static String HTTPS = "https://";*/

    /**
     * 默认值：1  >> 、自适应高度、排序值
     */
   /* private final static String DEFAULT_CHECKED_NUM = "1";*/
    /**
     * 默认值：0 不显示标题、不显示摘要
     */
    /*private final static String DEFAULT_UNCHECKED_NUM = "0";
    private static final String DEFAULT_HEIGHT = "300";*/

    @Override
    public String getCardId() {
        return "CUS_CARD_CAROUSEL";
    }

    @Override
    public void destroy() {
        logger.debug("card[CUS_CARD_CAROUSEL] destroyed ...");
    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            /*case "render":
                result = this.render(request);
                break;*/
            /*case "config":
                result = this.config(request);
                break;*/
            case "addItem":
                result = this.addItem(request);
                break;
            case "getMarkNumber":
                result = 0;
                break;
            case "renderData":
                result = this.renderData(request);
                break;
        }

        return result;
    }

    private Map<String,Object> renderData(CardAjaxRequest request){
        HashMap<String, Object> result = new HashMap<>();
        //查询配置信息
        String configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(),request.getCardId());
        result.put("config",configInfo);
        return result;
    }

    /**
     * 加载轮播图卡片
     *
     * @param request
     * @return
     */
    /*private String render(CardAjaxRequest request) {
        logger.debug("render carousel view ..");
        Template t = groupTemplate.getTemplate("/" + this.getCardId() + "/view.html");

        //先查询传参，如果传参没有则需要查询库，在没有则初始化默认
        Map<String, Object> configMap = getConfigInfo(request);

        List<Map<String, Object>> usedCarouselList = Lists.newArrayList();

        String carouselListStr = StringUtil.getString(configMap.get(CAROUSEL_LIST));
        if (StringUtil.isNotEmpty(carouselListStr)) {
            List<Map<String, Object>> carouselList = toListMap(carouselListStr);
            for (Map<String, Object> stringObjectMap : carouselList) {
                String url = StringUtil.getString(stringObjectMap.get("URL"));
                if (url.equalsIgnoreCase(HTTP) || url.equalsIgnoreCase(HTTPS)) {
                    stringObjectMap.put("URL", "");
                }
                if (StringUtil.isNotEmpty(StringUtil.getString(stringObjectMap.get("IMAGE")))) {
                    usedCarouselList.add(stringObjectMap);
                }
            }
        }

        //默认展示第一个
        if (usedCarouselList.size() > 0) {
            usedCarouselList.get(0).put("defaultActive", "active");
        }
        //获取配置信息：
        String cardHeightType = StringUtil.getString(configMap.get(CARD_HEIGHT_TYPE));
        String cardShowTitle = StringUtil.getString(configMap.get(CARD_SHOW_TITLE));
        String cardShowSummary = StringUtil.getString(configMap.get(CARD_SHOW_SUMMARY));
        if (StringUtil.isEmpty(cardHeightType)) {
            cardHeightType = "1";
            configMap.put(CARD_HEIGHT_TYPE, cardHeightType);
        }
        if (StringUtil.isEmpty(cardShowTitle)) {
            cardShowTitle = "1";
            configMap.put(CARD_SHOW_TITLE, cardShowTitle);
        }
        if (StringUtil.isEmpty(cardShowSummary)) {
            cardShowSummary = "1";
            configMap.put(CARD_SHOW_SUMMARY, cardShowSummary);
        }
        //卡片高度设置（默认：自适应高度 目前1200:200）
        String style = "height:calc(100%);min-height:300px;";
        if ("2".equals(cardHeightType)) {
            //最大 高度
            String height = StringUtil.getString(configMap.get(FIXED_HEIGHT));
            if(Integer.parseInt(height) < 300){
                height = "300";
            }
            style = "height:calc(100%);min-height:300px;max-height:" + height + "px;";
        } else if ("3".equals(cardHeightType)) {
            //固定高度
            String height = StringUtil.getString(configMap.get(MAX_HEIGHT));
            if(Integer.parseInt(height) < 300){
                height = "300";
            }
            style = "min-height:300px;height:" + height + "px;";
        }
        t.binding(CARD_SHOW_TITLE, cardShowTitle.equals(DEFAULT_CHECKED_NUM));
        t.binding(CARD_SHOW_SUMMARY, cardShowSummary.equals(DEFAULT_CHECKED_NUM));

        t.binding("config", configMap);
        t.binding("cardWid", request.getCardWid());
        t.binding("cardId", request.getCardId());
        t.binding(CAROUSEL_LIST, usedCarouselList);
        t.binding("style", style);

        return t.render();
    }*/

    /**
     * 加载轮播图设置页面
     *
     * @param request
     * @return
     */
    /*private String config(CardAjaxRequest request) {
        logger.debug("render carousel config ..");

        Template t = groupTemplate.getTemplate("/" + this.getCardId() + "/config.html");

        //先查询传参，如果传参没有则需要查询库，再没有则初始化默认
        Map<String, Object> configMap = getConfigInfo(request);

        String carouselListStr = StringUtil.getString(configMap.get(CAROUSEL_LIST));
        List<Map<String, Object>> carouselList = new ArrayList<>();
        if (StringUtil.isNotEmpty(carouselListStr)) {
            carouselList = toListMap(carouselListStr);
        }

        if (carouselList.size() < 1) {
            //如果为空，默认添加一项
            Map<String, Object> carouselMap = new HashMap<>();
            carouselMap.put(ID, DEFAULT_CHECKED_NUM);
            carouselMap.put(SORT_VALUE, DEFAULT_CHECKED_NUM);
            carouselMap.put(TITLE, "");
            carouselMap.put(SUMMARY, "");
            carouselMap.put(PROTOCOL, HTTP);
            carouselMap.put(URL, "");
            carouselMap.put(IMAGE, "");
            carouselList.add(carouselMap);
            //初始化配置
            configMap.putIfAbsent(FIXED_HEIGHT, DEFAULT_HEIGHT);
            configMap.putIfAbsent(MAX_HEIGHT, DEFAULT_HEIGHT);
            configMap.putIfAbsent(CARD_HEIGHT_TYPE, DEFAULT_CHECKED_NUM);
            configMap.putIfAbsent(CARD_SHOW_TITLE, DEFAULT_UNCHECKED_NUM);
            configMap.putIfAbsent(CARD_SHOW_SUMMARY, DEFAULT_UNCHECKED_NUM);
        } else {
            for (Map<String, Object> carouselMap : carouselList) {
                String url = StringUtil.getString(carouselMap.get(URL));
                if (StringUtil.isNotEmpty(url)) {
                    if (url.startsWith(HTTPS)) {
                        url = url.replaceFirst(HTTPS, "");
                        carouselMap.put(PROTOCOL, HTTPS);
                    } else {
                        url = url.replaceFirst(HTTP, "");
                        carouselMap.put(PROTOCOL, HTTP);
                    }
                } else {
                    carouselMap.put(PROTOCOL, HTTP);
                }
                carouselMap.put(URL, url);
            }
        }

        configMap.put(CAROUSEL_LIST, carouselList);

        t.binding("config", configMap);
        t.binding("sum", carouselList.size());
        t.binding("cardWid", request.getCardWid());
        t.binding("cardId", request.getCardId());

        return t.render();
    }*/

    /**
     * 添加一个轮播图
     *
     * @param request
     * @return
     */
    private String addItem(CardAjaxRequest request) {
        logger.debug("carousel addItem ..");

        Template t = getGroupTemplate().getTemplate("/" + this.getCardId() + "/item.html");

        String id = request.getParam().get("ID");
        Map<String, Object> carouselMap = new HashMap<>();
        carouselMap.put(ID, id);
        carouselMap.put(SORT_VALUE, 6);
        carouselMap.put(TITLE, "");
        carouselMap.put(SUMMARY, "");
        carouselMap.put(PROTOCOL, HTTP);
        carouselMap.put(URL, "");
        carouselMap.put(IMAGE, "");
        carouselMap.put(DEFAULTACTIVE, "");

        t.binding("item", carouselMap);
        t.binding("cardWid", request.getCardWid());
        t.binding("cardId", request.getCardId());
        return t.render();
    }

    private static List<Map<String, Object>> toListMap(String json) {
        List<Object> list = JSON.parseArray(json);
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        for (Object object : list) {
            Map<String, Object> ret = (Map<String, Object>) object;
            lists.add(ret);
        }
        return lists;
    }

    private static Map<String, Object> toMap(String json) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotEmpty(json) && !BRACKETS.equals(json)) {
            map = JSON.parseObject(json, HashMap.class);
        }
        return map;
    }

    //配置信息
    private Map<String, Object> getConfigInfo(CardAjaxRequest request){
        //先查询传参，如果传参没有则需要查询库，在没有则初始化默认
        String configInfo;
        if (null != request.getParam() && null != request.getParam().get("config")) {
            configInfo = request.getParam().get("config");
        } else {
            configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(),request.getCardId());
        }
        logger.debug("卡片配置信息:" + configInfo);
        if (StringUtils.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
        }
        return toMap(configInfo);
    }

}
