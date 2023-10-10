package com.wisedu.minos.casp.portal.common.constant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ConfigCache
 * @Author: 01116267
 * @Date: 2021/12/15
 */
public class ConfigCache {
    private static final Logger logger = LogManager.getLogger(ConfigCache.class);

    private static Map<String, String> cacheMap = new ConcurrentHashMap<>(100);

    public static void loadCache(String key){
        Arrays.asList(Global.PlatformType.values()).forEach(item ->
            cacheMap.put(getCacheKey(key, item.getType()), getConfigFromFile(key, item.getType()))
        );
        logger.info("==============加载配置至本地缓存:{}===============", key);
    }

    public static String getConfig(String key, int platformType){
        String cacheData = cacheMap.get(getCacheKey(key, platformType));
        return null == cacheData ? "" : cacheData;
    }

    private static String getConfigFromFile(String id, int platformType){
        String resultConfig = "";
        //判断是否移动端，读取的配置文件位置不同
        String configPath = platformType == Global.PlatformType.MOBILE.getType() ?
                "config/" + id + "/configMetaDataMobile.json"
                :
                "config/" + id + "/configMetaData.json";
        try ( InputStreamReader reader = new InputStreamReader(ConfigCache.class.getClassLoader().getResourceAsStream(configPath))) {
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            resultConfig = sb.toString();
        } catch ( Exception e ) {
          logger.error(e);
        }
        return resultConfig;
    }

    private static String getCacheKey(String key, int platformType){
        return key + "_" + platformType;
    }
}
