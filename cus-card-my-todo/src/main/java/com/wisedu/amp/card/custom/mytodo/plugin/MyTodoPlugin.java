package com.wisedu.amp.card.custom.mytodo.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wisedu.amp.card.custom.mytodo.model.*;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.AllServiceItemRequest;
import com.wisedu.minos.casp.portal.model.AllServiceItemResponse;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.service.impl.ServiceApiService;
import com.wisedu.minos.casp.portal.service.impl.ServiceItemApiService;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;


@MinosSPI
public class MyTodoPlugin extends AbstractCard {
    private static final Logger logger = LogManager.getLogger(MyTodoPlugin.class);

    @Override
    public String getCardId() {
        return "CUS_CARD_MY_TODO";
    }

    @Override
    public void destroy() {

    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "getConfigs":
                result = this.getConfigs(request);
                break;
            case "searchServiceItem":
                result = this.viewData(request);
                break;
            case "queryItemByCategoryList":
                result = this.queryItemByCategoryList(request);
                break;
            case "collectServiceItem":
                result = this.collectServiceItem(request);
                break;
            case "getMarkNumber":
                result = 0;
                break;
            case "favoriteList":
                result = favoriteList();
                break;
        }
        return result;
    }

    private FavoriteResponse favoriteList() {
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        FavoriteRequest favoriteRequest = new FavoriteRequest();
        favoriteRequest.setUserId(user.getWid());
        favoriteRequest.setUserAccount(user.getUserAccount());
        logger.debug("开始调用获取事项收藏列表接口...,参数=" + JSON.toJSONString(favoriteRequest));
        FavoriteResponse favoriteResponse = null;
        try {
            favoriteResponse = RestTemplateUtils.post("/coosk/userItemFavoriteList", favoriteRequest, FavoriteResponse.class).getBody();
            logger.debug("调用获取事项收藏列表接口成功..., 返回结果 = " + JSON.toJSONString(favoriteRequest));
        } catch (Exception e) {
            logger.error("调用获取事项收藏列表接口失败", e);
        }
        return favoriteResponse;
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

    /**
     * 按分类主题查询服务事项
     *
     * @return
     */
    private ServiceItemCategoryResponse queryItemByCategoryList(CardAjaxRequest request) {
        ConfigInfo config = getConfigInfo(request);
        String categoryWid = request.getParam().get("categoryWid");
        String searchKey = request.getParam().get("searchKey");
        String pageNumber = request.getParam().get("pageNumber");
        String pageSize = request.getParam().get("pageSize");
        String isAuthority = request.getParam().get("isAuthority");
        String visitOrder = request.getParam().get("visitOrder");
        String isMobileOnline = request.getParam().get("isMobileOnline");
        String roleWid = request.getParam().get("roleWid");
        ServiceItemCategoryDetailRequest serviceitemcategoryrequest = new ServiceItemCategoryDetailRequest();
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if (user == null) {
            serviceitemcategoryrequest.setUserId("guest");
            serviceitemcategoryrequest.setIsRelate(config.getNotLogin() == null ? 0 : config.getNotLogin());
            //加遊客狀態前台傳入的瀏覽器語言參數
            if (!request.getParam().isEmpty() && !StringUtils.isEmpty(request.getParam().get("lang"))) {
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
        serviceitemcategoryrequest.setSearchKey(searchKey);
        if ("true".equals(request.getParam().get("orderByVisitCount"))) {
            serviceitemcategoryrequest.setVisitOrder(visitOrder);
        }

        serviceitemcategoryrequest.setAuthorityEnabled(config.getAuthorityEnabled());

        if ("true".equals(isAuthority)) {
            serviceitemcategoryrequest.setAuthority(true);
        }
        if ("true".equals(isMobileOnline)) {
            serviceitemcategoryrequest.setMobileOnline(true);
        }
        HttpEntity entity = new HttpEntity(serviceitemcategoryrequest);
        if (roleWid == null || "".equals(roleWid)) {
            return RestTemplateUtils
                    .post("/coosk/itemManager/queryItemByCategoryListNoRoleId", entity, ServiceItemCategoryResponse.class)
                    .getBody();
        } else {
            serviceitemcategoryrequest.setRoleWid(roleWid);
            return RestTemplateUtils
                    .post("/coosk/itemManager/queryItemByCategoryList", entity, ServiceItemCategoryResponse.class)
                    .getBody();
        }

    }

    private Object getConfigs(CardAjaxRequest request) {
        List<ThemeInfo> serviceSubject = getServiceSubject();
        List<Role> serviceObject = getServiceObject();
        Map<String, Object> map = new HashMap<>();
        map.put("serviceSubject", serviceSubject);
        map.put("serviceObject", serviceObject);
        map.put("allServiceItemCount", getAllServiceItemCount());

        return map;
    }

    private List<ThemeInfo> getServiceSubject() {
        List<ThemeInfo> list = new ArrayList<>();

        ThemeRequest themeRequest = new ThemeRequest();
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if (null != currentUser) {
            themeRequest.setUserId(currentUser.getWid());
        }
        HttpEntity entity = new HttpEntity(themeRequest);

        ThemeResponse themeResponse = null;
        try {
            themeResponse = RestTemplateUtils.post("/casp-sim/itemManager/getServiceSubjectList", entity, ThemeResponse.class).getBody();
            logger.debug("调用服务主题返回结果..." + JSON.toJSONString(themeResponse));
        } catch (Exception e) {
            logger.error("调用服务主题,返回错误信息" + e);
        }
        if (null != themeResponse && themeResponse.getErrcode().equals("0")) {
            list = themeResponse.getData();
        }

        return list;
    }

    private Map<String, Object> viewData(CardAjaxRequest request) {
        logger.debug("render allServiceItem view ..");
        Map<String, Object> result = new HashMap<>(17);
        // 配置保存分组条件

        // 组装请求全部服务事项的request
        AllServiceItemRequest allserviceItemRequest = getAllserviceItemRequest(request);
        AllServiceItemResponse list = ApplicationContextUtil.get(ServiceItemApiService.class)
                .getAllServiceItemPage(allserviceItemRequest);
        List<AllServiceItemResponse.ServiceItemModel> models = list.getData();
        List<String> grantedServiceList = ApplicationContextUtil.get(ServiceApiService.class).getGrantedServiceList();
        models.forEach(e -> e.setOnlineServiceType(
                ApplicationContextUtil.get(ServiceApiService.class).getOnlineServiceType(grantedServiceList, e.getServiceList())
        ));

        //基础信息
        result.put("cardWid", request.getCardWid());
        result.put("cardId", request.getCardId());
        result.put("userId", getCurrentUserId());
        result.put("pageSize", list.getPage().getSize());
        result.put("pageNumber", list.getPage().getCurrent());
        result.put("total", list.getPage().getTotal());
        result.put("data", list.getData());
        return result;
    }

    private AllServiceItemRequest getAllserviceItemRequest(CardAjaxRequest request) {
        AllServiceItemRequest allserviceItemRequest = new AllServiceItemRequest();
        allserviceItemRequest.setUserId(getCurrentUserId());
        allserviceItemRequest.setIsRelate(0);
        int pageSize;
        int pageNumber;

        pageNumber = Integer.parseInt(request.getParam().get("pageNumber"));
        pageSize = Integer.parseInt(request.getParam().get("pageSize"));

        if (request.getParam() != null) {
            // 搜索条件
            String searchKey = StringUtil.getString(request.getParam().get("searchKey"));
            // 服务主题
            String subject = StringUtil.getString(request.getParam().get("categoryWids"));
            // 服务对象
            String object = StringUtil.getString(request.getParam().get("roleWids"));
            //
            String availableOnline = StringUtil.getString(request.getParam().get("availableOnline"));
            String orderByVisitCount = StringUtil.getString(request.getParam().get("orderByVisitCount"));
            //是否展示可在线办理
            allserviceItemRequest.setAvailableOnline(Boolean.parseBoolean(availableOnline));
            //是否按访问量排序
            allserviceItemRequest.setOrderByVisitCount(Boolean.parseBoolean(orderByVisitCount));

            if (StringUtils.isNotEmpty(searchKey)) {
                //关键字
                allserviceItemRequest.setSearchKey(searchKey);
            }
            if (StringUtils.isNotEmpty(subject)) {
                //服务主题
                allserviceItemRequest.setCategoryWids(subject);
            }

            if (StringUtils.isNotEmpty(object)) {
                //服务对象
                allserviceItemRequest.setRoleWids(object);
            }

        }
        allserviceItemRequest.setPageSize(pageSize);
        allserviceItemRequest.setPageNumber(pageNumber);

        return allserviceItemRequest;
    }

    public String getCurrentUserId() {
        String userId;
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            if (user == null) {
                userId = "guest";
            } else {
                userId = user.getWid();
            }
        } catch (Exception e) {
            userId = "guest";
            logger.error("获取用户信息异常：" + e);
        }
        return userId;
    }

    /**
     * 查询事项服务对象
     *
     * @return
     */
    private List<Role> getServiceObject() {
        List<Role> list = new ArrayList<>();

        RoleRequest roleRequest = new RoleRequest();
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if (null != currentUser) {
            roleRequest.setUserId(currentUser.getWid());
        }
        roleRequest.setIsRelate(0);
        HttpEntity entity = new HttpEntity(roleRequest);
        RoleResponse roleResponse = null;
        try {
            roleResponse = RestTemplateUtils.post("/casp-sim/itemManager/allRoleList", entity, RoleResponse.class).getBody();
            logger.debug("调用服务对象返回结果..." + JSON.toJSONString(roleResponse));
        } catch (Exception e) {
            logger.error("调用服务对象,返回错误信息" + e);
        }
        if (null != roleResponse && roleResponse.getErrcode().equals("0")) {
            list = roleResponse.getData();
        }

        return list;
    }

    private Object collectServiceItem(CardAjaxRequest request) {
        CollectServiceItemRequest collect = new CollectServiceItemRequest();
        String itemId = request.getParam().get("serviceItemId");
        if (StringUtils.isBlank(itemId)) {
            itemId = request.getParam().get("serviceId");
        }
        collect.setItemId(itemId);
        String flag = request.getParam().get("flag");
        if (StringUtils.isBlank(flag)) {
            flag = request.getParam().get("operate");
        }
        collect.setItemId(itemId);
        collect.setFlag(flag);
        collect.setUserId(getCurrentUserId());

        HttpEntity entity = new HttpEntity(collect);

        AmpBaseResponse response = null;
        try {
            response = RestTemplateUtils.post("/casp-sim/itemManager/favoriteItem", entity, AmpBaseResponse.class).getBody();
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


    Object getAllServiceItemCount() {

        String url = "/casp-sim/itemManager/queryItemStatistics2";
        logger.debug("开始调用获取事项统计接口,url=" + url);
        ItemStatisticsRequest itemStatisticsRequest = new ItemStatisticsRequest();

        List<ItemStatisticsInfo> list = Lists.newArrayList();
        itemStatisticsRequest.setRoleIds(Collections.emptyList());
        HttpEntity entity = new HttpEntity(itemStatisticsRequest);
        AmpBaseResponse response = RestTemplateUtils
                .post(url, entity, AmpBaseResponse.class).getBody();
        logger.debug("获取事项统计接口返回结果..." + JSON.toJSONString(response));

        if (null != response && "0".equals(response.getErrcode())) {
            Object data = response.getData();
            list = JSON.parseArray(JSON.toJSONString(data), ItemStatisticsInfo.class);
        }
        Map<String, String> data = list.stream()
                .collect(Collectors.toMap(ItemStatisticsInfo::getDataId, ItemStatisticsInfo::getCount));
        ItemStatisticsInfo onlineItem = list.stream()
                .filter(e -> ("currentItem").equals(e.getDataId()))
                .findAny().orElse(new ItemStatisticsInfo());
        return onlineItem.getCount();
    }
}
