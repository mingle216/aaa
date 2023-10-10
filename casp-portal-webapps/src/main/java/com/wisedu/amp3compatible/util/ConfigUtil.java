package com.wisedu.amp3compatible.util;

//import com.wisedu.amp.base.util.StringUtil;
//import com.wisedu.amp.base.util.license.LicenseManager;
//import com.wisedu.amp.core.base.internal.web.WebContext;
import com.wisedu.minos.casp.portal.common.utils.FileUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 类名称：配置工具类
 * <p>应用管理平台</p>
 * <p>江苏金智教育股份有限公司</p>
 * <p>类说明：提供配置获取等茶用操作</p>
 * 
 * @author vector
 * @version 1.0	创建时间：2015年6月8日下午4:46:36	vector	发布
 */
public class ConfigUtil {
	private ConfigUtil() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);
	private static final String PATH_CHAR = "/";
	private static final String FILE_SUFFIX = ".properties";
	
	private static Map<String, Properties> _cache = new HashMap<String, Properties>();
	
	public static Properties getProperties(String fileName) {
		String tempFileName=fileName;
		if (StringUtil.isEmpty(tempFileName))
		return null;
		
		tempFileName = wrapFileName(tempFileName);
		Properties properties = _cache.get(tempFileName);
		if (properties != null)
			return properties;
		try{
			properties = loadProperties(tempFileName);
		}catch (Exception e){
			LOGGER.error("载入属性异常：",e);
			return null;
		}
		_cache.put(tempFileName, properties);
		return properties;
	}
	
	/**
	 * 方法名：获得指定配置文件的属性
	 * <p>功能说明：</p>
	 * @param fileName	配置文件名（相对于resources的相对路径，以“/”开头）
	 * @param property	配置名称
	 * @return	不存在的配置返回<code>null</code>
	 */
	public static String getProperty(String fileName, String property) {
		Properties properties = getProperties(fileName);
		if (MapUtils.isEmpty(properties))
			return null;

		String result = properties.getProperty(property);
		if(null != result){
			result = result.trim();
		}
		return result;
	}
	
//	/**
//	 * 方法名：获得系统内部环境（学校）配置信息
//	 * <p>功能说明：</p>
//	 * @return
//	 */
//	public static Properties getInnerProperties() {
//		String innerConfFile = getEnvProperty(Constant.ENV_CONF_KEY);
//		return getProperties(innerConfFile);
//	}
//
	/**
	 * 方法名：获得"amp.environment.conf"指定的配置文件中的配置值
	 * <p>功能说明：</p>
	 * @param proKey
	 * @return
	 */
	public static String getInnerProperty(String proKey) {
		return getInnerProperty("amp.environment.conf", proKey);
	}
	
	/**
	 * 方法名：获得二级配置文件的指定配置
	 * <p>功能说明：</p>
	 * @param envKey
	 * @param proKey
	 * @return
	 */
	public static String getInnerProperty(String envKey, String proKey) {
		String confFile = getEnvProperty(envKey);
		if (StringUtil.isEmpty(confFile))
			return null;
		
		return getProperty(confFile, proKey);
	}
	
	/**
	 * 方法名：获得环境配置对象
	 * <p>功能说明：</p>
	 * @return
	 */
	public static Properties getEnvProperties() {
		return getProperties("/properties/environment.properties");
	}
	
	/**
	 * 方法名：获得指定的环境配置
	 * <p>功能说明：从environment.properties获得指定配置</p>
	 * @param propertyName
	 * @return
	 */
	public static String getEnvProperty(String propertyName) {
		return getEnvProperty(propertyName, true);
	}
	
	/**
	 * 方法名：获得指定的环境配置
	 * <p>功能说明：从environment.properties获得指定配置</p>
	 * @param propertyName
	 * @param getIfSysEnvExists	系统变量为主
	 * @return
	 */
	public static String getEnvProperty(String propertyName, boolean getIfSysEnvExists) {
		String value = null;
		if (getIfSysEnvExists){
			value = System.getProperty(propertyName);
			if (value == null) {
				value = System.getenv(propertyName);
			}
			if (value == null) {
				value = getEnvProperties().getProperty(propertyName);
			}
			return value;
		}
		return getEnvProperties().getProperty(propertyName);
	}
	
	/**
	 * 方法名：移除指定配置文件的配置缓存
	 * <p>功能说明：</p>
	 * @param fileName	配置文件的文件名（相对于resources的相对路径名称）
	 */
	public static void removePropertyCache(String fileName) {
		String tempFileName=fileName;
		if (StringUtil.isEmpty(tempFileName))
			return ;
		
		tempFileName = wrapFileName(tempFileName);
		_cache.remove(tempFileName);
	}
	
	public static Properties loadProperties(String location) {
		Properties properties = new Properties();
		Properties propertiesTrim = new Properties();
		Reader reader = null;
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
			if(inputStream == null){
				LOGGER.error("文件未找到");
				return new Properties();
			}
			reader = new InputStreamReader(inputStream, "UTF-8");
			properties.load(reader);
			Set<Object> keys = properties.keySet();
			String key = "";
			for(Object obj:keys){
				key = (String) obj;
				propertiesTrim.put(key,properties.getProperty(key).trim());
			}
			return propertiesTrim;
		} catch (FileNotFoundException e) {
			LOGGER.error("文件未找到异常：",e);
			return new Properties();
		} catch (IOException e) {
			LOGGER.error("流异常：",e);
			return new Properties();
		}finally {
			IOUtils.closeQuietly(reader);
		}
	}
	
	private static String wrapFileName(String fileName) {
		String tempFileName=fileName;
		if (StringUtil.isEmpty(tempFileName))
			return "";
		
		tempFileName = tempFileName.trim();
		while (tempFileName.startsWith(PATH_CHAR))
			tempFileName = tempFileName.substring(PATH_CHAR.length());
		if (!tempFileName.endsWith(FILE_SUFFIX))
			tempFileName += FILE_SUFFIX;
		return tempFileName;
	}
	
//	/**
//	 * @param configName
//	 * @param value
//	 */
//	public static void setRuntimeConfig(String configName, Object value) {
//		CacheUtil.put(configName, value);
//	}
//
//	public static <T> T getRuntimeConfig(String configName) {
//		return CacheUtil.get(configName);
//	}
//
//	public static String getAmpUrlPrefix() {
//		String schoolProtocol = com.wisedu.amp.core.base.util.ConfigUtil.getInnerProperty("amp.school.protocol");
//		String schoolDomain = com.wisedu.amp.core.base.util.ConfigUtil.getInnerProperty("amp.school.domain");
//		String bgUrlPrefix = schoolProtocol + "://" + schoolDomain;
//		if (bgUrlPrefix.endsWith("/")) {
//			bgUrlPrefix = bgUrlPrefix.substring(0, bgUrlPrefix.length() - 1);
//		}
//		bgUrlPrefix = bgUrlPrefix + WebContext.getContextPath();
//		return bgUrlPrefix;
//	}
//
//	/**
//	 * 获取pc端门户模板配置信息
//	 *
//	 * @return
//	 */
//	public static String getPortalModule(){
//		String module = Constant.DEFAULT_YWTB_MODULE;
//		if ("1".equals(LicenseManager.LICENSE.getYwtbFixed())) {
//			module = com.wisedu.amp.core.base.util.ConfigUtil.getInnerProperty(Constant.YWTB_PORTAL_MODULE_CONFIG);
//		}
//		return module;
//	}
//
//	/**
//	 * 获取移动端门户模板配置信息
//	 *
//	 * @return
//	 */
//	public static String getMobileModule() {
//		String module = com.wisedu.amp.core.base.util.ConfigUtil.getInnerProperty(Constant.YWTB_MOBILE_MODULE_CONFIG);
//		if (StringUtils.isEmpty(module)) {
//			module = Constant.DEFAULT_YWTB_MOBILE_MODULE;
//		}
//		return module;
//	}
}
