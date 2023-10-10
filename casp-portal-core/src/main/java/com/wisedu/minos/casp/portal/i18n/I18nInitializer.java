package com.wisedu.minos.casp.portal.i18n;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.spi.itf.AbstractCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class I18nInitializer {

    private static final Logger logger = LogManager.getLogger(AbstractCard.class);

    private static final String MESSAGES_S_PROPERTIES = "messages_%s.properties";

    private static final String DEFAULT_MESSAGES_PROPERTIES = "messages.properties";

    /**
     * loadMessage
     * <p/>
     * 国际化初始化
     *
     * @param spiTypeId
     * @return java.util.Map<java.lang.String ,   java.lang.Object>
     * @throws
     * @date 2020/9/10 12:55
     */
    public static Map<String, Object> load(Map<String, Object> param, Global.SpiPluginType spiPluginType, String spiTypeId) {
        Map<String, Object> langMessagesMap = new HashMap<>();
        //国际化初始化加载
        try {
            I18nService i18nService = (I18nService) param.get("i18nService");
            List<Lang> supportLanguages = i18nService.getSupportLanguages();
            for (Lang supportLanguage : supportLanguages) {
                String resourceName = supportLanguage.isDefault ? DEFAULT_MESSAGES_PROPERTIES : String.format(MESSAGES_S_PROPERTIES, supportLanguage.getLangCode());
                langMessagesMap.put(supportLanguage.getLangCode(), getMessageProperties(spiPluginType, spiTypeId, resourceName));
            }
        } catch (IOException e) {
            logger.warn(spiPluginType.name() + "{}国际化初始化失败", spiTypeId, e);
            throw new RuntimeException(spiPluginType.name() + "国际化初始化失败", e);
        }
        return langMessagesMap;
    }

    private static Properties getMessageProperties(Global.SpiPluginType spiPluginType, String spiTypeId, String messagePropertiesName) throws IOException {
        Properties messagesProperties = new Properties();
        ClassPathResource classPathResource = new ClassPathResource("/static/" + spiTypeId + "/i18n/" + messagePropertiesName);
        if (!classPathResource.exists()) {
            if (messagePropertiesName.equals(DEFAULT_MESSAGES_PROPERTIES)) {
                logger.warn(spiPluginType.name() + "{}国际化初始化失败,没找到默认语言文件:" + DEFAULT_MESSAGES_PROPERTIES, spiTypeId);
                return messagesProperties;
            } else {
                logger.warn(spiPluginType.name() + "{}国际化初始化失败,没有设置对应语言文件,使用默认语言", spiTypeId);
                return getMessageProperties(spiPluginType, spiTypeId, DEFAULT_MESSAGES_PROPERTIES);
            }
        }
        messagesProperties.load(classPathResource.getInputStream());
        return messagesProperties;
    }


}
