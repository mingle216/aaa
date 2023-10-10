package com.wisedu.minos.casp.portal.common.utils;

import com.wisedu.minos.casp.portal.utils.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * PropertiesUtil
 * <p/>
 * 资源文件加载工具类
 *
 * @author hyluan
 * @date 2020-10-3 10:59
 * Copyright (c) 2018 wisedu
 */
public class PropertiesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    public static Properties loadString(String str) {
        Properties p = new Properties();
        if (StringUtil.isEmpty(str)) {
            return p;
        }
        try {
            p.load(new StringReader(str));
        } catch (IOException e) {
            LOGGER.error("资源加载失败", e);
        }
        return p;
    }

    /**
     * toMap
     * <p/>
     *  properties转map
     * @param properties
     * @throws
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @date 2020-10-3 11:09
     */
    public static Map<String, Object> toMap(Properties properties) {
        Map<String, Object> m = new HashMap<>();
        Enumeration enum1 = properties.propertyNames();
        while (enum1.hasMoreElements()) {
            String strKey = (String) enum1.nextElement();
            String strValue = properties.getProperty(strKey, "");
            m.put(strKey, strValue);
        }
        return m;
    }
}
