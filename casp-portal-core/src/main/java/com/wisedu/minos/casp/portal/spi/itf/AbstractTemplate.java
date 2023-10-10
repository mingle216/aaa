package com.wisedu.minos.casp.portal.spi.itf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wisedu.minos.api.model.DubboCardNewsReadRequest;
import com.wisedu.minos.casp.portal.CasProperties;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.InternationalizationEntity;
import com.wisedu.minos.casp.portal.dao.entity.PersonalDataAuthEntity;
import com.wisedu.minos.casp.portal.dao.entity.PersonalDataEntity;
import com.wisedu.minos.casp.portal.dao.mapper.PersonalDataMapper;
import com.wisedu.minos.casp.portal.i18n.I18nInitializer;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.configcomponent.AbstractComponent;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.ProgrammeApiAdapter;
import com.wisedu.minos.casp.portal.service.impl.*;
import com.wisedu.minos.casp.portal.service.impl.personaldata.PersonalDataAuthService;
import com.wisedu.minos.casp.portal.utils.*;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.MinosException;
import com.wisedu.minos.util.jdbc.JDBCTools;
import com.wisedu.minos.util.jdbc.bean.JDBCConnectParamBean;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.GroupTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.wisedu.minos.casp.portal.common.constant.Global.DEFAULT_LANGUAGE;
import static com.wisedu.minos.casp.portal.common.constant.Global.SEARCH_KEY_MAX_LENGTH;

public abstract class AbstractTemplate implements ITemplate {
    private static final Logger logger = LogManager.getLogger(AbstractTemplate.class);

    protected Map<String, Object> langMessagesMap = new HashMap<>();
    protected GroupTemplate groupTemplate;
    public static final String PERSONAL_KEY = "redisKey:personalData";
    public static final String INTERNATIONAL_KEY = "redisKey:perTitleInternation";
    public static final String INTERNAL_PERSONAL_WID = "3b032f77193e4b65b78cf89936dba5f9";
    /**
     * 初始化方法，初始化模板引擎
     */
    @Override
    public void initialize(Map<String, Object> param) {
        logger.info("initialize template..{}", getTemplateId());
        try {
            //模板国际化初始化加载
            this.langMessagesMap = I18nInitializer.load(param, Global.SpiPluginType.TEMPLATE, getTemplateId());

            ApplicationContextUtil.get(TemplateService.class).updateTemplatesStatus(getTemplateId());
        } catch (Exception e) {
            logger.warn("模板{}初始化失败", getTemplateId(), e);
            throw new RuntimeException("模板初始化失败", e);
        }
    }
    /***
     * @Author jcx
     * @Description VUE前端调用统一方法
     * @Date 10:34 2020/12/16
     * @Param request:
     * @return Object
     **/
    @Override
    public Object execDispatcherData(TemplateAjaxRequest request) {
        if ("saveProgrammeConfig".equals(request.getMethod())) {
            String templateId = request.getParam().get("wid");
            String templateConfig = request.getParam().get("templateConfig");
            return this.saveProgrammeConfig(templateId, templateConfig);
        } else if (Global.TemPlateGlobalMethod.GET_TEMPLATE_CONTENT.getMethodName().equals(request.getMethod())) {
            return this.getTemplateContent(request);
        }  else if ("visitService".equals(request.getMethod())) {
            Map<String, String> requestParam = request.getParam();
            return ApplicationContextUtil.get(ServiceItemApiService.class).visitService(requestParam, request);
        } else if ("getServiceItemRelService".equals(request.getMethod())) {
            Map<String, String> requestParam = request.getParam();
            return this.getServiceItemRelService(requestParam, request);
        } else if ("matchSearch".equals(request.getMethod())) {
            return this.matchSearch(request);
        } else if ("clearSearchHis".equals(request.getMethod())) {
            return this.clearSearchHis(request);
        } else if ("globalSearch".equals(request.getMethod())) {
            return this.globalSearch(request);
        } else if ("analyzeSearch".equals(request.getMethod())) {
            return this.analyzeSearch(request);
        } else if("enabledGroupSearch".equals(request.getMethod())){
            return this.enabledGroupSearch(request);
        } else if("getWorkConfigData".equals(request.getMethod())) {
            return this.getWorkConfigData(request);
        }else if("getPersonalData".equals(request.getMethod())){
            return this.getPersonalData(request);
        }else if("newsRead".equals(request.getMethod())){
            return this.getNewsRead(request);
        }
        return doExecDispatcher(request);
    }


    /**
     * visitService
     * <p/>
     * 根据服务事项得到服务
     *
     * @param requestParam
     * @param request
     * @return java.lang.String
     * @throws
     * @date 2020/10/22 17:31
     */

    protected ServiceRelServiceResponse.ServiceItemAttr getServiceItemRelService(Map<String, String> requestParam, TemplateAjaxRequest request) {
        String id = requestParam.get("id");//getKeywords(requestParam, "id");
        String langCountry = requestParam.get("langCountry");//getKeywords(requestParam, "langCountry");
        return ApplicationContextUtil.get(ServiceItemApiService.class)
                .getServiceItemRelService(
                        new ServiceItemRelServiceRequest().setServiceItemWid(id).setLangCountry(langCountry));
    }
    /**
     * getTemplateContent
     * <p/>
     * 获取模板信息
     *
     * @param
     * @return java.util.List<com.wisedu.minos.casp.portal.model.TemplateContent>
     * @throws
     * @date 2020-10-8 15:38
     */
    public List<TemplateContent> getTemplateContent(TemplateAjaxRequest request) {
        Map<String, String> param = request.getParam();
        String templateId="";
        if(null!=param&&param.containsKey("templateId")){
            templateId = param.get("templateId");
        }
        return ApplicationContextUtil.get(TemplatePageUtil.class).getTemplateInfo(templateId,Integer.valueOf(String.valueOf(request.getPlatformType())));
    }

    protected String saveProgrammeConfig(String programmeId, String templateConfig) {
        ProgrammeInfoRes programmeInfoRes = new ProgrammeInfoRes();
        programmeInfoRes.setWid(programmeId);
        programmeInfoRes.setTemplateConfig(templateConfig);
        ApplicationContextUtil.get(ProgrammeApiAdapter.class).saveProgrammeInfo(programmeInfoRes);
        return "success";
    }

    protected abstract Object doExecDispatcher(TemplateAjaxRequest request);

    @Override
    public ComponentContainer getConfig(TemplateConfigRequest templateConfigRequest) {
        ComponentContainer componentContainer = templateConfigRequest.getComponentContainer();
        List<AbstractComponent> components = componentContainer.getComponents();
        if (components != null) {
            AbstractComponent abstractComponent = components.stream().filter(e -> "footerConfig".equals(e.getKey()))
                    .findFirst().orElse(null);
            if (abstractComponent != null) {
                abstractComponent.setValue(
                        abstractComponent.getValue() instanceof String ?replaceAbsolutePath(String.valueOf(abstractComponent.getValue()))
                                :JSON.parseArray(replaceJsonAbsolutePath(JSON.toJSONString(abstractComponent.getValue()))));
                abstractComponent.setDefaultValue(
                        abstractComponent.getDefaultValue() instanceof String ?replaceAbsolutePath(String.valueOf(abstractComponent.getDefaultValue()))
                                :JSON.parseArray(replaceJsonAbsolutePath(JSON.toJSONString(abstractComponent.getDefaultValue()))));
            }

            AbstractComponent configLogoComponent = components.stream().filter(e -> "configLogo".equals(e.getKey()))
                    .findFirst().orElse(null);
            if (configLogoComponent != null) {
                Map<String,String> values = (Map) configLogoComponent.getValue();
                configLogoComponent.setValue(replaceAbsoluteLogoPath(values));
                configLogoComponent.setDefaultValue(replaceAbsoluteLogoPath((Map<String,String>) configLogoComponent.getDefaultValue()));
            }
            AbstractComponent headerNavBarComponent = components.stream().filter(e -> "headerNavBar".equals(e.getKey()))
                    .findFirst().orElse(null);
            if (headerNavBarComponent != null) {
                if(headerNavBarComponent.getValue() == null){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("datas",new ArrayList<Object>());
                    componentContainer.setData("headerNavBar", Global.ComponentParam.VALUE, jsonObject);
                }
            }
        }
        return componentContainer;
    }

    /***
     * replaceAbsolutePath
     * 讲footer配置中img路劲相对路劲改成url形式<p/>
     *
     * @param footerConfig
     * @throws
     * @return java.lang.String
     * @date 2021/2/26 13:30
     */
    public String replaceAbsolutePath(String footerConfig){
        if(StringUtils.isBlank(footerConfig)){
            return footerConfig;
        }
        Pattern compile = Pattern.compile("<img.*?>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(footerConfig);
        String imgRegex = "(src|SRC)=([\"'])(.*?)([\"'])";
        String domain = ApplicationContextUtil.get(CasProperties.class).getModule().getDomain();
        while (matcher.find()) {
            String img = matcher.group();
            Matcher m = Pattern.compile(imgRegex).matcher(img);
            if (m.find()) {
                String imgSrc = m.group(3);
                if (imgSrc.indexOf("/") == 0) {
                    footerConfig = footerConfig
                            .replaceAll("(src|SRC)=[\"']" + imgSrc + "[\"']", "src=\"" + domain + imgSrc + "\"");
                }
            }
        }
        return footerConfig;
    }

    public String replaceJsonAbsolutePath(String footerConfig){
        if(StringUtils.isBlank(footerConfig)){
            return footerConfig;
        }
        Pattern compile = Pattern.compile("<img.*?>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(footerConfig);
        String imgRegex = "(src|SRC)=(\\\\\")(.*?)(\\\\\")";
        String domain = ApplicationContextUtil.get(CasProperties.class).getModule().getDomain();
        while (matcher.find()) {
            String img = matcher.group();
            Matcher m = Pattern.compile(imgRegex).matcher(img);
            if (m.find()) {
                String imgSrc = m.group(3);
                if (imgSrc.indexOf("/") == 0) {
                    footerConfig = footerConfig
                            .replaceAll("(src|SRC)=\\\\\"" + imgSrc + "\\\\\"", "src=\\\\\"" + domain + imgSrc + "\\\\\"");
                }
            }
        }
        return footerConfig;
    }

    public Map replaceAbsoluteLogoPath(Map<String,String> logoValue){
        String domain = ApplicationContextUtil.get(CasProperties.class).getModule().getDomain();
        String logoUrlValue = logoValue.get("logoUrl");
        String whiteLogoUrlValue = logoValue.get("whiteLogoUrl");
        if(StringUtil.isNotEmpty(logoUrlValue)&&!logoUrlValue.contains("http")&&!logoUrlValue.contains("https")){
            logoUrlValue=logoUrlValue.replace(logoUrlValue,domain+logoUrlValue);
            logoValue.put("logoUrl",logoUrlValue);
        }
        if(StringUtil.isNotEmpty(whiteLogoUrlValue)&&!whiteLogoUrlValue.contains("http")&&!whiteLogoUrlValue.contains("https")){
            whiteLogoUrlValue=whiteLogoUrlValue.replace(whiteLogoUrlValue,domain+whiteLogoUrlValue);
            logoValue.put("whiteLogoUrl",whiteLogoUrlValue);
        }
        return logoValue;
    }

    private String getKeywords(Map<String, String> param, String keywords) {
        String keywords1 = param.get(keywords);
        return StringUtil.xssEncode(StringUtil.defaultIfEmpty(keywords1, ""));
    }

    /**
     * matchSearch
     * <p/>
     * 搜索匹配
     *
     * @param request
     * @return com.wisedu.minos.casp.portal.common.result.ResultData
     * @throws
     * @date 2020/10/16 13:02
     */
    public Map<String, Object> matchSearch(TemplateAjaxRequest request) {
        Map<String, Object> matchSearchResponses = new HashMap<>(2);
        String keywords = org.apache.commons.lang3.StringUtils.substring(request.getParam().get("keywords"),0,SEARCH_KEY_MAX_LENGTH);;
        if (StringUtils.isBlank(keywords)) {
            matchSearchResponses.put("service", Collections.emptyList());
            matchSearchResponses.put("serviceItem", Collections.emptyList());
            return matchSearchResponses;
        }
        String lang = !request.getParam().isEmpty() && !org.springframework.util.StringUtils.isEmpty(request.getParam().get("lang")) ?
                request.getParam().get("lang") : "";
        keywords=keywords.replaceAll("\\s*", "");
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        MinosSearchForPortalRequest searchForPortalRequest = new MinosSearchForPortalRequest();
        searchForPortalRequest.setLang(lang);
        searchForPortalRequest.setUserId(user != null ? user.getUserAccount() : "");
        searchForPortalRequest.setUserWid(user != null ? user.getWid() : "");
        searchForPortalRequest.setSearchKey(keywords);
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if(isMobileDevice){
            // 看移动端是否需要展示pc端服务
            boolean isShowPCService = ApplicationContextUtil.get(CardUtil.class).getIsShowPCService();
            if(!isShowPCService){
                searchForPortalRequest.setServiceStation(1);
            }
        } else{
            searchForPortalRequest.setServiceStation(0);
        }

        //自建分组匹配搜索
        if(StringUtils.isNotBlank(request.getParam().get("groupWid"))){
            return getSingleGroupResponse(request.getParam().get("groupType"), request.getParam().get("groupWid"), searchForPortalRequest);
        }
        else{
            Map<String, Object> searchResultMap = ApplicationContextUtil.get(MinosSearchServiceImpl.class).suggestForPortal(searchForPortalRequest);

            List<AllServiceResponse.ServiceModel> allService = getServiceFromSearchSuggest(searchResultMap);
            matchSearchResponses.put("service", allService);

            List<AllServiceItemResponse.ServiceItemModel> allServiceItem = getServiceItemsFromSearchSuggest(searchResultMap);
            if (5 < allServiceItem.size()) {
                allServiceItem = new ArrayList<>(allServiceItem.subList(0, 5));
            }

            matchSearchResponses.put("serviceItem", allServiceItem);
            matchSearchResponses.put("newsItem", getNewsMapFromSearchSuggest(searchResultMap));
            matchSearchResponses.put("oneThing", getOneThingsFromSearchSuggest(searchResultMap));
            matchSearchResponses.put("dept", getDeptsFromSearchSuggest(searchResultMap));
            matchSearchResponses(searchResultMap, matchSearchResponses);
        }
        return matchSearchResponses;
    }

    private Map<String, Object> getSingleGroupResponse(String groupType, String groupWid,MinosSearchForPortalRequest searchForPortalRequest){
        Map<String, Object> searchResultMap = ApplicationContextUtil.get(MinosSearchServiceImpl.class).suggestForPortal(searchForPortalRequest);

        Map<String, Object> singleGroupResponse = new HashMap<>(1);
        //内置分组单个匹配搜索
        if("1".equals(groupType)){
            searchForPortalRequest.setGroupWid(groupWid);
            Map<String, Object> searchCustomResultMap = ApplicationContextUtil.get(MinosSearchServiceImpl.class).suggestCustomForPortal(searchForPortalRequest);
            List<CustomGroupResponse.CustomGroupModel> customGroupModels = getCustomGroupFromSearchSuggest(searchCustomResultMap);
            singleGroupResponse.put("customGroup", CollectionUtils.isNotEmpty(customGroupModels)?customGroupModels.get(0):customGroupModels);
        }
        else if("2".equals(groupType)){
            List<AllServiceResponse.ServiceModel> allService = getServiceFromSearchSuggest(searchResultMap);
            singleGroupResponse.put("service", allService);
        }
        else if("3".equals(groupType)){
            List<AllServiceItemResponse.ServiceItemModel> allServiceItem = getServiceItemsFromSearchSuggest(searchResultMap);
            if (5 < allServiceItem.size()) {
                allServiceItem = new ArrayList<>(allServiceItem.subList(0, 5));
            }
            allServiceItem.sort(Comparator.comparing(AllServiceItemResponse.ServiceItemModel::getItemName));

            singleGroupResponse.put("serviceItem", allServiceItem);
        }
        else if("4".equals(groupType)){
            singleGroupResponse.put("newsItem", getNewsMapFromSearchSuggest(searchResultMap));
        }
        else if("5".equals(groupType)){
            singleGroupResponse.put("oneThing", getOneThingsFromSearchSuggest(searchResultMap));
        }
        else if("6".equals(groupType)){
            singleGroupResponse.put("dept", getDeptsFromSearchSuggest(searchResultMap));
        }

        return singleGroupResponse;
    }

    private Map<String, Object> matchSearchResponses(Map<String, Object> searchResultMap,Map<String, Object> matchSearchResponses){
        if(searchResultMap != null && searchResultMap.get("newsOrderNumber") != null){
            matchSearchResponses.put("newsOrderNumber",searchResultMap.get("newsOrderNumber"));
        }
        if(searchResultMap != null && searchResultMap.get("serviceOrderNumber") != null){
            matchSearchResponses.put("serviceOrderNumber",searchResultMap.get("serviceOrderNumber"));
        }
        if(searchResultMap != null && searchResultMap.get("serviceItemOrderNumber") != null){
            matchSearchResponses.put("serviceItemOrderNumber",searchResultMap.get("serviceItemOrderNumber"));
        }
        if(searchResultMap != null && searchResultMap.get("oneThingOrderNumber") != null){
            matchSearchResponses.put("oneThingOrderNumber",searchResultMap.get("oneThingOrderNumber"));
        }
        if(searchResultMap != null && searchResultMap.get("deptOrderNumber") != null){
            matchSearchResponses.put("deptOrderNumber",searchResultMap.get("deptOrderNumber"));
        }
        matchSearchResponses.put("serviceGroupName",searchResultMap.get("serviceGroupName"));
        matchSearchResponses.put("serviceItemGroupName",searchResultMap.get("serviceItemGroupName"));
        matchSearchResponses.put("newsGroupName",searchResultMap.get("newsGroupName"));
        matchSearchResponses.put("oneThingGroupName",searchResultMap.get("oneThingGroupName"));
        matchSearchResponses.put("deptGroupName",searchResultMap.get("deptGroupName"));
        return matchSearchResponses;
    }

    public List<QuerySearchGroupMulNameRes> enabledGroupSearch(TemplateAjaxRequest request){
        String lang = request.getParam().get("lang");
        if(StringUtils.isBlank(lang)){
            lang = DEFAULT_LANGUAGE;
        }
        JSONObject param = new JSONObject();
        param.put("lang", lang);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(param);
        GroupNameResponse groupNameResponse;
        groupNameResponse = RestTemplateUtils.post("/minos-search/searchManager/queryGroupMultiLang", httpEntity, GroupNameResponse.class).getBody();
        List<QuerySearchGroupMulNameRes> searchGroupDtos = null;
        if(null != groupNameResponse){
            searchGroupDtos = groupNameResponse.getGroupNameList();
        }
        return searchGroupDtos;
    }

    private List<PersonalDataEntity> getPersonalDataList(String lang, String personalDataConfig){
        List<PersonalDataEntity> personalDataEntities = new ArrayList<>();
        //模板配置里选择的个人数据内容
        List<String> personalWids = Arrays.asList(personalDataConfig.split(","));
        String redisPersonalData = (String) ApplicationContextUtil.get(RedisUtil.class).get(PERSONAL_KEY);
        String redisInternationalData = (String) ApplicationContextUtil.get(RedisUtil.class).get(INTERNATIONAL_KEY);
        //redis中的个人数据聚合data
        List<PersonalDataEntity> personalDataEntityList = JSON.parseObject(redisPersonalData,new TypeReference<List<PersonalDataEntity>>() {
        });
        // 个人数据data过滤当前用户授权
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        PersonalDataAuthService personalDataAuthService = ApplicationContextUtil.get(PersonalDataAuthService.class);
        List<String> wids = personalDataAuthService.getAuthPersonalData(user.getWid()).stream().map(PersonalDataAuthEntity::getDataId).collect(Collectors.toList());
        personalWids = personalWids.stream().filter(e -> wids.contains(e)).collect(Collectors.toList());
        //redis中的多语言list
        List<InternationalizationEntity> internationalList = JSON.parseObject(redisInternationalData,new TypeReference<List<InternationalizationEntity>>() {
        });
        for(String personalWid:personalWids){
            PersonalDataEntity personalDataEntity = personalDataEntityList.stream().filter(e -> personalWid.equals(e.getWid())).findFirst().orElse(null);
            if(null != personalDataEntity){
                if(!DEFAULT_LANGUAGE.equals(lang)){
                    List<InternationalizationEntity> internationalizationEntityList = internationalList.stream().filter(f -> personalDataEntity.getTitleLangKey().equals(f.getLangKey()) && lang.equals(f.getLangCountry())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(internationalizationEntityList)){
                        personalDataEntity.setTitle(internationalizationEntityList.get(0).getLangValue());
                    }
                }
                personalDataEntities.add(personalDataEntity);
            }
        }

        return personalDataEntities;
    }

    public ShowServiceResponse getWorkConfigData(TemplateAjaxRequest request){
        ShowServiceResponse showServiceResponse = new ShowServiceResponse();
        String personalDataConfig = request.getParam().get("personalDataConfig");
        String lang = request.getParam().get("lang");

        if(StringUtils.isNotBlank(personalDataConfig)) {
            List<PersonalDataEntity> personalDataEntities = getPersonalDataList(lang, personalDataConfig);
            showServiceResponse.setPersonalDataEntityList(personalDataEntities);
        }

        Integer type = Integer.valueOf(request.getParam().get("type"));
        //查询收藏的服务
        if(1 == type){
            List<AppInfo> data = getFavoriteServiceList();
            showServiceResponse.setFavoriteServiceList(data);
        }
        //查询最近使用的服务
        else if(2 == type){
            List<AllServiceResponse.ServiceModel> latestServices = new ArrayList<>();
            String userWid = getUserWid();
            //获取最近应用查询接口
            if (StringUtil.isNotBlank(userWid)) {
                latestServices = ApplicationContextUtil.get(ServiceLatestUseService.class).getLatestServices(userWid);
            }
            showServiceResponse.setLatestServiceList(latestServices);
        }
        else if(3 == type){
            List<ServiceBiz> serviceBizList = getClassifyServiceList(request);
            showServiceResponse.setClassifyServiceList(serviceBizList);
        }
        return showServiceResponse;
    }

    private List<AppInfo> getFavoriteServiceList(){
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
        List<AppInfo> data = null;
        if ( null != response && null != response.getData() ) {
            data = response.getData();
        }

        return data;
    }

    private List<ServiceBiz> getClassifyServiceList(TemplateAjaxRequest request){
        String classifyWid = request.getParam().get("value");
        String userWid = getUserWid();
        JSONObject param = new JSONObject();
        param.put("userId", userWid);
        param.put("lang", request.getParam().get("lang"));
        param.put("classifyWid", classifyWid);
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if(!isMobileDevice){
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
        List<ServiceBiz> serviceBizList = classifyServiceListRes.getData();
        if(CollectionUtils.isNotEmpty(serviceBizList)){
            serviceBizList = serviceBizList.stream().filter(distinctByKey(b->b.getServiceWid())).collect(Collectors.toList());
        }

        return serviceBizList;
    }

    static <T> Predicate<T> distinctByKey(Function<?  super T,?> keyExtractor){
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();

        return t-> seen.putIfAbsent(keyExtractor.apply(t),Boolean.TRUE) == null;
    }


    /**
     * work模板获取个人信息数据
     * @param request
     */
    private PersonalDataDto getPersonalData(TemplateAjaxRequest request){
        String lang = request.getParam().get("lang");
        PersonalDataDto personalDataDto = new PersonalDataDto();
        //读取ids当前登录用户信息
        if(INTERNAL_PERSONAL_WID.equals(request.getParam().get("personalWid"))){
            personalDataDto = new PersonalDataDto();
            UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
            personalDataDto.setPersonalWid(currentUser.getWid());
            personalDataDto.setPicUrl(currentUser.getUserIcon());
            personalDataDto.setName(currentUser.getUserName());
            personalDataDto.setStuNumber(currentUser.getUserAccount());
            personalDataDto.setOrgName(currentUser.getDeptName());
            return personalDataDto;
        }
        //获取个人数据配置
        LambdaQueryWrapper<PersonalDataEntity> wrapper = new LambdaQueryWrapper<PersonalDataEntity>()
                .eq(PersonalDataEntity::getIsDeleted,0)
                .eq(PersonalDataEntity::getEnabled,1)
                .eq(PersonalDataEntity::getWid,request.getParam().get("personalWid"));
        List<PersonalDataEntity> personalDataEntityList = ApplicationContextUtil.get(PersonalDataMapper.class).selectList(wrapper);
        if(CollectionUtils.isNotEmpty(personalDataEntityList)){
            PersonalDataEntity personalDataEntity = personalDataEntityList.get(0);
            Integer sourceType = personalDataEntity.getSourceType();
            switch(sourceType){
                //JSON接口获取个人数据
                case 1:
                    personalDataDto = getPersonalDataFromJSON(personalDataEntity, lang);
                    break;
                //外部数据源获取数据
                case 2:
                    personalDataDto = getPersonalDataFromDB(personalDataEntity, lang);
                    break;
            }
        }
        return personalDataDto;
    }

    private PersonalDataDto getPersonalDataFromDB(PersonalDataEntity personalDataEntity, String lang){
        PersonalDataDto personalDataDto = new PersonalDataDto();
        try{
            JDBCConnectParamBean jdbcConnectParamBean = ApplicationContextUtil.get(DataSourceUtil.class).getJDBCConnectParamBeanByName(personalDataEntity.getSourceKey());
            List<Map<String, String>> matchInformationList;
            matchInformationList = JDBCTools.query(jdbcConnectParamBean, personalDataEntity.getDataSql(), null);
            Map<String, String> matchInformationMap = CollectionUtils.isNotEmpty(matchInformationList) ? matchInformationList.get(0) : Collections.emptyMap();
            String orgName = "orgName"+lang;
            if(StringUtils.isBlank(matchInformationMap.get(orgName))){
                personalDataDto.setOrgName(matchInformationMap.get("orgname_zh_cn"));
            }else{
                personalDataDto.setOrgName(matchInformationMap.get(orgName));
            }
            personalDataDto.setName(matchInformationMap.get("name"));
            personalDataDto.setStuNumber(matchInformationMap.get("stu_number"));
            personalDataDto.setPicUrl(matchInformationMap.get("pic_url"));
            personalDataDto.setPersonalWid(matchInformationMap.get("personal_wid"));
        }catch (Exception e){
            throw new MinosException("查询外部数据源出错", e);
        }

        return personalDataDto;
    }

    private PersonalDataDto getPersonalDataFromJSON(PersonalDataEntity personalDataEntity, String lang){
        StringBuilder url = new StringBuilder(personalDataEntity.getSourceKey());
        String resultStr;
        if ( "1".equals(personalDataEntity.getHttpMethod()) ) {
            // post请求
            JSONObject entityJson = new JSONObject();
            entityJson.put("langKey", lang);
            resultStr = RestTemplateUtils.post(url.toString(), entityJson, String.class).getBody();
        } else {
            try{
                // get请求
                if ( url.toString().contains("?") ) {
                    url.append("&");
                } else {
                    url.append("?");
                }
                url.append("langKey").append("=").append(lang);
                resultStr = RestTemplateUtils.get(url.toString(), String.class).getBody();
            }catch (Exception e){
                throw new MinosException("调用JSON接口出错", e);
            }
        }
        return JSON.parseObject(resultStr,PersonalDataDto.class);
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

    public Map<String, Object> analyzeSearch(TemplateAjaxRequest request) {
        Map<String, Object> matchSearchResponses = new HashMap<>(2);
        String keywords = org.apache.commons.lang3.StringUtils.substring(request.getParam().get("keywords"),0,SEARCH_KEY_MAX_LENGTH);//getKeywords(request.getParam(), "keywords");
        if (StringUtils.isBlank(keywords)) {
            matchSearchResponses.put("service", Collections.emptyList());
            matchSearchResponses.put("serviceItem", Collections.emptyList());
            return matchSearchResponses;
        }
        keywords=keywords.replaceAll("\\s*", "");
        String lang = !request.getParam().isEmpty() && !org.springframework.util.StringUtils.isEmpty(request.getParam().get("lang")) ?
                request.getParam().get("lang") : "";

        MinosSearchAnalyzeRequest analyzeRequest = new MinosSearchAnalyzeRequest();
        analyzeRequest.setLang(lang);
        analyzeRequest.setSearchKey(keywords);
        return ApplicationContextUtil.get(MinosSearchServiceImpl.class).searchAnalyze(analyzeRequest);
    }


    protected List<Map<String,String>> getNewsMapFromSearchSuggest(Map<String, Object> searchResultMap){
        List<Map<String,String>> result = new ArrayList<>();
        if(searchResultMap != null && searchResultMap.get("newsData") != null && searchResultMap.get("newsData") instanceof List){
            for (Object newsData : ((List) searchResultMap.get("newsData"))) {
                if(newsData instanceof Map){
                    Map<String,String> map = new HashMap<>();
                    map.put("wid",StringUtil.getString(((Map) newsData).get("wid")));
                    map.put("title",StringUtil.getString(((Map) newsData).get("title")));
                    map.put("url",StringUtil.getString(((Map) newsData).get("url")));
                    map.put("calculatedScore",((Map) newsData).get("calculatedScore")!=null ? ((Map) newsData).get("calculatedScore").toString() : "");
                    map.put("sideFlag",((Map) newsData).get("sideFlag")!=null ? ((Map) newsData).get("sideFlag").toString() : "");
                    result.add(map);
                }
            }
        }

        return result;
    }

    protected List<CustomGroupResponse.CustomGroupModel> getCustomGroupFromSearchSuggest(Map<String, Object> searchResultMap){
        List<CustomGroupResponse.CustomGroupModel> result = new ArrayList<>();
        if(searchResultMap != null && searchResultMap.get("customGroupData") != null && searchResultMap.get("customGroupData") instanceof List){
            for (Object customGroupData : ((List) searchResultMap.get("customGroupData"))) {
                if(customGroupData instanceof Map){
                    CustomGroupResponse.CustomGroupModel customGroupModel = new CustomGroupResponse.CustomGroupModel();
//                    customGroupModel.setWid(StringUtil.getString(((Map) customGroupData).get("wid")));
//                    customGroupModel.setGroupName(StringUtil.getString(((Map) customGroupData).get("groupName")));
                    customGroupModel.setGroupDataList((List<Object>) ((Map) customGroupData).get("groupDataList"));
                    customGroupModel.setGroupParamList((List<CustomGroupResponse.CustomGroupModel.MinosSearchCustomGroupParamForPortal>) ((Map) customGroupData).get("groupParamList"));
                    customGroupModel.setGroupDataSize(((Map) customGroupData).get("groupDataSize") != null? Integer.parseInt(((Map) customGroupData).get("groupDataSize").toString()):null);
//                    customGroupModel.setOrderNumber(((Map) customGroupData).get("orderNumber") != null? Integer.parseInt(((Map) customGroupData).get("orderNumber").toString()):null);
                    result.add(customGroupModel);
                }
            }
        }
        return result;
    }

    protected List<AllServiceItemResponse.ServiceItemModel> getServiceItemsFromSearchSuggest(Map<String, Object> searchResultMap){
        List<AllServiceItemResponse.ServiceItemModel> result = new ArrayList<>();
        if(searchResultMap != null && searchResultMap.get("serviceItemData") != null && searchResultMap.get("serviceItemData") instanceof List){
            for (Object serviceData : ((List) searchResultMap.get("serviceItemData"))) {
                if(serviceData instanceof Map){
                    AllServiceItemResponse.ServiceItemModel serviceItemModel = new AllServiceItemResponse.ServiceItemModel();
                    serviceItemModel.setItemWid(StringUtil.getString(((Map) serviceData).get("itemWid")));
                    serviceItemModel.setItemName(StringUtil.getString(((Map) serviceData).get("itemName")));
                    serviceItemModel.setItemDept(StringUtil.getString(((Map) serviceData).get("itemDept")));
                    serviceItemModel.setIsEnabled(getIntegerValueFromMap((Map) serviceData, "isEnabled"));
                    serviceItemModel.setIsShow(getIntegerValueFromMap((Map) serviceData, "isShow"));
                    serviceItemModel.setItemDetailSourceAsMap(((Map) serviceData).get("itemDetailSourceAsMap"));
                    serviceItemModel.setOnlineServiceType(getIntegerValueFromMap((Map) serviceData, "onlineServiceType"));
                    serviceItemModel.setCalculatedScore(((Map) serviceData).get("calculatedScore")!=null ? Double.valueOf((Double) ((Map) serviceData).get("calculatedScore")) : null);
                    result.add(serviceItemModel);
                }
            }
        }
        return result;
    }

    protected List<OneThingModel> getOneThingsFromSearchSuggest(Map<String, Object> searchResultMap){
        List<OneThingModel> result = new ArrayList<>();
        if(searchResultMap != null && searchResultMap.get("oneThingData") != null && searchResultMap.get("oneThingData") instanceof List){
            for (Object OneThingData : ((List) searchResultMap.get("oneThingData"))) {
                if(OneThingData instanceof Map){
                    OneThingModel oneThingModel = new OneThingModel();
                    oneThingModel.setOneThingWid(StringUtil.getString(((Map) OneThingData).get("oneThingWid")));
                    oneThingModel.setOneThingName(StringUtil.getString(((Map) OneThingData).get("oneThingName")));
                    oneThingModel.setOneThingClassify((List<String>) ((Map) OneThingData).get("oneThingClassify"));
                    oneThingModel.setRoleList((List<String>) ((Map) OneThingData).get("roleList"));
                    oneThingModel.setVisitCount(getIntegerValueFromMap((Map) OneThingData, "visitCount"));
                    result.add(oneThingModel);
                }
            }
        }
        return result;
    }

    protected List<DeptModel> getDeptsFromSearchSuggest(Map<String, Object> searchResultMap){
        List<DeptModel> result = new ArrayList<>();
        if(searchResultMap != null && searchResultMap.get("deptData") != null && searchResultMap.get("deptData") instanceof List){
            for (Object deptData : ((List) searchResultMap.get("deptData"))) {
                if(deptData instanceof Map){
                    DeptModel deptModel = new DeptModel();
                    deptModel.setDeptWid(StringUtil.getString(((Map) deptData).get("deptWid")));
                    deptModel.setDeptName(StringUtil.getString(((Map) deptData).get("deptName")));
                    deptModel.setDeptLinkUrl(StringUtil.getString(((Map) deptData).get("deptLinkUrl")));
                    deptModel.setCalculatedScore(StringUtil.getString(((Map) deptData).get("calculatedScore")));
                    result.add(deptModel);
                }
            }
        }
        return result;
    }

    private Integer getIntegerValueFromMap(Map serviceData, String key){
        return ((Map) serviceData).get(key) != null ? Integer.parseInt(((Map) serviceData).get(key).toString()) : null;
    }

    protected List<AllServiceResponse.ServiceModel> getServiceFromSearchSuggest(Map<String, Object> searchResultMap){
        List<AllServiceResponse.ServiceModel> result = new ArrayList<>();
        if(searchResultMap != null && searchResultMap.get("serviceData") != null && searchResultMap.get("serviceData") instanceof List){
            for (Object serviceData : ((List) searchResultMap.get("serviceData"))) {
                if(serviceData instanceof Map){
                    AllServiceResponse.ServiceModel serviceModel = new AllServiceResponse.ServiceModel();
                    serviceModel.setServiceId(StringUtil.getString(((Map) serviceData).get("serviceId")));
                    serviceModel.setServiceWid(StringUtil.getString(((Map) serviceData).get("serviceWid")));
                    serviceModel.setServiceName(StringUtil.getString(((Map) serviceData).get("serviceName")));
                    serviceModel.setPcAccessUrl(StringUtil.getString(((Map) serviceData).get("pcAccessUrl")));
                    serviceModel.setIconLink(StringUtil.getString(((Map) serviceData).get("iconLink")));
                    serviceModel.setMobileAccessUrl(StringUtil.getString(((Map) serviceData).get("mobileAccessUrl")));
                    serviceModel.setMobileIconLink(StringUtil.getString(((Map) serviceData).get("mobileIconLink")));
                    serviceModel.setPermission(((Map) serviceData).get("permission") != null ? (boolean)((Map) serviceData).get("permission") : false);
                    serviceModel.setServiceStation(((Map) serviceData).get("serviceStation") != null ? Integer.parseInt(((Map) serviceData).get("serviceStation").toString()) : null);
                    serviceModel.setCalculatedScore( ((Map) serviceData).get("calculatedScore")!=null ? Double.valueOf ((Double) ((Map) serviceData).get("calculatedScore")) : null);
                    result.add(serviceModel);
                }
            }
        }
        return result;
    }

    private <T> Object getNewService(List<T> list, String redex, boolean isService) {
        List<AllServiceResponse.ServiceModel> result = new ArrayList<>();
        List<AllServiceItemResponse.ServiceItemModel> resultFir = new ArrayList<>();
        list.forEach(serviceModel -> {
            if (isService) {
                char[] chars1 = ((AllServiceResponse.ServiceModel) serviceModel).getServiceName().toCharArray();
                String c1 = Character.toString(chars1[0]);
                if (c1.matches(redex)) {
                    result.add(((AllServiceResponse.ServiceModel) serviceModel));
                }
            } else {
                char[] chars1 = ((AllServiceItemResponse.ServiceItemModel) serviceModel).getItemName().toCharArray();
                String c1 = Character.toString(chars1[0]);
                if (c1.matches(redex)) {
                    resultFir.add(((AllServiceItemResponse.ServiceItemModel) serviceModel));
                }
            }
        });
        if (isService) {
            return result;
        } else {
            return resultFir;
        }
    }

    //对数字排序
    private <T> Object getNumService(List<T> list, String redex, boolean isService) {
        if (isService) {
            List<AllServiceResponse.ServiceModel> result = (List<AllServiceResponse.ServiceModel>) getNewService(list,
                    redex, isService);
            Collections.sort(result, new Comparator<AllServiceResponse.ServiceModel>() {
                public int compare(AllServiceResponse.ServiceModel s1, AllServiceResponse.ServiceModel s2) {
                    return s1.getServiceName().compareTo(s2.getServiceName());
                }
            });
            return result;
        } else {
            List<AllServiceItemResponse.ServiceItemModel> result = (List<AllServiceItemResponse.ServiceItemModel>) getNewService(
                    list, redex, isService);
            Collections.sort(result, new Comparator<AllServiceItemResponse.ServiceItemModel>() {
                public int compare(AllServiceItemResponse.ServiceItemModel s1,
                                   AllServiceItemResponse.ServiceItemModel s2) {
                    return s1.getItemName().compareTo(s2.getItemName());
                }
            });
            return result;
        }
    }

    //对中文汉字排序
    private <T> Object getChineseService(List<T> list, String redex, boolean isService) {
        if (isService) {
            List<AllServiceResponse.ServiceModel> result = (List<AllServiceResponse.ServiceModel>) getNewService(list,
                    redex, isService);
            result.sort((v1, v2) -> {
                char[] chars1 = v1.getServiceName().toCharArray();
                char[] chars2 = v2.getServiceName().toCharArray();
                String c1 = PinyinHelper.toHanyuPinyinStringArray(chars1[0])[0].toUpperCase();
                String c2 = PinyinHelper.toHanyuPinyinStringArray(chars2[0])[0].toUpperCase();
                return c1.compareTo(c2);
            });
            return result;
        } else {
            List<AllServiceItemResponse.ServiceItemModel> result = (List<AllServiceItemResponse.ServiceItemModel>) getNewService(
                    list, redex, isService);
            result.sort((v1, v2) -> {
                char[] chars1 = v1.getItemName().toCharArray();
                char[] chars2 = v2.getItemName().toCharArray();
                String c1 = PinyinHelper.toHanyuPinyinStringArray(chars1[0])[0].toUpperCase();
                String c2 = PinyinHelper.toHanyuPinyinStringArray(chars2[0])[0].toUpperCase();
                return c1.compareTo(c2);
            });
            return result;
        }
    }

    //对英文排序
    private <T> Object getUsService(List<T> list, String redex, boolean isService) {
        if (isService) {
            List<AllServiceResponse.ServiceModel> result = (List<AllServiceResponse.ServiceModel>) getNewService(list,
                    redex, isService);
            Collections.sort(result, new Comparator<AllServiceResponse.ServiceModel>() {
                @Override
                public int compare(AllServiceResponse.ServiceModel o1, AllServiceResponse.ServiceModel o2) {
                    return o1.getServiceName().compareToIgnoreCase(o2.getServiceName());
                }
            });
            return result;
        } else {
            List<AllServiceItemResponse.ServiceItemModel> result = (List<AllServiceItemResponse.ServiceItemModel>) getNewService(
                    list, redex, isService);
            Collections.sort(result, new Comparator<AllServiceItemResponse.ServiceItemModel>() {
                @Override
                public int compare(AllServiceItemResponse.ServiceItemModel o1,
                                   AllServiceItemResponse.ServiceItemModel o2) {
                    return o1.getItemName().compareToIgnoreCase(o2.getItemName());
                }
            });
            return result;
        }
    }

    /***
     * @Author jcx
     * @Description 清除搜索历史
     * @Date 10:51 2020/12/16
     * @Param request:
     * @return String
     **/
    public String clearSearchHis(TemplateAjaxRequest request) {
        SearchService searchService = ApplicationContextUtil.get(SearchService.class);
        UserUtil userUtil = ApplicationContextUtil.get(UserUtil.class);
        String keywords = request.getParam().get("keywords");//getKeywords(request.getParam(), "keywords");
        searchService.deleteSearchHis(userUtil.getUserAccount(), keywords);
        return "ok";
    }

    /***
     * @Author jcx
     * @Description 全局搜索
     * @Date 10:52 2020/12/16
     * @Param request:
     * @return String
     **/
    public String globalSearch(TemplateAjaxRequest request) {
        SearchService searchService = ApplicationContextUtil.get(SearchService.class);
        UserUtil userUtil = ApplicationContextUtil.get(UserUtil.class);
        String keywords = request.getParam().get("keywords");//getKeywords(request.getParam(), "keywords");
        searchService.addSearchHis(userUtil.getUserAccount(), keywords);
        return "ok";
    }

    public String getNewsRead(TemplateAjaxRequest request){
        NewsService newsService = ApplicationContextUtil.get(NewsService.class);
        UserUtil userUtil = ApplicationContextUtil.get(UserUtil.class);
        String title = request.getParam().get("title");
        String context = request.getParam().get("contexts");
        String wid = request.getParam().get("wid");
        DubboCardNewsReadRequest readRequest = new DubboCardNewsReadRequest();
        readRequest.setTitle(title);
        readRequest.setContext(context);
        readRequest.setUserAccount(userUtil.getCurrentUser().getWid());
        readRequest.setWid(wid);

        newsService.updateNewsReadStatus(readRequest);
        return "ok";
    }
}
