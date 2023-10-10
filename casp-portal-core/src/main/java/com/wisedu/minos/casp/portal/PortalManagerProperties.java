package com.wisedu.minos.casp.portal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wisedu.minos.api.data.UserService;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.SystemConfigEntity;
import com.wisedu.minos.casp.portal.model.CheckUserServiceReq;
import com.wisedu.minos.casp.portal.model.CheckUserServiceRes;
import com.wisedu.minos.casp.portal.model.PositionUrlReq;
import com.wisedu.minos.casp.portal.model.PositionUrlResponse;
import com.wisedu.minos.casp.portal.model.license.CooskLicenseDto;
import com.wisedu.minos.casp.portal.model.v353beta2.*;
import com.wisedu.minos.casp.portal.service.impl.CommonApiAdapterImpl;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.gateway.client.bean.LicenseInfo;
import com.wisedu.minos.gateway.client.license.MinosLicenseManager;
import com.wisedu.minos.util.MinosException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PortalManagerProperties
 * <p/>
 * 门户管理员配置
 *
 * @author hyluan
 * @date 2020/10/10 19:29
 * Copyright (c) 2018 wisedu
 */
@Configuration
@Data
@Slf4j
public class PortalManagerProperties {
    //个人中心
    public static final String PERSON_CENTER_URL = "personCenterUrl";
    //帐号管理
    public static final String ACCOUNT_MANAGE_URL = "accountManageUrl";
    //岗位管理
    public static final String POSTITION_MANAGER_URL = "postitionManagerUrl";
    //后台管理
    public static final String BACKEND_MANAGE_URL = "backendManageUrl";
    //事项管理
    public static final String SIM_MANAGE_URL = "simManageUrl";
    //效能看板
    public static final String SIM_PORTAL_URL = "simPortalUrl";


    //以上的集合，以后上面增加一个系统预置的，需放到以下List中，测试复制站点的情况
    public static final List<String> systemUrl= Arrays.asList(PERSON_CENTER_URL,ACCOUNT_MANAGE_URL,POSTITION_MANAGER_URL,BACKEND_MANAGE_URL,SIM_MANAGE_URL,SIM_PORTAL_URL);

    @Autowired
    private SystemConfigService systemConfigService;

    @DubboReference
    private UserService userService;

    @Autowired
    private UserUtil util;

    /**
     * 个人中心地址
     */
    String personCenterUrl;
    /**
     * 个人中心地址(给新的下拉菜单使用)
     */
    private static String personCenterUrlSelect;
    /**
     * 帐号管理地址
     */
    String accountManageUrl;

    /**
     * 融合管控台地址
     */
   String backendManageUrl;
    /**
     * 融合管控台地址(给新的下拉菜单使用)
     */
    private static String backendManageUrlSelect;
    /**
     * 岗位中心地址
     */
    String postitionManagerUrl;

    private static SystemMenuEnable systemMenuEnable;

    /**
     * <p/>
     *
     * @param
     * @return
     * @throws
     * @date 2020-11-1 12:07
     */
    public PortalManagerProperties loadData() {
        PortalManagerProperties portalManagerProperties = new PortalManagerProperties();
        //是否管控台管理员
        String userAccount = util.getUserAccount();
        Boolean flag = userService.checkManagerUser(userAccount);
        List<String> list = Lists.newArrayList();
        list.add(PERSON_CENTER_URL);
        list.add(ACCOUNT_MANAGE_URL);
        if(flag){
            list.add(BACKEND_MANAGE_URL);
        }
        List<SystemConfigEntity> systemConfigs = systemConfigService.getSystemConfigs(list);
        for (SystemConfigEntity systemConfig : systemConfigs) {
            switch (systemConfig.getConfigKey()) {
                case PERSON_CENTER_URL:
                    portalManagerProperties.personCenterUrl = getValue(systemConfig);
                    break;
                case ACCOUNT_MANAGE_URL:
                    portalManagerProperties.accountManageUrl = getValue(systemConfig);
                    break;
                case BACKEND_MANAGE_URL:
                    portalManagerProperties.backendManageUrl = getValue(systemConfig);
                    break;
                default:
                    break;
            }
        }
//        portalManagerProperties.setPostitionManagerUrl(getPositionUrl(userAccount));
        return portalManagerProperties;
    }

    public static void setPersonCenterUrlSelect () {
        PortalManagerProperties.personCenterUrlSelect = getPersonUrl();
    }

    public static void setBackendManageUrlSelect () {
        PortalManagerProperties.backendManageUrlSelect = getConsoleUrl();
    }

    public static String getBackendManageUrlSelect () {
        return backendManageUrlSelect;
    }

    public static String getPersonCenterUrlSelect () {
        return personCenterUrlSelect;
    }

    public static SystemMenuEnable getSystemMenuEnable () {
        return systemMenuEnable;
    }

    public static void setSystemMenuEnable () {
        PortalManagerProperties.systemMenuEnable = getSystemSelectMenuEnable();
    }

    /**
     * 获取岗位中心地址
     * @param userAccount
     * @return
     */
    public String getPositionUrl(String userAccount) {
        if ( StringUtil.isEmpty(userAccount) ) {
            return "";
        }
        PositionUrlResponse response = null;
        PositionUrlReq req = new PositionUrlReq();
        req.setUserAccount(userAccount);
        HttpEntity entity = new HttpEntity(req);
        try {
            response = RestTemplateUtils.post("casp-position/portal/position/getPortalAccessUrl", entity, PositionUrlResponse.class).getBody();
        } catch (Exception e) {
            LOGGER.debug("调用getPortalAccessUrl接口失败...,返回错误信息",e);
            return "";
        }
        if ( response == null || response.getErrcode().equals(ResultData.DEFAULT_ERROR_CODE) ) {
            return "";
        }
        return response.getData();
    }

    /***
     * @Author jcx
     * @Description 获取管控台地址
     * @Date 18:35 2022/10/8
     * @return String
     **/
    public static String getConsoleUrl () {
        GetModuleDomainRes response = null;
        try {
            response = RestTemplateUtils.get("/console/getModuleDomain", GetModuleDomainRes.class).getBody();
        } catch (Exception e) {
            LOGGER.error("调用/console/getModuleDomain接口失败...,返回错误信息",e);
            return "";
        }
        if ( response == null || response.getErrcode().equals(ResultData.DEFAULT_ERROR_CODE) ) {
            return "";
        }
        return response.getData();
    }
    /***
     * @Author jcx
     * @Description 获取个人中心地址
     * @Date 18:35 2022/10/8
     * @return String
     **/
    public static String getPersonUrl () {
        PersonalInfoUrlRes response = null;
        try {
            response = RestTemplateUtils.post("/minos-manager/core/personalInfo/getAccessUrl", PersonalInfoUrlRes.class).getBody();
        } catch (Exception e) {
            LOGGER.debug("调用/personalInfo/getAccessUrl接口失败...,返回错误信息",e);
            return "";
        }
        if ( response == null || response.getErrcode().equals(ResultData.DEFAULT_ERROR_CODE) ) {
            return "";
        }
        return response.getData();
    }

    //查询系统下拉菜单是否可用
    private static SystemMenuEnable getSystemSelectMenuEnable (){
        SystemMenuEnable systemMenuEnable = new SystemMenuEnable();
        //获取license信息
        LicenseInfo licenseInfo = MinosLicenseManager.getLicenseInfo();
        List<LicenseInfo.App> apps = licenseInfo.getApps();
        if( CollectionUtils.isEmpty(apps)){
            systemMenuEnable.setBackendManager(true);
            systemMenuEnable.setPersonCenter(true);
            systemMenuEnable.setPostitionManager(true);
            systemMenuEnable.setSimManager(true);
            systemMenuEnable.setSimPortal(true);
        }else{
            apps.forEach(app -> {
                if("ciap".equalsIgnoreCase(app.getId())){
                    systemMenuEnable.setPersonCenter(true);
                }else if("console".equalsIgnoreCase(app.getId())){
                    systemMenuEnable.setBackendManager(true);
                }
            });
        }
        CooskLicenseDto data = ApplicationContextUtil.get(CommonApiAdapterImpl.class).getCooskLicense().getData();
        if(data.isCaspSim()||data.isCooskSimSim()){
            systemMenuEnable.setSimManager(true);
        }
        if(data.isCooskHokFec()||data.isCooskHokhok()){
            systemMenuEnable.setSimPortal(true);
        }
        EnableServerInfosRes res=null;
        try {
            res = RestTemplateUtils.get("/server/gateway/getEnableServerInfos", EnableServerInfosRes.class).getBody();
        } catch (Exception e) {
            LOGGER.error("调用getEnableServerInfos接口失败...,返回错误信息:{}",e);
        }
        if(null!=res&&"0".equals(res.getErrcode())&&CollectionUtils.isNotEmpty(res.getData())){
            List<EnableServerInfo> enableServerInfos = res.getData();
            List<EnableServerInfo> position = enableServerInfos.stream().filter(k -> k.getAppName().equals(Global.CASP_POSITION)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(position)){
                systemMenuEnable.setPostitionManager(true);
            }
        }
        return systemMenuEnable;
    }


    private String getValue(SystemConfigEntity item) {
        return StringUtils.isEmpty(item.getConfigValue()) ? item.getDefaultValue() : item.getConfigValue();
    }
}
