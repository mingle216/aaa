package com.wisedu.minos.casp.portal.i18n;

import com.wisedu.minos.api.config.LanguageConfigService;
import com.wisedu.minos.api.constant.MinosCommonConstant;
import com.wisedu.minos.api.model.DubboLanguageConfig;
import com.wisedu.minos.api.model.DubboLanguageConfigResp;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.dao.entity.SystemConfigEntity;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * DubboI18nService
 * <p/>
 * 从dubbo接口获取多语言配置
 *
 * @author hyluan
 * @date 2020/9/23 19:28
 * Copyright (c) 2018 wisedu
 */
@Service
public class DubboI18nService implements I18nService {

    private static final Logger logger = LogManager.getLogger(DubboI18nService.class);

    @Autowired
    LocaleResolver localeResolver;

    @Autowired
    SystemConfigService systemConfigService;

    private static DubboLanguageConfigResp resp;

    public static void setResp (DubboLanguageConfigResp resp) {
        DubboI18nService.resp = resp;
    }


    @Override
    public List<Lang> getSupportLanguages() {
        List<Lang> defaultLangs = new ArrayList<>();
//        DubboLanguageConfigResp resp = languageConfigService.getAllLanguageConfigs(MinosCommonConstant.FUNCTION_PORTAL,"");


        defaultLangs.add(new Lang("中文","中文", Global.DEFAULT_LANGUAGE, true));
        if(resp == null){
            return defaultLangs;
        }
        logger.debug("从gateway获取多语言配置：code:{},message:{}", resp.getCode(), resp.getMessage());
        List<DubboLanguageConfig> languageConfigs = resp.getLanguageConfigs();
        if(CollectionUtils.isNotEmpty(languageConfigs)){
            List<DubboLanguageConfig> collect = languageConfigs.stream().filter(e -> e.getLanguageKey().equals(Global.DEFAULT_LANGUAGE)).collect(Collectors.toList());
            String zhCName = collect.get(0).getLanguageName();
            Lang lang = defaultLangs.get(0);
            lang.setLangName(zhCName);
            lang.setLangCname(collect.get(0).getLanguageCname());
            defaultLangs.set(0,lang);
        }
        languageConfigs=languageConfigs.stream().filter(e->!e.getLanguageKey().equals(Global.DEFAULT_LANGUAGE)).collect(Collectors.toList());
        if( CollectionUtils.isEmpty(languageConfigs) ){
            return defaultLangs;
        }
        List<Lang> langs = languageConfigs.stream()
                .map(languageConfig ->
                        new Lang(languageConfig.getLanguageName().trim(),languageConfig.getLanguageCname().trim(),languageConfig.getLanguageKey().trim(),
                                resp.getDefaultLanguage().equals(languageConfig.getLanguageKey()))).collect(Collectors.toList());
        defaultLangs.addAll(langs);
        return defaultLangs;
    }

    @Override
    public Locale getCurrentLanguage() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return localeResolver.resolveLocale(request);
    }

    @Override
    public List<Lang> getSupportDisplayLanguages() {
//        List<Lang> supportLanguages = this.getSupportLanguages();
//        List<SystemConfigEntity> langs = systemConfigService.matchConfigKey("lang_");
        return this.getSupportLanguages();
    }

    private String getConfigLangValueByKey(List<SystemConfigEntity> langs, String key) {
        return langs.stream().filter(entity -> entity.getConfigKey().replaceAll("lang_", "").equals(key))
                .map(SystemConfigEntity::getConfigValue).findFirst().orElse("默认");
    }


}
