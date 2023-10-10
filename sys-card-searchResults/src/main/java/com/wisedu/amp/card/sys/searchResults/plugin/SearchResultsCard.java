package com.wisedu.amp.card.sys.searchResults.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wisedu.amp.card.sys.searchResults.model.faq.Faq;
import com.wisedu.amp.card.sys.searchResults.util.SearchResultsUtil;
import com.wisedu.amp.card.sys.searchResults.util.SortingUtil;
import com.wisedu.casp.sim.model.DubboDeptInfo;
import com.wisedu.casp.sim.model.DubboPaginationResponse;
import com.wisedu.casp.sim.model.DubboQueryDeptRequest;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.RequestUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.model.license.CooskLicenseDto;
import com.wisedu.minos.casp.portal.proxy.model.CollectObjectRequest;
import com.wisedu.minos.casp.portal.proxy.model.CollectResultResponse;
import com.wisedu.minos.casp.portal.proxy.service.CardApiProxy;
import com.wisedu.minos.casp.portal.service.impl.CommonApiAdapterImpl;
import com.wisedu.minos.casp.portal.service.impl.MinosMoreCustomGroupDataForPortalRequest;
import com.wisedu.minos.casp.portal.service.impl.MinosMoreNewsForPortalRequest;
import com.wisedu.minos.casp.portal.service.impl.MinosSearchServiceImpl;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import com.wisedu.minos.casp.portal.utils.CardDubboUtil;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.wisedu.minos.casp.portal.common.constant.Global.SEARCH_KEY_MAX_LENGTH;


@MinosSPI
public class SearchResultsCard extends AbstractCard {
    private final static Logger logger = LogManager.getLogger(SearchResultsCard.class);


    private static final String NO_SEACH_RESULT = "noSeachResult";
    private static final String SEARCH_RESULT_ITEM1 = "searchResultItem";
    public static final String SHOW_ANCHOR = "showAnchor";

    @Override
    public String getCardId() {
        return "SYS_CARD_SEARCHRESULTS";
    }

    @Override
    public void destroy() {

    }

    @Override
    public Object execDispatcher(CardAjaxRequest request) {
        Object result = null;
        switch (request.getMethod()) {
            case "renderData":
                result = this.renderData2(request);
                break;
            case "moreNewsForPortal":
                result = this.moreNewsForPortal(request);
                break;
            case "moreTdcForPortal":
                result = this.moreTdcForPortal(request);
                break;
            case "moreCustomGroupDataForPortal":
                result = this.moreCustomGroupDataForPortal(request);
                break;
            case "collectApp":
                result = this.collectApp(request);
                break;
            case "getMarkNumber":
                result = 0;
                break;
            case "sortNewsForPortal":
                result = this.sortNewsForPortal(request);
                break;
            case "getHotSearchData":
                result = this.getHotSearchData(request);
                break;
            case "getDeptInfoData":
                result = this.getDeptInfoData(request);
                break;
            default:

        }
        return result;
    }


    private CollectResultResponse collectApp(CardAjaxRequest request) {
        CardApiProxy cardApiProxy = ApplicationContextUtil.get(CardApiProxy.class);
        CollectObjectRequest collectObjectRequest = new CollectObjectRequest();
        Set<String> objectList = new HashSet<>();
        objectList.add(request.getParam().get("appId"));
        collectObjectRequest.setObjectIds(objectList);
        collectObjectRequest.setOperate(request.getParam().get("operate"));

        return cardApiProxy.collectService(collectObjectRequest);
    }

    private String getKeywords(Map<String, String> param, String keywords) {
        String keywords1 = param.get(keywords);
        return StringUtil.xssEncode(StringUtil.defaultIfEmpty(keywords1, ""));
    }

    /**
     * 获取当前用户ID
     *
     * @return
     */
    private String getCurrentUserId() {
        String userId;
        try {
            UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            if (user == null) {
                userId = "";
            } else {
                userId = user.getWid();
            }
        } catch (Exception e) {
            logger.error("获取用户信息异常", e);
            userId = "";
        }
        return userId;
    }

    private Object getHotSearchData(CardAjaxRequest request){
        //服务、事项搜索关键词
        String pageSize = request.getParam().get("pageSize");

        Map<String, Object> configMap = getConfig(request);

        String lang = ApplicationContextUtil.get(UserUtil.class).getCurrentLanguage(request);

        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        HotSearchDataRequest hotSearchDataRequest = new HotSearchDataRequest();
        hotSearchDataRequest.setLang(lang);
        hotSearchDataRequest.setUserWid(user != null ? user.getWid() : "");
        hotSearchDataRequest.setAuthorityEnabled(Integer.parseInt(String.valueOf(configMap.get("authorityEnabled"))));
        Object hotSearchDisplay = configMap.get("hotSearchDisplay");
        Map<String, Object> hotSearchDisplayObj;
        if(null != hotSearchDisplay) {
            hotSearchDisplayObj = JSONObject.parseObject(String.valueOf(hotSearchDisplay), Map.class);
        }else{
            hotSearchDisplayObj = new HashMap<>();
            hotSearchDisplayObj.put("displaySwitch", 0);
        }
        Object displaySwitch = hotSearchDisplayObj.get("displaySwitch");
        if(null == displaySwitch || 0 == Integer.valueOf(String.valueOf(displaySwitch))){
            throw new RuntimeException("当前配置不显示热门搜索，请先打开热门搜索开关");
        }

        // 根据license过滤配置
        CooskLicenseDto license = ApplicationContextUtil.get(CommonApiAdapterImpl.class).getCooskLicense().getData();
        String dataTypeConfig = null == hotSearchDisplayObj.get("dataTypes") ? "0,1,2" : String.valueOf(hotSearchDisplayObj.get("dataTypes"));
        StringBuilder dataTypeSb = new StringBuilder();
        for(String dataType : dataTypeConfig.split(",")){
            if("0".equals(dataType)){
                dataTypeSb.append(dataType).append(",");
            }else if("1".equals(dataType) && (license.isCooskSimSim() || license.isCaspSim())){
                dataTypeSb.append(dataType).append(",");
            }else if("2".equals(dataType) && license.isCooskSimOneThing()){
                dataTypeSb.append(dataType).append(",");
            }
        }
        String dataTypes = dataTypeSb.toString();
        if(dataTypes.endsWith(",")){
            dataTypes = dataTypes.substring(0, dataTypes.length() - 1);
        }
        Map<String, Object> searchResultMap = new HashMap<>();
        if(!StringUtils.isEmpty(dataTypes)) {
            hotSearchDataRequest.setDataTypes(dataTypes);
            Object timeDelay = hotSearchDisplayObj.get("timeDelay");
            hotSearchDataRequest.setTimeDelay(null == timeDelay ? 30 : Integer.parseInt(String.valueOf(timeDelay)));
            if (org.apache.commons.lang.StringUtils.isNotEmpty(pageSize)) {
                hotSearchDataRequest.setPageSize(Integer.parseInt(pageSize));
            }
            int isShowPcService = 1;
            if (MapUtils.isNotEmpty(request.getParam()) && request.getParam().containsKey("isShowPcService") && org.apache.commons.lang3.StringUtils.isNotBlank(request.getParam().get("isShowPcService"))) {
                isShowPcService = Integer.parseInt(request.getParam().get("isShowPcService"));
            }

            boolean isMobileDevice = CommonUtil.isMobileDevice();
            if (isMobileDevice) {
                if (0 == isShowPcService) {
                    hotSearchDataRequest.setServiceStation(1);
                }
            } else {
                hotSearchDataRequest.setServiceStation(0);
            }
            searchResultMap = ApplicationContextUtil.get(MinosSearchServiceImpl.class).getHotSearchData(hotSearchDataRequest);
        }
        searchResultMap.put("hotSearchConfig",hotSearchDisplayObj);
        return searchResultMap;
    }

    /**
     * 以下方法为 重构后台配置页面所重写或重构的方法
     */

    /**
     * renderData 方法
     *
     * @param request
     * @return
     */
    private Object renderData2(CardAjaxRequest request) {
        Map<String, Object> results = new HashMap<>();
        //服务、事项搜索关键词
        String keyword = org.apache.commons.lang3.StringUtils.substring(request.getParam().get("keyword"),0,SEARCH_KEY_MAX_LENGTH);
        String pageSize = request.getParam().get("pageSize");

        Map<String, Object> configMap = getConfig(request);
        Object hotSearchDisplay = configMap.get("hotSearchDisplay");
        Map<String, Object> hotSearchDisplayObj;
        if(null != hotSearchDisplay) {
            hotSearchDisplayObj = JSONObject.parseObject(String.valueOf(hotSearchDisplay), Map.class);
        }else{
            hotSearchDisplayObj = new HashMap<>();
            hotSearchDisplayObj.put("displaySwitch", 0);
        }

        String dataTypeConfig = (null == hotSearchDisplayObj.get("dataTypes")) ? "0,1,2" : String.valueOf(hotSearchDisplayObj.get("dataTypes"));
        StringBuilder dataTypeSb = new StringBuilder();
        CooskLicenseDto license = ApplicationContextUtil.get(CommonApiAdapterImpl.class).getCooskLicense().getData();
        for(String dataType : dataTypeConfig.split(",")){
            if("0".equals(dataType)){
                dataTypeSb.append(dataType).append(",");
            }else if("1".equals(dataType) && (license.isCooskSimSim() || license.isCaspSim())){
                dataTypeSb.append(dataType).append(",");
            }else if("2".equals(dataType) && license.isCooskSimOneThing()){
                dataTypeSb.append(dataType).append(",");
            }
        }
        String dataTypes = dataTypeSb.toString();
        if(dataTypes.endsWith(",")){
            dataTypes = dataTypes.substring(0, dataTypes.length() - 1);
        }
        hotSearchDisplayObj.put("dataTypes", dataTypes);
        configMap.put("hotSearchDisplay", hotSearchDisplayObj);

        //加遊客狀態前台傳入的瀏覽器語言參數
        String lang = ApplicationContextUtil.get(UserUtil.class).getCurrentLanguage(request);

        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        MinosSearchForPortalRequest searchForPortalRequest = new MinosSearchForPortalRequest();
        searchForPortalRequest.setLang(lang);
        searchForPortalRequest.setUserId(user != null ? user.getUserAccount() : "");
        searchForPortalRequest.setUserWid(user != null ? user.getWid() : "");
        if( org.apache.commons.lang3.StringUtils.isNotBlank(keyword) ){
            keyword=keyword.replaceAll("\\s*", "");
        }
        searchForPortalRequest.setSearchKey(keyword);
        searchForPortalRequest.setSortType(request.getParam().get("sortType"));
        searchForPortalRequest.setAuthorityEnabled(Integer.parseInt(String.valueOf(configMap.get("authorityEnabled"))));
        if(org.apache.commons.lang.StringUtils.isNotEmpty(pageSize)){
            searchForPortalRequest.setPageSize(Integer.parseInt(pageSize));
        }
        int isShowPcService=1;
        if( MapUtils.isNotEmpty(request.getParam())&& request.getParam().containsKey("isShowPcService")&& org.apache.commons.lang3.StringUtils.isNotBlank(request.getParam().get("isShowPcService")) ){
            isShowPcService=Integer.parseInt(request.getParam().get("isShowPcService"));
        }

        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if(isMobileDevice){
            // 看移动端是否需要展示pc端服务
//            boolean isShowPCService = ApplicationContextUtil.get(CardUtil.class).getIsShowPCService();
//            if(!isShowPCService){
//                searchForPortalRequest.setServiceStation(1);
//            }
            if(0==isShowPcService){
                searchForPortalRequest.setServiceStation(1);
            }
        } else{
            searchForPortalRequest.setServiceStation(0);
        }
        Map<String, Object> searchResultMap = ApplicationContextUtil.get(MinosSearchServiceImpl.class).searchForPortal(searchForPortalRequest);
        results.put("searchData", searchResultMap);
        results.put("config", configMap);
        results.put("userId", getCurrentUserId());
        results.put("keyword", keyword);
        results.put("cardId", request.getCardId());
        results.put("cardWid", request.getCardWid());
        results.put("isMobileDevice", isMobileDevice);

        return results;
    }

    protected Object moreNewsForPortal(CardAjaxRequest request){
        Map<String, Object> results = new HashMap<>();
        //服务、事项搜索关键词
        String keyword = org.apache.commons.lang3.StringUtils.substring(request.getParam().get("keyword"),0,SEARCH_KEY_MAX_LENGTH);
        String pageNumber = request.getParam().get("pageNumber");
        String pageSize = request.getParam().get("pageSize");

        Map<String, Object> configMap = getConfig(request);

        //加遊客狀態前台傳入的瀏覽器語言參數
        String lang = ApplicationContextUtil.get(UserUtil.class).getCurrentLanguage(request);

        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        MinosMoreNewsForPortalRequest moreNewsForPortalRequest = new MinosMoreNewsForPortalRequest();
        moreNewsForPortalRequest.setLang(lang);
        moreNewsForPortalRequest.setUserId(user != null ? user.getUserAccount() : "");
        moreNewsForPortalRequest.setUserWid(user != null ? user.getWid() : "");
        moreNewsForPortalRequest.setSearchKey(keyword);
        moreNewsForPortalRequest.setSortType(request.getParam().get("sortType"));
        if(org.apache.commons.lang.StringUtils.isNotEmpty(pageNumber)){
            moreNewsForPortalRequest.setPageNumber(Integer.parseInt(pageNumber));
        }
        if(org.apache.commons.lang.StringUtils.isNotEmpty(pageSize)){
            moreNewsForPortalRequest.setPageSize(Integer.parseInt(pageSize));
        }
        Map<String, Object> searchResultMap = ApplicationContextUtil.get(MinosSearchServiceImpl.class).moreNewsForPortal(moreNewsForPortalRequest);

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        boolean isMobileDevice = RequestUtil.isMobileDevice(httpServletRequest);

        results.put("searchData", searchResultMap);
        results.put("config", configMap);
        results.put("userId", getCurrentUserId());
        results.put("keyword", keyword);
        results.put("cardId", request.getCardId());
        results.put("cardWid", request.getCardWid());
        results.put("isMobileDevice", isMobileDevice);

        return results;
    }

    private Object moreTdcForPortal(CardAjaxRequest request) {
        Map<String, Object> results = new HashMap<>();
        //服务、事项搜索关键词
        String keyword = org.apache.commons.lang3.StringUtils.substring(request.getParam().get("keyword"),0,SEARCH_KEY_MAX_LENGTH);
        String pageNumber = request.getParam().get("pageNumber");
        String pageSize = request.getParam().get("pageSize");

        Map<String, Object> configMap = getConfig(request);

        //加遊客狀態前台傳入的瀏覽器語言參數
        String lang = ApplicationContextUtil.get(UserUtil.class).getCurrentLanguage(request);

        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        MinosMoreTdcForPortalRequest minosMoreTdcForPortalRequest = new MinosMoreTdcForPortalRequest();
        minosMoreTdcForPortalRequest.setLang(lang);
        minosMoreTdcForPortalRequest.setUserId(user != null ? user.getUserAccount() : "");
        minosMoreTdcForPortalRequest.setUserWid(user != null ? user.getWid() : "");
        minosMoreTdcForPortalRequest.setSearchKey(keyword);
        if(org.apache.commons.lang.StringUtils.isNotEmpty(pageNumber)){
            minosMoreTdcForPortalRequest.setPageNumber(Integer.parseInt(pageNumber));
        }
        if(org.apache.commons.lang.StringUtils.isNotEmpty(pageSize)){
            minosMoreTdcForPortalRequest.setPageSize(Integer.parseInt(pageSize));
        }
        Map<String, Object> searchResultMap = ApplicationContextUtil.get(MinosSearchServiceImpl.class).moreTdcForPortal(minosMoreTdcForPortalRequest);

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        boolean isMobileDevice = RequestUtil.isMobileDevice(httpServletRequest);

        results.put("searchData", searchResultMap);
        results.put("config", configMap);
        results.put("userId", getCurrentUserId());
        results.put("keyword", keyword);
        results.put("cardId", request.getCardId());
        results.put("cardWid", request.getCardWid());
        results.put("isMobileDevice", isMobileDevice);

        return results;
    }


    private Object sortNewsForPortal(CardAjaxRequest request) {
        Map<String, Object> results = new HashMap<>();
        //服务、事项搜索关键词
        String keyword = org.apache.commons.lang3.StringUtils.substring(request.getParam().get("keyword"),0,SEARCH_KEY_MAX_LENGTH);
        String pageSize = request.getParam().get("pageSize");

        Map<String, Object> configMap = getConfig(request);

        //加遊客狀態前台傳入的瀏覽器語言參數
        String lang = !request.getParam().isEmpty() && !StringUtils.isEmpty(request.getParam().get("lang")) ?
                request.getParam().get("lang") : "";

        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        MinosSearchForPortalRequest searchForPortalRequest = new MinosSearchForPortalRequest();
        searchForPortalRequest.setLang(lang);
        searchForPortalRequest.setUserId(user != null ? user.getUserAccount() : "");
        searchForPortalRequest.setUserWid(user != null ? user.getWid() : "");
        searchForPortalRequest.setSearchKey(keyword);
        searchForPortalRequest.setSortType(request.getParam().get("sortType"));
        if(org.apache.commons.lang.StringUtils.isNotEmpty(pageSize)){
            searchForPortalRequest.setPageSize(Integer.parseInt(pageSize));
        }
        int isShowPcService=1;
        if( MapUtils.isNotEmpty(request.getParam())&& request.getParam().containsKey("isShowPcService")&& org.apache.commons.lang3.StringUtils.isNotBlank(request.getParam().get("isShowPcService")) ){
            isShowPcService=Integer.parseInt(request.getParam().get("isShowPcService"));
        }

        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if(isMobileDevice){
            // 看移动端是否需要展示pc端服务
            if(0==isShowPcService){
                searchForPortalRequest.setServiceStation(1);
            }
        } else{
            searchForPortalRequest.setServiceStation(0);
        }
        Map<String, Object> searchResultMap = ApplicationContextUtil.get(MinosSearchServiceImpl.class).sortNewsForPortal(searchForPortalRequest);
        results.put("searchData", searchResultMap);
        results.put("config", configMap);
        results.put("userId", getCurrentUserId());
        results.put("keyword", keyword);
        results.put("cardId", request.getCardId());
        results.put("cardWid", request.getCardWid());
        results.put("isMobileDevice", isMobileDevice);

        return results;
    }

    protected Object moreCustomGroupDataForPortal(CardAjaxRequest request){
        Map<String, Object> results = new HashMap<>();
        //服务、事项搜索关键词
        String keyword = org.apache.commons.lang3.StringUtils.substring(request.getParam().get("keyword"),0,SEARCH_KEY_MAX_LENGTH);
        String pageSize = request.getParam().get("pageSize");
        String pageNumber = request.getParam().get("pageNumber");
        String groupWid = request.getParam().get("groupWid");

        Map<String, Object> configMap = getConfig(request);

        //加遊客狀態前台傳入的瀏覽器語言參數
        String lang = ApplicationContextUtil.get(UserUtil.class).getCurrentLanguage(request);

        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        MinosMoreCustomGroupDataForPortalRequest moreCustomGroupDataRequest = new MinosMoreCustomGroupDataForPortalRequest();
        moreCustomGroupDataRequest.setLang(lang);
        moreCustomGroupDataRequest.setGroupWid(groupWid);
        moreCustomGroupDataRequest.setUserId(user != null ? user.getUserAccount() : "");
        moreCustomGroupDataRequest.setUserWid(user != null ? user.getWid() : "");
        moreCustomGroupDataRequest.setSearchKey(keyword);
        if(org.apache.commons.lang.StringUtils.isNotEmpty(pageNumber)){
            moreCustomGroupDataRequest.setPageNumber(Integer.parseInt(pageNumber));
        }
        if(org.apache.commons.lang.StringUtils.isNotEmpty(pageSize)){
            moreCustomGroupDataRequest.setPageSize(Integer.parseInt(pageSize));
        }
        Map<String, Object> searchResultMap = ApplicationContextUtil.get(MinosSearchServiceImpl.class).moreCustomGroupDataForPortal(moreCustomGroupDataRequest);

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        boolean isMobileDevice = RequestUtil.isMobileDevice(httpServletRequest);

        results.put("searchData", searchResultMap);
        results.put("config", configMap);
        results.put("userId", getCurrentUserId());
        results.put("keyword", keyword);
        results.put("cardId", request.getCardId());
        results.put("cardWid", request.getCardWid());
        results.put("isMobileDevice", isMobileDevice);

        return results;
    }

    private void searchQuestion(Map<String, Object> results, String keyword, String isShowNoSearchResult, String lang) {
        List<Faq> questionResult;
        questionResult = SortingUtil.searchQuestionList(keyword,lang);
        if (!CollectionUtils.isEmpty(questionResult) && isShowNoSearchResult.equals(SearchResultsUtil.NoSearchResult.HIDE.getIndex())) {
            results.put("activeItem", SearchResultsUtil.SearchResultsItem.QUESTION.getIndex());
            results.put("noData", false);
        } else if (isShowNoSearchResult.equals(SearchResultsUtil.NoSearchResult.SHOW.getIndex())) {
            results.put("activeItem", SearchResultsUtil.SearchResultsItem.QUESTION.getIndex());
            results.put("noData", false);
        }
        results.put("questionList", questionResult);
    }

    private void searchServiceItem(Map<String, Object> results, String keyword, String isShowNoSearchResult, String lang) {
        List<AllServiceItemResponse.ServiceItemModel> serviceItemResult;
        serviceItemResult = SortingUtil.searchServiceItemList(keyword, lang);
        if (!CollectionUtils.isEmpty(serviceItemResult) && isShowNoSearchResult.equals(SearchResultsUtil.NoSearchResult.HIDE.getIndex())) {
            results.put("activeItem", SearchResultsUtil.SearchResultsItem.SERVICE_ITEM.getIndex());
            results.put("noData", false);
        } else if (isShowNoSearchResult.equals(SearchResultsUtil.NoSearchResult.SHOW.getIndex())) {
            results.put("activeItem", SearchResultsUtil.SearchResultsItem.SERVICE_ITEM.getIndex());
            results.put("noData", false);
        }
        results.put("serviceItemList", serviceItemResult);
    }

    private void searchService(Map<String, Object> results, String keyword, String isShowNoSearchResult, String lang) {
        List<AllServiceResponse.ServiceModel> serviceInfoResult;
        serviceInfoResult = SortingUtil.searchAppList(keyword,lang);
        if (!CollectionUtils.isEmpty(serviceInfoResult) && isShowNoSearchResult.equals(SearchResultsUtil.NoSearchResult.HIDE.getIndex())) {
            results.put("activeItem", SearchResultsUtil.SearchResultsItem.ONLINE_SERVICE.getIndex());
            results.put("noData", false);
        } else if (isShowNoSearchResult.equals(SearchResultsUtil.NoSearchResult.SHOW.getIndex())) {
            results.put("activeItem", SearchResultsUtil.SearchResultsItem.ONLINE_SERVICE.getIndex());
            results.put("noData", false);
        }
        results.put("appList", serviceInfoResult);
    }

    private Map<String, Object> getConfig(CardAjaxRequest request) {
        Map<String, Object> configMap = new HashMap<>();
        String config = ApplicationContextUtil.get(CardUtil.class).getCardConfigByCardWid(request.getCardWid(), request.getCardId());
        if (StringUtil.isNotEmpty(config)) {
            config = JSON.parse(config).toString();
            configMap = SearchResultsUtil.toMap(config);
        } else {
            configMap.put(NO_SEACH_RESULT, "0");
            List<String> searchResultItemDefault = new ArrayList<>();
            searchResultItemDefault.add("onlineService");
            searchResultItemDefault.add("serviceItem");
            searchResultItemDefault.add("FAQ");
            configMap.put(SEARCH_RESULT_ITEM1, searchResultItemDefault);
            configMap.put(SHOW_ANCHOR, "1");
        }
        return configMap;
    }


    private Object getDeptInfoData(CardAjaxRequest request) {
        String deptWid = request.getParam().get("deptWid");

        //加遊客狀態前台傳入的瀏覽器語言參數
        String lang = ApplicationContextUtil.get(UserUtil.class).getCurrentLanguage(request);
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        DubboQueryDeptRequest dubboQueryDeptRequest = new DubboQueryDeptRequest();
        dubboQueryDeptRequest.setDeptWidList(Lists.newArrayList(deptWid));
        dubboQueryDeptRequest.setUserId(user != null ? user.getWid() : "");
        dubboQueryDeptRequest.setLang(lang);

        DubboPaginationResponse<DubboDeptInfo> response = null;
        try {
            response = ApplicationContextUtil.get(CardDubboUtil.class).getDeptById(dubboQueryDeptRequest);
        } catch (Exception e) {
            logger.error("获取部门信息失败, 原因:", e);
        }

        return response;
    }

    @Override
    public ComponentContainer getConfig(CardConfigReq cardConfigReq) {
        ComponentContainer componentContainer = cardConfigReq.getComponentContainer();
//        if(0 == cardConfigReq.getPlatformType().intValue()){
            Map<String, Object> serviceParam = new HashMap<>();
            serviceParam.put("label", "服务");
            serviceParam.put("value", 0);

            List<Map<String, Object>> dataList = new ArrayList<>();
            dataList.add(serviceParam);

            CooskLicenseDto license = ApplicationContextUtil.get(CommonApiAdapterImpl.class).getCooskLicense().getData();
            if(license.isCooskSimSim() || license.isCaspSim()){
                Map<String, Object> itemParam = new HashMap<>();
                itemParam.put("label", "事项");
                itemParam.put("value", 1);
                dataList.add(itemParam);
            }
           if(license.isCooskSimOneThing()){
                Map<String, Object> oneThingParam = new HashMap<>();
                oneThingParam.put("label", "一件事");
                oneThingParam.put("value", 2);
                dataList.add(oneThingParam);
            }
            componentContainer.setData(Global.Components.HOT_SEARCH_DISPLAY.getKey(), Global.ComponentParam.DATAS, dataList);
//        }
        return componentContainer;
    }


}
