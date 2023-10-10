package com.wisedu.minos.casp.portal.spi.itf;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.i18n.I18nInitializer;
import com.wisedu.minos.casp.portal.utils.ScriptDefenseFormat;
import com.wisedu.minos.casp.portal.utils.XSSDefenseFormat;
import com.wisedu.minos.casp.portal.utils.beetlfunction.PortalFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title AbstractAuth
 * @Author: 01319098
 * @Date: 2021/5/20
 */

public abstract class AbstractAuth implements IAuth {
    private static final Logger LOGGER = LogManager.getLogger(AbstractMail.class);

    @Override
    public void initialize (Map<String, Object> param) {
        LOGGER.info("initialize template..{}", getAuthId());
//        try {
//            //模板国际化初始化加载
//            Map<String, Object> langMessagesMap = I18nInitializer.load(param, Global.SpiPluginType.TEMPLATE, getAuthId());
//        } catch ( Exception e ) {
//            LOGGER.warn("认证插件{}初始化失败", getAuthId(), e);
//            throw new RuntimeException("认证插件初始化失败", e);
//        }
    }
}
