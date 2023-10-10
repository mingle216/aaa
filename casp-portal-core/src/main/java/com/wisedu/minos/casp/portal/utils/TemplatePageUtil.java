package com.wisedu.minos.casp.portal.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.utils.*;
import com.wisedu.minos.casp.portal.dao.entity.PageInfoEntity;
import com.wisedu.minos.casp.portal.dao.entity.ServiceSearchHisEntity;
import com.wisedu.minos.casp.portal.dao.entity.TemplateEntity;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.service.impl.*;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TemplatePageUtil
 * <p/>
 * 模板页面工具类
 *
 * @author Administrator
 * @date 2020-10-3 13:47
 * Copyright (c) 2018 wisedu
 */
@Component
public class TemplatePageUtil implements ApplicationContextAware {

    private static final String RANDOM_SERVICE = "randomService";
    private static final String RANDOM_SERVICE_MATTER = "randomServiceMatter";
    private static final String DEFAULT_SERVICE_NAME = "";
    private static final String DEFAULT_SERVICE_ITEM_NAME = "获取随机服务事项";
    private static final String ENABLE = "Y";

    private static final Logger logger = LogManager.getLogger(TemplatePageUtil.class);
    ApplicationContext applicationContext;


    @Autowired
    HttpServletRequest httpRequest;
    @Autowired
    ShowProgrammeService showProgrammeService;
    @Autowired
    TemplateService templateService;

    @Autowired
    SearchService searchService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    PageInfoService pageInfoService;
    @Autowired
    ServiceItemApiService serviceItemApiService;
    @Autowired
    UserUtil userUtil;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * getTemplatePage
     * <p/>
     * 获取模板页面
     *
     * @param templatePageCode
     * @return com.wisedu.minos.casp.portal.spi.itf.ITemplatePage
     * @throws
     * @date 2020-10-3 13:45
     */
//    public ITemplatePage getTemplatePage(String templatePageCode) {
//        Map<String, ITemplatePage> beans = applicationContext.getBeansOfType(ITemplatePage.class);
//        Global.PlatformType platformType = RequestUtil.getPlatform(httpRequest);
//        //获取当前展示方案
//        ShowProgrammeEntity defaultProgramme = showProgrammeService.getDefaultProgramme(platformType.getType());
//        String localTemplateId = templateService.getById(defaultProgramme.getTemplateId()).getTemplateId();
//        return beans.entrySet().stream()
//                .filter(stringITemplatePageEntry -> stringITemplatePageEntry.getValue().templateId().equals(localTemplateId)&&stringITemplatePageEntry.getValue().templatePageCode().equals(templatePageCode))
//                .map(Map.Entry::getValue).findFirst().orElseThrow(() -> new BusinessException("未匹配到模板页面"));
//    }


    /***
     * @Author jcx
     * @Description 获取当前启用的所有模板的ID
     * @Date 19:24 2021/7/19
     * @return List<String>
     **/
    private List<String> getTemplateIds(){
        return templateService.list(new QueryWrapper<TemplateEntity>().eq(DbFieldConstant.IS_DELETED,Global.DeleteStatus.NO.getId())).stream().map(TemplateEntity::getTemplateId).collect(Collectors.toList());
    }

    /**
     * getTemplatePageContents
     * <p/>
     * 返回模板页面展示内容  用于管控台预览
     * @param
     * @return java.util.List<TemplateContent>
     * @throws
     * @date 2020-10-3 14:17
     */
    public List<TemplateContent> getTemplatePageContents(String platformType) {
        List<TemplateContent> templateContents = new ArrayList<>();
        List<String> templateIds = getTemplateIds();
        templateIds.forEach(templateId->{
            templateContents.addAll(getTemplateInfo(templateId,Integer.valueOf(platformType)));
        });
       return templateContents;
    }

    /***
     * @Author jcx
     * @Description 获取某个模板下的配置信息
     * @Date 20:02 2021/7/19
     * @Param id:
     * @Param platformType:
     * @return List<TemplateContent>
     **/
    public List<TemplateContent> getTemplateInfo(String id,int platformType){
        String resultConfig="";
        List<TemplateContent> templateContents = new ArrayList<>();
        //判断是否移动端，读取的配置文件位置不同
        String configPath= platformType==Global.PlatformType.MOBILE.getType()?"config/" +id + "/templateMobileInfo.json":"config/" +id + "/templateInfo.json";
        try ( InputStreamReader reader = new InputStreamReader(HomeService.class.getClassLoader().getResourceAsStream(configPath))){
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            resultConfig=sb.toString();
            TemplateContent[] array = new Gson().fromJson(resultConfig,TemplateContent[].class);
            templateContents= Arrays.asList(array);

            // ---- add by czj 2021-08-23 start 模板文件配置文件中，可以不配置模板ID及支持的终端类型
            if(CollectionUtils.isNotEmpty(templateContents)){
                List<String> platforms = new ArrayList<>();
                platforms.add(platformType + "");
                templateContents.stream().forEach(item -> {
                    item.setTemplateId(id);
                    item.setSupportPlatform(platforms);
                });
            }
            // ---- add by czj 2021-08-23 end
            logger.debug("获取到"+id+"的配置信息---{}",resultConfig);
        } catch ( Exception e ) {
            logger.error("获取"+id+"的配置信息发生异常---{}",e);
        }
        return templateContents;
    }

    /***
     * @Author jcx
     * @Description 格式化页面配置
     * @Date 20:02 2021/7/19
     * @Param pageConfig:
     * @return Map<String,Object>
     **/
    public Map<String, Object> transPageConfig(String pageConfig){
        Properties properties = PropertiesUtil.loadString(pageConfig);
        Map<String, Object> pageConfigMap = PropertiesUtil.toMap(properties);
        this.processHeaderTitle(pageConfigMap);
        this.processHeaderSubTitle(pageConfigMap);
        return pageConfigMap;
    }

    /**
     * processHeaderSubTitle
     * <p/>
     * 处理标题国际化,转为List<LangResoure>由模板函数cI18n处理
     *
     * @param pageConfigMap
     * @return void
     * @throws
     * @date 2020-10-4 21:41
     */
    protected void processHeaderTitle(Map<String, Object> pageConfigMap) {
        List<LangResoure> headTitleLangs = new ArrayList<>();
        pageConfigMap.forEach((s, o) -> {
            if (s.startsWith("header.title")) {
                headTitleLangs.add(new LangResoure().setKey(s.replaceAll("header\\.title\\.", "")).setValue((String) o));
            }
        });
        pageConfigMap.put("header.titleLangs", headTitleLangs);
    }

    /**
     * processHeaderSubTitle
     * <p/>
     * 处理子标题国际化,转为List<LangResoure>由模板函数cI18n处理
     *
     * @param pageConfigMap
     * @return void
     * @throws
     * @date 2020-10-4 21:41
     */
    protected void processHeaderSubTitle(Map<String, Object> pageConfigMap) {
        List<LangResoure> headSubTitleLangs = new ArrayList<>();
        pageConfigMap.forEach((s, o) -> {
            if (s.startsWith("header.subTitle")) {
                headSubTitleLangs.add(new LangResoure().setKey(s.replaceAll("header\\.subTitle\\.", "")).setValue((String) o));
            }
        });
        pageConfigMap.put("header.subTitleLangs", headSubTitleLangs);
    }

    private void processSearchPlaceholder(Map<String, Object> pageConfigMap,String lang) {
        String searchPlaceholder;
        String placeholderType = (String) pageConfigMap.get("search.placeholder");
        if (! StringUtil.isEmpty(placeholderType)) {
            switch (placeholderType) {
                case RANDOM_SERVICE:
                    searchPlaceholder = getRandomService();
                    break;
                case RANDOM_SERVICE_MATTER:
                    searchPlaceholder = getRandomServiceItem(lang);
                    break;
                default:
                    searchPlaceholder = placeholderType;
            }
            pageConfigMap.put("search.placeholderVal", searchPlaceholder);
        } else {
            pageConfigMap.put("search.placeholderVal", "");
        }
    }
    /**
     * getRandomService
     * <p/>
     * 获取随机服务名称
     *
     * @param
     * @return java.lang.String
     * @throws
     * @date 2020/10/15 18:01
     */
    protected String getRandomService() {
        logger.info("获取随机服务");
        AllServiceRequest allServiceRequest = new AllServiceRequest().setPageSize(9999);
        UserUtil userUtil = ApplicationContextUtil.get(UserUtil.class);
        UserInfo currentUser = userUtil.getCurrentUser();
        if (currentUser == null) {
            //TODO 未登陆情况下获取不到服务了 柳俊确认 2020/11/4，后续需要获取有游客权限的服务
            allServiceRequest.setUserId("");
            allServiceRequest.setConsole(false);
        } else {
            allServiceRequest.setUserId(currentUser.getWid());
        }
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if(isMobileDevice){

            // 看移动端是否需要展示pc端服务
            boolean isShowPCService = ApplicationContextUtil.get(CardUtil.class).getIsShowPCService();
            if(!isShowPCService){
                allServiceRequest.setServiceStation("1");
            }
        } else {
            allServiceRequest.setServiceStation("0");
        }
        List<AllServiceResponse.ServiceModel> allService = ApplicationContextUtil.get(ServiceApiService.class).getAllService(allServiceRequest);
        if (allService.isEmpty()) {
            logger.info("获取服务数量为0，返回默认服务名称为空:{}", DEFAULT_SERVICE_NAME);
            return DEFAULT_SERVICE_NAME;
        }
        int i = RandomUtils.nextInt(allService.size());
        String serviceName = allService.get(i).getServiceName();
        logger.info("获取服务数量为{}，返回随机服务名称:{}", allService.size(), serviceName);
        return serviceName;
    }

    /**
     * getRandomServiceItem
     * <p/>
     * 获取随机服务事项名称
     *
     * @param
     * @return java.lang.String
     * @throws
     * @date 2020/10/15 18:10
     */
    protected String getRandomServiceItem(String lang) {
        logger.info("获取随机服务事项");
        UserInfo currentUser = userUtil.getCurrentUser();
        String userId="guest";
        if(null!=currentUser){
            userId=currentUser.getWid();
            lang=currentUser.getPreferredLanguage();
        }else{
            lang=StringUtil.isEmpty(lang)?Global.DEFAULT_LANGUAGE:lang;
        }

        List<AllServiceItemResponse.ServiceItemModel> allServiceItem = serviceItemApiService.getAllServiceItem
                (new AllServiceItemRequest().setService(false).setIsRelate(0).setUserId(userId).setLang(lang));
        if (allServiceItem.isEmpty()) {
            logger.info("获取服务事项数量为0，返回默认服务事项名称:{}", DEFAULT_SERVICE_ITEM_NAME);
            return "";
        }
        if ( CollectionUtils.isNotEmpty(allServiceItem)&&5 < allServiceItem.size()) {
            allServiceItem = new ArrayList<>(allServiceItem.subList(0, 5));
        }
//        allServiceItem.sort(Comparator.comparing(AllServiceItemResponse.ServiceItemModel::getItemName));
        int i = RandomUtils.nextInt(allServiceItem.size());
        String serviceName = allServiceItem.get(i).getItemName();
        logger.info("获取服务事项数量为{}，返回随机服务事项名称:{}", allServiceItem.size(), serviceName);
        return serviceName;
    }

    /***
     * @Author jcx
     * @Description 获取面包屑
     * @Date 20:04 2021/7/19
     * @Param pageInfoEntity:
     * @return List<Breadcrumb>
     **/
    public List<Breadcrumb> getBreadcrumb (PageInfoEntity pageInfoEntity) {
        List<PageInfoEntity> allParentPages = pageInfoService.getAllParentPage(pageInfoEntity.getWid());
        List<Breadcrumb> breadcrumbs = new ArrayList<>();
        for (int i = 0; i < allParentPages.size(); i++) {
            PageInfoEntity allParentPage = allParentPages.get(i);
            breadcrumbs.add(new Breadcrumb()
                    .setPageCode(allParentPage.getPageCode())
                    .setPageName(allParentPage.getPageName())
                    .setI18nKey(allParentPage.getPageTitleLangKey())
                    .setUri("/" + allParentPage.getPageCode())
                    .setIndex(i)
                    .setIsCurrent(i == allParentPages.size() - 1));
        }
        return breadcrumbs;
    }

    /***
     * @Author jcx
     * @Description 获取全局搜索随机值
     * @Date 20:03 2021/7/19
     * @Param pageConfig:
     * @return Map<String,Object>
     **/
    public Map<String, Object> getPlaceholderVal(String pageConfig,String lang) {
        Map<String, Object> pageConfigMap = this.transPageConfig(pageConfig);
        this.processSearchPlaceholder(pageConfigMap,lang);
        return pageConfigMap;
    }

    /***
     * @Author jcx
     * @Description 获取搜索历史
     * @Date 20:04 2021/7/19
     * @Param pageConfig:
     * @Param userInfo:
     * @return List<ServiceSearchHisEntity>
     **/
    public List<ServiceSearchHisEntity> getSearchHis(Map<String, Object> pageConfig, UserInfo userInfo) {
        //判断历史搜索是否展示
        if (userInfo != null && (CommonUtil.isMobileDevice(httpServletRequest)||ENABLE.equals(pageConfig.get("search.history.display"))||StringUtil.isEmpty(String.valueOf(pageConfig.get("search.history.display"))))) {
            //查询历史搜索
            List<ServiceSearchHisEntity> serviceSearchHis = searchService.getServiceSearchHis(10);
            return serviceSearchHis;
        } else {
            return null;
        }
    }



}
