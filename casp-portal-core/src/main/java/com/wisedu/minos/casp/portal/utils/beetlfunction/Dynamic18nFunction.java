package com.wisedu.minos.casp.portal.utils.beetlfunction;

import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.service.impl.InternationalizationService;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Context;
import org.springframework.stereotype.Component;

/**
 * Dynamic18nFunction
 * <p/>
 * 动态资源 国际化函数 来源于数据库配置
 *
 * @author hyluan
 * @date 2020-10-1 22:31
 * Copyright (c) 2018 wisedu
 */
@Component
public class Dynamic18nFunction extends PortalFunction {

    private static final Logger logger = LogManager.getLogger(Dynamic18nFunction.class);

    @Override
    public Object call(Object[] obj, Context context) {
        String message = "";
        String langKey = null;
        try {
            langKey = (String) obj[0];
            message = ApplicationContextUtil.get(InternationalizationService.class).getlangValue(langKey);
        } catch (Exception e) {
            logger.error("获取不到国际化资源：{}", langKey, e);
        }
        if (StringUtil.isEmpty(message)) {
            message = langKey;
        }
        return message;
    }


    @Override
    public String functionName() {
        return "dI18n";
    }

    @Override
    public <T> PortalFunction beforeRegister(T obj) {
        return this;
    }
}