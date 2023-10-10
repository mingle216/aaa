package com.wisedu.amp.card.sys.mygorydetail.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.wisedu.amp.card.sys.mygorydetail.model.*;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.CardConfigReq;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Template;
import org.springframework.http.HttpEntity;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author kaisir
 */
@MinosSPI
public class ServiceItemCategoryCardDetail extends AbstractCard {

    private final static Logger logger = LogManager.getLogger(ServiceItemCategoryCardDetail.class);
    public static final String SUCCESS_CODE = "0";

    @Override
    public String getCardId() {
        return "CUS_CARD_MYGORYDETAIL";
    }

    @Override
    public ComponentContainer getConfig(CardConfigReq cardConfigReq) {
        ComponentContainer componentContainer = cardConfigReq.getComponentContainer();
        String configInfo = ApplicationContextUtil.get(CardUtil.class).getConsoleCardConfigByCardWid(cardConfigReq.getCardWid(), cardConfigReq.getCardId(), cardConfigReq.getPlatformType());
        if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
        }
        ConfigInfo config = (ConfigInfo) JSONObject.parseObject(configInfo, ConfigInfo.class);
        if (cardConfigReq.getPlatformType() == 0) {
            List<CheckBoxModel> cateList = getCategoryList();
            List<String> list = new ArrayList<>();
            list.add("dept-subject");
            componentContainer.setData("tabConfigure", Global.ComponentParam.DATAS, JSONArray.parseArray(JSON.toJSONString(cateList)));
            componentContainer.setData("tabConfigure", Global.ComponentParam.DEFAULT_VALUE,
                    JSONArray.parseArray(JSON.toJSONString(list)));
            if (config == null || ArrayUtils.getLength(config.getTabConfigure()) == 0) {
                componentContainer.setData("tabConfigure", Global.ComponentParam.VALUE,
                        JSONArray.parseArray(JSON.toJSONString(list)));
            } else {
                componentContainer.setData("tabConfigure", Global.ComponentParam.VALUE,
                        JSONArray.parseArray(JSON.toJSONString(config.getTabConfigure())));
            }

        }
        if (cardConfigReq.getPlatformType() == 1) {
            List<String> defaultColumn = new ArrayList<>();
//            defaultColumn.add("duty_dept_id");
            List<CheckBoxModel> columnList = getItemShowColumn(Global.DEFAULT_LANGUAGE);
            componentContainer.setData("displayInfo", Global.ComponentParam.DATAS, JSONArray.parseArray(JSON.toJSONString(columnList)));
            componentContainer.setData("displayInfo", Global.ComponentParam.DEFAULT_VALUE,
                    JSONArray.parseArray(JSON.toJSONString(defaultColumn)));
            if (config != null && ArrayUtils.getLength(config.getDisplayInfo()) > 0) {
                componentContainer.setData("displayInfo", Global.ComponentParam.VALUE,
                        JSONArray.parseArray(JSON.toJSONString(config.getDisplayInfo())));
            } else {
                componentContainer.setData("displayInfo", Global.ComponentParam.VALUE,
                        JSONArray.parseArray(JSON.toJSONString(columnList)));
            }
        }

        return componentContainer;
    }

    private List<CheckBoxModel> getItemShowColumn(String lang) {
        List<CheckBoxModel> list = new ArrayList<>();
        ServiceItemCategoryRequest serviceitemcategoryrequest = new ServiceItemCategoryRequest();
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if(null!=currentUser){
            serviceitemcategoryrequest.setUserId(currentUser.getWid());
            serviceitemcategoryrequest.setLang(currentUser.getPreferredLanguage());
        }else{
            serviceitemcategoryrequest.setUserId("guest");
            serviceitemcategoryrequest.setLang(lang);
        }
        HttpEntity entity = new HttpEntity(serviceitemcategoryrequest);
        ServiceItemCategoryResponse response = RestTemplateUtils
                .post("/casp-sim/itemManager/getItemShowColumn", entity, ServiceItemCategoryResponse.class)
                .getBody();
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) response.getData();
        for (Map<String, Object> result : resultList) {
            CheckBoxModel checkBoxModel = new CheckBoxModel();
            checkBoxModel.setLabel(result.get("categoryName").toString());
            checkBoxModel.setValue(result.get("categoryWid").toString());
            list.add(checkBoxModel);
        }
        return list;
    }

    private List<CheckBoxModel> getCategoryList() {
        List<CheckBoxModel> list = new ArrayList<>();
        ServiceItemCategoryRequest serviceitemcategoryrequest = new ServiceItemCategoryRequest();
        serviceitemcategoryrequest.setUserId("guest");
        HttpEntity entity = new HttpEntity(serviceitemcategoryrequest);
        ServiceItemCategoryResponse response = RestTemplateUtils
                .post("/casp-sim/itemManager/getOneCategoryList", entity, ServiceItemCategoryResponse.class)
                .getBody();
        if(null!=response&&"0".equals(response.getErrcode())&&null!=response.getData()
                &&CollectionUtils.isNotEmpty((List<Map<String, Object>>)response.getData())){
            List<Map<String, Object>> resultList = (List<Map<String, Object>>) response.getData();
            for (Map<String, Object> result : resultList) {
                CheckBoxModel checkBoxModel = new CheckBoxModel();
                if (!"按部门分类".equals(result.get("categoryName"))) {
                    checkBoxModel.setLabel(String.format("按%s分类", result.get("categoryName")));
                } else {
                    checkBoxModel.setLabel(result.get("categoryName").toString());
                }
                checkBoxModel.setValue(result.get("categoryWid").toString());
                list.add(checkBoxModel);
            }
        }
        return list;
    }

    @Override
    public void destroy() {
        logger.debug("destroy..");
    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "render":
                result = this.view(request);
                break;
            case "getOneCategoryList":
                result = this.getOneCategoryList(request);
                break;
            case "allCateGoryList":
                result = this.allCateGoryList(request);
                break;
            case "queryItemByCategoryList":
                result = this.queryItemByCategoryList(request);
                break;
            case "queryServiceInfo":
                result = this.queryServiceInfo(request);
                break;
            case "checkUserServiceGrant":
                result = this.checkUserServiceGrant(request);
                break;
        }

        return result;
    }

    public CheckUserServiceGrantResp checkUserServiceGrant(CardAjaxRequest request) {
        CheckUserServiceGrantResp response = null;
        String userId = getCurrentUserId();
        String serviceWid = request.getParam().get("serviceWid");
        if (StringUtil.isEmpty(serviceWid)) {
            logger.error("serviceWid不能为空");
            response.setErrcode("500");
            response.setErrmsg("serviceWid不能为空");
            return response;
        }
        CheckUserServiceGrantReq req = new CheckUserServiceGrantReq();
        req.setUserId(userId);
        req.setServiceWid(serviceWid);
        HttpEntity entity = new HttpEntity(req);
        try {
            response = RestTemplateUtils.post("/service/checkUserServiceGrant", entity, CheckUserServiceGrantResp.class).getBody();
            logger.debug("调用checkUserServiceGrant返回结果..." + JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("调用checkUserServiceGrant接口失败...,返回错误信息" + e);
        }
        return response;
    }

    /**
     * 获取当前用户ID
     *
     * @return
     */
    public String getCurrentUserId() {
        String userId;
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            userId = user.getWid();
        } catch (Exception e) {
            logger.error("获取用户信息异常：" + e);
            userId = "";
        }
        return userId;
    }

    /**
     * 获取一级分类
     *
     * @return
     */
    private ServiceItemCategoryResponse getOneCategoryList(CardAjaxRequest request) {
        ServiceItemCategoryRequest serviceitemcategoryrequest = new ServiceItemCategoryRequest();
        ConfigInfo config = getConfig(request.getCardWid(), request.getCardId());
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if (user == null) {
            serviceitemcategoryrequest.setUserId("guest");
            serviceitemcategoryrequest.setIsRelate(config.getNotLogin());
            //加遊客狀態前台傳入的瀏覽器語言參數
            if(!request.getParam().isEmpty() && !StringUtils.isEmpty(request.getParam().get("lang"))){
                serviceitemcategoryrequest.setLang(request.getParam().get("lang"));
            }
        } else {
            serviceitemcategoryrequest.setUserId(user.getWid());
            serviceitemcategoryrequest.setIsRelate(config.getLogined());
        }
        HttpEntity entity = new HttpEntity(serviceitemcategoryrequest);
        ServiceItemCategoryResponse response = RestTemplateUtils
                .post("/casp-sim/itemManager/getOneCategoryList", entity, ServiceItemCategoryResponse.class)
                .getBody();
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) response.getData();
        List<Map<String, Object>> finalList = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < resultList.size(); i++) {
            for (int j = 0; j < config.getTabConfigure().length; j++) {
                if (config.getTabConfigure()[j].equals(resultList.get(i).get("categoryWid"))) {
                    finalList.add(resultList.get(i));
                }
            }
        }
        for (Map<String, Object> map : finalList) {
            if (!"按部门分类".equals(map.get("categoryName"))) {
                map.put("categoryName", "按" + map.get("categoryName") + "分类");
            }
        }
        if (finalList.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("categoryWid", "dept-subject");
            map.put("categoryName", "按部门分类");
            finalList.add(map);
        }
        response.setData(finalList);
        return response;
    }

    private ServiceItemCategoryResponse getTabCategoryList(CardAjaxRequest request) {
        ServiceItemCategoryRequest serviceitemcategoryrequest = new ServiceItemCategoryRequest();
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        serviceitemcategoryrequest.setUserId(user == null ? "guest" : user.getWid());
        HttpEntity entity = new HttpEntity(serviceitemcategoryrequest);
        ServiceItemCategoryResponse response = RestTemplateUtils
                .post("/casp-sim/itemManager/getOneCategoryList", entity, ServiceItemCategoryResponse.class)
                .getBody();
        return response;
    }

    /**
     * 所有服务分类及一二级主题
     *
     * @return
     */
    private ServiceItemCategoryResponse allCateGoryList(CardAjaxRequest request) {
        ConfigInfo config = getConfigInfo(request);
        ServiceItemCategoryRequest serviceitemcategoryrequest = new ServiceItemCategoryRequest();
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        String roleWid = request.getParam().get("roleWid");
        String oneCategoryWid = request.getParam().get("oneCategoryWid");
        String searchValue = StringUtil.trim(request.getParam().get("searchValue"));
        if (user == null) {
            serviceitemcategoryrequest.setUserId("guest");
            serviceitemcategoryrequest.setIsRelate(config.getNotLogin());
            //加遊客狀態前台傳入的瀏覽器語言參數
            if(!request.getParam().isEmpty() && !StringUtils.isEmpty(request.getParam().get("lang"))){
                serviceitemcategoryrequest.setLang(request.getParam().get("lang"));
            }
        } else {
            serviceitemcategoryrequest.setUserId(user.getWid());
            serviceitemcategoryrequest.setIsRelate(config.getLogined());
            serviceitemcategoryrequest.setLang(user.getPreferredLanguage());
        }
        serviceitemcategoryrequest.setRoleWid(roleWid);
        HttpEntity entity = new HttpEntity(serviceitemcategoryrequest);
        ServiceItemCategoryResponse response = RestTemplateUtils
                .post("/casp-sim/itemManager/allCateGoryList", entity, ServiceItemCategoryResponse.class)
                .getBody();
        if (response.getData() != null) {
            List<Map<String, Object>> resultList = (List<Map<String, Object>>) response.getData();
            if (CollectionUtils.isNotEmpty(resultList)) {
                for (Map map : resultList) {
                    if (oneCategoryWid.equals(map.get("categoryWid"))) {
                        response.setData(map);
                    }
                }
                if (StringUtil.isNotBlank(searchValue)) {
                    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                    Map<String, Object> searchResult = (Map<String, Object>) filterEmptyCateGory(response.getData());
                    if (searchResult != null) {
                        List<Map<String, Object>> childernList = (List<Map<String, Object>>) searchResult.get("children");
                        if (childernList != null) {
                            for (Map<String, Object> map : childernList) {
                                if (map.get("categoryName").toString().toLowerCase().contains(searchValue.toLowerCase())) {
                                    dataList.add(map);
                                } else {
                                    List<Map<String, Object>> cchildernList = (List<Map<String, Object>>) map.get("children");
                                    if (CollectionUtils.isNotEmpty(cchildernList)) {
                                        List<Map<String, Object>> cdataList = new ArrayList<Map<String, Object>>();
                                        for (Map<String, Object> map2 : cchildernList) {
                                            if (map2.get("categoryName").toString().toLowerCase().contains(searchValue.toLowerCase())) {
                                                cdataList.add(map2);
                                            }
                                        }
                                        map.put("children", cdataList);
                                        if (CollectionUtils.isNotEmpty(cdataList)) {
                                            dataList.add(map);
                                        }
                                    }
                                }
                            }
                        }
                        searchResult.put("children", dataList);
                        response.setData(searchResult);
                    }
                }
                response.setData(filterEmptyCateGory(response.getData()));
            }
        }
        return response;
    }

    private Object filterEmptyCateGory(Object obj) {
        Map<String, Object> resultMap = (Map<String, Object>) obj;
        List<Map<String, Object>> result1 = new ArrayList<>();
        List<Map<String, Object>> twoList = (List<Map<String, Object>>) resultMap.get("children");
        if (CollectionUtils.isNotEmpty(twoList)) {
            for (Map<String, Object> two : twoList) {
                List<Map<String, Object>> result2 = new ArrayList<>();
                List<Map<String, Object>> threeList = (List<Map<String, Object>>) two.get("children");
                if (CollectionUtils.isNotEmpty(threeList)) {
                    for (Map<String, Object> three : threeList) {
                        if (three.get("total") != null && Integer.parseInt(three.get("total").toString()) > 0) {
                            result2.add(three);
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(result2) || Integer.parseInt(two.get("total").toString()) > 0) {
                    two.put("children", result2);
                    result1.add(two);
                }
            }
        }
        resultMap.put("children", result1);
        return resultMap;
    }

    /**
     * 按分类主题查询服务事项
     *
     * @return
     */
    private ServiceItemCategoryResponse queryItemByCategoryList(CardAjaxRequest request) {
        ConfigInfo config = getConfigInfo(request);
        String roleWid = request.getParam().get("roleWid");
        String categoryWid = request.getParam().get("categoryWid");
        String searchKey = request.getParam().get("searchKey");
        String pageNumber = request.getParam().get("pageNumber");
        String pageSize = request.getParam().get("pageSize");
        String isAuthority = request.getParam().get("isAuthority");
        String visitOrder = request.getParam().get("visitOrder");
        String isMobileOnline = request.getParam().get("isMobileOnline");
        ServiceItemCategoryDetailRequest serviceitemcategoryrequest = new ServiceItemCategoryDetailRequest();
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if (user == null) {
            serviceitemcategoryrequest.setUserId("guest");
            serviceitemcategoryrequest.setIsRelate(config.getNotLogin() == null ? 0 : config.getNotLogin());
            //加遊客狀態前台傳入的瀏覽器語言參數
            if(!request.getParam().isEmpty() && !StringUtils.isEmpty(request.getParam().get("lang"))){
                serviceitemcategoryrequest.setLang(request.getParam().get("lang"));
            }
        } else {
            serviceitemcategoryrequest.setUserId(user.getWid());
            serviceitemcategoryrequest.setUserAccount(user.getUserAccount());
            serviceitemcategoryrequest.setIsRelate(config.getLogined() == null ? 0 : config.getLogined());
        }
        serviceitemcategoryrequest.setCategoryWid(categoryWid);
        serviceitemcategoryrequest.setPageNumber(Integer.parseInt(pageNumber));
        serviceitemcategoryrequest.setPageSize(Integer.parseInt(pageSize));
        serviceitemcategoryrequest.setRoleWid(roleWid);
        serviceitemcategoryrequest.setSearchKey(searchKey);
        serviceitemcategoryrequest.setVisitOrder(visitOrder);
        if ("true".equals(isAuthority)) {
            serviceitemcategoryrequest.setAuthority(true);
        }
        if ( "true".equals(isMobileOnline) ) {
            serviceitemcategoryrequest.setMobileOnline(true);
        }
        HttpEntity entity = new HttpEntity(serviceitemcategoryrequest);
        ServiceItemCategoryResponse response = RestTemplateUtils
                .post("/casp-sim/itemManager/queryItemByCategoryList", entity, ServiceItemCategoryResponse.class)
                .getBody();
        return response;
    }

    private Object queryServiceInfo(CardAjaxRequest request) {
        Map<String, Object> resultMap = Maps.newHashMap();
        String serviceListStr = request.getParam().get("serviceList").replaceAll("[\\[\\]]", "");
        List<String> serviceList = Arrays.asList(serviceListStr.split(","));
        List<ServiceDetail> serviceInfos = new ArrayList<>();
        for (String serviceWid : serviceList) {
            ServiceDetailRequest serviceDetailRequest = new ServiceDetailRequest();
            List<String> dataList = new ArrayList<>();
            dataList.add(serviceWid.replace(" ", ""));
            serviceDetailRequest.setData(dataList);

            HttpEntity entity = new HttpEntity(serviceDetailRequest);

            ServiceDetailResponse response = null;
            try {
                response = RestTemplateUtils.post("/service/queryByWids", entity, ServiceDetailResponse.class).getBody();
                logger.debug("调用查询服务详情返回结果..." + JSON.toJSONString(response));
            } catch (Exception e) {
                logger.error("调用查询服务详情接口失败...,返回错误信息" + e);
            }
            if (null != response && response.getErrcode().equals(SUCCESS_CODE)) {
                if (response.getData().size() > 0) {
                    ServiceDetail serviceDetail = response.getData().get(0);
                    // if(serviceDetail.getServiceVisibility().equals("1") && !serviceDetail.getPcAccessUrl().equals("")){
                    //判断服务是否授权后可见，如果是所有人不可见，则不展示
                    if ("1".equals(serviceDetail.getServiceVisibility())) {
                        if (null == serviceDetail.getPcIconUrl() || StringUtil.isEmpty(serviceDetail.getPcIconUrl())) {
                            serviceDetail.setPcIconUrl("");
                        } else if (!serviceDetail.getPcIconUrl().startsWith("http")) {
                            serviceDetail.setPcIconUrl("data:image/png;base64," + serviceDetail.getPcIconUrl());
                        }
                        serviceInfos.add(serviceDetail);
                    }

                    //}
                }
            }
        }
        if (CollectionUtils.isEmpty(serviceInfos)) {
            //提示信息
            resultMap.put("code", -1);
            resultMap.put("msg", "没有访问权限");
        } else if (serviceInfos.size() == 1) {
            resultMap.put("code", 0);
            resultMap.put("url", serviceInfos.get(0).getPcAccessUrl());
        } else {
            //页面
            resultMap.put("code", 200);
            resultMap.put("list", serviceInfos);
        }
        return resultMap;
    }

    /**
     * @return ConfigInfo
     * @Author jcx
     * @Description //获取后台配置
     * @Date 12:18 2021/2/24
     **/
    private ConfigInfo getConfig(String cardWid, String cardId) {
        String configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(cardWid, cardId);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
        }
        ConfigInfo config = (ConfigInfo) JSONObject.parseObject(configInfo, ConfigInfo.class);
        if (null == config) {
            config = new ConfigInfo();
            config.setLogined(0);
            config.setNotLogin(0);
            config.setDisplayInfo(new String[]{"duty_dept_id"});
            config.setTabConfigure(new String[]{"dept-subject"});
            config.setQuickConfig(new String[]{});
        } else {
            if (config.getTabConfigure() == null || config.getTabConfigure().length == 0) {
                config.setTabConfigure(new String[]{"dept-subject"});
            }
            if (config.getDisplayInfo() == null || config.getDisplayInfo().length == 0) {
                config.setDisplayInfo(new String[]{"duty_dept_id"});
            }
        }
        return config;
    }

    private Map<String, Object> view(CardAjaxRequest request) {
        logger.debug("doAjax..");
        Map<String, Object> t = new HashMap<>();
        String roleWid = request.getParam().get("roleWid");
        String oneCategoryWid = request.getParam().get("oneCategoryWid");
        String categoryWid = request.getParam().get("categoryWid");
        String lang = StringUtil.isEmpty(request.getParam().get("lang"))?Global.DEFAULT_LANGUAGE:request.getParam().get("lang");
        ConfigInfo config = getConfig(request.getCardWid(), request.getCardId());
        t.put("cardId", getCardId());
        t.put("cardWid", request.getCardWid());
        t.put("config", config);
        RoleModel roleModel = getRole(roleWid, config,lang);
        t.put("roleWid", roleModel != null ? roleModel.getRoleWid() : roleWid);
        t.put("roleName", roleModel != null ? roleModel.getRoleName() : "");
        t.put("preLogin", config.getNotLogin());
        t.put("afterLogin", config.getLogined());
        t.put("oneCategoryWid", oneCategoryWid);
        t.put("categoryWid", categoryWid);
        t.put("cateNames", getItemShowColumn(lang));
        t.put("departClassIcon", config.getDepartClassIcon());
        t.put("otherClassIcon", config.getOtherClassIcon());
        t.put("quickConfig", config.getQuickConfig());
//        Map<String, Object> categoryCardConfigInfo = getCategoryCardConfigInfo(request.getParam().get("parentCardWid"));
//        if( null != categoryCardConfigInfo && categoryCardConfigInfo.containsKey("departClassIcon") ) {
//            t.put("deptIconType", categoryCardConfigInfo.get("departClassIcon"));
//        }else{
//            t.put("deptIconType", 1);
//        }
        return t;
    }

    private Map<String, Object> getCategoryCardConfigInfo(String cardWid){
        //先查询传参，如果传参没有则需要查询库，在没有则初始化默认
        String configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(cardWid, "SYS_CARD_SERVICEITEMCATEGORY");
        if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
        }
        return toMap(configInfo);
    }

    private static Map<String, Object> toMap(String json) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotEmpty(json) && !"[]".equals(json)) {
            map = JSON.parseObject(json, HashMap.class);
        }
        return map;
    }

    private RoleModel getRole(String roleWid, ConfigInfo config,String lang) {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        ServiceItemCategoryDetailRequest roleRequest = new ServiceItemCategoryDetailRequest();
        if (user == null) {
            roleRequest.setUserId("guest");
            roleRequest.setIsRelate(config.getNotLogin() == null ? 0 : config.getNotLogin());
            roleRequest.setLang(lang);
        } else {
            roleRequest.setUserId(user.getWid());
            roleRequest.setLang(user.getPreferredLanguage());
            roleRequest.setIsRelate(config.getLogined() == null ? 0 : config.getLogined());
        }
        HttpEntity entity = new HttpEntity(roleRequest);
        RoleModelResponse roleResponse = RestTemplateUtils.post("/casp-sim/itemManager/allRoleList", entity, RoleModelResponse.class).getBody();
        if (null != roleResponse && "0".equals(roleResponse.getErrcode())) {
            List<RoleModel> list = roleResponse.getData();
            if (CollectionUtils.isNotEmpty(list)) {
                for (RoleModel model : list) {
                    if (model.getRoleWid().equals(roleWid)) {
                        return model;
                    }
                }
                return list.get(0);
            }
        }
        return null;
    }


    private void handleCheckTabData(Template t, ServiceItemCategoryResponse response, ConfigInfo config) {
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) response.getData();
        String[] strArray = null;
        if (null != config) {
            if (null != config.getTabConfigure()) {
                strArray = config.getTabConfigure();
            }
        }
        if (resultList != null) {
            for (int i = 0; i < resultList.size(); i++) {
                if (!"按部门分类".equals(resultList.get(i).get("categoryName"))) {
                    resultList.get(i).put("categoryName", "按" + resultList.get(i).get("categoryName") + "分类");
                }
                if (strArray != null) {
                    for (int j = 0; j < strArray.length; j++) {
                        if (strArray[j].equals(resultList.get(i).get("categoryWid"))) {
                            resultList.get(i).put("isChecked", "checked");
                        }
                    }
                }
            }
            t.binding("serviceItemsInfo", resultList);
        }
    }

    /**
     * 处理config初始化的复选框值
     */
    private void handleCheckBoxData(Template t, ConfigInfo config) {

    }

    private ConfigInfo getConfigInfo(CardAjaxRequest request) {
        String configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(), request.getCardId());
        if (org.apache.commons.lang.StringUtils.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
        }
        ConfigInfo config = (ConfigInfo) JSONObject.parseObject(configInfo, ConfigInfo.class);
        if (null == config) {
            config = new ConfigInfo();
        }
        if (config.getNotLogin() == null) {
            config.setNotLogin(0);
        }
        if (config.getLogined() == null) {
            config.setLogined(0);
        }
        return config;
    }

}