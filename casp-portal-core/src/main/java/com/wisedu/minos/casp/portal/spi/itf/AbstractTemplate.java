package com.wisedu.minos.casp.portal.spi.itf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wisedu.minos.casp.portal.CasProperties;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.i18n.I18nInitializer;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.configcomponent.AbstractComponent;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.ProgrammeApiAdapter;
import com.wisedu.minos.casp.portal.service.impl.SearchService;
import com.wisedu.minos.casp.portal.service.impl.ServiceApiService;
import com.wisedu.minos.casp.portal.service.impl.ServiceItemApiService;
import com.wisedu.minos.casp.portal.service.impl.TemplateService;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.TemplatePageUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.GroupTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractTemplate implements ITemplate {
    private static final Logger logger = LogManager.getLogger(AbstractTemplate.class);

    protected Map<String, Object> langMessagesMap = new HashMap<>();
    protected GroupTemplate groupTemplate;

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
            return this.visitService(requestParam, request);
        } else if ("getServiceItemRelService".equals(request.getMethod())) {
            Map<String, String> requestParam = request.getParam();
            return this.getServiceItemRelService(requestParam, request);
        } else if ("matchSearch".equals(request.getMethod())) {
            return this.matchSearch(request);
        } else if ("clearSearchHis".equals(request.getMethod())) {
            return this.clearSearchHis(request);
        } else if ("globalSearch".equals(request.getMethod())) {
            return this.globalSearch(request);
        }
        return doExecDispatcher(request);
    }


    /**
     * visitService
     * <p/>
     * 访问服务计数
     *
     * @param requestParam
     * @param request
     * @return java.lang.String
     * @throws
     * @date 2020/10/22 17:31
     */
    protected String visitService(Map<String, String> requestParam, TemplateAjaxRequest request) {
        String id = requestParam.get("id");//getKeywords(requestParam, "id");
        String type = requestParam.get("type");//getKeywords(requestParam, "type");
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        String userWid="guest";
        if(null!=currentUser){
            userWid=currentUser.getWid();
        }
        if ("service".equals(type)) {
            return ApplicationContextUtil.get(ServiceApiService.class)
                    .visitService(new VisitServiceRequest().setServiceWid(id).setUserWid(userWid)).getErrmsg();
        } else if ("serviceItem".equals(type)) {
            return ApplicationContextUtil.get(ServiceItemApiService.class)
                    .visitServiceItem(new VisitServiceItemRequest().setItemWid(id)).getErrmsg();
        } else {
            return "ok";
        }
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
        String keywords = request.getParam().get("keywords");//getKeywords(request.getParam(), "keywords");
        if (StringUtils.isBlank(keywords)) {
            matchSearchResponses.put("service", Collections.emptyList());
            matchSearchResponses.put("serviceItem", Collections.emptyList());
            return matchSearchResponses;
        }
        AllServiceRequest allServiceRequest = new AllServiceRequest().setPageSize(5).setSearchKey(keywords);

        UserUtil userUtil = ApplicationContextUtil.get(UserUtil.class);
        UserInfo currentUser = userUtil.getCurrentUser();
        if (currentUser == null) {
            // 未登陆情况下获取不到服务了 柳俊确认 2020/11/4，后续需要获取有游客权限的服务
            // 获取有游客权限的服务 产品确认 3.1.2版本
            allServiceRequest.setUserId("");
            allServiceRequest.setConsole(false);
        } else {
            allServiceRequest.setUserId(currentUser.getWid());
//            allServiceRequest.setPermission(true);
        }
        boolean isMobileDevice = CommonUtil.isMobileDevice();
        if(isMobileDevice){
            // 看移动端是否需要展示pc端服务
            boolean isShowPCService = ApplicationContextUtil.get(CardUtil.class).getIsShowPCService();
            if(!isShowPCService){
                allServiceRequest.setServiceStation("1");
            }
        } else{
            allServiceRequest.setServiceStation("0");
        }
        List<AllServiceResponse.ServiceModel> allService = ApplicationContextUtil.get(ServiceApiService.class)
                .getAllService(allServiceRequest);
        //        List<AllServiceResponse.ServiceModel> allService = getMockServiceData(keywords);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(allService)) {
            //对中文汉字排序
            List<AllServiceResponse.ServiceModel> chineseService = (List<AllServiceResponse.ServiceModel>) getChineseService(
                    allService, "[\\u4E00-\\u9FA5]+", true);
            //对英文排序
            List<AllServiceResponse.ServiceModel> usService = (List<AllServiceResponse.ServiceModel>) getUsService(
                    allService, "[a-zA-Z]+", true);
            //对数字排序
            List<AllServiceResponse.ServiceModel> numResult = (List<AllServiceResponse.ServiceModel>) getNumService(
                    allService, "^[0-9]+[0-9]*$", true);
            chineseService.addAll(usService);
            chineseService.addAll(numResult);
            if (chineseService.size() != allService.size()) {
                allService.removeAll(chineseService);
                chineseService.addAll(allService);
            }
            matchSearchResponses.put("service", chineseService);
        } else {
            matchSearchResponses.put("service", allService);
        }

        AllServiceItemRequest allServiceItemRequest = new AllServiceItemRequest().setPageSize(5).setSearchKey(keywords);
        if (currentUser == null) {
            //未登陆情况下先获取所有服务事项，后续需要获取有游客权限的服务
            allServiceItemRequest.setUserId("guest");
            allServiceItemRequest.setIsRelate(0);
        } else {
            allServiceItemRequest.setIsRelate(0);
            allServiceItemRequest.setUserId(currentUser.getWid());
        }
        List<AllServiceItemResponse.ServiceItemModel> allServiceItem = ApplicationContextUtil.get(ServiceItemApiService.class)
                .getAllServiceItem(allServiceItemRequest);
        if (5 < allServiceItem.size()) {
            allServiceItem = new ArrayList<>(allServiceItem.subList(0, 5));
        }
        allServiceItem.sort(Comparator.comparing(AllServiceItemResponse.ServiceItemModel::getItemName));
        //        List<AllServiceItemResponse.ServiceItemModel> allServiceItem = getMockServiceItemData(keywords);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(allServiceItem)) {
            //对中文汉字排序
            List<AllServiceItemResponse.ServiceItemModel> chineseServiceItem = (List<AllServiceItemResponse.ServiceItemModel>) getChineseService(
                    allServiceItem, "[\\u4E00-\\u9FA5]+", false);
            //对英文排序
            List<AllServiceItemResponse.ServiceItemModel> usServiceItem = (List<AllServiceItemResponse.ServiceItemModel>) getUsService(
                    allServiceItem, "[a-zA-Z]+", false);
            //对数字排序
            List<AllServiceItemResponse.ServiceItemModel> numResult = (List<AllServiceItemResponse.ServiceItemModel>) getNumService(
                    allServiceItem, "^[0-9]+[0-9]*$", false);
            chineseServiceItem.addAll(usServiceItem);
            chineseServiceItem.addAll(numResult);
            if (chineseServiceItem.size() != allServiceItem.size()) {
                allServiceItem.removeAll(chineseServiceItem);
                chineseServiceItem.addAll(allServiceItem);
            }
            matchSearchResponses.put("serviceItem", chineseServiceItem);
        } else {
            matchSearchResponses.put("serviceItem", allServiceItem);
        }
        return matchSearchResponses;
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
}
