package com.wisedu.amp.card.cus.myservice.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisedu.amp.card.cus.myservice.enums.DisplayTypeEnum;
import com.wisedu.amp.card.cus.myservice.enums.ItemDisplayInfoEnum;
import com.wisedu.amp.card.cus.myservice.model.*;
import com.wisedu.amp.card.cus.myservice.util.AllServiceItemUtil;
import com.wisedu.amp.card.cus.myservice.util.ChinesePinyinUtil;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.constant.Global.ComponentParam;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.CollectionUtil;
import com.wisedu.minos.casp.portal.common.utils.FastJsonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.AllServiceItemRequest;
import com.wisedu.minos.casp.portal.model.AllServiceItemResponse;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.CardConfigReq;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.configcomponent.AbstractComponent;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.impl.ServiceApiService;
import com.wisedu.minos.casp.portal.service.impl.ServiceItemApiService;
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
import org.springframework.util.Assert;

import java.text.Collator;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * 全部服务事项
 *
 * @author dali(01317002)
 * @date 2020年9月7日
 */
@MinosSPI
public class AllServiceItemCard extends AbstractCard {

    private final static Logger logger = LogManager.getLogger(AllServiceItemCard.class);

    /**
     * 请求成功返回值
     */
    private final static String SUCCESS_CODE = "0";

    private final static List<String> category = Arrays.asList(new String[]{"1", "2", "3"});

    @Override
    public String getCardId() {
        return "CUS_CARD_MYSERVICE";
    }

    @Override
    public void destroy() {
        logger.debug("card[CUS_CARD_MYSERVICE] destroyed ...");
    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "renderData":
                result = this.viewData(request);
                break;
            case "searchServiceItem":
                result = this.searchServiceItem(request);
                break;
            case "collectServiceItem":
                result = this.collectServiceItem(request);
                break;
            case "getMarkNumber":
                result = 0;
                break;
        }
        return result;
    }

    @Override
    public ComponentContainer getConfig(CardConfigReq cardConfigReq) {
        ComponentContainer container = cardConfigReq.getComponentContainer();

        List<FilterDisplayInfo> filterDisplayInfos = FilterDisplayInfo.initList();
        List<ServiceGroupDisplayInfo> serviceGroupDisplayInfos = ServiceGroupDisplayInfo.initList();
        List<ServiceItemCategory> oneCategoryList = getOneCategoryList();
        if (CollectionUtils.isNotEmpty(oneCategoryList)) {
            transItemToDisplayInfosAndGroups(oneCategoryList,filterDisplayInfos,serviceGroupDisplayInfos);
        }
        Map<String,Object> dataMap =new HashMap<>();
        dataMap.put("filterFields",filterDisplayInfos);
        dataMap.put("itemDisplayGroups",serviceGroupDisplayInfos);
        dataMap.put("itemDisplayInfo",getItemDisplayInfo());
        container.setData("allServiceItemsDisplay", ComponentParam.DATAS,dataMap);
        return container;
    }

    /***
     * transItemToDisplayInfosAndGroups
     * 把事项分组添加到过滤信息和分组信息中<p/>
     *
     * @param categories
     * @param filterInfo
     * @param groupInfo
     * @throws
     * @return void
     * @date 2021/5/25 10:21
     */
    public void transItemToDisplayInfosAndGroups(List<ServiceItemCategory> categories,
        List<FilterDisplayInfo> filterInfo, List<ServiceGroupDisplayInfo> groupInfo
    ) {
        categories.forEach(e -> {
            if (Pattern.matches("\\d*", e.getCategoryWid())) {
                filterInfo.add(new FilterDisplayInfo(e.getCategoryWid(), e.getCategoryName()));
            }
            groupInfo.add(new ServiceGroupDisplayInfo(e.getCategoryWid(), "按" + e.getCategoryName() + "分组"));
        });
    }
    /***
     * getItemDisplayInfo
     * 事项展示信息<p/>
     *
     * @param
     * @throws
     * @return java.util.List<com.wisedu.amp.card.cus.myservice.enums.ItemDisplayInfoEnum>
     * @date 2021/5/24 15:53
     */
    private List<ItemDisplayInfoEnum> getItemDisplayInfo() {
        return Lists.newArrayList(ItemDisplayInfoEnum.values());
    }

    /**
     * 查询事项分类维度
     *
     * @return
     */
    private List<ServiceItemCategory> getOneCategoryList() {
        ServiceItemCategoryRequest serviceitemcategoryrequest = new ServiceItemCategoryRequest();
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if(null==currentUser){
            serviceitemcategoryrequest.setUserId("guest");
        }else{
            serviceitemcategoryrequest.setUserId(currentUser.getWid());
        }
        HttpEntity entity = new HttpEntity(serviceitemcategoryrequest);
        ServiceItemCategoryResponse response = null;
        List<ServiceItemCategory> data = new ArrayList<>();
        try {
            logger.debug(">>请求参数：" + JSON.toJSONString(entity));
            response = RestTemplateUtils.post("/coosk/itemManager/getOneCategoryList", entity, ServiceItemCategoryResponse.class).getBody();
        } catch (Exception e) {
            logger.error("调用查询一级服务事项分类,返回错误信息" + e);
        }
        if (null != response && SUCCESS_CODE.equals(response.getErrcode()) && CollectionUtils
                .isNotEmpty(response.getData())) {
            List<ServiceItemCategory> responseData = response.getData();
            data = new ArrayList<>();
            for (ServiceItemCategory category : responseData) {
                if (Pattern.matches("\\d*", category.getCategoryWid()) || !"dept-subject".equals(category.getCategoryWid())) {
                    data.add(category);
                }
            }
        }
        return data;
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

    private Object searchServiceItem(CardAjaxRequest request) {
        return this.viewData(request);
    }


    private Map<String, Object> viewData(CardAjaxRequest request) {
        logger.debug("render allServiceItem view ..");
        Map<String, Object> result = new HashMap<>(17);
        String lang = Global.DEFAULT_LANGUAGE;
        if(!request.getParam().isEmpty() && !org.springframework.util.StringUtils.isEmpty(request.getParam().get("lang"))){
            lang = request.getParam().get("lang");
        }
        // 处理配置
        Config config = handleConfig(request);
        // 配置保存分组条件
        String group = config.getAllServiceItemsDisplay().getItemDisplayGroups();

        //展示方式
        String displayType = config.getAllServiceItemsDisplay().getDisplayType();

        // 组装请求全部服务事项的request
        AllServiceItemRequest allserviceItemRequest = getAllserviceItemRequest(config, request);
        AllServiceItemResponse list = ApplicationContextUtil.get(ServiceItemApiService.class)
            .getAllServiceItemPage(allserviceItemRequest);
        List<String> oneCategoryWids = null;
        List<AllServiceItemResponse.ServiceItemModel> models = list.getData();
        List<String> grantedServiceList = ApplicationContextUtil.get(ServiceApiService.class).getGrantedServiceList();
        models.forEach(e->e.setOnlineServiceType(
            ApplicationContextUtil.get(ServiceApiService.class).getOnlineServiceType(grantedServiceList, e.getServiceList())
        ));
        if(CollectionUtils.isNotEmpty(list.getData())){
            List<ServiceItemCategory> oneCategoryList = getOneCategoryList();
            oneCategoryWids = oneCategoryList.stream().map(ServiceItemCategory::getCategoryWid)
                .collect(Collectors.toList());
        }
        if(DisplayTypeEnum.TILE.getCode().equals(displayType)){
            if (CollectionUtils.isNotEmpty(list.getData())) {
                List<ContentModel> serviceItemList = new ArrayList<>();
                if ("0".equals(group) || !oneCategoryWids.contains(group)) {
                    // 根据字母分组
                    getNavModels(list.getData(), serviceItemList, result);
                } else {
                    // 非字母分组
                    getModelsByGroup(allserviceItemRequest, config, list.getData(), serviceItemList,lang);
                }
                // 数据
                result.put("serviceItemList", serviceItemList);
            }
        }else{
            result.put("serviceItemList", list.getData());

        }
        // 处理搜索条件
        processFilterItem(result, config, request, oneCategoryWids);
        //基础信息
        result.put("config", config);
        result.put("cardWid", request.getCardWid());
        result.put("cardId", request.getCardId());
        result.put("userId", getCurrentUserId());
        result.put("pageSize", list.getPage().getSize());
        result.put("pageNumber",list.getPage().getCurrent());
        result.put("total",list.getPage().getTotal());

        return result;
    }

    /**
     * 处理筛选条件
     *
     * @param result
     * @param config
     */
    private void processFilterItem(Map<String, Object> result, Config config, CardAjaxRequest request,
        List<String> oneCategoryWids) {
        if (CollectionUtils.isNotEmpty(config.getAllServiceItemsDisplay().getFilterFields()) && "renderData".equals(request.getMethod())) {
            List<Map<String, Object>> filterList = new ArrayList<>();
            //加遊客狀態前台傳入的瀏覽器語言參數
            String lang = Global.DEFAULT_LANGUAGE;
            if(!request.getParam().isEmpty() && !org.springframework.util.StringUtils.isEmpty(request.getParam().get("lang"))){
                lang = request.getParam().get("lang");
            }
            if (config.getAllServiceItemsDisplay().getFilterFields().contains(AllServiceItemUtil.SearchType.SERVICE_SUBJECT.getIndex())) {
                Map<String, Object> subjectMap = new HashMap<>();
                subjectMap.put("propertyWid", "subjectWid");
                subjectMap.put("propertyName", "subjectName");
                subjectMap.put("alias", "服务主题");
                //服务主题
                List<ThemeInfo> itemList = getServiceSubject(lang);
                subjectMap.put("data", itemList);
                filterList.add(subjectMap);
            }
            if (config.getAllServiceItemsDisplay().getFilterFields().contains(AllServiceItemUtil.SearchType.RESPONSIBLE_DEPARTMENT.getIndex())) {
                // 责任部门
                Map<String, Object> deptMap = new HashMap<>();
                deptMap.put("propertyWid", "deptWid");
                deptMap.put("propertyName", "deptName");
                deptMap.put("alias", "责任部门");

                List<LocalDept> itemList = getDutyDept(lang);
                deptMap.put("data", itemList);
                filterList.add(deptMap);
            }
            if (config.getAllServiceItemsDisplay().getFilterFields().contains(AllServiceItemUtil.SearchType.SERVICE_OBJECT.getIndex())) {
                //服务对象
                Map<String, Object> serviceObjectMap = new HashMap<>();
                serviceObjectMap.put("propertyWid", "roleWid");
                serviceObjectMap.put("propertyName", "roleName");
                serviceObjectMap.put("alias", "服务对象");
                List<Role> itemList = getServiceObject(lang);
                serviceObjectMap.put("data", itemList);
                filterList.add(serviceObjectMap);
            }
            List<String> categoryWidList = CollectionUtil.subtract(config.getAllServiceItemsDisplay().getFilterFields(), category);
            if(CollectionUtils.isNotEmpty(categoryWidList) && CollectionUtils.isNotEmpty(oneCategoryWids)){
                categoryWidList.retainAll(oneCategoryWids);
                processDynamicsType(filterList, categoryWidList,lang);
            }
            result.put("filterItem", filterList);
        }
    }

    /**
     * 动态筛选条件处理
     *
     * @param filterList
     * @param categoryWidList
     */
    private void processDynamicsType(List<Map<String, Object>> filterList, List<String> categoryWidList,String lang) {
        if (CollectionUtils.isNotEmpty(categoryWidList)) {
            String categoryWids = String.join(",", categoryWidList);
            UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            String temp=null==currentUser?"guest":currentUser.getWid();
            List<ServiceItemCategory> serviceItemCategoryList = getCategoryList(temp, 0, categoryWids,lang);
            if (CollectionUtils.isNotEmpty(serviceItemCategoryList)) {
                for (ServiceItemCategory category : serviceItemCategoryList) {
                    Map<String, Object> categoryMap = new HashMap<>();
                    categoryMap.put("propertyWid", "categoryWid");
                    categoryMap.put("propertyName", "categoryName");
                    categoryMap.put("alias", category.getCategoryName());
                    List<ServiceItemCategory> twoCategoryList = category.getChildren();
                    if (CollectionUtils.isNotEmpty(twoCategoryList)) {
                        processTwoCategoryList(twoCategoryList);
                    }
                    categoryMap.put("data", twoCategoryList);

                    filterList.add(categoryMap);
                }
            }
        }
    }

    /**
     * 动态处理二级分类
     * @param twoCategoryList
     */
    private void processTwoCategoryList(List<ServiceItemCategory> twoCategoryList) {
        for (int i = 0; i < twoCategoryList.size(); i++) {
            dealChildrenCategory(twoCategoryList.get(i));
            if (CollectionUtils.isEmpty(twoCategoryList.get(i).getChildren()) && twoCategoryList.get(i).getTotal() == 0) {
                twoCategoryList.remove(twoCategoryList.get(i));
                i--;
            }
        }
    }

    /**
     * 递归排除分类下没有挂事项的分类
     *
     * @param category
     */
    private void dealChildrenCategory(ServiceItemCategory category) {
        List<ServiceItemCategory> categoryList = category.getChildren();
        if (CollectionUtils.isNotEmpty(categoryList)) {
            categoryList = categoryList.stream().filter(serviceItemCategory -> serviceItemCategory.getTotal() != 0).collect(Collectors.toList());
            category.setChildren(categoryList);
            if (CollectionUtils.isNotEmpty(categoryList)) {
                for (ServiceItemCategory serviceItemCategory : categoryList) {
                    dealChildrenCategory(serviceItemCategory);
                }
            }
        }
    }

    /**
     * 获取请求全部服务事项的request
     *
     * @param request
     * @return
     */
    private AllServiceItemRequest getAllserviceItemRequest(Config config, CardAjaxRequest request) {
        AllServiceItemRequest allserviceItemRequest = new AllServiceItemRequest();
        String userId = getCurrentUserId();
        if(StringUtils.isEmpty(userId) || "guest".equals(userId)){
            if(!request.getParam().isEmpty() && !StringUtils.isEmpty(request.getParam().get("lang"))){
                allserviceItemRequest.setLang(request.getParam().get("lang"));
            }
        }
        allserviceItemRequest.setUserId(userId);
        allserviceItemRequest.setIsRelate(0);
        allserviceItemRequest.setAuthorityEnabled(config.getAuthorityEnabled());
        String displayType = config.getAllServiceItemsDisplay().getDisplayType();
       int pageSize;
       int pageNumber;
        if(DisplayTypeEnum.TILE.getCode().equals(displayType)){
            pageNumber = 1;
            pageSize = 2000;
            if (!"0".equals(config.getAllServiceItemsDisplay().getItemDisplayGroups())) {
                allserviceItemRequest.setGroupWid(config.getAllServiceItemsDisplay().getItemDisplayGroups());
            }else{
                allserviceItemRequest.setGroupWid("0");
            }
        }else{
            pageNumber = Integer.parseInt(request.getParam().get("pageNumber"));
            pageSize = Integer.parseInt(request.getParam().get("pageSize"));
        }
        getRequest( request, allserviceItemRequest);
        allserviceItemRequest.setPageSize(pageSize);
        allserviceItemRequest.setPageNumber(pageNumber);

        return allserviceItemRequest;
    }
    private void getRequest(CardAjaxRequest request,AllServiceItemRequest allserviceItemRequest){
        if (request.getParam() != null) {
            // 搜索条件
            String searchKey = StringUtil.getString(request.getParam().get("searchKey"));
            // 服务主题
            String subject = StringUtil.getString(request.getParam().get("categoryWids"));
            // 责任部门
            String dept = StringUtil.getString(request.getParam().get("deptWids"));
            // 服务对象
            String object = StringUtil.getString(request.getParam().get("roleWids"));

            // 分类维度wid
            String dimensions = StringUtil.getString(request.getParam().get("dimensions"));

            String availableOnline = StringUtil.getString(request.getParam().get("availableOnline"));
            String orderByVisitCount =StringUtil.getString( request.getParam().get("orderByVisitCount"));
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
            if (StringUtils.isNotEmpty(dept)) {
                //责任部门
                allserviceItemRequest.setDeptWids(dept);
            }
            if (StringUtils.isNotEmpty(object)) {
                //服务对象
                allserviceItemRequest.setRoleWids(object);
            }
            if (StringUtils.isNotEmpty(dimensions)) {
                // 分类维度wid
                allserviceItemRequest.setDimensions(dimensions);
            }
        }
    }

    private void getModelsByGroup(AllServiceItemRequest allserviceItemRequest, Config config, List<AllServiceItemResponse.ServiceItemModel> allServiceItems,
                                  List<ContentModel> serviceItemList,String lang) {
        List<ServiceItemCategory> twoGroupItemCategorys;
        // 二级分类的id集合
        List<String> twoCategoryWids = null;
        Map<String, ServiceItemCategory> twoCategoryMap = null;
        String displayGroup = config.getAllServiceItemsDisplay().getItemDisplayGroups();
        // 根据某个一级分类查询二级分类数据
        List<ServiceItemCategory> groupItemCategorys = getCategoryList("guest", 0, displayGroup,lang);
        if (CollectionUtils.isNotEmpty(groupItemCategorys)) {
            twoGroupItemCategorys = groupItemCategorys.get(0).getChildren();
            if (CollectionUtils.isNotEmpty(twoGroupItemCategorys)) {
                twoCategoryMap = twoGroupItemCategorys.stream().collect(Collectors.toMap(ServiceItemCategory::getCategoryWid, entity -> entity));
                twoCategoryWids = twoGroupItemCategorys.stream().map(ServiceItemCategory::getCategoryWid).collect(Collectors.toList());
            }
        }
        if (CollectionUtils.isNotEmpty(twoCategoryWids)) {
            for (String categoryWid : twoCategoryWids) {
                if (StringUtil.isNotEmpty(allserviceItemRequest.getDimensions())) {
                    if (allserviceItemRequest.getDimensions().contains(categoryWid)) {
                        setModelByCategory(allServiceItems, serviceItemList, twoCategoryMap, categoryWid);
                    }
                } else {
                    setModelByCategory(allServiceItems, serviceItemList, twoCategoryMap, categoryWid);
                }

            }
        }

        // 处理不在分类里的服务事项
        List<AllServiceItemResponse.ServiceItemModel> otherServiceItem = allServiceItems.stream().filter(serviceItem -> (CollectionUtils.isEmpty(serviceItem.getGroupCategoryList()))).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(otherServiceItem)) {
            ContentModel contentModel = new ContentModel();
            contentModel.setNavId("other");
            contentModel.setNavName("其它");
            contentModel.setNavValue("其它");
            orderSerivceItem(otherServiceItem);
            contentModel.setDatas(otherServiceItem);
            serviceItemList.add(contentModel);
        }

    }

    private void setModelByCategory(List<AllServiceItemResponse.ServiceItemModel> allServiceItems, List<ContentModel> serviceItemList, Map<String, ServiceItemCategory> twoCategoryMap, String categoryWid) {
        ContentModel contentModel = new ContentModel();
        contentModel.setNavId(categoryWid);
        contentModel.setNavName(twoCategoryMap.get(categoryWid).getCategoryName());
        contentModel.setNavValue(twoCategoryMap.get(categoryWid).getCategoryName());
        List<AllServiceItemResponse.ServiceItemModel> contentModelData = new ArrayList<>();
        for (AllServiceItemResponse.ServiceItemModel serviceItem : allServiceItems) {
            List<String> groupCategoryList = serviceItem.getGroupCategoryList();
            if (groupCategoryList.contains(categoryWid)) {
                contentModelData.add(serviceItem);
            }
        }
        orderSerivceItem(contentModelData);
        contentModel.setDatas(contentModelData);
        serviceItemList.add(contentModel);
    }


    /**
     * 根据一级分类获取二级分类
     */
    private List<ServiceItemCategory> getCategoryList(String userId, int isRelate, String categoryWids,String lang) {
        ServiceItemCategoryRequest serviceitemcategoryrequest = new ServiceItemCategoryRequest();
        serviceitemcategoryrequest.setUserId(userId);
        serviceitemcategoryrequest.setIsRelate(isRelate);
        serviceitemcategoryrequest.setLang(lang);
        if (StringUtil.isNotEmpty(categoryWids)) {
            serviceitemcategoryrequest.setCategoryWids(categoryWids);
        }
        HttpEntity entity = new HttpEntity(serviceitemcategoryrequest);
        List<ServiceItemCategory> serviceItemCategoryList = null;
        ServiceItemCategoryResponse response = null;
        try {
            logger.debug(">>请求参数" + FastJsonUtil.convertObjectToJSON(serviceitemcategoryrequest));
            response = RestTemplateUtils.post("/coosk/itemManager/allCateGoryList", entity, ServiceItemCategoryResponse.class).getBody();
            logger.debug("调用全部服务事项一二级分类返回结果..." + JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("调用全部服务事项一二级分类,返回错误信息" + e);
        }

        Assert.isTrue("0".equals(response.getErrcode()), response.getErrmsg());
        if (response.getData() != null) {
            serviceItemCategoryList = response.getData();
        }
        return serviceItemCategoryList;
    }

    /**
     * 按首字母排序:处理数据
     *
     * @param list
     * @param serviceItemList
     * @return
     */
    private void getNavModels(List<AllServiceItemResponse.ServiceItemModel> list, List<ContentModel> serviceItemList, Map<String, Object> result) {
        List<NavModel> navList;
        ContentModel data;
        //1.获取导航条
        navList = AllServiceItemUtil.navItem();
        //2.判断首字母是否包含数据
        for (AllServiceItemResponse.ServiceItemModel serviceItem : list) {
            // 服务事项名称
            String serviceName = serviceItem.getItemName();
            // 首字母[多音字不准确]
            String headChar = ChinesePinyinUtil.headChar(serviceName);

            if (AllServiceItemUtil.LETTERS.contains(headChar)) {
                navList.get(AllServiceItemUtil.LETTERS.indexOf(headChar)).setHasValue(true);
                serviceItem.setHeadChar(headChar);//首字母
            } else {
                serviceItem.setHeadChar("#");//首字母
            }

            // 排序值，用于排序时将A开头的应用放在中文开头的后面
            if (serviceName.substring(0, 1).toUpperCase().equals(headChar)) {
                serviceItem.setOrder("1");
            } else {
                // 汉语在前
                serviceItem.setOrder("0");
            }
        }

        //3.数据排序
        if (CollectionUtils.isNotEmpty(list)) {
            orderSerivceItem(list);
        }

        //4.组合结果数据
        String nodataName = "";
        // [A-D : E-H : I-L : M-P : Q-T : U-X : Y-Z]  一共七项(最多)
        for (int i = 7; i > 0; i--) {
            data = new ContentModel();
            // 获取当前所属的位置的字母  Y-Z
            String name = AllServiceItemUtil.nearly(AllServiceItemUtil.LETTERS.substring(i * 4 - 4, i * 4 - 3));

            // 判断这几个字母下是否有服务事项数据
            boolean hasData = hasData(name, navList);
            if (hasData) {
                name = name + nodataName;
                data.setNavId(name.substring(0, 1));
                data.setNavValue(name);
                data.setNavName(name.substring(0, 1) + "-" + name.substring(name.length() - 1));
                data.setDatas(getList(name, list));
                serviceItemList.add(0, data);
            } else {
                //获取最近的一个有值的项
                if (CollectionUtils.isNotEmpty(serviceItemList)) {
                    String nextname = serviceItemList.get(0).getNavValue();
                    name = name + nextname;
                    serviceItemList.get(0).setNavId(name.substring(0, 1));
                    serviceItemList.get(0).setNavValue(name);
                    serviceItemList.get(0).setNavName(name.substring(0, 1) + "-" + name.substring(name.length() - 1));
                }
            }
            nodataName = "";
        }

        List<AllServiceItemResponse.ServiceItemModel> otherList = getList("#", list);
        data = new ContentModel();
        data.setNavId("other");
        data.setNavValue("other");
        data.setNavName("#");
        data.setDatas(otherList);
        if (CollectionUtils.isNotEmpty(otherList)) {
            serviceItemList.add(data);
            navList.get(navList.size() - 1).setHasValue(true);
        }

        // 5.修改导航条对应的ID
        setNavId(serviceItemList, navList);
        result.put("navList", navList);
    }

    private void setNavId(List<ContentModel> serviceItemList, List<NavModel> navList) {
        for (int i = 0; i < serviceItemList.size(); i++) {
            //  ABCD
            String name = serviceItemList.get(i).getNavValue();
            // ABCD > A
            String headChar = name.substring(0, 1);
            if ("other".equals(name)) {
                headChar = name;
            }
            for (NavModel nav : navList) {
                String id = nav.getId();
                if (name.contains(id)) {
                    nav.setHref(headChar);
                }
            }
        }
    }

    private void orderSerivceItem(List<AllServiceItemResponse.ServiceItemModel> list) {
        Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
        list.sort((e1, e2) -> {
            int compareResult = comparator.compare(StringUtil.getString(e1.getHeadChar()), StringUtil.getString(e2.getHeadChar()));
            if (compareResult == 0) {
                //比较排序值
                int result2 = comparator.compare(StringUtil.getString(e1.getOrder()), StringUtil.getString(e2.getOrder()));
                if (result2 == 0) {
                    return comparator.compare(StringUtil.getString(e1.getItemName()), StringUtil.getString(e2.getItemName()));
                }
                return result2;
            }
            return compareResult;
        });
    }

    /**
     * renderData 方法中处理config
     *
     * @param request
     * @return
     */
    private Config handleConfig(CardAjaxRequest request) {
        String configInfo = ApplicationContextUtil.get(CardUtil.class)
            .getCardConfigByCardWid(request.getCardWid(), request.getCardId());
        if (StringUtils.isNotEmpty(configInfo)) {
            configInfo = JSON.parse(configInfo).toString();
        }
        logger.debug("卡片配置信息:" + configInfo);
        Config config = JSONObject.parseObject(configInfo, Config.class);
        if (config == null) {
            config = new Config();
            AllServiceItemsDisplayDB allServiceItemsDisplayDB = new AllServiceItemsDisplayDB();
            allServiceItemsDisplayDB.setDisplayType("1");
            List<String> filerFields = Lists.newArrayList("-1","0","1", "2","3");
            allServiceItemsDisplayDB.setFilterFields(filerFields);
            allServiceItemsDisplayDB.setItemDisplayGroups("1");
            List<String> itemDisplayInfo = Lists.newArrayList("0","1", "2","3");

            allServiceItemsDisplayDB.setItemDisplayInfo(itemDisplayInfo);
            config.setSearch("1");
            config.setAllServiceItemsDisplay(allServiceItemsDisplayDB);
            config.setAuthorityEnabled(0);
        }
        return config;
    }


    /**
     * 查询服务部门
     *
     * @return
     */
    private List<LocalDept> getServiceDept() {
        List<DeptInfo> list = new ArrayList<>();

        DeptRequest deptRequest = new DeptRequest();
        deptRequest.setUserId("");
        HttpEntity entity = new HttpEntity(deptRequest);
        DeptResponse deptResponse = null;
        try {
            deptResponse = RestTemplateUtils.post("/coosk/itemManager/getServiceDeptList", entity, DeptResponse.class).getBody();
            logger.debug("调用服务部门返回结果..." + JSON.toJSONString(deptResponse));
        } catch (Exception e) {
            logger.error("调用服务部门,返回错误信息" + e);
        }
        if (null != deptResponse && deptResponse.getErrcode().equals(SUCCESS_CODE)) {
            list = deptResponse.getData();
        }
        return toLocalDept(list);
    }

    private List<ThemeInfo> getServiceSubject(String lang) {
        List<ThemeInfo> list = new ArrayList<>();

        ThemeRequest themeRequest = new ThemeRequest();
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if(null!=currentUser){
            themeRequest.setUserId(currentUser.getWid());
        }else if(StringUtils.isNotEmpty(lang)){
            themeRequest.setLang(lang);
        }
        HttpEntity entity = new HttpEntity(themeRequest);

        ThemeResponse themeResponse = null;
        try {
            themeResponse = RestTemplateUtils.post("/coosk/itemManager/getServiceSubjectList", entity, ThemeResponse.class).getBody();
            logger.debug("调用服务主题返回结果..." + JSON.toJSONString(themeResponse));
        } catch (Exception e) {
            logger.error("调用服务主题,返回错误信息" + e);
        }
        if (null != themeResponse && themeResponse.getErrcode().equals(SUCCESS_CODE)) {
            list = themeResponse.getData();
        }

        return list;
    }

    /**
     * 查询事项服务对象
     *
     * @return
     */
    private List<Role> getServiceObject(String lang) {
        List<Role> list = new ArrayList<>();

        RoleRequest roleRequest = new RoleRequest();
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if(null!=currentUser){
            roleRequest.setUserId(currentUser.getWid());
            roleRequest.setLang(lang);
        }
        roleRequest.setIsRelate(0);
        HttpEntity entity = new HttpEntity(roleRequest);
        RoleResponse roleResponse = null;
        try {
            roleResponse = RestTemplateUtils.post("/coosk/itemManager/allRoleList", entity, RoleResponse.class).getBody();
            logger.debug("调用服务对象返回结果..." + JSON.toJSONString(roleResponse));
        } catch (Exception e) {
            logger.error("调用服务对象,返回错误信息" + e);
        }
        if (null != roleResponse && roleResponse.getErrcode().equals(SUCCESS_CODE)) {
            list = roleResponse.getData();
        }

        return list;
    }

    /**
     * 获取责任部门
     *
     * @return
     */
    private List<LocalDept> getDutyDept(String lang) {
        List<DeptInfo> list = new ArrayList<>();

        DeptRequest deptRequest = new DeptRequest();
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if(null!=currentUser){
            deptRequest.setUserId(currentUser.getWid());
        }else if(StringUtils.isNotEmpty(lang)){
            deptRequest.setLang(lang);
        }
        HttpEntity entity = new HttpEntity(deptRequest);

        DeptResponse deptResponse = null;
        try {
            deptResponse = RestTemplateUtils.post("/coosk/itemManager/getDutyDeptList", entity, DeptResponse.class).getBody();
            logger.debug("调用责任部门返回结果..." + JSON.toJSONString(deptResponse));
        } catch (Exception e) {
            logger.error("调用责任部门,返回错误信息" + e);
        }
        if (null != deptResponse && deptResponse.getErrcode().equals(SUCCESS_CODE)) {
            list = deptResponse.getData();
        }
        return toLocalDept(list);
    }

    /**
     * 服务器返回的部门转本地格式
     *
     * @param list
     * @return
     */
    private List<LocalDept> toLocalDept(List<DeptInfo> list) {
        String topWid = "-1";
        List<LocalDept> localList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getParentWid().equals(topWid)) {
                String wid = list.get(i).getDeptWid();
                LocalDept localDept = new LocalDept();
                localDept.setDeptWid(wid);
                localDept.setDeptName(list.get(i).getDeptName());
                localDept.setParentWid(list.get(i).getParentWid());
                List<DeptInfo> children = new ArrayList<>();
                //获取子部门
                for (DeptInfo deptItem : list) {
                    if (deptItem.getParentWid().equals(wid)) {
                        children.add(deptItem);
                    }
                }
                localDept.setChildren(children);
                localList.add(localDept);
            }
        }
        return localList;
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
     * 获取name对应的数据集合
     */
    private List<AllServiceItemResponse.ServiceItemModel> getList(String name, List<AllServiceItemResponse.ServiceItemModel> list) {
        List<AllServiceItemResponse.ServiceItemModel> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (AllServiceItemResponse.ServiceItemModel serviceItem : list) {
                String headChar = StringUtil.getString(serviceItem.getHeadChar());
                if (name.contains(headChar)) {
                    resultList.add(serviceItem);
                }
            }
        }
        return resultList;
    }

    /**
     * 判断name是否有对应的数据
     */
    private static boolean hasData(String name, List<NavModel> navList) {

        for (int i = 0; i < name.length(); i++) {
            String str = name.substring(i, i + 1);
            int index = AllServiceItemUtil.LETTERS.indexOf(str);
            if (navList.get(index).isHasValue()) {
                return true;
            }
        }
        return false;
    }


}
