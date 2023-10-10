package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.*;
import com.wisedu.minos.casp.portal.dao.entity.VersionManagementEntity;
import com.wisedu.minos.casp.portal.dao.mapper.VersionManagementMapper;
import com.wisedu.minos.casp.portal.i18n.DubboI18nService;
import com.wisedu.minos.casp.portal.i18n.Lang;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.license.CooskLicenseDto;
import com.wisedu.minos.casp.portal.model.license.CooskLicenseRes;
import com.wisedu.minos.casp.portal.service.CommonApiAdapter;
import com.wisedu.minos.casp.portal.vo.RestartServiceVo;
import com.wisedu.minos.gateway.client.bean.LicenseInfo;
import com.wisedu.minos.gateway.client.config.MinosConfRemoteConf;
import com.wisedu.minos.gateway.client.license.MinosLicenseManager;
import com.wisedu.minos.gateway.client.util.DeploySignUtil;
import com.wisedu.minos.gateway.client.util.GatewayClientUtil;
import com.wisedu.minos.gateway.client.util.HttpClientUtil;
import com.wisedu.minos.gateway.client.util.WecIpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CommonApiAdapterImpl
 * @Author: d
 * @Date: 2020/9/17
 */
@Service
public class CommonApiAdapterImpl implements CommonApiAdapter {

    private static final Logger logger = LogManager.getLogger(CommonApiAdapterImpl.class);
    @Autowired
    DubboI18nService dubboI18nService;
    @Autowired
    VersionManagementMapper versionManagementMapper;

    @Value("${system.school.serialnumber}")
    private String serialnumber;

    @Override
    public InlineResponse2001 getConfigPage () {
        return null;
    }

    @Override
    public InlineResponse2002 queryLangList() {
        List<Lang> result = dubboI18nService.getSupportLanguages();
        List<LangVo> langList=new ArrayList<>();
        for(Lang lang : result){
            LangVo langVo = ( LangVo ) ObjectUtil.copyProperties(lang, new LangVo());
            langList.add(langVo);
        }
        InlineResponse2002 inlineResponse2002=new InlineResponse2002();
        inlineResponse2002.setData(langList);
        return inlineResponse2002;
    }

    @Override
    public InlineResponse2003 queryVersions (QueryVersionsReq body) {
        Assert.notNull(body, GlobalEnum.PARAM_FAIL.getCode(), GlobalEnum.PARAM_FAIL.getMsg());
        QueryWrapper<VersionManagementEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(DbFieldConstant.FOREIGN_KEY,body.getForeignKey()).eq(DbFieldConstant.VERSION_TYPE,Integer.valueOf(body.getVersionType()))
                .eq(DbFieldConstant.IS_DELETED,Global.DeleteStatus.NO.getId())
                .orderByDesc(DbFieldConstant.UPDATE_TIME);
        List<VersionManagementEntity> list = versionManagementMapper.selectList(wrapper);
        if( CollectionUtils.isNotEmpty(list) ){
            List<VersionInfo> versionInfos = ObjectUtil.copyListProperties(list, VersionInfo::new, (versionManagementEntity, versionInfo) ->{
                // 这里可以定义特定的转换规则，用来转换时间
                versionInfo.setUpdateTime(DateUtil.getStrFromDate(versionManagementEntity.getUpdateTime(),DateUtil.DATE_FORMATE_STRING_A));
            });
            //去掉当前版本号的其他版本数据
            versionInfos= versionInfos.stream().filter(o -> !o.getVersionNumber().equals(body.getVersionNumber())).collect(Collectors.toList());
            return new InlineResponse2003().data(versionInfos);
        }
        return new InlineResponse2003();
    }

    //TODO 待修改20201023 19:30
    @Override
    public InlineResponse200 restartService () {

        try {
            //从配置文件获取实施作业平台地址
            String deployAddress = GatewayClientUtil.getSchoolDeployHost();
            if( StringUtil.isNotEmpty(deployAddress)){
                RestartServiceVo paramsVo = new RestartServiceVo();
                Map<String, String> param = new HashMap<>();
                paramsVo.setHostIp(WecIpUtil.getLocalIpAddress(deployAddress));
                paramsVo.setServiceId("casp-portal");
                String envId = MinosConfRemoteConf.getEnvironmentId();
                paramsVo.seteWid(envId);
                param.put("serviceId","casp-portal");
                param.put("envId",envId);
                param.put("hostIp",WecIpUtil.getLocalIpAddress(deployAddress));
                StringBuilder builder = new StringBuilder();
                String timeStamp= HttpClientUtil.getTimeStamp();
                HttpHeaders headers = new HttpHeaders();
                builder.append(timeStamp).append(serialnumber).append(DeploySignUtil.SIGN_KEY);
                headers.add("w-deploy-timestamp", timeStamp);
                headers.add("w-deploy-signature", DeploySignUtil.getSHA256Str(builder.toString()));
                HttpEntity<Map<String,String>> entity = new HttpEntity<>(param, headers);
                ResponseEntity<ResultData> result = RestTemplateUtils.post(deployAddress+"/api/restartService", entity, ResultData.class);
                if(null!=result&&"0".equals(result.getBody().getErrcode())){
                    //成功调用
                    String wid = String.valueOf(result.getBody().getData());
                    ResponseEntity<ResultData> resultDataRes= RestTemplateUtils.get(deployAddress+"/api/" + wid + "/getRestartResult", ResultData.class);
                    if(null!=resultDataRes&&"0".equals(resultDataRes.getBody().getErrcode())){
                        Map<String,Object> data = (Map<String,Object>)resultDataRes.getBody().getData();
                        int serviceDeployStatus = Integer.valueOf(String.valueOf(data.get("serviceDeployStatus")));
                        if(serviceDeployStatus==1){
                            return new InlineResponse200();
                        }
                    }
                }
                return new InlineResponse200();
            }
        } catch ( Exception e ) {
            logger.info("重启服务发生了异常:{}",e);
        }
        InlineResponse200 inlineResponse200 = new InlineResponse200();
        inlineResponse200.setErrcode("999");
        inlineResponse200.setErrmsg("重启失败");
        return inlineResponse200;
    }

    public CooskLicenseRes getCooskLicense () {
        CooskLicenseRes cooskLicenseRes = new CooskLicenseRes();
        CooskLicenseDto cooskLicenseDto = new CooskLicenseDto();
        //获取license信息
        LicenseInfo licenseInfo = MinosLicenseManager.getLicenseInfo();
        List<LicenseInfo.App> apps = licenseInfo.getApps();
        if(org.apache.commons.collections.CollectionUtils.isEmpty(apps)){
            //开发环境
            cooskLicenseDto.setCaspSim(true);
            getCooskLicenseDto(cooskLicenseDto,true);
        }else{
            AtomicBoolean isAllExpired = new AtomicBoolean(true);
            apps.forEach(app->{
                List<LicenseInfo.Module> modules = app.getModules();
                //过期时间
                String expired = app.getExpired();
                if("coosk".equalsIgnoreCase(app.getId())){
                    modules.forEach(module -> {
                        List<LicenseInfo.Function> functions = module.getFunctions();
                        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(functions)){
                            functions.forEach(fun->{
                                if(Global.COOSK_SIM_SIM.equalsIgnoreCase(fun.getName())){
                                    cooskLicenseDto.setCooskSimSim(true);
                                }else if(Global.COOSK_SIM_EVALUATE.equalsIgnoreCase(fun.getName())){
                                    cooskLicenseDto.setCooskSimEvaluate(true);
                                }else if(Global.COOSK_SIM_SORT.equalsIgnoreCase(fun.getName())){
                                    cooskLicenseDto.setCooskSimSort(true);
                                }else if(Global.COOSK_SIM_RESPONSIBILITIES.equalsIgnoreCase(fun.getName())){
                                    cooskLicenseDto.setCooskSimResponsibilities(true);
                                }else if(Global.COOSK_SIM_DEPTSET.equalsIgnoreCase(fun.getName())){
                                    cooskLicenseDto.setCooskSimDeptset(true);
                                }else if(Global.COOSK_SIM_OBJECT.equalsIgnoreCase(fun.getName())){
                                    cooskLicenseDto.setCooskSimObject(true);
                                }else if(Global.COOSK_SIM_CONFIG.equalsIgnoreCase(fun.getName())){
                                    cooskLicenseDto.setCooskSimConfig(true);
                                }else if ( Global.COOSK_SIF_FLOW.equalsIgnoreCase(fun.getName()) ) {
                                    cooskLicenseDto.setCooskSifFlow(true);
                                } else if ( Global.COOSK_SIL_QUERY.equalsIgnoreCase(fun.getName()) ) {
                                    cooskLicenseDto.setCooskSilQuery(true);
                                }else if ( Global.COOSK_HOK_HOK.equalsIgnoreCase(fun.getName()) ) {
                                    cooskLicenseDto.setCooskHokhok(true);
                                } else if ( Global.COOSK_HOK_FEC.equalsIgnoreCase(fun.getName()) ) {
                                    cooskLicenseDto.setCooskHokFec(true);
                                }else if ( Global.COOSK_SIM_ONETHING.equalsIgnoreCase(fun.getName()) ) {
                                    cooskLicenseDto.setCooskSimOneThing(true);
                                }
                            });
                        }
                        if(! org.springframework.util.StringUtils.isEmpty(expired) ){
                            Date expire = DateUtil.getDateFromStr(expired,DateUtil.DATE_FORMAT_YYYY_MM_DD);
                            if ( expire.after(new Date()) ) {
                                isAllExpired.set(false);
                            }
                        }else{
                            //为空表示未过期
                            isAllExpired.set(false);
                        }
                    });
                }else if("casp".equalsIgnoreCase(app.getId())){
                    modules.forEach(module -> {
                        if(Global.CASP_SIM.equalsIgnoreCase(module.getName())){
                            cooskLicenseDto.setCaspSim(true);
                        }
                    });
                }
            });
            if(isAllExpired.get()){
                getCooskLicenseDto(cooskLicenseDto,false);
            }
        }
        cooskLicenseRes.setData(cooskLicenseDto);
        return cooskLicenseRes;
    }

    private CooskLicenseDto getCooskLicenseDto(CooskLicenseDto cooskLicenseDto, boolean flag){
        cooskLicenseDto.setCooskSimSim(flag);
        cooskLicenseDto.setCooskSimEvaluate(flag);
        cooskLicenseDto.setCooskSimSort(flag);
        cooskLicenseDto.setCooskSimResponsibilities(flag);
        cooskLicenseDto.setCooskSimDeptset(flag);
        cooskLicenseDto.setCooskSimConfig(flag);
        cooskLicenseDto.setCooskSimObject(flag);
        cooskLicenseDto.setCooskSilQuery(flag);
        cooskLicenseDto.setCooskSifFlow(flag);
        cooskLicenseDto.setCooskHokFec(flag);
        cooskLicenseDto.setCooskHokhok(flag);
        cooskLicenseDto.setCooskSimOneThing(flag);
        return cooskLicenseDto;
    }


}
