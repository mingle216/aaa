package com.wisedu.casp.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IOUtils;
import com.wisedu.casp.cal.api.UserCalDubboService;
import com.wisedu.casp.cal.model.DubboResponse;
import com.wisedu.minos.api.model.DubboModelInfo;
import com.wisedu.minos.api.model.DubboModelInfoResponse;
import com.wisedu.minos.api.model.DubboServiceInfo;
import com.wisedu.minos.api.model.DubboServiceInfoApiResp;
import com.wisedu.minos.casp.portal.CasProperties;
import com.wisedu.minos.casp.portal.PortalManagerProperties;
import com.wisedu.minos.casp.portal.common.baseservice.CaspSafetyService;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.*;
import com.wisedu.minos.casp.portal.dao.entity.*;
import com.wisedu.minos.casp.portal.i18n.I18nService;
import com.wisedu.minos.casp.portal.i18n.Lang;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.impl.*;
import com.wisedu.minos.casp.portal.spi.itf.ICard;
import com.wisedu.minos.casp.portal.spi.itf.ITemplate;
import com.wisedu.minos.casp.portal.spi.model.TemplateContext;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.TemplatePageUtil;
import com.wisedu.minos.casp.portal.utils.TemplateUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.casp.portal.vo.FileVo;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author kaisir
 */
@Controller
public class HomeController extends AbstractController {
    private static final Logger logger = LogManager.getLogger(HomeController.class);
    public static final String MOBILE_SERVICE_VISITS = "mobile_service_visits";

    @Autowired
    CasProperties casProperties;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    ShowProgrammeService showProgrammeService;


    @Autowired
    SidebarService sidebarService;

    @Autowired
    TemplateService templateService;

    @Autowired
    TemplateUtil templateUtil;
    @Autowired
    TemplatePageUtil templatePageUtil;

    @Autowired
    CardUtil cardUtil;

    @Autowired
    InternationalizationService internationalizationService;

    @Autowired
    UserUtil userUtil;
    @Autowired
    I18nService i18nService;
    @Autowired
    HomeService homeService;
    @Autowired
    PageCardConfigService pageCardConfigService;
    @Autowired
    PageInfoService pageInfoService;
    @Autowired
    ProgrammeLocalStyleService proLocalStyleService;
    @Autowired
    private CaspSafetyService caspSafetyService;

    @Autowired
    MenuService menuService;

    @Autowired
    RedisUtil redisUtil;

    @DubboReference
    UserCalDubboService userCalDubboService;
    @Autowired
    HttpServletResponse response;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private HttpSession session;

    @Value("${system.card.redis.expireTime:5}")
    private Long redisExpireTime;

    @Value("${module.domain}")
    private String moudleDomain;
    @Value("${minos.stata.server.addresses}")
    private String stataAddress;

    //此版本定义在minos.properties
    @Value("${minos.version:1.0.0}")
    private String portalVersion;
    @Value("${minos.appName:门户}")
    private String portalName;

    private static RSAPrivateKey IndentityTokenKey = null;



    private final List<String> viewMethods = Arrays.asList(new String[]{"render", "renderData", "view", "viewData"
            ,"getMarkNumber", "getConfig","getChannelNews","getConfiguredChannel","getPersonalDataDetail","getPersonalDataList"});

    @GetMapping(value = {"/"})
    public String getPageView() {
        return "redirect:" + Global.INDEX_URI;
    }

    static {
        //加载Rsa私钥文件
        ObjectInputStream out = null;
        try {
            out = new ObjectInputStream(HomeController.class.getClassLoader().getResourceAsStream("public/key/PrivateKey"));
            IndentityTokenKey = ( RSAPrivateKey ) out.readObject();
            if(null==IndentityTokenKey){
                throw new BusinessException("IndentityTokenKey不能为空");
            }
        } catch ( ClassNotFoundException | IOException e ) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    @GetMapping(value = {Global.GET_PAGEVIEW_URI})
    @ResponseBody
    public ResultData getPageView(HttpServletRequest httpRequest) {
        if(null!=httpRequest.getSession().getAttribute("filterError")){
            httpRequest.getSession().removeAttribute("filterError");
        }
        String pageCode = httpRequest.getParameter("pageCode");
        Map<String, Object> result = new HashMap<>(7);
        PageContext pageContext = null;
        UserInfo currentUser = userUtil.getCurrentUser();
        String siteWid = HttpUtil.getValueFromSessionAndCookie(Global.SITE_WID,httpRequest);

        String getPageViewCacheKey="";
        if(null==currentUser){
            getPageViewCacheKey="getPageView:visitorKey:"+pageCode+":"+siteWid;
        }else{
            getPageViewCacheKey="getPageView:"+currentUser.getUserAccount()+":"+pageCode+":"+siteWid;
        }
        logger.debug("-----------------登录的用户----------：{}",currentUser);
        try {
            pageContext = (PageContext) httpRequest.getAttribute("pageContext");
            if (pageContext == null) {
                throw new BusinessException("未获取到PageContext页面配置信息，请检查是否启用cas认证,是否在cas认证服务器授权");
            }
            Object getPageViewResult = redisUtil.get(getPageViewCacheKey);
            if(!redisUtil.isHasKey(getPageViewCacheKey)||(redisUtil.isHasKey(getPageViewCacheKey)&&null==getPageViewResult)){
                result.put("pageTitle",internationalizationService.getlangValue(pageContext.getPageInfoEntity().getPageTitleLangKey()));
                //获取当前侧边栏
                SideBarGroup sidebarGroup = sidebarService.getSidebarGroup(pageContext.getShowProgrammeEntity().getWid());
                //获取当前模板
                TemplateEntity templateEntity = templateService.getById(pageContext.getShowProgrammeEntity().getTemplateId());
                MenuBiz currentLv1Menu = pageContext.getCurrentLv1Menu();
                String activeMenuId ="";
                if(currentLv1Menu == null){
                    MenuEntity activeMenu = menuService.getMenu(pageContext.getShowProgrammeEntity().getWid(), pageContext.getPageInfoEntity().getParentId());
                    if(activeMenu != null) {
                        activeMenuId = activeMenu.getWid();
                    }
                }else {
                    activeMenuId =currentLv1Menu.getWid();
                }
                ITemplate template = templateUtil.getTemplate(templateEntity.getTemplateId());

                if(StringUtil.isEmpty(pageContext.getShowProgrammeEntity().getTemplateConfig())){
                    ShowProgrammeEntity showProgrammeEntity = pageContext.getShowProgrammeEntity();
                    showProgrammeEntity.setTemplateConfig(template.getTemplateConfig(String.valueOf(showProgrammeEntity.getPlatformType())));
                    showProgrammeService.updateById(showProgrammeEntity);
                    pageContext.setShowProgrammeEntity(showProgrammeEntity);
                }
                TemplateContext templateContext = new TemplateContext(activeMenuId, pageContext.getPageInfoEntity(), pageContext.getShowProgrammeEntity(), templateEntity, pageContext.getMenuTree(), sidebarGroup, null);
                result.put("pageContext",templateContext);
                result.put("renderData",renderData(templateContext));
                //处理返回按钮
                handlerBack( templateContext, result);
                // URL参数
                result.put("globalPageParam", JSON.toJSON(RequestUtil.paramMaps(httpRequest)));
                String baiduSdkAk = systemConfigService.getSystemConfigValue("baidu_sdk_ak");
                result.put("baiduSdkAk",baiduSdkAk);
                Map<String, String> pageConfig = systemConfigService.getSystemConfigMap(SystemConfigService.ConfigModel.PAGE);
                result.put("pageConfigMap",pageConfig);
                result.put("websiteNavigation",moudleDomain+"/index.html#/webInfo");
                result.put("stataAddress",stataAddress);
                result.put("portalVersion",portalVersion);
                result.put("portalName",portalName);
                result.put("blackAndWhiteMode",switchBlackAndWhiteMode(new BlackAndWhiteModeReq("2")).getData());
                result.put("mobileBlackAndWhiteMode",switchMobileBlackAndWhiteMode(new BlackAndWhiteModeReq("2")).getData());
                redisUtil.set(getPageViewCacheKey, result, redisExpireTime, TimeUnit.SECONDS);
            }else{
                result= (Map<String, Object>) getPageViewResult;
            }
        } catch ( Exception e ) {
            logger.error("获取页面展示信息错误" , e);
        }
        return ResultData.success(result);
    }

    /***
     * @Author jcx
     * @Description 处理返回按钮
     * @Date 16:09 2021/11/11
     * @Param templateContext:
     * @Param result:
     * @return void
     **/
    private void handlerBack(TemplateContext templateContext,Map<String, Object> result){
        if(Global.PlatformType.MOBILE.getType()==templateContext.getShowProgrammeEntity().getPlatformType()){
            //移动端，所有非一级页面和内部页面，头部都需要返回按钮
            MenuEntity activeMenu = menuService.getMenu(templateContext.getShowProgrammeEntity().getWid(), templateContext.getPageInfoEntity().getWid());
            boolean flag=false;
            if(activeMenu==null||!Global.ROOT_PAGE_ID.equals(templateContext.getPageInfoEntity().getParentId())){
                flag=true;
            }
            result.put("isShowBackButton",flag);
        }
    }

    private Map<String, Object> renderData(TemplateContext templateContext) {
        logger.info("renderData..");
        Map<String, Object> result = new HashMap<>();
        TemplatePageUtil templatePageUtil = ApplicationContextUtil.get(TemplatePageUtil.class);
        //绑定页面配置
        Map<String, Object> pageConfig = templatePageUtil
                .transPageConfig(templateContext.getPageInfoEntity().getPageConfig());
        result.put("pageConfig", pageConfig);
        result.put("portalManager", ApplicationContextUtil.get(PortalManagerProperties.class).loadData());
        List<Breadcrumb> breadcrumb = templatePageUtil.getBreadcrumb(templateContext.getPageInfoEntity());
        result.put("breadcrumb", breadcrumb);
        return result;
    }

    /***
     * @Author jcx
     * @Description 根据人获取消息中心数量
     * @Date 15:32 2021/4/10
     * @return ResultData
     **/
    @GetMapping("/getMessageCount")
    @ResponseBody
    public ResultData getMessageCount(){
        return ResultData.success(homeService.getMessageCount());
    }

    /***
     * @Author yyu
     * @Description 根据侧边栏id获取数量
     * @Date 15:32 2021/8/26
     * @return ResultData
     **/
    @RequestMapping("/getSidebarCount")
    @ResponseBody
    public ResultData getSidebarCount(@RequestParam("wid") String wid, @RequestParam(value = "type", defaultValue = "0") String type){
        return ResultData.success(homeService.getSidebarCount(wid, type));
    }

    /***
     * @Author yyu
     * @Description 获取待办数量
     * @Date 15:32 2021/8/26
     * @return ResultData
     **/
    @RequestMapping("/getTodoTaskCount")
    @ResponseBody
    public ResultData getTodoTaskCount(@RequestParam("userId") String userId){
        return ResultData.success(homeService.getTodoTaskCount(userId));
    }

    /***
     * @Author yyu
     * @Description 获取待办数量
     * @Date 15:32 2021/8/26
     * @return ResultData
     **/
    @RequestMapping("/getUserMessageCount")
    @ResponseBody
    public ResultData getUserMessageCount(@RequestParam("userId") String userId){
        return ResultData.success(homeService.getUserMessageCount(userId));
    }

    /***
     * @Author jcx
     * @Description 提供前端获取当前登录用户信息接口
     * @Date 17:01 2020/12/23
     * @return ResultData
     **/
    @GetMapping("/getLoginUser")
    @ResponseBody
    public ResultData getLoginUser(){
        UserInfo currentUser = userUtil.getCurrentUser();
        //查询多语言接口（不放入getCurrentUser接口的原因是，有可能每一张卡片都会调用，影响性能）
        List<Lang> supportDisplayLanguages = i18nService.getSupportDisplayLanguages();
        if(null!=currentUser){
            logger.debug("获取用户：{} ", JSON.toJSONString(currentUser));
            currentUser.setSupportLanguages(supportDisplayLanguages);
            if(CollectionUtils.isNotEmpty(supportDisplayLanguages)&&supportDisplayLanguages.size()==1){
                currentUser.setPreferredLanguage(Global.DEFAULT_LANGUAGE);
                UserInfo minosUser = (UserInfo) session.getAttribute("minosUserInfo");
                minosUser.setPreferredLanguage(Global.DEFAULT_LANGUAGE);
                session.setAttribute("minosUserInfo",minosUser);
            }
        }
        return ResultData.success(currentUser);
    }

    /***
     * @Author jcx
     * @Description  跳转服务逻辑判断
     * @Date 18:30 2020/12/23
     * @return ResultData
     **/
    @RequestMapping("/serviceShow")
    @ResponseBody
    public ResultData serviceShow(HttpServletRequest request,@RequestParam("serviceId") String serviceId,@RequestParam(value = "isMobile", defaultValue = "0") Integer isMobile){
        if(StringUtil.isEmpty(serviceId)){
            return ResultData.error("传参错误，请检查");
        }
        //查询服务信息
        ResultData resultData = queryServiceByWid(serviceId,"0", request);
        DubboServiceInfo dubboServiceInfo = null;
        String serviceUrl = "";
        if("0".equals(resultData.getErrcode())){
            Map<String, Object> data = (Map<String, Object>) resultData.getData();
            List<DubboServiceInfo> dubboServiceInfos= (List<DubboServiceInfo>) data.get("serviceInfo");
            if(CollectionUtils.isEmpty(dubboServiceInfos)){
                return ResultData.error("noServicePermission");
            }
            dubboServiceInfo = dubboServiceInfos.get(0);
            if(dubboServiceInfo.getServiceStation().intValue() == 0){
                serviceUrl = dubboServiceInfo.getPcAccessUrl();
            }else if(dubboServiceInfo.getServiceStation().intValue() == 1){
                serviceUrl = dubboServiceInfo.getMobileAccessUrl();
            }else{
                serviceUrl = CommonUtil.isMobileDevice() ? dubboServiceInfo.getMobileAccessUrl() : dubboServiceInfo.getPcAccessUrl();
            }
            //应用停用删除、服务删除、服务不可见，都会提示该服务不可用
            if(Global.DeleteStatus.YES.getId()==dubboServiceInfo.getIsDeleted() ||
                    Global.EnableStatus.DISABLE.getId()==dubboServiceInfo.getAppEnabled()){
                return ResultData.error("noServicePermission");
            }else{
                if(Global.CONSTANT_NO.equals(dubboServiceInfo.getIsShowGroup())){
                    Map<String, Object> result = new HashMap<>();
                    result.put("serviceUrl", serviceUrl);
                    return ResultData.success(result);
                }
            }
        }
        //获取当前登录用户信息
        UserInfo currentUser = userUtil.getCurrentUser();
        String userAccount =null!=currentUser?currentUser.getUserAccount():"";
        //检查用户是否有该服务使用权限
        CheckUserServiceRes checkUserServiceData = (CheckUserServiceRes)homeService.isUseService(userAccount, serviceId);
        //当前登录用户无此服务的使用权限
        if(!checkUserServiceData.isData()){
            return ResultData.error("noPermission");
        }
        //根据服务ID查询该服务授权用户组信息
        DubboModelInfoResponse queryGrantConfigData = homeService.queryGrantConfig(serviceId);

        List<DubboModelInfo> dubboModelInfos = queryGrantConfigData.getData();

        if(CollectionUtils.isEmpty(dubboModelInfos)){
            Map<String, Object> result = new HashMap<>();
            result.put("serviceUrl", serviceUrl);
            return ResultData.success(result);
        }
        List<ServiceGrant> grantData = ObjectUtil.copyListProperties(dubboModelInfos,ServiceGrant::new);
        return ResultData.success(homeService.serviceShow(request, grantData,serviceUrl,IndentityTokenKey));
    }
    /***
     * @Author jcx
     * @Description 根据服务wid查询服务信息、展示方案信息和用户信息
     * @Date 10:36 2020/12/24
     * @return ResultData
     **/
    @RequestMapping("/queryServiceByWid/{serviceWid}/{isFilterService}")
    @ResponseBody
    public ResultData queryServiceByWid(@PathVariable("serviceWid") String serviceWid,@PathVariable("isFilterService") String isFilterService,HttpServletRequest request){
        DubboServiceInfoApiResp dubboServiceInfoApiResp = homeService.queryServiceByWid(serviceWid);
        Map<String, Object> result = new HashMap<>();
        List<DubboServiceInfo> data = dubboServiceInfoApiResp.getData();
        if(CollectionUtils.isNotEmpty(data)&&Global.CONSTANT_YES.equals(isFilterService)){
            //过滤掉服务删除，应用禁用的服务数据
            result.put("serviceInfo",data.stream().filter(e -> e.getIsDeleted()!=Global.DeleteStatus.YES.getId()&&e.getAppEnabled()!=Global.EnableStatus.DISABLE.getId()).collect(Collectors.toList()));
        }else{
            result.put("serviceInfo",data);
        }
        Global.PlatformType platformType = RequestUtil.getPlatform(request);
        //获取当前展示方案
        UserInfo currentUser = ( UserInfo ) this.getLoginUser().getData();
        result.put("isLogin",null!=currentUser?true:false);
        result.put("localLang",null!=currentUser?currentUser.getPreferredLanguage():Global.DEFAULT_LANGUAGE);
        return ResultData.success(result);
    }

    /***
     * @Author jcx
     * @Description 查询国际化数据
     * @Date 11:09 2021/1/26
     * @return ResultData
     **/
    @RequestMapping("/queryI18nList")
    @ResponseBody
    public ResultData queryI18nList(@RequestParam("lang")String lang){
        return homeService.queryI18nList(lang);
    }

    /***
     * @Author jcx
     * @Description 切换用户首选语言
     * @Date 11:09 2021/2/22
     * @return ResultData
     **/
    @ManagerGatewayApi(name = "切换用户首选语言", realPath = "/portal/switchUserLanguage")
    @RequestMapping("/portal/switchUserLanguage")
    @ResponseBody
    public ResultData switchUserLanguage(@RequestParam("userLanguage")String userLanguage,@RequestParam("pageCode")String pageCode){
        return ResultData.success(homeService.switchUserLanguage(userLanguage,pageCode));
    }

    /***
     * @Author jcx
     * @Description 收藏服务
     * @Date 13:31 2021/1/4
     * @return ResultData
     **/
    @RequestMapping("/collectService")
    @ResponseBody
    public ResultData collectService(@RequestParam("id")String id,@RequestParam("operate")String operate){
        return homeService.collectService(id,operate);
    }
    /***
     * @Author jcx
     * @Description 收藏服务事项
     * @Date 13:31 2021/1/4
     * @return ResultData
     **/
    @RequestMapping("/collectServiceItem")
    @ResponseBody
    public ResultData collectServiceItem(@RequestParam("id")String id,@RequestParam("operate")String operate){
        return homeService.collectServiceItem(id,operate);
    }

    @PostMapping("/execCardMethod/{cardWid}/{cardId}")
    @ManagerGatewayApi(name = "调用所有安装的卡片的内部方法", realPath = "/execCardMethod/{cardWid}/{cardId}")
    @ResponseBody
    public ResultData execCardMethod(HttpServletRequest httpRequest, @RequestBody CardAjaxRequest cardAjaxRequest,@PathVariable("cardId") String cardId
            ,@PathVariable("cardWid") String cardWid) {
        if (caspSafetyService.legalCharacter(cardId) || caspSafetyService.legalCharacter(cardWid)){
            throw new BusinessException("非法的请求参数");
        }
        if (StringUtil.isEmpty(cardAjaxRequest.getCardId()) && StringUtil.isNotEmpty(cardId)) {
            cardAjaxRequest.setCardId(cardId);
        }
        if (StringUtil.isEmpty(cardAjaxRequest.getCardWid()) && StringUtil.isNotEmpty(cardWid)) {
            cardAjaxRequest.setCardWid(cardWid);
        }
        if ( caspSafetyService.legalCharacter(cardAjaxRequest.getCardId()) || caspSafetyService.legalCharacter(cardAjaxRequest.getCardWid()) ) {
            throw new BusinessException("非法的请求参数");
        }

        Object result;
        String method = cardAjaxRequest.getMethod();
        Map<String, String> requestParam = cardAjaxRequest.getParam();
        if(requestParam == null){
            // 防止map equals比较出现nullPointException
            requestParam = new HashedMap<>();
        }
        String key = "userId:" + userUtil.getUserAccount() + ":cardId:" + cardId + ":cardWid:" + cardWid;
        String paramHashKey ;
        if (viewMethods.contains(method)) {
            String hashKey = ":method:" + method + ":param:" + requestParam;
            result = redisUtil.hashGet(key, hashKey);
            paramHashKey = hashKey + ":parameter";
            if (null == result || !redisUtil.isHaveHashKey(key, paramHashKey)) {
                result = executeCardMethod(cardAjaxRequest);
                logger.debug("key"+ key + "存缓存");
                redisUtil.hashSet(key, hashKey, result, redisExpireTime);
                redisUtil.hashSet(key, paramHashKey, requestParam, redisExpireTime);
            }else {
                if (!redisUtil.hashGet(key, paramHashKey).equals(requestParam)){
                    result = executeCardMethod(cardAjaxRequest);
                    logger.debug("key"+ key + "存缓存");
                    redisUtil.hashSet(key, hashKey, result, redisExpireTime);
                    redisUtil.hashSet(key, paramHashKey, requestParam, redisExpireTime);
                }
                logger.debug("cardId:{}cardWid:{}method:{}直接取缓存内容",cardId,cardWid,method);
            }
        } else {
            redisUtil.del(key);
            result = executeCardMethod(cardAjaxRequest);
        }
        return ResultData.success(result);
    }

    @PostMapping("/execCardMethod")
    @ManagerGatewayApi(name = "调用所有安装的卡片的内部方法", realPath = "/execCardMethod")
    @ResponseBody
    public ResultData execCardMethod(@RequestBody CardAjaxRequest cardAjaxRequest) {
        Object result = executeCardMethod(cardAjaxRequest);
        return ResultData.success(result);
    }


    @PostMapping("/execTemplateMethod")
    @ManagerGatewayApi(name = "调用当前使用的模板的内部方法", realPath = "/execTemplateMethod")
    @ResponseBody
    public ResultData execTemplateMethod(HttpServletRequest request, @RequestBody TemplateAjaxRequest templateAjaxRequest) {
        Object result = executeTemplateMethod(request, templateAjaxRequest);
        return ResultData.success(result);
    }


    /***
     * @Author jcx
     * @Description 获取卡片配置
     * @Date 14:22 2021/1/15
     * @Param cardId:
     * @Param cardWid:
     * @return ResultData
     **/
    @GetMapping("/programme/getCardConfig")
    @ManagerGatewayApi(name = "获取卡片配置", realPath = "/programme/getCardConfig")
    @ResponseBody
    public ResultData getCardConfig(@RequestParam("cardId") String cardId,@RequestParam("cardWid")String cardWid
            ,@RequestParam("platformType")int platformType ,@RequestParam(name = "templateWid",required = false)String templateWid){
        if (caspSafetyService.legalCharacter(cardId) || caspSafetyService.legalCharacter(cardWid)){
            throw new BusinessException("非法的请求参数");
        }

        String valueConfig="";
        try {
            valueConfig=cardUtil.getCardConfigByCardWidFromDb(cardWid,String.valueOf(platformType)).getCardConfig();
        } catch ( Exception e ) {
            logger.debug("卡片【cardId】=="+cardId+"==【cardWid】=="+cardWid +",当前未有保存新配置，开启使用当前默认配置configMetaData.json" , e);
        }
        return ResultData.success(cardUtil.getComContainerConfig(cardWid,cardId,valueConfig,platformType, templateWid));
    }


    /***
     * @Author jcx
     * @Description 获取模板配置
     * @Date 14:22 2021/1/15
     * @Param templateId:
     * @Param templateWid:
     * @Param platformType: 平台类型 适配终端0：PC1：移动端
     * @return ResultData
     **/
    @GetMapping("/programme/getTemplateConfig")
    @ManagerGatewayApi(name = "获取模板配置", realPath = "/programme/getTemplateConfig")
    @ResponseBody
    public ResultData getTemplateConfig(HttpServletRequest request,@RequestParam("templateId") String templateId
            ,@RequestParam("templateWid")String templateWid,@RequestParam("platformType")int platformType){

        if (caspSafetyService.legalCharacter(templateId) || caspSafetyService.legalCharacter(templateWid)){
            throw new BusinessException("非法的请求参数");
        }

        Global.PlatformType localPlatformType = Global.PlatformType.getByType(platformType);
        ShowProgrammeEntity showProgramme = showProgrammeService.getById(templateWid);
        String templatePackageId = showProgramme.getTemplateId();
        String localTemplateId = templateService.getById(templatePackageId).getTemplateId();
        ITemplate template = templateUtil.getTemplate(localTemplateId);
        String configJson = homeService.getConfigJson(templateId,localPlatformType.getType());
        String valueTemplateConfig="";
        try {
            if(StringUtil.isEmpty(showProgramme.getTemplateConfig())){
                showProgramme.setTemplateConfig(template.getTemplateConfig(String.valueOf(platformType)));
                showProgrammeService.updateById(showProgramme);
                valueTemplateConfig=template.getTemplateConfig(String.valueOf(platformType));
            }else{
                valueTemplateConfig=showProgramme.getTemplateConfig();
            }
        } catch ( Exception e ) {
            logger.error("模板【templateId】=="+templateId+"==【templateWid】=="+templateWid +",当前未有保存新配置，开启使用当前默认配置configMetaData.json" , e);
        }
        return ResultData.success(template.getConfig(new TemplateConfigRequest(templateId,templateWid,configJson,new ComponentContainer(configJson,cardUtil.filterHttpOrHttps(valueTemplateConfig)))));
    }

    /***
     * @Author jcx
     * @Description 全局搜索框根据配置获取默认值
     * @Date 16:01 2021/3/24
     * @Param wid:
     * @return ResultData
     **/
    @GetMapping("/getPlaceholderVal/{wid}/{lang}")
    @ResponseBody
    public ResultData getPlaceholderVal(@PathVariable("wid")String wid,@PathVariable("lang")String lang){
        if(StringUtil.isEmpty(wid)){
            return ResultData.error("传参错误，请检查");
        }
        String getPlaceholderValCacheKey="getPlaceholderVal:"+wid;
        Object getPlaceholderValResult = redisUtil.get(getPlaceholderValCacheKey);
        if(!redisUtil.isHasKey(getPlaceholderValCacheKey)||(redisUtil.isHasKey(getPlaceholderValCacheKey)&&null==getPlaceholderValResult)){
            PageInfoEntity pageInfo = pageInfoService.getOne(new QueryWrapper<PageInfoEntity>().lambda().eq(PageInfoEntity::getWid, wid));
            if(null==pageInfo){
                return ResultData.error("全局搜索框根据配置获取默认值错误，无此对应页面");
            }
            Map<String, Object> placeholderVal = ApplicationContextUtil.get(TemplatePageUtil.class).getPlaceholderVal(pageInfo.getPageConfig(),lang);
            redisUtil.set(getPlaceholderValCacheKey, placeholderVal, redisExpireTime, TimeUnit.SECONDS);
            return ResultData.success(placeholderVal);
        }else{
            return ResultData.success(getPlaceholderValResult);
        }
    }


    /***
     * @Author jcx
     * @Description 全局搜索获取搜索历史
     * @Date 15:41 2021/5/28
     * @Param wid: 页面wid
     * @return ResultData
     **/
    @GetMapping("/getSearchHisVal/{wid}")
    @ResponseBody
    public ResultData getSearchHisVal(@PathVariable("wid")String wid){
        if(StringUtil.isEmpty(wid)){
            return ResultData.error("传参错误，请检查");
        }
        if(null==userUtil.getCurrentUser()){
            return ResultData.success();
        }
        PageInfoEntity pageInfo = pageInfoService.getOne(new QueryWrapper<PageInfoEntity>().lambda().eq(PageInfoEntity::getWid, wid)
                .eq(PageInfoEntity::getIsDeleted,Global.DeleteStatus.NO.getId()).eq(PageInfoEntity::getEnabledFlag,Global.EnableStatus.ENABLE.getId()));
        if(null==pageInfo){
            return ResultData.error("全局搜索框根据配置获取默认值错误，无此对应页面");
        }
        //获取模板页面
        Map<String, Object> pageConfig = ApplicationContextUtil.get(TemplatePageUtil.class).transPageConfig(pageInfo.getPageConfig());
        return ResultData.success(ApplicationContextUtil.get(TemplatePageUtil.class).getSearchHis(pageConfig, userUtil.getCurrentUser()));
    }


    @PostMapping("/execAllTemplateMethod")
    @ManagerGatewayApi(name = "调用所有安装模板的内部方法", realPath = "/execAllTemplateMethod")
    @ResponseBody
    public ResultData execAllTemplateMethod(@RequestBody TemplateAjaxRequest templateAjaxRequest) {
        Object result = homeService.executeAllTemplateMethod(templateAjaxRequest);
        return ResultData.success(result);
    }

    /**
     * 获取项目公共配置
     *
     * @param
     * @return
     */
    @ManagerGatewayApi(name = "获取项目公共配置", realPath = "/common/getCommonConfig")
    @PostMapping(value = {"/common/getCommonConfig"})
    @ResponseBody
    public ResultData getCommonConfig() {
        Map<String, Object> config = new HashedMap<>();
        config.put("serverPath", casProperties.getModule().getDomain());
        return ResultData.success(config);
    }

    /***
     * @Author jcx
     * @Description 获取或保存展示方案对应的本地化样式代码
     * @Date 19:48 2021/4/28
     * @Param wid: 展示方案wid
     * @return ResultData
     **/
    @ManagerGatewayApi(name = "获取或保存展示方案对应的本地化样式代码", realPath = "/common/getProgrammeLocalStyle")
    @PostMapping(value = {"/common/getProgrammeLocalStyle"})
    @ResponseBody
    public ResultData getProgrammeLocalStyle(@RequestBody ProgrammeLocalStyleReq programmeLocalStyleReq) {
        if(StringUtil.isEmpty(programmeLocalStyleReq.getWid())){
            return ResultData.error("传参错误，请检查");
        }
        String key = "programmeWid:" + programmeLocalStyleReq.getWid();
        if(programmeLocalStyleReq.getIsQuery()&&redisUtil.isHasKey(key)){
            return ResultData.success(JSON.parseObject(redisUtil.get(key).toString(),String.class));
        }else{
            ProLocalStyleEntity proLocalStyle = proLocalStyleService.getOne(new QueryWrapper<ProLocalStyleEntity>().lambda().eq(ProLocalStyleEntity::getProgrammeWid, programmeLocalStyleReq.getWid()));
            if(null==proLocalStyle){
                proLocalStyle = new ProLocalStyleEntity();
                proLocalStyle.setProgrammeWid(programmeLocalStyleReq.getWid());
                proLocalStyle.setProgrammeLocalStyle(programmeLocalStyleReq.getLocalStyle());
                proLocalStyleService.save(proLocalStyle);
            }else{
                if(!programmeLocalStyleReq.getIsQuery()){
                    proLocalStyle.setProgrammeLocalStyle(programmeLocalStyleReq.getLocalStyle());
                    proLocalStyleService.updateById(proLocalStyle);
                }
            }
            redisUtil.save(key,null==proLocalStyle.getProgrammeLocalStyle()?"":proLocalStyle.getProgrammeLocalStyle());
        }
        return ResultData.success(JSON.parseObject(redisUtil.get(key).toString(),String.class));
    }

    /***
     *
     * @modify since 3.3.1.Beta2 版本 区分pc端和移动端黑白模式
     *
     * @Author jcx
     * @Description 门户获取或切换黑白模式
     * @Date 11:09 2021/7/20
     * @Param blackAndWhiteModeReq:
     * @return ResultData
     **/
    @ManagerGatewayApi(name = "门户获取或切换黑白模式", realPath = "/programme/switchBlackAndWhiteMode")
    @PostMapping(value = {"/programme/switchBlackAndWhiteMode"})
    @ResponseBody
    public ResultData switchBlackAndWhiteMode(@RequestBody BlackAndWhiteModeReq blackAndWhiteModeReq){
        if(StringUtil.isEmpty(blackAndWhiteModeReq.getIsBlackMode())){
            return ResultData.error("传参错误，请检查");
        }
        SystemConfigEntity localBlackAndWhiteMode = systemConfigService.getSystemConfig("local_blackAndWhite_Mode");
        if(!"2".equals(blackAndWhiteModeReq.getIsBlackMode())){
            //切换黑白模式
            localBlackAndWhiteMode.setConfigValue(blackAndWhiteModeReq.getIsBlackMode());
            boolean flag = systemConfigService.saveOrUpdate(localBlackAndWhiteMode);
            if(flag){
                return ResultData.success(blackAndWhiteModeReq.getIsBlackMode());
            }
        }
        return ResultData.success(StringUtils.isEmpty(localBlackAndWhiteMode.getConfigValue()) ? localBlackAndWhiteMode.getDefaultValue() : localBlackAndWhiteMode.getConfigValue());
    }

    /**
     * 移动端黑白模式
     *
     * @param blackAndWhiteModeReq
     * @return
     */
    @ManagerGatewayApi(name = "门户获取或切换移动端黑白模式", realPath = "/programme/switchMobileBlackAndWhiteMode")
    @PostMapping(value = {"/programme/switchMobileBlackAndWhiteMode"})
    @ResponseBody
    public ResultData switchMobileBlackAndWhiteMode(@RequestBody BlackAndWhiteModeReq blackAndWhiteModeReq){
        if(StringUtil.isEmpty(blackAndWhiteModeReq.getIsBlackMode())){
            return ResultData.error("传参错误，请检查");
        }
        SystemConfigEntity localBlackAndWhiteMode = systemConfigService.getSystemConfig("local_mobile_blackAndWhite_Mode");
        if(!"2".equals(blackAndWhiteModeReq.getIsBlackMode())){
            //切换黑白模式
            localBlackAndWhiteMode.setConfigValue(blackAndWhiteModeReq.getIsBlackMode());
            boolean flag = systemConfigService.saveOrUpdate(localBlackAndWhiteMode);
            if(flag){
                return ResultData.success(blackAndWhiteModeReq.getIsBlackMode());
            }
        }
        return ResultData.success(StringUtils.isEmpty(localBlackAndWhiteMode.getConfigValue()) ? localBlackAndWhiteMode.getDefaultValue() : localBlackAndWhiteMode.getConfigValue());
    }

    /***
     * @Author jcx
     * @Description 获取门户配置
     * @Date 10:41 2021/7/29
     * @Param portalConfigReq:
     * @return ResultData
     **/
    @ManagerGatewayApi(name = "获取门户配置", realPath = "/base/getPortalConfig")
    @PostMapping(value = {"/base/getPortalConfig"})
    @ResponseBody
    public ResultData getPortalConfig(@RequestBody PortalConfigReq portalConfigReq){
        if(StringUtil.isNotEmpty(portalConfigReq.getKey())){
            return ResultData.success(systemConfigService.getSystemConfig(portalConfigReq.getKey()));
        }
        return ResultData.error("传参错误");
    }

    @RequestMapping("/base/getThemeData")
    @ResponseBody
    public ResultData getThemeData(HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        Global.PlatformType platformType = RequestUtil.getPlatform(request);
        //获取当前展示方案
        UserInfo currentUser = ( UserInfo ) this.getLoginUser().getData();
        String siteWid = HttpUtil.getValueFromSessionAndCookie(Global.SITE_WID, request);
        ShowProgrammeEntity defaultProgramme = showProgrammeService.getDefaultProgramme(platformType.getType(), siteWid);
        if(StringUtil.isNotEmpty(defaultProgramme.getTemplateConfig())){
            defaultProgramme.setTemplateConfig(cardUtil.filterHttpOrHttps(defaultProgramme.getTemplateConfig()));
        }
        result.put("showProgrammeEntity",defaultProgramme);
        result.put("isLogin",null!=currentUser?true:false);
        result.put("blackAndWhiteMode",switchBlackAndWhiteMode(new BlackAndWhiteModeReq("2")).getData());
        result.put("localLang",null!=currentUser?currentUser.getPreferredLanguage():Global.DEFAULT_LANGUAGE);
        return ResultData.success(result);
    }

    /***
     * @Author jcx
     * @Description 保存门户配置
     * @Date 11:20 2021/7/30
     * @Param portalConfigReq:
     * @return ResultData
     **/
    @ManagerGatewayApi(name = "保存门户配置", realPath = "/base/savePortalConfig")
    @PostMapping(value = {"/base/savePortalConfig"})
    @ResponseBody
    public ResultData savePortalConfig(@RequestBody PortalConfigReq portalConfigReq){
        if(StringUtil.isEmpty(portalConfigReq.getKey())||StringUtil.isEmpty(portalConfigReq.getValue())||StringUtil.isEmpty(portalConfigReq.getDefaultValue())){
            return ResultData.error("传参错误");
        }
        SystemConfigEntity systemConfig = systemConfigService.getSystemConfig(portalConfigReq.getKey());
        if(null==systemConfig){
            return ResultData.error("门户无此配置，请检查");
        }
        systemConfig.setConfigValue(portalConfigReq.getValue());
        systemConfig.setDefaultValue(String.valueOf(Global.EnableStatus.getById(Integer.valueOf(portalConfigReq.getDefaultValue())).getId()));
        systemConfigService.updateById(systemConfig);
        return ResultData.success();
    }

    /***
     * @Author hlchen02
     * @Description 获取门户配置(多个)
     * @Date 10:41 2021/10/24
     * @Param portalConfigReqList:
     * @return ResultData
     **/
    @ManagerGatewayApi(name = "获取门户配置(多个)", realPath = "/base/getPortalConfigMultiple")
    @PostMapping(value = {"/base/getPortalConfigMultiple"})
    @ResponseBody
    public ResultData getPortalConfigMultiple(@RequestBody PortalConfigMultipleReq portalConfigMultipleReq) {
        List<PortalConfigReq> portalConfigReqList = portalConfigMultipleReq.getPortalConfigList();
        if(CollectionUtils.isNotEmpty(portalConfigReqList)){
            List<String> configKeyList = Lists.newArrayList();
            for (PortalConfigReq portalConfigReq : portalConfigReqList) {
                if (StringUtil.isNotEmpty(portalConfigReq.getKey())) {
                    configKeyList.add(portalConfigReq.getKey());
                }
            }
            if (CollectionUtils.isNotEmpty(configKeyList)) {
                return ResultData.success(systemConfigService.getSystemConfigs(configKeyList));
            }
        }

        return ResultData.error("传参错误");
    }

    /***
     * @Author hlchen02
     * @Description 保存门户配置(多个)
     * @Date 11:20 2021/10/24
     * @Param portalConfigReq:
     * @return ResultData
     **/
    @ManagerGatewayApi(name = "保存门户配置(多个)", realPath = "/base/savePortalConfigMultiple")
    @PostMapping(value = {"/base/savePortalConfigMultiple"})
    @ResponseBody
    public ResultData savePortalConfigMultiple(@RequestBody PortalConfigMultipleReq portalConfigMultipleReq) {
        List<SystemConfigEntity> systemConfigEntityList = Lists.newArrayList();
        List<PortalConfigReq> portalConfigList = portalConfigMultipleReq.getPortalConfigList();

        if(CollectionUtils.isNotEmpty(portalConfigList)){
            for (PortalConfigReq portalConfigReq : portalConfigList) {
                if (StringUtil.isEmpty(portalConfigReq.getKey())) {
                    return ResultData.error("传参错误");
                }
                if(MOBILE_SERVICE_VISITS.equals(portalConfigReq.getKey())){
                    if(StringUtil.isEmpty(portalConfigReq.getDefaultValue())){
                        return ResultData.error("mobile_service_visits 该配置项的默认值不能为空");
                    }
                    if(!"0".equals(portalConfigReq.getDefaultValue()) && StringUtil.isEmpty(portalConfigReq.getValue())){
                        return ResultData.error("传参错误,mobile_service_visits 该配置项的值不能为空");
                    }

                }else{
                    if(StringUtil.isEmpty(portalConfigReq.getValue())){
                        return ResultData.error("传参错误，配置项的值不能为空");
                    }
                }
                SystemConfigEntity systemConfig = systemConfigService.getSystemConfig(portalConfigReq.getKey());
                if (null == systemConfig) {
                    return ResultData.error("门户无此配置{}，请检查", portalConfigReq.getKey());
                }
                systemConfig.setConfigValue(portalConfigReq.getValue());
                if(MOBILE_SERVICE_VISITS.equals(portalConfigReq.getKey())){
                    systemConfig.setDefaultValue(String.valueOf(Global.EnableStatus.getById(Integer.valueOf(portalConfigReq.getDefaultValue())).getId()));
                }
                if(StringUtil.isNotEmpty(portalConfigReq.getDefaultValue())){
                    systemConfig.setDefaultValue(portalConfigReq.getDefaultValue());
                }

                systemConfigEntityList.add(systemConfig);
            }

            if (CollectionUtils.isNotEmpty(systemConfigEntityList)) {
                systemConfigService.updateBatchById(systemConfigEntityList);
            }
        }else {
            return ResultData.error("传参错误");
        }

        return ResultData.success();
    }


    /**
     * @Author jcx
     * @Description 运营中心调用接口获取服务或者事项信息
     * @Date 11:26 2021/7/23
     * @Param sign:
     * @Param paramJson:
     * @return void
     **/
    @ManagerGatewayApi(name = "运营中心调用接口获取服务或者事项信息", realPath = "/restful/getItemOrAppInfoForCasp.do")
    @PostMapping(value = {"/restful/getItemOrAppInfoForCasp.do"})
    @ResponseBody
    public Map<String, Object> getItemOrAppInfoForCasp(@RequestHeader(value = "sign", required = false) String sign, @RequestBody String paramJson){
        return homeService.getItemOrAppInfoForCasp(sign,paramJson);
    }

    /***
     * @Author jcx
     * @Description 获取共享日历数据接口
     * @Date 19:44 2021/8/3
     * @Param shareId:
     * @return void
     **/
    @GetMapping("/cal/{shareId}")
    @ResponseBody
    public void getCalByShareId(@PathVariable("shareId") String shareId){
        DubboResponse res = userCalDubboService.getUserCalByShareId(shareId);
        if("0".equals(res.getErrcode())){
            String ical = String.valueOf(res.getData());
            byte[] datas = ical.getBytes(StandardCharsets.UTF_8);
            response.reset();
            response.setContentType("text/calendar; charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentLength(datas.length);
            response.setHeader("Content-Disposition", "attachment;filename=calendar.ics");

            try ( BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(datas))) {
                byte[] buff = new byte[1024];
                OutputStream os = response.getOutputStream();
                int i;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } catch (IOException e) {
                logger.error("======获取共享日历数据接口出现异常===>"+e);
            }
        }
    }

    /***
     * @Author jcx
     * @Description 获取客户端IP
     * @Date 13:32 2021/11/4
     * @return ResultData
     **/
    @GetMapping("/getClientIp")
    public ResultData getClientIp(){
        return ResultData.success(CommonUtil.getRemoteAddress(request));
    }

    @PostMapping("/collect/collectData")
    public ResultData collectData(){
        return ResultData.success(CommonUtil.getRemoteAddress(request));
    }

    /**
     * @return Map<String,Object>
     * @Author jcx
     * @Description 导出Excel公用接口
     * @Date 10:16 2020/8/18
     * @Param response:
     * @Param cardAjaxRequest:
     **/
    @PostMapping("/exportExcel")
    @ResponseBody
    public void exportExcel(HttpServletResponse response, @RequestBody FileVo vo) {
        if (StringUtil.isNotEmpty(vo.getCardId())) {
            //卡片内部调用
            vo = FastJsonUtil.parse(FastJsonUtil.convertObjectToJSON(executeCardMethod(vo)), FileVo.class);
        }
        if (StringUtil.isEmpty(vo.getSheetName()) || StringUtil.isEmpty(vo.getFileName()) ||
                ArrayUtil.isEmpty(vo.getSecondTitles()) || ArrayUtil.isEmpty(vo.getBeanProperty())) {
            throw new BusinessException(GlobalEnum.EXPORT_EXCEL_ERROR.getCode(), GlobalEnum.EXPORT_EXCEL_ERROR.getMsg());
        }
        //导出
        exportExcel(response, vo.getResult(), vo.getSheetName(), vo.getTitle(), vo.getSecondTitles(), vo.getBeanProperty(), vo.getFileName());
    }

    private Object executeTemplateMethod(HttpServletRequest httpServletRequest, TemplateAjaxRequest request) {
        Global.PlatformType platformType = RequestUtil.getPlatform(httpServletRequest);
        String templatePackageId = showProgrammeService.getDefaultProgramme(platformType.getType(),HttpUtil.getValueFromSessionAndCookie(Global.SITE_WID,httpServletRequest)).getTemplateId();
        String templateId = templateService.getById(templatePackageId).getTemplateId();
        ITemplate template = templateUtil.getTemplate(templateId);
        return template.execDispatcherData(request);
    }

    private Object executeCardMethod(CardAjaxRequest cardAjaxRequest) {
        logger.debug("cardAjaxRequest.getCardId()=======" + JSON.toJSONString(cardAjaxRequest));
        if(!"getConfig".equals(cardAjaxRequest.getMethod())) {
            ICard card = cardUtil.getCard(cardAjaxRequest.getCardId());
            return card.execDispatcher(cardAjaxRequest);
        }else{
            return cardUtil.getCardConfigByCardWid(cardAjaxRequest.getCardWid(), cardAjaxRequest.getCardId());
        }
    }

    /**
     * @return void
     * @Author jcx
     * @Description 数据导出Excel
     * @Date 16:12 2020/8/18
     * @Param response:
     * @Param list: 数据
     * @Param sheetName: sheet页名称
     * @Param title: 标题(可为空)
     * @Param secondTitles: 二级标题
     * @Param beanProperty: 二级标题字段名
     * @Param fileName: 导出的Excel名称
     **/
    public void exportExcel(HttpServletResponse response, List list, String sheetName, String title, String[] secondTitles,
                            String[] beanProperty, String fileName) {
        try {
            HSSFWorkbook workbook1 = ExcelUtil.makeExcelHead(sheetName, title, secondTitles.length - 1);
            HSSFWorkbook workbook2 = ExcelUtil.makeSecondHead(workbook1, secondTitles);
            HSSFWorkbook workbook = ExcelUtil.exportExcelData(workbook2, list, beanProperty);
            ExcelUtil.createHSSFWorkbook(workbook, response, fileName + ".xls");
        } catch (Exception e) {
            logger.error("数据导出Excel发生异常：", e);
            throw new BusinessException(GlobalEnum.EXPORT_EXCEL_ERROR.getCode(), GlobalEnum.EXPORT_EXCEL_ERROR.getMsg());
        }
    }

//    @Autowired
//    private CardService cardService;
//    @RequestMapping("/getCard")
//    @ResponseBody
//    public ResultData getCard(){
//        return ResultData.success(cardService.list(new QueryWrapper<CardEntity>().lambda().eq(CardEntity::getIsDeleted,0)));
//    }

}
