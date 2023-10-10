package com.wisedu.minos.casp.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wisedu.minos.api.data.AppServiceService;
import com.wisedu.minos.api.model.DubboModelInfoResponse;
import com.wisedu.minos.api.model.DubboServiceInfoApiResp;
import com.wisedu.minos.api.model.DubboUserInfo;
import com.wisedu.minos.api.model.DubboUserServiceGrantRequest;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.result.AmpBaseRes;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.common.utils.rsa.RSAUtil;
import com.wisedu.minos.casp.portal.dao.entity.InternationalizationEntity;
import com.wisedu.minos.casp.portal.dao.entity.MenuEntity;
import com.wisedu.minos.casp.portal.dao.entity.PageInfoEntity;
import com.wisedu.minos.casp.portal.dao.entity.SidebarEntity;
import com.wisedu.minos.casp.portal.dao.mapper.MenuMapper;
import com.wisedu.minos.casp.portal.dao.mapper.PageInfoMapper;
import com.wisedu.minos.casp.portal.dao.mapper.SidebarMapper;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.service.MinosApiAdapter;
import com.wisedu.minos.casp.portal.spi.itf.ITemplate;
import com.wisedu.minos.casp.portal.utils.ResultUtil;
import com.wisedu.minos.casp.portal.utils.TemplateUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.util.*;
import java.util.stream.Collectors;

import static com.wisedu.minos.casp.portal.common.result.ResultData.DEFAULT_SUCCESS_CODE;

@Service
public class HomeService {

    private static final Logger logger = LogManager.getLogger(HomeService.class);
    public static final String SUCCESS_CODE = "0000";
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private PageInfoMapper pageInfoMapper;
    @Autowired
    private SidebarMapper sidebarMapper;

    @Autowired
    UserUtil userUtil;
    @Autowired
    InternationalizationService internationalizationService;
    @Autowired
    ShowProgrammeService showProgrammeService;
    @Autowired
    MinosApiAdapter minosApiAdapter;
    @Autowired
    TemplateUtil templateUtil;
    @Autowired
    ServiceApiService serviceApiService;
    @Autowired
    ServiceItemApiService serviceItemApiService;

    @DubboReference
    AppServiceService appServiceService;
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private HttpSession session;

    public static final String ERROR_CODE = "errorCode";
    public static final String ERROR_MSG = "errorMsg";

    public Object executeAllTemplateMethod(TemplateAjaxRequest request) {
        String templateId = Global.SYS_TEMPLATE_OFFICIAL;
        if (null != request.getParam() && null != request.getParam().get(Global.TEMPLATE_ID)) {
            templateId = request.getParam().get(Global.TEMPLATE_ID);
        }

        ITemplate template = templateUtil.getTemplate(templateId);
        return template.execDispatcherData(request);
    }

    public PageInfoEntity getPageView(String pageCode) {
        return pageInfoMapper.selectOne(Wrappers.<PageInfoEntity>lambdaQuery().eq(PageInfoEntity::getPageCode, pageCode));
    }

    public List<MenuEntity> getMenuList() {
        List<MenuEntity> menuList = menuMapper.selectList(Wrappers.<MenuEntity>lambdaQuery().eq(MenuEntity::getIsEnabled, 1).orderByAsc(MenuEntity::getOrderNumber));
        return menuList;
    }

    public List<PageInfoEntity> getAccessAddresses(String rootPageCode, String parentPageCode, String childPageCode) {
        List<String> pageCodeList = new ArrayList<>();
        if (StringUtils.isNotEmpty(rootPageCode)) {
            pageCodeList.add(rootPageCode);
        }
        if (StringUtils.isNotEmpty(parentPageCode)) {
            pageCodeList.add(parentPageCode);
        }
        if (StringUtils.isNotEmpty(childPageCode)) {
            pageCodeList.add(childPageCode);
        }
        List<PageInfoEntity> pageInfos = pageInfoMapper.selectList(Wrappers.<PageInfoEntity>lambdaQuery().in(PageInfoEntity::getPageCode, pageCodeList));

        List<PageInfoEntity> accessAddresses = new ArrayList<>();

        for (String pageCode : pageCodeList) {
            for (PageInfoEntity pageInfo : pageInfos) {
                if (pageCode.equals(pageInfo.getPageCode())) {
                    accessAddresses.add(pageInfo);
                    break;
                }
            }
        }

        return accessAddresses;
    }

    /**
     * @return ResultData
     * @Author jcx
     * @Description 检查用户是否有该服务使用权限
     * @Date 19:06 2020/12/23
     * @Param userAccount:
     * @Param serviceId:
     **/
    public ResultData isUseService(String userAccount, String serviceId) {
        if (StringUtil.isEmpty(serviceId)) {
            throw new BusinessException("serviceId不能为空");
        }
        CheckUserServiceRes response = null;
        CheckUserServiceReq req = new CheckUserServiceReq();
        req.setUserId(userAccount);
        req.setServiceWid(serviceId);
        HttpEntity entity = new HttpEntity(req);
        try {
            response = RestTemplateUtils.post("/service/checkUserServiceGrant", entity, CheckUserServiceRes.class).getBody();
            logger.info("调用checkUserServiceGrant返回结果..." + JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("调用checkUserServiceGrant接口失败...,返回错误信息" + e);
            throw new BusinessException("调用checkUserServiceGrant接口失败");
        }
        if (response.getErrcode().equals(ResultData.DEFAULT_ERROR_CODE)) {
            throw new BusinessException("调用checkUserServiceGrant接口失败");
        }
        return response;
    }

    /**
     * @return ResultData
     * @Author hlchen02
     * @Description 检查用户是否有该服务使用权限(批量)
     * @Date 19:06 2020/12/23
     * @Param userAccount:
     * @Param serviceId:
     **/
    public ResultData isUseService(String userAccount, List<String> serviceWids) {
        if (StringUtil.isEmpty(userAccount) || org.springframework.util.CollectionUtils.isEmpty(serviceWids)) {
            throw new BusinessException("传参错误");
        }
        BatchCheckUserServiceRes response = null;
        BatchCheckUserServiceReq req = new BatchCheckUserServiceReq();
        req.setUserId(userAccount);
        req.setServiceWids(serviceWids);
        HttpEntity entity = new HttpEntity(req);
        try {
            response = RestTemplateUtils.post("/service/batchCheckUserServiceGrant", entity, BatchCheckUserServiceRes.class).getBody();
            logger.debug("调用batchCheckUserServiceGrant返回结果..." + JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("调用batchCheckUserServiceGrant接口失败...,返回错误信息" + e);
            throw new BusinessException("调用batchCheckUserServiceGrant接口失败");
        }
        if (response.getErrcode().equals(ResultData.DEFAULT_ERROR_CODE)) {
            throw new BusinessException("调用batchCheckUserServiceGrant接口失败");
        }
        return response;
    }

    /**
     * @return ResultData
     * @Author jcx
     * @Description 根据服务ID查询该服务授权用户组信息
     * @Date 19:37 2020/12/23
     * @Param serviceId:
     **/
    public DubboModelInfoResponse queryGrantConfig(String serviceId) {
        DubboModelInfoResponse dubboModelInfoResponse;
        try {
            logger.debug("当前用户账号为----{}", userUtil.getUserAccount());
            DubboUserServiceGrantRequest request = new DubboUserServiceGrantRequest();
            request.setServiceWid(serviceId);
            request.setUserId(userUtil.getUserAccount());
            dubboModelInfoResponse = appServiceService.queryUserServiceGrantedGroup(request);
            logger.debug("调用queryUserServiceGrantedGroup返回结果..." + JSON.toJSONString(dubboModelInfoResponse));
        } catch (Exception e) {
            logger.error("调用queryUserServiceGrantedGroup接口失败...,返回错误信息" + e);
            throw new BusinessException("调用queryUserServiceGrantedGroup接口失败");
        }
        if (!dubboModelInfoResponse.isSuccess()) {
            throw new BusinessException("调用queryUserServiceGrantedGroup接口失败");
        }

        return dubboModelInfoResponse;
    }

    /***
     * @Author jcx
     * @Description 根据服务wid查询服务信息
     * @Date 10:36 2020/12/24
     * @return ResultData
     **/
    public DubboServiceInfoApiResp queryServiceByWid(String serviceWid) {
        DubboServiceInfoApiResp dubboServiceInfoApiResp = null;
        try {
            DubboUserServiceGrantRequest dubboUserServiceGrantRequest = new DubboUserServiceGrantRequest();
            UserInfo currentUser = userUtil.getCurrentUser();
            dubboUserServiceGrantRequest.setUserId(null != currentUser ? currentUser.getUserAccount() : "");
            dubboUserServiceGrantRequest.setServiceWid(serviceWid);
            dubboServiceInfoApiResp = appServiceService.queryByWidAndUserId(dubboUserServiceGrantRequest);
            if (!"0".equals(dubboServiceInfoApiResp.getErrcode())) {
                throw new BusinessException("调用根据服务wid查询服务信息接口失败");
            }
            logger.info("调用根据服务wid查询服务信息成功返回结果..." + JSON.toJSONString(dubboServiceInfoApiResp.getData()));

        } catch (Exception e) {
            logger.error("调用根据服务wid查询服务信息接口失败...,返回错误信息" + e);
            throw new BusinessException("调用根据服务wid查询服务信息接口失败");
        }
        return dubboServiceInfoApiResp;
    }

    /***
     * @Author jcx
     * @Description 查询国际化数据
     * @Date 11:09 2021/1/26
     * @return ResultData
     **/
    public ResultData queryI18nList(String lang){
        //获取用户当前选择的语言
        String localeStr="";
        if(StringUtil.isNotEmpty(lang)){
            localeStr=lang;
        }else{
            localeStr=Global.DEFAULT_LANGUAGE;
        }
        //获取所有国际化数据
        List<InternationalizationEntity> list = internationalizationService.list(Wrappers.<InternationalizationEntity>lambdaQuery()
                .eq(InternationalizationEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));
        if( CollectionUtils.isNotEmpty(list)){
            Map<String, List<InternationalizationEntity>> langMap = list.stream().collect(Collectors.groupingBy(InternationalizationEntity::getLangKey));
            Map<String, String> langData=new HashMap<>();
            for (Map.Entry<String, List<InternationalizationEntity>> entry : langMap.entrySet()) {
                List<InternationalizationEntity> langs = entry.getValue();
                Map<String,String> langCountryMap = langs.stream().collect(HashMap::new, (m, v)->m.put(v.getLangCountry(), v.getLangValue()), HashMap::putAll);
                langData.put(entry.getKey(),StringUtil.isNotEmpty(langCountryMap.get(localeStr))?langCountryMap.get(localeStr):langCountryMap.get(Global.DEFAULT_LANGUAGE));
            }
            return ResultData.success(langData);
        }
        return ResultData.success(Collections.emptyList());
    }

    /***
     * @Author jcx
     * @Description 切换用户首选语言
     * @Date 11:09 2021/2/22
     * @return ResultData
     **/
    public boolean switchUserLanguage(String userLanguage,String pageCode){
        UserInfo currentUser = userUtil.getCurrentUser();
        if(null!=currentUser){
            String getPageViewCacheKey="getPageView:"+currentUser.getUserAccount()+":"+pageCode;
            DubboUserInfo dubboUserInfo = new DubboUserInfo();
            dubboUserInfo.setUserAccount(currentUser.getUserAccount());
            dubboUserInfo.setPreferredLanguage(userLanguage);
            Boolean flag = minosApiAdapter.updateUserInfo(dubboUserInfo).isData();
            if(flag){
                redisUtil.del(getPageViewCacheKey);
                UserInfo minosUser = (UserInfo) session.getAttribute("minosUserInfo");
                minosUser.setPreferredLanguage(userLanguage);
                session.setAttribute("minosUserInfo",minosUser);
                return true;
            }
            return false;
        }
        throw new NoLoginException();
    }

    /***
     * @Author jcx
     * @Description 收藏服务
     * @Date 13:31 2021/1/4
     * @return ResultData
     **/
    public ResultData collectService(String id, String operate) {
        String msg = "0".equals(operate) ? "取消收藏" : "收藏";
        String collectUrl = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue("collect_app_interface");

        String userId;
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        try {
            userId = user.getWid();
        } catch (Exception e) {
            logger.error("获取用户信息失败：" + e);
            return ResultData.error("获取用户信息失败");
        }
        CollectServiceReq collect = new CollectServiceReq();
        collect.setServiceItemId(id);
        collect.setFlag(operate);
        collect.setUserId(userId);

        HttpEntity entity = new HttpEntity(collect);
        logger.debug(">>请求地址：" + collectUrl);
        logger.debug(">>请求参数：" + JSON.toJSONString(collect));
        AmpBaseRes response = RestTemplateUtils.post("/serviceFavorite/favoriteServiceItem", entity,
                AmpBaseRes.class).getBody();
        logger.debug(">>返回参数：" + JSON.toJSONString(response));
        if ("0".equals(response.getErrcode())) {
            return ResultData.success(msg + "成功");
        }
        return ResultData.error(msg + "失败");
    }

    /***
     * @Author jcx
     * @Description 收藏服务事项
     * @Date 13:31 2021/1/4
     * @return ResultData
     **/
    public ResultData collectServiceItem(String id, String operate) {
        String msg = "0".equals(operate) ? "取消收藏" : "收藏";
        UserInfo user = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        String userId = "";
        try {
            userId = user.getWid();
        } catch (Exception e) {
            logger.error("获取用户信息失败" + e);
            return ResultData.error("获取用户信息失败");
        }
        CollectServiceItemReq collect = new CollectServiceItemReq();
        collect.setItemId(id);
        collect.setFlag(operate);
        collect.setUserId(userId);

        HttpEntity entity = new HttpEntity(collect);

        AmpBaseRes response = null;
        try {
            response = RestTemplateUtils.post("/casp-sim/itemManager/favoriteItem", entity, AmpBaseRes.class).getBody();
            logger.debug("调用服务事项收藏/取消收藏接口返回结果..." + JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("调用服务事项收藏/取消收藏接口失败...,返回错误信息" + e);
            throw new BusinessException("调用服务事项收藏/取消收藏接口失败" + e);
        }
        if ("0".equals(response.getErrcode())) {
            return ResultData.success(msg + "成功");
        }
        return ResultData.error(msg + "失败");
    }

    /***
     * @Author jcx
     * @Description
     * @Date 9:36 2021/1/18
     * @Param id: 卡片id 模板id
     * @return String 返回的配置信息
     **/
    public String getConfigJson(String id, int platformType) {
        String resultConfig = "";
        //判断是否移动端，读取的配置文件位置不同
        String configPath = platformType == Global.PlatformType.MOBILE.getType() ? "config/" + id + "/configMetaDataMobile.json" : "config/" + id + "/configMetaData.json";
        try (InputStreamReader reader = new InputStreamReader(HomeService.class.getClassLoader().getResourceAsStream(configPath))) {
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            resultConfig = sb.toString();
            logger.info("获取到" + id + "的配置---{}", resultConfig);
        } catch (Exception e) {
            logger.error("获取" + id + "的配置发生异常---{}", e);
        }
        return resultConfig;
    }

    /***
     * @Author jcx
     * @Description 跳转服务逻辑判断
     * @Date 18:30 2020/12/23
     * @return ResultData
     **/
    public Map<String, Object> serviceShow(HttpServletRequest request, List<ServiceGrant> grantData,
                                           String serviceUrl, RSAPrivateKey indentityTokenKey) {
        String userAccount = userUtil.getUserAccount();
        Map<String, Object> result = new HashMap<>();
        //获取用户当前选择的语言
        Locale locale = LocaleContextHolder.getLocale();
        grantData.forEach(serviceGrant -> {
            try {
                //gid_：rsa加密(userId + ":" + groupId)
                //amp_sec_version_：默认传1
                //EMAP_LANG：当前开启的国际化语言
                //THEME：传空
                String indentityString = RSAUtil.encryptByPrivateKey(userAccount + ":" + serviceGrant.getModelWid(), indentityTokenKey);
                String gid = Base64.encodeBase64URLSafeString(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(indentityString));
                StringBuilder paramsResult = new StringBuilder();
                paramsResult.append("?gid_=").append(gid).append("&amp_sec_version_=1&EMAP_LANG=").append(locale.toString()).append("&THEME=");
                serviceGrant.setServiceUrl(serviceUrl + paramsResult.toString());
            } catch (Exception e) {
                logger.error("RSA加密失败。。。。。" + e);
                throw new BusinessException("RSA加密失败，请联系管理员");
            }
        });
        result.put("grantData", grantData);
        return result;
    }

    /***
     * @Author jcx
     * @Description 根据人获取消息中心数量
     * @Date 15:15 2021/4/10
     * @return int
     **/
    public String getMessageCount() {
        UserInfo currentUser = userUtil.getCurrentUser();
        List<MessageInfo> messageList = Lists.newArrayList();
        ResponseResult response = null;
        try {
            response = RestTemplateUtils.get("/message_pocket_web/mp/restful/getTagsMessages?current" +
                            "=1&size=100" + "&unread=0&userId=" + currentUser.getUserAccount(),
                    ResponseResult.class).getBody();
            logger.debug("调用服务消息末读消息列表返回结果..." + JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("调用服务消息末读消息列表返回失败...,返回错误信息" + e);
        }
        if (response != null && response.getCode().equals(SUCCESS_CODE)) {
            messageList = response.getData();
        } else if (response != null && !response.getCode().equals(SUCCESS_CODE)) {
            logger.error("查询未读消息列表失败:" + response.getMsg());
        }
        if (CollectionUtils.isNotEmpty(messageList)) {
            if (messageList.size() > 99) {
                return "99+";
            } else {
                return String.valueOf(messageList.size());
            }
        }
        return "0";
    }

    /***
     * @Author yyu
     * @Description 根据wid获取数量
     * @Date 15:15 2021/8/24
     * @return int
     **/
    public String getSidebarCount(String wid) {
        UserInfo currentUser = userUtil.getCurrentUser();
        if (currentUser == null) {
            return "0";
        }
        SidebarEntity sidebarEntity = sidebarMapper.selectById(wid);
        if (sidebarEntity != null && org.apache.commons.lang3.StringUtils.isNotBlank(sidebarEntity.getCountAddress())) {
            ResultData response = null;
            try {
                response = RestTemplateUtils.get(sidebarEntity.getCountAddress() + "userId=" + currentUser.getUserAccount(),
                        ResultData.class).getBody();
                logger.info("调用" + sidebarEntity.getCountAddress() + "返回结果..." + JSON.toJSONString(response));
            } catch (Exception e) {
                logger.error("调用服务消息末读消息列表返回失败...,返回错误信息" + e);
            }
            if (response != null && response.getData() != null && response.getErrcode().equals(DEFAULT_SUCCESS_CODE)) {
                return response.getData().toString();
            }
        }
        return "0";
    }

    public String getTodoTaskCount(String userId) {
        String path = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ResultUtil.RequestUrl.TODO_TASK_COUNT_URL.getIndex());
        String taskCenterUrl = ApplicationContextUtil.get(SystemConfigService.class).getSystemConfigValue(ResultUtil.TASKCENTER_URL);
        if (org.apache.commons.lang3.StringUtils.isBlank(userId)) {
            logger.error("待办任务数量查询出错：用户获取失败！");
            return "0";
        }
        TaskInfoRequest taskInfoRequest = new TaskInfoRequest();
        taskInfoRequest.setUserId(userId);
        List<String> runPlatformList = new ArrayList<>();
        runPlatformList.add(ResultUtil.PC_APP);
        taskInfoRequest.setRunPlatformList(runPlatformList);
        HttpEntity entity = new HttpEntity(taskInfoRequest);
        int count = ResultUtil.ZERO;
        try {
            TaskInfoResponse response = RestTemplateUtils.post(taskCenterUrl + path, entity, TaskInfoResponse.class).getBody();
            if (ResultUtil.ResponseStatus.SUCCESS.getIndex().equals(response.getErrcode())) {
                count = response.getData().getTodoCount();
            }
        } catch (Exception e) {
            logger.error("待办任务查询待办数量出错：", e);
        }
        if (count > 99) {
            return "99+";
        }
        return String.valueOf(count);
    }

    public String getUserMessageCount(String userId) {
        List<MessageInfo> messageList = Lists.newArrayList();
        ResponseResult response = null;
        try {
            response = RestTemplateUtils.get("/message_pocket_web/mp/restful/getTagsMessages?current" +
                            "=1&size=100" + "&unread=0&userId=" + userId,
                    ResponseResult.class).getBody();
            logger.info("调用服务消息末读消息列表返回结果..." + JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("调用服务消息末读消息列表返回失败...,返回错误信息" + e);
        }
        if (response != null && response.getCode().equals(SUCCESS_CODE)) {
            messageList = response.getData();
        } else if (response != null && !response.getCode().equals(SUCCESS_CODE)) {
            logger.error("查询未读消息列表失败:" + response.getMsg());
        }
        if (CollectionUtils.isNotEmpty(messageList)) {
            if (messageList.size() > 99) {
                return "99+";
            } else {
                return String.valueOf(messageList.size());
            }
        }
        return "0";
    }

    /***
     * @Author jcx
     * @Description 运营中心调用接口获取服务或者事项信息
     * @Date 11:24 2021/8/5
     * @Param sign:
     * @Param paramJson:
     * @return Map<String,Object>
     **/
    public Map<String, Object> getItemOrAppInfoForCasp(String sign,String paramJson){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String mySign = this.sign(paramJson, Global.AMP_SALT);
            if ( sign == null ) {
                result.put(ERROR_CODE, "1");
                result.put(ERROR_MSG, "签名不存在");
                return result;
            }
            if(sign.equals(mySign)){
                //验证通过
                Map map = JSONObject.parseObject(paramJson, Map.class);
                //参数校验
                Map<String, Object> mapVer = this.verifParam(map);
                if ( Global.FAILED.equals(MapUtils.getString(mapVer, Global.FLAG)) ) {
                    result.put(ERROR_CODE, "2");
                    result.put(ERROR_MSG, MapUtils.getString(mapVer, Global.MESSAGE, ""));
                    return result;
                }
                //服务事项
                Object ob = null;
                String modelType = MapUtils.getString(map, "modelType", "");
                String nextMarkId = "";
                if ( Global.AMP_SERVICE_ITEM.equals(modelType) ) {

                    ob = this.getItemForCasp(map);
                    List<QueryItemPageListForCasp> listItem = ( List<QueryItemPageListForCasp> ) ob;
                    if ( CollectionUtils.isNotEmpty(listItem) ) {
                        nextMarkId = listItem.get(listItem.size() - 1).getServiceItemId();
                    }
                } else if ( Global.AMP_APP_INFO.equals(modelType) ) {
                    //服务
                    ob = this.getAppInfoForCasp(map);
                    List<AppInfoForCasp> appInfoList = ( List<AppInfoForCasp> ) ob;
                    if ( CollectionUtils.isNotEmpty(appInfoList)) {
                        nextMarkId = appInfoList.get(appInfoList.size() - 1).getMarkId();
                    }
                }

                result.put(ERROR_CODE, "0");
                result.put(ERROR_MSG, "成功");
                Map<String, Object> dataMap = new HashMap<String, Object>();
                Map<String, Object> headMap = new HashMap<String, Object>();
                headMap.put("header", sign);
                headMap.put("nextMarkId", nextMarkId);
                headMap.put("timestamp", MapUtils.getString(map, Global.TIMESTAMP, ""));

                Map<String, Object> bodyMap = new HashMap<String, Object>();

                bodyMap.put("list", ob);
                dataMap.put("header", headMap);
                dataMap.put("body", bodyMap);

                result.put("data", dataMap);
                return result;
            } else {
                //验证失败
                result.put(ERROR_CODE, "3");
                result.put(ERROR_MSG, "签名错误");
                return result;
            }
        } catch ( Exception e ) {
            logger.error("调用接口失败getServiceItemForWecCasIoc", e);
            result.put(ERROR_CODE, "4");
            result.put(ERROR_MSG, "系统异常");
        }
        return result;
    }

    /***
     * @Author jcx
     * @Description 获取签名
     * @Date 11:33 2021/7/23
     * @Param content:
     * @Param salt:
     * @return String
     **/
    public  String sign(String content,String salt) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("md5");
        String newContent = content + salt;
        byte[] md5Bytes = digest.digest(newContent.getBytes("UTF-8"));
        return byte2hex(md5Bytes);
    }

    /***
     * @Author jcx
     * @Description byte 转 hexString
     * @Date 11:33 2021/7/23
     * @Param b:
     * @return String
     **/
    public String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString();
    }

    /***
     * @Author jcx
     * @Description 参数校验
     * @Date 11:40 2021/7/23
     * @Param map:
     * @return Map<String,Object>
     **/
    public Map<String, Object> verifParam(Map map) {
        Map<String,Object> mapRe = new HashMap<>();
        mapRe.put(Global.FLAG,Global.SUCCESS);
        String  msgId = MapUtils.getString(map, Global.MSG_ID,"");
        int pageSize = MapUtils.getIntValue(map, Global.PAGE_SIZE,0);
        String timestamp = MapUtils.getString(map, Global.TIMESTAMP,"");
        String modelType = MapUtils.getString(map, Global.MODEL_TYPE,"");
        if (StringUtil.isEmpty(msgId)||StringUtil.isEmpty(timestamp)||StringUtil.isEmpty(modelType)||0>=pageSize){
            mapRe.put(Global.FLAG,Global.FAILED);
            mapRe.put(Global.MESSAGE,"msgId或timestamp或modelType不能为空,pageSize须大于0");
            return mapRe;
        }
        if (!(Global.AMP_SERVICE_ITEM.equals(modelType)||(Global.AMP_APP_INFO.equals(modelType)))){
            mapRe.put(Global.FLAG,Global.FAILED);
            mapRe.put(Global.MESSAGE,"modelType须为ampServiceItem或ampAppInfo");
            return mapRe;
        }
        return mapRe;
    }

    /***
     * @Author jcx
     * @Description 运营中心调用接口获取服务事项信息
     * @Date 14:52 2021/7/23
     * @Param map:
     * @return List<QueryItemPageListForCasp>
     **/
    public List<QueryItemPageListForCasp> getItemForCasp(Map map){
        List<QueryItemPageListForCasp> servicesReList = new ArrayList<>();
        QueryItemPageListForCaspRes queryItemPageListForCasp =
                serviceItemApiService.getQueryItemPageListForCasp(String.valueOf(map.get("markId")), String.valueOf(map.get("pageSize")));
        if(CollectionUtils.isNotEmpty(queryItemPageListForCasp.getData())){
            return queryItemPageListForCasp.getData();
        }
        return servicesReList;
    }
    /***
     * @Author jcx
     * @Description 运营中心调用接口获取服务信息
     * @Date 14:52 2021/7/23
     * @Param map:
     * @return List<ServiceItemForCaspRes>
     **/
    public List<AppInfoForCasp> getAppInfoForCasp(Map map){
        return serviceApiService.allServiceListForCasp(new ServiceForCaspReq(String.valueOf(map.get("markId")), Long.valueOf(String.valueOf(map.get("pageSize")))));
    }

}
