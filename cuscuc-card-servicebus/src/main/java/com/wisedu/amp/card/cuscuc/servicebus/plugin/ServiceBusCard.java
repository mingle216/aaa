package com.wisedu.amp.card.cuscuc.servicebus.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wisedu.amp.card.cuscuc.servicebus.model.AllClassifyListRes;
import com.wisedu.amp.card.cuscuc.servicebus.model.ClassifyBiz;
import com.wisedu.amp.card.cuscuc.servicebus.model.ClassifyServiceListRes;
import com.wisedu.amp.card.cuscuc.servicebus.model.ServiceBiz;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.CardConfigReq;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.configcomponent.AbstractComponent;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;

@MinosSPI
public class ServiceBusCard extends AbstractCard {

    private final static Logger logger = LogManager.getLogger(ServiceBusCard.class);

    @Override
    public String getCardId() {
        return "CUSCUC_CARD_SERVICEBUS";
    }

    @Override
    public ComponentContainer getConfig(CardConfigReq cardConfigReq) {
        ComponentContainer componentContainer = cardConfigReq.getComponentContainer();

        JSONObject param = new JSONObject();
        param.put("userId", "");
        param.put("source", "console");
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);

        AllClassifyListRes allClassifyListRes = null;
        logger.debug("调用所有分类接口查询接口,参数=" + JSON.toJSONString(httpEntity));
        try {
            allClassifyListRes = RestTemplateUtils.post("/groupService/allClassifyList", httpEntity, AllClassifyListRes.class).getBody();
            logger.debug("调用所有分类接口查询接口成功，返回结果：" + JSON.toJSONString(allClassifyListRes));
        } catch (Exception e) {
            logger.error("调用所有分类接口查询接口失败" , e);
        }

        if(null != allClassifyListRes && null != allClassifyListRes.getData()){
            List<ClassifyBiz> allClassifyListResData = allClassifyListRes.getData();
            List<ClassifyBiz> allClassifyList = allClassifyListResData.stream().filter(classifyBiz -> null != classifyBiz.getClassifyInfoList() && 0 != classifyBiz.getClassifyInfoList().size()).collect(Collectors.toList());
            componentContainer.setData("serviceCarCascader", Global.ComponentParam.DATAS, allClassifyList);
        }
        List<AbstractComponent> components = componentContainer.getComponents();
        AbstractComponent tabPositionComponent = components.stream().filter(e -> "tabPosition".equals(e.getKey()))
                .findFirst().orElse(null);
        if (tabPositionComponent != null) {
            if(tabPositionComponent.getValue() != null){
                componentContainer.setData("tabPosition", Global.ComponentParam.VALUE, Integer.parseInt(String.valueOf(tabPositionComponent.getValue())));
            }
        }

        return componentContainer;
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
                result = this.renderData2(request);
                break;
            case "collectClassify":
                result = this.collectClassify(request);
                break;
            case "collectApp":
                result = this.collectApp(request);
                break;
            case "queryAllClassifyList":
                result = this.queryAllClassifyList(request);
                break;
            case "getMarkNumber":
                result = 0;
                break;
            default:
        }
        return result;
    }

    private JSONObject collectApp(CardAjaxRequest request) {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        if (null == user) {
            logger.debug("用户未登录...");
            return null;
        }

        JSONObject param = new JSONObject();
        param.put("userId", user.getWid());
        param.put("serviceItemId", request.getParam().get("appId"));
        param.put("flag", request.getParam().get("operate"));
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);

        JSONObject result = null;
        logger.debug("调用应用收藏/取消收藏接口,usr=" + "/serviceFavorite/favoriteServiceItem" + "参数=" + JSON.toJSONString(httpEntity));
        try {
            result = RestTemplateUtils.post("/serviceFavorite/favoriteServiceItem", httpEntity, JSONObject.class).getBody();
            logger.debug("调用应用收藏/取消收藏接口返回结果..." + JSON.toJSONString(result));
        } catch (Exception e) {
            logger.error("调用应用收藏/取消收藏接口失败" , e);
        }

        logger.debug("-----Collect Result-----" + result);

        return result;

    }

    private JSONObject collectClassify(CardAjaxRequest request) {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        if (null == user) {
            logger.debug("用户未登录...");
//            user = new UserInfo();
//            user.setWid("cm");
            return null;
        }

        JSONObject param = new JSONObject();
        param.put("userWid", user.getWid());
        param.put("cardConfigWid", request.getCardWid());
        String classifyIds = request.getParam().get("classifyIds");
        String[] idsList = new String[1];
        if(StringUtil.isNotEmpty(classifyIds)){
            idsList = classifyIds.split(",");
        }
        param.put("serviceClassifyIds", idsList);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);

        JSONObject result = null;
        logger.debug("调用二级分类收藏/取消收藏接口,usr=" + "/serviceManager/saveServiceClassifyFavourite" + "参数=" + JSON.toJSONString(httpEntity));
        try {
            result = RestTemplateUtils.post("/serviceManager/saveServiceClassifyFavourite", httpEntity, JSONObject.class).getBody();
            logger.debug("调用二级分类收藏/取消收藏接口返回结果..." + JSON.toJSONString(result));
        } catch (Exception e) {
            logger.error("调用二级分类收藏/取消收藏接口失败" , e);
        }

        logger.debug("-----Collect Result-----" + result);

        return result;

    }


    // 找到父亲是某个id的list
    private List<ClassifyBiz> getParentResultList(List<ClassifyBiz> classList, String id) {
        List<ClassifyBiz> resultList = new ArrayList<>();
        if (null != classList && !CollectionUtils.isEmpty(classList)) {
            //循环判断
            for (int i = 0; i < classList.size(); i++) {
                if (null != resultList && !CollectionUtils.isEmpty(resultList)) {
                    return resultList;
                }
                ClassifyBiz classifyBiz = classList.get(i);
                if (id.equals(classifyBiz.getClassifyWid())) {
                    resultList = classifyBiz.getClassifyInfoList();
                } else if (null != classifyBiz.getClassifyInfoList() && classifyBiz.getClassifyInfoList().size() > 0) {
                    resultList = getParentResultList(classifyBiz.getClassifyInfoList(), id);
                }
            }
        }
        return resultList;
    }

    //找到自己是某个id的list
    private List<ClassifyBiz> getChirdResultList(List<ClassifyBiz> classList, String id) {
        List<ClassifyBiz> resultList = new ArrayList<>();
        if (null != classList && !CollectionUtils.isEmpty(classList)) {
            //循环判断
            for (int i = 0; i < classList.size(); i++) {
                if (null != resultList && !CollectionUtils.isEmpty(resultList)) {
                    return resultList;
                }
                ClassifyBiz classifyBiz = classList.get(i);
                if (id.equals(classifyBiz.getClassifyWid())) {
                    resultList.add(classifyBiz);
                } else if (null != classifyBiz.getClassifyInfoList() && classifyBiz.getClassifyInfoList().size() > 0) {
                    resultList = getChirdResultList(classifyBiz.getClassifyInfoList(), id);
                }
            }
        }
        return resultList;
    }

    private List<ServiceBiz> getServiceBusApp2(String classifyWid, String lang) {

        String userWid = getUserWid();
        JSONObject param = new JSONObject();
        param.put("userId", userWid);
        if((StringUtils.isEmpty(userWid) || "guest".equals(userWid)) && StringUtils.isNotEmpty(lang)){
            param.put("lang", lang);
        }
        param.put("classifyWid", classifyWid);
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if(isMobileDevice){
            // 看移动端是否需要展示pc端服务
            boolean isShowPCService = ApplicationContextUtil.get(CardUtil.class).getIsShowPCService();
            if(!isShowPCService){
                param.put("serviceStation", "1");
            }
        } else{
            param.put("serviceStation", "0");
        }

        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);
        ClassifyServiceListRes classifyServiceListRes = null;
        try {
            classifyServiceListRes = RestTemplateUtils.post("/groupService/classifyServiceList", httpEntity, ClassifyServiceListRes.class).getBody();
            logger.debug("调用服务分类及对应服务接口查询接口成功，返回结果：" + JSON.toJSONString(classifyServiceListRes));
        } catch (Exception e) {
            logger.error("调用服务分类及对应服务接口查询接口失败" , e);
        }

        if (null != classifyServiceListRes && "0".equals(classifyServiceListRes.getErrcode()) && null != classifyServiceListRes.getData()) {
            return classifyServiceListRes.getData();
        }
        return null;
    }

    private String queryAllClassifyList(CardAjaxRequest request) {
        JSONObject param = new JSONObject();
        param.put("userId", "");
        param.put("source", "concole");
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);

        AllClassifyListRes allClassifyListRes = null;
        logger.debug("调用所有分类接口查询接口,参数=" + JSON.toJSONString(httpEntity));
        try {
            allClassifyListRes = RestTemplateUtils.post("/groupService/allClassifyList", httpEntity, AllClassifyListRes.class).getBody();
            logger.debug("调用所有分类接口查询接口成功，返回结果：" + JSON.toJSONString(allClassifyListRes));
        } catch (Exception e) {
            logger.error("调用所有分类接口查询接口失败" , e);
        }

        String sysOrganizeByParentIdJSON = "";
        if (null != allClassifyListRes && null != allClassifyListRes.getData()) {
            sysOrganizeByParentIdJSON = getSysOrganizeByParentIdJSON(allClassifyListRes.getData(), "-1");
        }

        return sysOrganizeByParentIdJSON;
    }

    /**
     * 全部分类的JSON拼接
     *
     * @param
     * @return
     */
    private String getSysOrganizeByParentIdJSON(List<ClassifyBiz> classifyBizList, String id) {
        String sonNodes = "";
        List<ClassifyBiz> list = classifyBizList.stream().filter(item -> item.getParentId().equals(id)
        ).collect(Collectors.toList());
        for (ClassifyBiz classifyBiz : list) {
//            sonNodes += "{ text: '" + StringUtil.xssEncode(classifyBiz.getClassifyName()) + "', id: '" + classifyBiz.getClassifyWid() + "'";
            sonNodes += "{ text: '" + classifyBiz.getClassifyName() + "', id: '" + classifyBiz.getClassifyWid() + "'";
            if (null != classifyBiz.getClassifyInfoList() && classifyBiz.getClassifyInfoList().size() > 0) {
                if (!getSysOrganizeByParentIdJSON(classifyBiz.getClassifyInfoList(), classifyBiz.getClassifyWid()).isEmpty()) {

                    sonNodes += ", nodes: [" + getSysOrganizeByParentIdJSON(classifyBiz.getClassifyInfoList(), classifyBiz.getClassifyWid()) + "] ";

                }
            }
            sonNodes += "},";
        }
        if (!"0".equals(id)) {
            sonNodes = sonNodes.substring(0, sonNodes.length() - 1);
        }
        return sonNodes;
    }

    /**
     * 以下方法为 重构后台配置页面所重写或重构的方法
     */

    /**
     * renderData方法
     * @param request
     * @return
     */
    private Object renderData2(CardAjaxRequest request) {
        Map<String, Object> renderData = new HashMap<>();
        logger.debug("-----Load Service Bus renderData2-----");
        String heightFlag = "0";
        String height = "0";
        String tabPosition = "0";
        String configTypeId = "";

        String configStr = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(),request.getCardId());
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if(StringUtil.isNotEmpty(configStr)){

            //格式化
            String str = StringEscapeUtils.unescapeJava(configStr);
            JSONObject jsonObject = JSONObject.parseObject(str.substring(1, str.length() - 1));
            if(isMobileDevice){
                renderData.put("mobileConfig",JSONObject.parseObject(JSON.parse(configStr).toString()));
            }else {
                heightFlag = jsonObject.getJSONObject("serviceCarHeight").getString("type");
                height = jsonObject.getJSONObject("serviceCarHeight").getString("value");
                if (jsonObject.getInteger("tabPosition") != null) {
                    tabPosition = String.valueOf(jsonObject.getInteger("tabPosition"));
                }
            }
            if(jsonObject.get("columns") != null){
                renderData.put("columns", jsonObject.get("columns"));
            }

            configTypeId = jsonObject.getString("serviceCarCascader");
        }else{
            renderData.put("mobileConfig",new HashMap<String, String>());
        }

        String lang = (!request.getParam().isEmpty() && StringUtils.isNotEmpty(request.getParam().get("lang")))
                ? request.getParam().get("lang") : "";

        if (StringUtil.isNotEmpty(configTypeId)) {
            String typeId = "";
            Map<String, String> param = request.getParam();
            if (param != null) {
                typeId = param.get("typeId");
            }
            // 设置展示数据
            Map<String, Object> map = handleViewData3(configTypeId, typeId, isMobileDevice, request.getCardWid(), lang);
            renderData.put("classifyData", map.get("classifyData"));
            renderData.put("appData", map.get("appData"));
            renderData.put("hasChildClassify", map.get("hasChildClassify"));
            renderData.put("heightFlag", heightFlag);
            renderData.put("tabPosition", tabPosition);
            renderData.put("height", height);
        } else {
            renderData.put("heightFlag", "1");
            renderData.put("height", 300);
            renderData.put("classifyData", null);
            renderData.put("hasChildClassify", false);
            renderData.put("appData", new ArrayList<>());
        }

        renderData.put("isMobileDevice", isMobileDevice);
        return renderData;
    }

    /**
     * 初始化展示数据
     * @param configTypeId
     * @param typeId
     * @param isMobileDevice
     * @return
     */
    private Map<String, Object> handleViewData3(String configTypeId, String typeId, boolean isMobileDevice,
                                                String cardWid, String lang) {
        //获取二级分类，根据头结点去实时查询二级分类
        List<ClassifyBiz> list = querySecondaryClassificationList(isMobileDevice, configTypeId, cardWid, lang);

        //默认进来typeid为第一个,
        // 获取第一个服务分组
        if (StringUtil.isEmpty(typeId) && !CollectionUtils.isEmpty(list)) {
            for (ClassifyBiz classifyBiz : list) {
                if(classifyBiz.getShow()!=null && classifyBiz.getShow()){
                    typeId = classifyBiz.getClassifyWid();
                    break;
                }
            }
        }
        return getViewData(list, typeId, lang);
    }
    private Map<String,Object> getViewData(List<ClassifyBiz> list, String typeId, String lang){
        // 分类集合
        List<LinkedHashMap<String, Object>> classifyList = new ArrayList<>();
        // 所有业务直通车应用集合
        List<LinkedHashMap<String, Object>> appList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<ServiceBiz> serviceBizList2;
        for (ClassifyBiz classifyBiz : list) {

            LinkedHashMap<String, Object> classifyMap = new LinkedHashMap<>();

            classifyMap.put("typeId", classifyBiz.getClassifyWid());
            classifyMap.put("typeName", classifyBiz.getClassifyName());
            classifyMap.put("show", classifyBiz.getShow());

            if (null == classifyBiz.getCount() || classifyBiz.getCount() == 0) {
                classifyMap.put("count", "");
            } else {
                classifyMap.put("count", classifyBiz.getCount());
            }

            classifyList.add(classifyMap);


            if (typeId.equals(classifyBiz.getClassifyWid())) {
                // 根据classifyWId获取所有业务直通车应用
                serviceBizList2 = getServiceBusApp2(classifyBiz.getClassifyWid(), lang);
            } else {
                serviceBizList2 = null;
            }

            if (!CollectionUtils.isEmpty(serviceBizList2)) {
                for (ServiceBiz serviceBiz : serviceBizList2) {
                    LinkedHashMap<String, Object> appMap = new LinkedHashMap<>();
                    appMap.put("typeId", classifyMap.get("typeId"));
                    appMap.put("appId", serviceBiz.getServiceWid());
                    appMap.put("serviceId", serviceBiz.getServiceId());
                    appMap.put("appName", serviceBiz.getServiceName());
                    appMap.put("serviceName", serviceBiz.getServiceName());
                    appMap.put("iconLink", serviceBiz.getIconUrl());
                    appMap.put("appIcon", serviceBiz.getIconUrl());
                    appMap.put("appFavorite", serviceBiz.getFavorite());
                    appMap.put("mobileAccessUrl", serviceBiz.getMobileAccessUrl());
                    appMap.put("pcAccessUrl", serviceBiz.getPcAccessUrl());
                    appMap.put("permission", serviceBiz.getPermission());
                    appMap.put("mobileIconLink",serviceBiz.getMobileIconLink());
                    appMap.put("serviceStation",serviceBiz.getServiceStation());

                    appList.add(appMap);
                }
            }
        }
        map.put("classifyData",classifyList);
        map.put("hasChildClassify",classifyList.size() > 1);
        map.put("appData",appList);
        return map;
    }

    /**
     * 获取二级分类，根据头结点去实时查询二级分类
     * @param isMobileDevice
     */
    private List<ClassifyBiz> querySecondaryClassificationList(boolean isMobileDevice, String configTypeId,
                                                               String cardWid, String lang){
        //获取用户wid
        String userWid = getUserWid();//"cm";
        JSONObject param = new JSONObject();
        param.put("source", "portal");
        param.put("userId", userWid);
        param.put("cardWid", cardWid);
        if((StringUtils.isEmpty(userWid) || "guest".equals(userWid)) && StringUtils.isNotEmpty(lang)){
            param.put("lang", lang);
        }
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);
        param.put("serviceStation", isMobileDevice ? "1" : "0");

        AllClassifyListRes  allClassifyListRes = null;
        try {
            allClassifyListRes = RestTemplateUtils.post("/groupService/allClassifyList", httpEntity, AllClassifyListRes.class).getBody();
            logger.debug("调用所有分类接口查询接口成功，返回结果：" + JSON.toJSONString(allClassifyListRes));
        } catch (Exception e) {
            logger.error("调用所有分类接口查询接口失败" , e);
        }
        //实时找到父id是配置的id的list
        List<ClassifyBiz> list = new ArrayList<>();
        if (StringUtil.isNotEmpty(configTypeId)) {
            list = getParentResultList(allClassifyListRes.getData(), configTypeId);
            if (CollectionUtils.isEmpty(list)) {
                //找不到父id的list则实时在自己的记录
                list = getChirdResultList(allClassifyListRes.getData(), configTypeId);
            }
        }

        if(list!=null) {
            for (ClassifyBiz classifyInfo : list) {
                classifyInfo.setShow(true);
            }
        }

        List<String> classifyWids = allClassifyListRes.getFavClassifyWids();
        if(!CollectionUtils.isEmpty(classifyWids)){
            for (ClassifyBiz classifyInfo : list) {
                if(classifyWids.contains(classifyInfo.getClassifyWid())){
                    classifyInfo.setShow(false);
                }
            }
        }

        return list;
    }

    private String getUserWid(){
        String userWid = "";
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            if(null != user){
                userWid = user.getWid();
            }
        } catch (Exception e) {
            logger.debug("用户未登录，游客。。。", e);
        }
        return userWid;
    }

    private UserInfo getUser(){
        UserInfo user = null;
        try {
            user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        } catch (Exception e) {
            logger.debug("用户未登录，游客。。。", e);
        }
        return user;
    }
}
