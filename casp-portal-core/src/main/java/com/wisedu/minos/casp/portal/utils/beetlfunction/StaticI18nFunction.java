package com.wisedu.minos.casp.portal.utils.beetlfunction;

import com.wisedu.minos.casp.portal.i18n.I18nService;
import com.wisedu.minos.config.ApplicationContextUtil;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Context;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * 功能描述：静态资源 卡片，模板支持国际化函数
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title I18nFunction
 * @Author: jcx
 * @Date: 2020/9/8
 */
@Component
@Scope("prototype")
public class StaticI18nFunction extends PortalFunction {

    private static final Logger logger = LogManager.getLogger(StaticI18nFunction.class);

    @Setter
    private Map<String, Object> messagesMap;

    @Override
    public Object call(Object[] obj, Context context) {
        String message = null;
        String key = null;
        try {
            key = (String) obj[0];
            I18nService i18nService = ApplicationContextUtil.get(I18nService.class);
            Locale currentLanguage = i18nService.getCurrentLanguage();
            Properties properties = (Properties) messagesMap.get(currentLanguage.toString());
            message = properties.getProperty(key);
        } catch (Exception e) {
            logger.error("获取不到国际化资源：{}", key, e);
            message = key;
        }
        return message;
    }

    @Override
    public String functionName() {
        return "i18n";
    }

    @Override
    public <T> PortalFunction beforeRegister(T obj) {
        this.messagesMap = (Map<String, Object>) obj;
        return this;
    }
}