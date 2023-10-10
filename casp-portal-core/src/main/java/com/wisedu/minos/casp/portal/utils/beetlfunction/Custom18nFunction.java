package com.wisedu.minos.casp.portal.utils.beetlfunction;

import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.i18n.I18nService;
import com.wisedu.minos.casp.portal.i18n.Lang;
import com.wisedu.minos.casp.portal.model.LangResoure;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Context;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Custom18nFunction
 * <p/>
 * 定制类 国际化函数 用于展示方案等自定义配置，第一个参数需要为 List<LangResoure> 第二参数为未匹配到的默认值（可不填）
 *
 * @author hyluan
 * @date 2020-10-1 22:31
 * Copyright (c) 2018 wisedu
 */
@Component
public class Custom18nFunction extends PortalFunction {

    private static final Logger logger = LogManager.getLogger(Custom18nFunction.class);

    @Override
    public Object call(Object[] obj, Context context) {
        String message = null;
        List<LangResoure> langResoures = new ArrayList<>();
        try {
            //第一个参数为多语言列表
            List langs = (List) obj[0];
            if (langs != null) {
                for (Object lang : langs) {
                    if (lang instanceof LangResoure) {
                        langResoures.add((LangResoure) lang);
                    } else {
                        Lang lang1 = (Lang) lang;
                        langResoures.add(new LangResoure().setKey(lang1.getLangCode()).setValue(lang1.getLangName()));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取不到国际化资源 类型转化异常，第二参数不为 List<LangResoure|Lang>对象：{}", langResoures, e);
            return message;
        }
        try {
            //第二个参数为默认值
            String defaultMessage = "";
            if (obj.length > 1) {
                defaultMessage = StringUtil.defaultIfEmpty((String) obj[1], "");
            }
            Locale currentLanguage = ApplicationContextUtil.get(I18nService.class).getCurrentLanguage();
            message = langResoures.stream().filter(langResoure -> langResoure.getKey().equals(currentLanguage.toString()))
                    .map(LangResoure::getValue).findFirst()
                    .orElse(defaultMessage);
        } catch (Exception e) {
            logger.error("获取不到国际化资源：{}", langResoures, e);
        }
        return message;
    }

    @Override
    public String functionName() {
        return "cI18n";
    }

    @Override
    public <T> PortalFunction beforeRegister(T obj) {
        return this;
    }

}