package com.wisedu.amp.card.cus.serviceItemCategory1.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wisedu.amp.card.cus.serviceItemCategory1.model.*;
import com.wisedu.amp.card.cus.serviceItemCategory1.util.ServiveItemCategoryUtil;
import com.wisedu.amp.card.cus.serviceItemCategory1.util.ServiveItemCategoryUtil.SHOW_ROLE_RULES;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.CardConfigReq;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务事项分类
 *
 * @author 01118293
 */
@MinosSPI
public class ServiceItemCategoryCard extends AbstractCard {
    private final static Logger logger = LogManager.getLogger(ServiceItemCategoryCard.class);

    @Override
    public String getCardId () {
        return "CUS_CARD_SERVICEITEMCATEGORY1";
    }

    @Override
    public void destroy () {
        logger.debug("destroy..");
    }

    public ComponentContainer getConfig (CardConfigReq cardConfigReq) {
        ServiceItemCategoryRequest request = new ServiceItemCategoryRequest();
        HttpEntity entity = new HttpEntity(request);
        String oneCategoryListUrl = "/coosk/itemManager/getOneCategoryList";
        logger.debug("开始调用一级分类维度接口,url=" + oneCategoryListUrl, "参数=" + JSON.toJSONString(entity));
        ServiceItemCategoryResponse response = RestTemplateUtils.post(oneCategoryListUrl, entity, ServiceItemCategoryResponse.class).getBody();
        List<Map<String, Object>> value = Lists.newArrayList();
        if ( null!=response&&ServiveItemCategoryUtil.ResponseStatus.SUCCESS.getIndex().equals(response.getErrcode()) ) {
            logger.debug("一级分类维度接口返回结果..." + JSON.toJSONString(response));
            Object data = response.getData();
            List<CategoryModel> categoryList = JSON.parseArray(JSON.toJSONString(data), CategoryModel.class);
            for ( CategoryModel category : categoryList ) {
                Map<String, Object> map = new HashMap<>();
                map.put("label", "按" + category.getCategoryName() + "分类");
                if ( category.getCategoryName().equals("按部门分类") ) {
                    map.put("label", category.getCategoryName());
                }
                map.put("value", category.getCategoryWid());
                value.add(map);
            }
        }
        return cardConfigReq.getComponentContainer().setData("resultItem", Global.ComponentParam.DATAS, value);
    }

    @Override
    public Object execDispatcher (CardAjaxRequest request) {
        Object result = null;
        switch ( request.getMethod() ) {
            case "render":
                result = this.view(request);
                break;
            case "renderData":
                result = this.viewData2(request);
                break;
            case "getMarkNumber":
                result = 0;
                break;
        }

        return result;
    }

    private String view (CardAjaxRequest request) {
        logger.debug("serviceItemCategory doAjax..");

        //获取传参
       /* String sourceItem="";
        String categoryItem = "";
        Map<String,String> param=request.getParam();
        if(null!=param && null!=param.get(ServiveItemCategoryUtil.ROLE_WID)){
            sourceItem=param.get(ServiveItemCategoryUtil.ROLE_WID);
        }
        if(null!=param && null!=param.get(ServiveItemCategoryUtil.CATEGORY_WID)){
            categoryItem = param.get(ServiveItemCategoryUtil.CATEGORY_WID);
        }

        //平铺状态下获取参数
        String s = param.get("tileParams");
        List<TileParamModel> tileParamModels = new ArrayList<>();
        if(!StringUtils.isEmpty(s)){
            JSONObject jsonObject = JSON.parseObject(s);
            String tileParams = jsonObject.getString("tileParams");
             tileParamModels = JSON.parseArray(tileParams, TileParamModel.class);

        }

        //获取后台配置配置
        ConfigInfo config = getServiceItemCategoryConfig(request);

        Template t;
        if(StringUtils.isEmpty(request.getTarget())) {
            //获取角色展示方式 平铺 || Tab
            if(ServiveItemCategoryUtil.SHOW_ROLE_RULES.TILE.getIndex().equals(config.getShowRoleRules())){
                t = groupTemplate.getTemplate("/" + this.getCardId() + "/tileView.html");
            }else {
                t = groupTemplate.getTemplate("/" + this.getCardId() + "/views.html");
            }
        }else{
            if(ServiveItemCategoryUtil.SHOW_ROLE_RULES.TILE.getIndex().equals(config.getShowRoleRules())){
                t = groupTemplate.getAjaxTemplate("/" + this.getCardId() + "/tileView.html",request.getTarget());
            }else {
                t = groupTemplate.getAjaxTemplate("/" + this.getCardId() + "/views.html", request.getTarget());
            }
        }

        //分类展示
        //获取一级分类数据
        List<CategoryModel> list = this.getCheckedOneCategoryList(config);
        t.binding(ServiveItemCategoryUtil.CATEGORYITEM,list);
        //展示行列
        t.binding(ServiveItemCategoryUtil.ROWS,config.getShowRows());
        t.binding(ServiveItemCategoryUtil.COLUMNS,config.getRowNumber());
        //内容
        t.binding(ServiveItemCategoryUtil.CONTENT,config.getContentRules());
        //获取用户信息
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
       //获取tab
        List<RoleModel> tabList = this.getRole(config,user);
        t.binding(ServiveItemCategoryUtil.TAB,tabList);

        //初始化一级分类 默认为部门分类
        if(StringUtils.isEmpty(categoryItem) && list.size()>0){
            categoryItem = list.get(0).getCategoryWid();
        }
        //绑定返回信息
        if(ServiveItemCategoryUtil.SHOW_ROLE_RULES.TAB.getIndex().equals(config.getShowRoleRules())){
            //初始化第一个tab数据
            if(StringUtils.isEmpty(sourceItem) && tabList.size() > 0){
                sourceItem = tabList.get(0).getRoleWid();
            }
            List<ServiceItemCategoryModel>  appList = getItemList(user,config,sourceItem,categoryItem);
            t.binding(ServiveItemCategoryUtil.ITEM_INFO,appList);
        }else if(ServiveItemCategoryUtil.SHOW_ROLE_RULES.TILE.getIndex().equals(config.getShowRoleRules())){
            //重新组装数据
            List<TileDataModel> tileList = getTileItemList(user,config,categoryItem,tileParamModels);
            t.binding(ServiveItemCategoryUtil.ITEM_INFO,tileList);
        }

        t.binding(ServiveItemCategoryUtil.CARD_WID, request.getCardWid());
        t.binding(ServiveItemCategoryUtil.CARD_ID, request.getCardId());*/

        return null;
    }

    private Map<String, Object> viewData (CardAjaxRequest request) {
        /*
        logger.debug("serviceItemCategory doAjax..");

        //获取传参
        String sourceItem="";
        String categoryItem = "";
        Map<String, Object> result = new HashMap<>(17);
        Map<String,String> param=request.getParam();
        if(null!=param && null!=param.get(ServiveItemCategoryUtil.ROLE_WID)){
            sourceItem=param.get(ServiveItemCategoryUtil.ROLE_WID);
        }
        if(null!=param && null!=param.get(ServiveItemCategoryUtil.CATEGORY_WID)){
            categoryItem = param.get(ServiveItemCategoryUtil.CATEGORY_WID);
        }

        //平铺状态下获取参数
        String s = param.get("tileParams");
        List<TileParamModel> tileParamModels = new ArrayList<>();
        if(!StringUtils.isEmpty(s)){
            JSONObject jsonObject = JSON.parseObject(s);
            String tileParams = jsonObject.getString("tileParams");
            tileParamModels = JSON.parseArray(tileParams, TileParamModel.class);

        }

        //获取后台配置配置
        ConfigInfo config = getServiceItemCategoryConfig(request);

        //分类展示
        //获取一级分类数据
        List<CategoryModel> list = this.getCheckedOneCategoryList(config);
        result.put(ServiveItemCategoryUtil.CATEGORYITEM,list);
        //展示行列
        result.put(ServiveItemCategoryUtil.ROWS,config.getShowRows());
        result.put(ServiveItemCategoryUtil.COLUMNS,config.getRowNumber());
        //内容
        result.put(ServiveItemCategoryUtil.CONTENT,config.getContentRules());
        //获取用户信息
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        //获取tab
        List<RoleModel> tabList = this.getRole(config,user);
        result.put(ServiveItemCategoryUtil.TAB,tabList);

        //初始化一级分类 默认为部门分类
        if(StringUtils.isEmpty(categoryItem) && list.size()>0){
            categoryItem = list.get(0).getCategoryWid();
        }
        //绑定返回信息
        if(ServiveItemCategoryUtil.SHOW_ROLE_RULES.TAB.getIndex().equals(config.getShowRoleRules())){
            //初始化第一个tab数据
            if(StringUtils.isEmpty(sourceItem) && tabList.size() > 0){
                sourceItem = tabList.get(0).getRoleWid();
            }
            List<ServiceItemCategoryModel>  appList = getItemList(user,config,sourceItem,categoryItem);
            result.put(ServiveItemCategoryUtil.ITEM_INFO,appList);
            result.put("showType",ServiveItemCategoryUtil.SHOW_ROLE_RULES.TAB.getIndex());
        }else if(ServiveItemCategoryUtil.SHOW_ROLE_RULES.TILE.getIndex().equals(config.getShowRoleRules())){
            //重新组装数据
            List<TileDataModel> tileList = getTileItemList(user,config,categoryItem,tileParamModels);
            result.put(ServiveItemCategoryUtil.ITEM_INFO,tileList);
            result.put("showType",ServiveItemCategoryUtil.SHOW_ROLE_RULES.TILE.getIndex());
        }

        result.put(ServiveItemCategoryUtil.CARD_WID, request.getCardWid());
        result.put(ServiveItemCategoryUtil.CARD_ID, request.getCardId());

        return result;*/
        return null;
    }

    /**
     * 获取平铺展现方式的数据
     *
     * @param user
     * @param config
     * @param categoryItem
     * @return
     */
    private List<TileDataModel> getTileItemList (UserInfo user, Map<String, Object> config, String categoryItem, List<TileParamModel> tileParamModels, String lang) {
        List<TileDataModel> appList = getTileServiceItemByCategoryAndRole(user, config, categoryItem, tileParamModels, lang);
        return new ArrayList<>(appList);
    }

    /**
     * 获取平铺展现方式下对应角色的服务对象
     *
     * @param user
     * @param config
     * @return
     */
    private List<TileDataModel> getTileServiceItemByCategoryAndRole (UserInfo user, Map<String, Object> config, String categoryItem,
                                                                     List<TileParamModel> tileParamModels, String lang) {
        List<TileDataModel> respList = new ArrayList<>();
        String configPreLogin = String.valueOf(config.get("notLogin"));
        String afterLogin = String.valueOf(config.get("logged"));
        //获取服务对象与一级分类
        List<RoleModel> role = getRole(config, user, lang);
        List<CategoryModel> checkedOneCategoryList = getCheckedOneCategoryList(config, lang);
        String serviceListUrl = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ServiveItemCategoryUtil.RequestUrl.ALL_SUBJECT_URL.getIndex());
        if ( ! CollectionUtils.isEmpty(role) && ! CollectionUtils.isEmpty(checkedOneCategoryList) ) {
            String roleWids = "";
            ServiceItemCategoryRequest itemCategoryRequest = new ServiceItemCategoryRequest();
            for ( RoleModel roleModel : role ) {
                TileDataModel dataModel = new TileDataModel();
                String roleWid = roleModel.getRoleWid();
                String roleName = roleModel.getRoleName();
                dataModel.setRoleWid(roleWid);
                dataModel.setRoleName(roleName);
                //先展示默认的一级分类
                itemCategoryRequest.setCategoryWid(categoryItem);
                //如果tileParamModels不为空的话说明是ajax刷新
                if ( ! CollectionUtils.isEmpty(tileParamModels) ) {
                    for ( TileParamModel model : tileParamModels ) {
                        if ( roleModel.getRoleWid().equals(model.getRoleWid()) ) {
                            itemCategoryRequest.setCategoryWid(model.getCategoryWid());
                        }
                    }
                }
                itemCategoryRequest.setRoleWid(roleModel.getRoleWid());
                if ( user == null ) {
                    itemCategoryRequest.setIsRelate(Integer.parseInt(configPreLogin));
                } else {
                    itemCategoryRequest.setUserId(user.getWid());
                    itemCategoryRequest.setUserAccount(user.getUserAccount());
                    itemCategoryRequest.setIsRelate(Integer.parseInt(afterLogin));
                }
                roleWids += roleWid + ",";
            }
            itemCategoryRequest.setLang(lang);
            itemCategoryRequest.setRoleWid(roleWids.substring(0, roleWids.length() - 1));
            HttpEntity entity = new HttpEntity(itemCategoryRequest);
            logger.debug("开始调用按服务对象服务分类批量获取服务主题接口,url=" + serviceListUrl + ",参数=" + JSON.toJSONString(entity));
            serviceListUrl = "/coosk/itemManager/getBatchSubjectByRoleCateGoryList";
            ServiceItemCategoryResponse response = RestTemplateUtils.post(serviceListUrl, entity, ServiceItemCategoryResponse.class).getBody();
            logger.debug("按服务对象服务分类批量获取服务主题接口返回结果..." + JSON.toJSONString(response));
            ServiceItemCategoryResponse resp = JSON.parseObject(JSON.toJSONString(response), ServiceItemCategoryResponse.class);
            Object data = resp.getData();
            List<ServiceItemCategoryModelResponse> itemList = JSON.parseArray(JSON.toJSONString(data), ServiceItemCategoryModelResponse.class);
            Map<String, List<ServiceItemCategoryModel>> serviceItemModelsMap = new HashMap<>(10);
            itemList.forEach(it -> {
                List<ServiceItemCategoryModel> serviceItemModels = handleServiceItemList1(it.getData());
                serviceItemModelsMap.put(it.getRoleWid(), serviceItemModels);
            });
            role.stream().forEach(it -> {
                TileDataModel dataModel = new TileDataModel();
                String roleWid = it.getRoleWid();
                dataModel.setRoleWid(roleWid);
                dataModel.setRoleName(it.getRoleName());
                List<ServiceItemCategoryModel> itemModelList = serviceItemModelsMap.get(roleWid);
                dataModel.setItemModelList(itemModelList != null ? itemModelList : new ArrayList<>());
                respList.add(dataModel);
            });
        }
        return respList;
    }

    /**
     * 获取后台配置的一级分类
     *
     * @param config
     * @return List<CategoryModel>
     */
    private List<CategoryModel> getCheckedOneCategoryList (Map<String, Object> config, String lang) {
        List<CategoryModel> oneCategoryList = this.getOneCategoryList(config, lang);
        List<CategoryModel> respList = new ArrayList<>();
        List<String> result = JSON.parseArray(JSON.toJSONString(config.get("resultItem")), String.class);
        if ( ! CollectionUtils.isEmpty(oneCategoryList) ) {
            for ( CategoryModel categoryModel : oneCategoryList ) {
                if ( result.contains(categoryModel.getCategoryWid()) ) {
                    String categoryName = categoryModel.getCategoryName();
                    if ( categoryName.length() > 10 ) {
                        categoryModel.setShowCategoryName(categoryName.substring(0, 10) + "...");
                    } else {
                        categoryModel.setShowCategoryName(categoryName);
                    }
                    respList.add(categoryModel);
                }
            }
        }
        if ( respList.isEmpty() ) {
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setShowCategoryName("按部门分类");
            categoryModel.setCategoryName("按部门分类");
            categoryModel.setCategoryWid("dept-subject");
            respList.add(categoryModel);
        }
        return respList;
    }

    /**
     * 获取展示的数据
     *
     * @param user
     * @param configInfo
     * @param sourceItem
     * @return
     */
    private List<ServiceItemCategoryModel> getItemList (UserInfo user, Map<String, Object> configInfo,
                                                        String sourceItem, String categoryItem, String lang) {
        /*List<ServiceItemCategoryModel> newAppList;
        //获取展示行数以及一行展示数量
        int rows = Integer.parseInt(String.valueOf(configInfo.get("rows")));
        int columns = Integer.parseInt(String.valueOf(configInfo.get("columns")));
        if(appList.size() >= rows*columns){
            newAppList = new ArrayList<>(appList.subList(0,rows*columns));
        }else{
            newAppList = new ArrayList<>(appList);
        }*/
        return getServiceItemByCategoryAndRole(user, configInfo, sourceItem, categoryItem, false, lang);
    }

    /**
     * 获取后台配置
     *
     * @param request
     * @return
     */
    private ConfigInfo getServiceItemCategoryConfig (CardAjaxRequest request) {
        String configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(), request.getCardId());
        if ( org.apache.commons.lang.StringUtils.isNotEmpty(configInfo) ) {
            configInfo = JSON.parse(configInfo).toString();
        }
        ConfigInfo config = JSONObject.parseObject(configInfo, ConfigInfo.class);
        if ( null == config ) {
            config = new ConfigInfo();
        }
        return config;
    }

    /**
     * 获取一级分类
     *
     * @param config
     * @return
     */
    private List<CategoryModel> getOneCategoryList (Map<String, Object> config, String lang) {
        List<CategoryModel> respList;
        ServiceItemCategoryRequest request = new ServiceItemCategoryRequest();
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        if ( null != currentUser ) {
            request.setUserId(currentUser.getWid());
        } else {
            request.setLang(lang);
        }
        HttpEntity entity = new HttpEntity(request);
        //地址先写死
        String oneCategoryListUrl = "/coosk/itemManager/getOneCategoryList";
        logger.debug("开始调用一级分类维度接口,url=" + oneCategoryListUrl, "参数=" + JSON.toJSONString(entity));
        ServiceItemCategoryResponse response = RestTemplateUtils.post(oneCategoryListUrl, entity, ServiceItemCategoryResponse.class).getBody();
        logger.debug("一级分类维度接口返回结果..." + JSON.toJSONString(response));
        respList = handleCategoryList(response, config);
        return respList;
    }

    /**
     * 处理一级分类维度数据
     *
     * @param response
     * @return
     */
    private List<CategoryModel> handleCategoryList (ServiceItemCategoryResponse response, Map<String, Object> config) {
        List<CategoryModel> categoryList = new ArrayList<>();
        if ( ServiveItemCategoryUtil.ResponseStatus.SUCCESS.getIndex().equals(response.getErrcode()) ) {
            Object data = response.getData();
            categoryList = JSON.parseArray(JSON.toJSONString(data), CategoryModel.class);
            //将按部门分类置为第一个
            List<CategoryModel> finalCategoryList = categoryList;
            categoryList.forEach(s -> {
                int index = finalCategoryList.indexOf(s);
                if ( ServiveItemCategoryUtil.DEFAULT_CATEGORY.equals(s.getCategoryName()) ) {
                    Collections.swap(finalCategoryList, index, 0);
                } else {
                    s.setCategoryName("按" + s.getCategoryName() + "分类");
                }
            });
        }
        return categoryList;
    }

    /**
     * 获取所有服务对象
     *
     * @param config
     * @param user
     * @return
     */
    public List<RoleModel> getRole (Map<String, Object> config, UserInfo user, String lang) {
        List<RoleModel> respList;
        ServiceItemCategoryRequest roleRequest = new ServiceItemCategoryRequest();
        String configPreLogin = String.valueOf(config.get("notLogin"));
        String afterLogin = String.valueOf(config.get("logged"));
        if ( null == user ) {
            roleRequest.setUserId(ServiveItemCategoryUtil.GUEST);
            roleRequest.setIsRelate(Integer.parseInt(configPreLogin));
            roleRequest.setLang(lang);
        } else {
            String userWid = user.getWid();
            roleRequest.setUserId(userWid);
            roleRequest.setLang(user.getPreferredLanguage());
            roleRequest.setIsRelate(Integer.parseInt(afterLogin));
        }
        String allRoleListUrl = "/coosk/itemManager/allRoleList";
        HttpEntity entity = new HttpEntity(roleRequest);
        logger.debug("开始调用所有服务对象接口,url=" + allRoleListUrl, "参数=" + JSON.toJSONString(entity));
        ServiceItemCategoryResponse response = RestTemplateUtils.post(allRoleListUrl, entity, ServiceItemCategoryResponse.class).getBody();
        logger.debug("所有服务对象接口返回结果..." + JSON.toJSONString(response));
        respList = handleRoleList(response);
        return respList;
    }


    /**
     * 获取所有服务对象
     *
     * @param user
     * @return
     */
    public List<RoleModel> getTotalRole (Map<String, Object> config, UserInfo user, String lang) {
        List<RoleModel> respList;
        ServiceItemCategoryRequest roleRequest = new ServiceItemCategoryRequest();
        String configPreLogin = String.valueOf(config.get("notLogin") == null ? 0 : config.get("notLogin"));
        String afterLogin = String.valueOf(config.get("logged") == null ? 0 : config.get("logged"));
        if ( null == user ) {
            roleRequest.setUserId(ServiveItemCategoryUtil.GUEST);
            roleRequest.setIsRelate(Integer.parseInt(configPreLogin));
            roleRequest.setLang(lang);
        } else {
            String userWid = user.getWid();
            roleRequest.setUserId(userWid);
            roleRequest.setLang(user.getPreferredLanguage());
            roleRequest.setIsRelate(Integer.parseInt(afterLogin));
        }
//        roleRequest.setIsRelate(0);
        String allRoleListUrl = "/coosk/itemManager/allRoleList";
        HttpEntity entity = new HttpEntity(roleRequest);
        ServiceItemCategoryResponse response = RestTemplateUtils.post(allRoleListUrl, entity, ServiceItemCategoryResponse.class).getBody();
        respList = handleRoleList(response);
        return respList;
    }

    /**
     * 处理所有对象数据
     *
     * @param response
     * @return
     */
    private List<RoleModel> handleRoleList (ServiceItemCategoryResponse response) {
        List<RoleModel> roleList = new ArrayList<>();
        if ( ServiveItemCategoryUtil.ResponseStatus.SUCCESS.getIndex().equals(response.getErrcode()) ) {
            Object data = response.getData();
            roleList = JSON.parseArray(JSON.toJSONString(data), RoleModel.class);
            if ( CollectionUtils.isEmpty(roleList) ) {
                logger.debug("获取的服务对象为空");
            } else {
                for ( RoleModel roleModel : roleList ) {
                    String name = roleModel.getRoleName();
                    if ( name.length() > 10 ) {
                        roleModel.setShowRoleName(name.substring(0, 10) + "...");
                    } else {
                        roleModel.setShowRoleName(name);
                    }

                }
            }
        }
        return roleList;
    }

    /**
     * 按服务对象服务分类获取服务主题
     *
     * @param user
     * @param config
     * @return
     */
    private List<ServiceItemCategoryModel> getServiceItemByCategoryAndRole (UserInfo user, Map<String, Object> config, String sourceItem,
                                                                            String categoryItem, boolean mobilePlatForm, String lang) {
        List<ServiceItemCategoryModel> respList;
        ServiceItemCategoryRequest itemCategoryRequest = new ServiceItemCategoryRequest();
        itemCategoryRequest.setRoleWid(sourceItem);
        itemCategoryRequest.setCategoryWid(categoryItem);
        if ( mobilePlatForm ) {
            itemCategoryRequest.setIsRelate(0);
        } else {
            int configPreLogin = Integer.parseInt(String.valueOf(config.get("notLogin")));
            int afterLogin = Integer.parseInt(String.valueOf(config.get("logged")));
            if ( user == null ) {
                itemCategoryRequest.setIsRelate(configPreLogin);
            } else {
                itemCategoryRequest.setIsRelate(afterLogin);
            }
        }
        if ( null != user ) {
            itemCategoryRequest.setUserId(user.getWid());
            itemCategoryRequest.setUserAccount(user.getUserAccount());
            itemCategoryRequest.setLang(user.getPreferredLanguage());
        } else {
            itemCategoryRequest.setLang(lang);
        }
        String resultWayStr = ObjectUtils.isEmpty(config.get("resultWay")) ? "[]" : JSON.toJSONString(config.get("resultWay"));
        List<String> list = Lists.newArrayList();
        if ( resultWayStr.startsWith("[") && resultWayStr.endsWith("]") ) {
            list = JSON.parseArray(resultWayStr, String.class);
        }
        if ( ! CollectionUtils.isEmpty(list) && "0".equals(list.get(0)) && "1".equals(list.get(1)) ) {
            itemCategoryRequest.setOrderByVisitCount(true);
        } else {
            itemCategoryRequest.setOrderByVisitCount(false);
        }

        HttpEntity entity = new HttpEntity(itemCategoryRequest);
        String allServiceListUrl = "/coosk/itemManager/getSubjectByRoleCateGoryList";
        logger.debug("开始调用按服务对象服务分类获取服务主题接口,url=" + allServiceListUrl, "参数=" + JSON.toJSONString(entity));
        ServiceItemCategoryResponse response = RestTemplateUtils.post(allServiceListUrl, entity, ServiceItemCategoryResponse.class).getBody();
        logger.debug("按服务对象服务分类获取服务主题接口返回结果..." + JSON.toJSONString(response));
        respList = handleServiceItemList(response);
        return respList;
    }


    /**
     * 处理服务主题数据
     *
     * @param response
     * @return
     */
    private List<ServiceItemCategoryModel> handleServiceItemList (ServiceItemCategoryResponse response) {
        List<ServiceItemCategoryModel> itemList = new ArrayList<>();
        if ( ServiveItemCategoryUtil.ResponseStatus.SUCCESS.getIndex().equals(response.getErrcode()) ) {
            Object data = response.getData();
            itemList = JSON.parseArray(JSON.toJSONString(data), ServiceItemCategoryModel.class);
            //组装二级分类数据
            for ( ServiceItemCategoryModel model : itemList ) {
                StringBuilder secondCategory = new StringBuilder();
                List<ServiceItemModel> serviceItemModelList = model.getChildren();
                if ( ! CollectionUtils.isEmpty(serviceItemModelList) ) {
                    for ( int i = 0 ; i < serviceItemModelList.size() ; i++ ) {
                        String subjectName = serviceItemModelList.get(i).getSubjectName();
                        if ( ! StringUtils.isEmpty(subjectName) && i < serviceItemModelList.size() - 1 ) {
                            secondCategory.append(subjectName + " | ");
                        }
                        if ( ! StringUtils.isEmpty(subjectName) && i == serviceItemModelList.size() - 1 ) {
                            secondCategory.append(subjectName);
                        }
                    }
                    model.setSecondCategory(secondCategory.toString());
                }
            }
        }
        //循环获取当前级别和所有下级有没有itemList，如果没有的话则把父级去除
        for ( int i = itemList.size() - 1 ; i >= 0 ; i-- ) {
            //如果当前事项列表为空
            if ( null == itemList.get(i).getItemList() || itemList.get(i).getItemList().size() <= 0 ) {
                List<ServiceItemModel> list = itemList.get(i).getChildren();
                //如果child为空直接去除当前分类
                if ( null == list || list.isEmpty() ) {
                    itemList.remove(i);
                } else {
                    boolean flag = false;
                    //循环所有chid，如果所有child的事项列表都是空，则删除
                    for ( int j = 0 ; j < list.size() ; j++ ) {
                        if ( null != list.get(j).getItemList() && list.get(j).getItemList().size() > 0 ) {
                            flag = true;
                        }
                    }
                    //如果都没有事项列表则删除
                    if ( ! flag ) {
                        itemList.remove(i);
                    }
                }
            }
        }
        //将子集数据给到父级
        for ( int i = 0 ; i < itemList.size() ; i++ ) {
            List<ItemModel> newItemList = itemList.get(i).getItemList();
            if ( null == newItemList ) {
                newItemList = new ArrayList<ItemModel>();
            }
            //获取子集列表
            List<ServiceItemModel> list = itemList.get(i).getChildren();
            if ( null != list && ! list.isEmpty() ) {
                for ( int j = 0 ; j < list.size() ; j++ ) {
                    //循环获取子集事项数据
                    List<ItemModel> modelList = list.get(j).getItemList();
                    if ( null != modelList && ! modelList.isEmpty() ) {
                        newItemList.addAll(modelList);
                    }
                }
            }
            itemList.get(i).setItemList(newItemList);
        }
        return itemList;
    }

    /**
     * 处理服务主题数据
     *
     * @param response
     * @return
     */
    private List<ServiceItemCategoryModel> handleServiceItemList1 (List<ServiceItemCategoryModel> response) {
        List<ServiceItemCategoryModel> itemList = response;
        //循环获取当前级别和所有下级有没有itemList，如果没有的话则把父级去除
        for ( int i = itemList.size() - 1 ; i >= 0 ; i-- ) {
            //如果当前事项列表为空
            if ( null == itemList.get(i).getItemList() || itemList.get(i).getItemList().size() <= 0 ) {
                List<ServiceItemModel> list = itemList.get(i).getChildren();
                //如果child为空直接去除当前分类
                if ( null == list || list.isEmpty() ) {
                    itemList.remove(i);
                } else {
                    boolean flag = false;
                    //循环所有chid，如果所有child的事项列表都是空，则删除
                    for ( int j = 0 ; j < list.size() ; j++ ) {
                        if ( null != list.get(j).getItemList() && list.get(j).getItemList().size() > 0 ) {
                            flag = true;
                        }
                    }
                    //如果都没有事项列表则删除
                    if ( ! flag ) {
                        itemList.remove(i);
                    }
                }
            }
        }
        //将子集数据给到父级
        for ( int i = 0 ; i < itemList.size() ; i++ ) {
            List<ItemModel> newItemList = itemList.get(i).getItemList();
            if ( null == newItemList ) {
                newItemList = new ArrayList<ItemModel>();
            }
            //获取子集列表
            List<ServiceItemModel> list = itemList.get(i).getChildren();
            if ( null != list && ! list.isEmpty() ) {
                for ( int j = 0 ; j < list.size() ; j++ ) {
                    //循环获取子集事项数据
                    List<ItemModel> modelList = list.get(j).getItemList();
                    if ( null != modelList && ! modelList.isEmpty() ) {
                        newItemList.addAll(modelList);
                    }
                }
            }
            itemList.get(i).setItemList(newItemList);
        }
        //组装二级分类数据
        for ( ServiceItemCategoryModel model : itemList ) {
            StringBuilder secondCategory = new StringBuilder();
            List<ServiceItemModel> serviceItemModelList = model.getChildren();
            if ( ! CollectionUtils.isEmpty(serviceItemModelList) ) {
                for ( int i = 0 ; i < serviceItemModelList.size() ; i++ ) {
                    String subjectName = serviceItemModelList.get(i).getSubjectName();
                    if ( ! StringUtils.isEmpty(subjectName) && i < serviceItemModelList.size() - 1 ) {
                        secondCategory.append(subjectName + " | ");
                    }
                    if ( ! StringUtils.isEmpty(subjectName) && i == serviceItemModelList.size() - 1 ) {
                        secondCategory.append(subjectName);
                    }
                }
                model.setSecondCategory(secondCategory.toString());
            }
        }
        return itemList;
    }


    private Map<String, Object> viewData2 (CardAjaxRequest request) {
        logger.debug("serviceItemCategory doAjax..");
        //获取传参
        String sourceItem = "";
        String categoryItem = "";
        Map<String, Object> result = new HashMap<>(17);
        Map<String, String> param = request.getParam();
        if ( null != param && null != param.get(ServiveItemCategoryUtil.ROLE_WID) ) {
            sourceItem = param.get(ServiveItemCategoryUtil.ROLE_WID);
        }
        if ( null != param && null != param.get(ServiveItemCategoryUtil.CATEGORY_WID) ) {
            categoryItem = param.get(ServiveItemCategoryUtil.CATEGORY_WID);
        }
        //平铺状态下获取参数
        String s = param.get("tileParams");
        List<TileParamModel> tileParamModels = new ArrayList<>();
        if ( ! StringUtils.isEmpty(s) ) {
            JSONObject jsonObject = JSON.parseObject(s);
            String tileParams = jsonObject.getString("tileParams");
            tileParamModels = JSON.parseArray(tileParams, TileParamModel.class);

        }
        //获取后台配置配置
        Map<String, Object> config = getConfigInfo(request);
        String lang = Global.DEFAULT_LANGUAGE;
        //加遊客狀態前台傳入的瀏覽器語言參數
        if ( ! request.getParam().isEmpty() && ! StringUtils.isEmpty(request.getParam().get("lang")) ) {
            lang = request.getParam().get("lang");
        }
        //获取一级分类数据
        List<CategoryModel> list = this.getCheckedOneCategoryList(config, lang);
        result.put(ServiveItemCategoryUtil.CATEGORYITEM, list);
        //获取用户信息
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        //获取tab
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        List<RoleModel> tabList = null;
        if ( isMobileDevice ) {
            tabList = this.getTotalRole(config, user, lang);
        } else {
            tabList = this.getRole(config, user, lang);
        }

        result.put(ServiveItemCategoryUtil.TAB, tabList);

        List<String> relateList = Lists.newArrayList();
        if ( ! CollectionUtils.isEmpty(tabList) ) {
            relateList = tabList.stream().filter(e -> "1".equals(e.getIsRelate())).map(RoleModel::getRoleWid).collect(Collectors.toList());
        }
        // 返回当前用户的有权限的服务对象id
        result.put(ServiveItemCategoryUtil.RELATE_LIST, relateList);
        result.put(ServiveItemCategoryUtil.TAB, tabList);
        result.put("config", config);

        //初始化一级分类 默认为部门分类
        if ( StringUtils.isEmpty(categoryItem) && ! list.isEmpty() ) {
            categoryItem = list.get(0).getCategoryWid();
        }
        //绑定返回信息
        if ( isMobileDevice ) {
            //初始化第一个tab数据
            if ( StringUtils.isEmpty(sourceItem) && ! tabList.isEmpty() ) {
                sourceItem = tabList.get(0).getRoleWid();
            }
            List<ServiceItemCategoryModel> appList = getServiceItemByCategoryAndRole(user, config, sourceItem, categoryItem, true, lang);
            result.put(ServiveItemCategoryUtil.ITEM_INFO, appList);
        } else if ( SHOW_ROLE_RULES.TAB.getIndex() == Integer.parseInt(String.valueOf(config.get("roleTab"))) ) {
            //初始化第一个tab数据
            if ( StringUtils.isEmpty(sourceItem) && ! tabList.isEmpty() ) {
                sourceItem = tabList.get(0).getRoleWid();
            }
            List<ServiceItemCategoryModel> appList = getItemList(user, config, sourceItem, categoryItem, lang);
            result.put(ServiveItemCategoryUtil.ITEM_INFO, appList);
        } else if ( SHOW_ROLE_RULES.TILE.getIndex() == Integer.parseInt(String.valueOf(config.get("roleTab"))) ) {
            //重新组装数据
            List<TileDataModel> tileList = getTileItemList(user, config, categoryItem, tileParamModels, lang);
            result.put(ServiveItemCategoryUtil.ITEM_INFO, tileList);
        }
        return result;
    }

    //配置信息
    private Map<String, Object> getConfigInfo (CardAjaxRequest request) {
        //先查询传参，如果传参没有则需要查询库，在没有则初始化默认
        String configInfo;
        if ( null != request.getParam() && null != request.getParam().get("config") ) {
            configInfo = request.getParam().get("config");
        } else {
            configInfo = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(), request.getCardId());
        }
        logger.debug("卡片配置信息:" + configInfo);
        if ( org.apache.commons.lang.StringUtils.isNotEmpty(configInfo) ) {
            configInfo = JSON.parse(configInfo).toString();
        }
        return toMap(configInfo);
    }

    public static final String BRACKETS = "[]";

    private static Map<String, Object> toMap (String json) {
        Map<String, Object> map = new HashMap<>();
        if ( StringUtil.isNotEmpty(json) && ! BRACKETS.equals(json) ) {
            map = JSON.parseObject(json, HashMap.class);
        }
        return map;
    }
}
