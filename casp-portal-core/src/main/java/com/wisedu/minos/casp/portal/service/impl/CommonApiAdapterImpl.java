package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.*;
import com.wisedu.minos.casp.portal.dao.entity.MenuEntity;
import com.wisedu.minos.casp.portal.dao.entity.VersionManagementEntity;
import com.wisedu.minos.casp.portal.dao.mapper.VersionManagementMapper;
import com.wisedu.minos.casp.portal.i18n.DubboI18nService;
import com.wisedu.minos.casp.portal.i18n.Lang;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.service.CommonApiAdapter;
import com.wisedu.minos.casp.portal.vo.RestartServiceVo;
import com.wisedu.minos.gateway.client.util.GatewayClientUtil;
import com.wisedu.minos.gateway.client.util.WecIpUtil;
import liquibase.command.CommandResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
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
        wrapper.eq(DbFieldConstant.FOREIGN_KEY,body.getForeignKey()).eq(DbFieldConstant.VERSION_TYPE,body.getVersionType())
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
                paramsVo.setHostIp(WecIpUtil.getLocalIpAddress(deployAddress));
                paramsVo.setServiceId("casp-portal");
                ResponseEntity<ResultData> result = RestTemplateUtils.post(deployAddress+"/api/restartService", paramsVo, ResultData.class);
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



}
