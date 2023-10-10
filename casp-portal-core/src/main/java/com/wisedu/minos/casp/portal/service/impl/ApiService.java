package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.ObjectUtil;
import com.wisedu.minos.casp.portal.dao.entity.InternationalizationEntity;
import com.wisedu.minos.casp.portal.dao.entity.SystemConfigEntity;
import com.wisedu.minos.casp.portal.model.v354Beta2.CommonConfig;
import com.wisedu.minos.casp.portal.model.v354Beta2.SysConfigLang;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ApiService
 * @Author: 01120034
 * @Date: 2022/12/1
 */
@Service
public class ApiService {

    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private InternationalizationService langService;

    public CommonConfig getCommonConfig(){
        CommonConfig commonConfig = new CommonConfig();
        List<String> keys = Lists.newArrayList(Global.LOCAL_BLACKANDWHITE_MODE, Global.LOCAL_MOBILE_BLACKANDWHITE_MODE, Global.SITE_RULE,
                Global.IS_BROWSER_COMPATIBLE,Global.COMPATIBLE_BROWSER,Global.COMPATIBLE_BROWSER_INFO,Global.BROWSER_INFO_IS_CHANGED,
                Global.IS_CUSTOM_JUMP,Global.CUSTOM_JUMP_URL);
        List<SystemConfigEntity> systemConfigs = systemConfigService.getSystemConfigs(keys);
        if( CollectionUtils.isNotEmpty(systemConfigs) ){
            systemConfigs.forEach(config->{
                String value = StringUtils.isNotBlank(config.getConfigValue())?config.getConfigValue():config.getDefaultValue();
                if(Global.LOCAL_BLACKANDWHITE_MODE.equals(config.getConfigKey())){
                    commonConfig.setIsPcBlackAndWhite(value);
                }else if(Global.LOCAL_MOBILE_BLACKANDWHITE_MODE.equals(config.getConfigKey())){
                    commonConfig.setIsH5BlackAndWhite(value);
                }else if(Global.SITE_RULE.equals(config.getConfigKey())){
                    commonConfig.setSiteSwitchRule(value);
                }else if(Global.IS_BROWSER_COMPATIBLE.equals(config.getConfigKey())){
                    commonConfig.setIsBrowserCompatible(value);
                }else if(Global.COMPATIBLE_BROWSER.equals(config.getConfigKey())){
                    commonConfig.setCompatibleBrowser(value);
                }else if(Global.BROWSER_INFO_IS_CHANGED.equals(config.getConfigKey())){
                    commonConfig.setIsChanged(value);
                }else if(Global.COMPATIBLE_BROWSER_INFO.equals(config.getConfigKey())){
                    if( StringUtils.isNotBlank(config.getValueLangKey())){
                        List<InternationalizationEntity> list = langService.list(new QueryWrapper<InternationalizationEntity>().lambda().eq(InternationalizationEntity::getLangKey, config.getValueLangKey()));
                        if(CollectionUtils.isNotEmpty(list)){
                            commonConfig.setCompatibleInfo(ObjectUtil.copyListProperties(list, SysConfigLang::new));
                        }
                    }
                }else if(Global.IS_CUSTOM_JUMP.equals(config.getConfigKey())){
                    commonConfig.setIsCustomJump(value);
                }else if(Global.CUSTOM_JUMP_URL.equals(config.getConfigKey())){
                    commonConfig.setCustomJumpUrl(value);
                }
            });
        }
        return commonConfig;
    }
}
