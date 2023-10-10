package com.wisedu.amp3compatible.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.JsonNull;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.PersonalDataEntity;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.spi.itf.IAuth;
import com.wisedu.minos.casp.portal.utils.AuthUtil;
import com.wisedu.minos.casp.portal.utils.DataSourceUtil;
import com.wisedu.minos.casp.portal.utils.MailUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.MinosException;
import com.wisedu.minos.util.jdbc.JDBCTools;
import com.wisedu.minos.util.jdbc.bean.JDBCConnectParamBean;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2022/8/11
 */

public class PersonalInfoUtil {
    private PersonalInfoUtil() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalInfoUtil.class);

    private static final String URL_PARAM_HOLDER = "?";
    private static final String URL_PARAM_AND = "&";
    private static final Integer POST_REQUEST = 1;
    private static final String SEPARATOR_EQUAL = "=";
    private static final String SEPARATOR_LEFT = "【";
    private static final String SEPARATOR_RIGHT = "】";
    private static final String SEPARATOR_DOT = ".";
    private static final String SEPARATOR = "~";
    private static final String NO_DATA = "-";
    public static final String USER_ACCOUNT = "【userAccount】";
    public static final String SEPARATOR_LINE = "/";
    public static final String SEPARATOR_ATTR = "@";

    /**
     * 获取json接口个人数据信息
     * @param dataInfoMap
     * @param user
     */
    public static void getJsonInformation (Map<String, PersonalDataEntity> dataInfoMap, UserInfo user) {
        List<PersonalDataEntity> values = new ArrayList<>(dataInfoMap.values());
        PersonalDataEntity information = values.get(0);
        try {
            // 接口地址
            org.springframework.util.Assert.hasLength(information.getSourceKey(), "接口地址为空");
            StringBuilder url = new StringBuilder(information.getSourceKey());
            JSONObject entityJson = new JSONObject();
            if ( StringUtil.isNotBlank(information.getUserKey()) ) {
                if ( url.toString().contains(URL_PARAM_HOLDER) ) {
                    url.append(URL_PARAM_AND);
                } else {
                    url.append(URL_PARAM_HOLDER);
                }
                url.append(information.getUserKey()).append(SEPARATOR_EQUAL).append(user.getUserAccount());
                entityJson.put(information.getUserKey(), user.getUserAccount());
            }

            String resultStr;
            if ( POST_REQUEST.equals(information.getHttpMethod()) ) {
                // post请求
                resultStr = RestTemplateUtils.post(url.toString(), entityJson, String.class).getBody();
            } else {
                // get请求
                resultStr = RestTemplateUtils.get(url.toString(), String.class).getBody();
            }

            Assert.hasLength(resultStr, "获取到的数据信息为空");
            JSONObject resultJson = JSONObject.parseObject(resultStr);
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(buildInfoWithJson(item.getSubInfo(), resultJson));
                // 次要信息拼装
                item.setMainInfo(buildInfoWithJson(item.getMainInfo(), resultJson));
            });
        } catch ( Exception e ) {
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(buildErrorInfo(item.getSubInfo()));
                // 次要信息拼装
                item.setMainInfo(buildErrorInfo(item.getMainInfo()));
            });
            throw new MinosException("调用JSON接口出错", e);
        }
    }

    public static void getDbInformation (Map<String, PersonalDataEntity> dataInfoMap, UserInfo user) {
        List<PersonalDataEntity> values = new ArrayList<>(dataInfoMap.values());
        PersonalDataEntity information = values.get(0);
        try {
            Assert.hasLength(information.getSourceKey(), "数据源为空");
            JDBCConnectParamBean jdbcConnectParamBean = ApplicationContextUtil.get(DataSourceUtil.class).getJDBCConnectParamBeanByName(information.getSourceKey());
            List<Map<String, String>> matchInformationList;
            //根据sql是否含有(#{userId})决定是否加上当前登录用户ID
            if ( information.getDataSql().contains(URL_PARAM_HOLDER) ) {
                String[][] params = {{"1", user.getUserAccount()}};
                matchInformationList = JDBCTools.query(jdbcConnectParamBean, information.getDataSql(), params);
            } else {
                matchInformationList = JDBCTools.query(jdbcConnectParamBean, information.getDataSql(),null);
            }
            Map<String, String> matchInformationMap = CollectionUtils.isNotEmpty(matchInformationList) ? matchInformationList.get(0) : Collections.emptyMap();

            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(buildInfo(item.getSubInfo(), matchInformationMap));
                // 次要信息拼装
                item.setMainInfo(buildInfo(item.getMainInfo(), matchInformationMap));
            });

        } catch ( Exception e ) {
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(buildErrorInfo(item.getSubInfo()));
                // 次要信息拼装
                item.setMainInfo(buildErrorInfo(item.getMainInfo()));
            });
            throw new MinosException("调用DB接口出错", e);
        }
    }


    public static void getMailInformation (Map<String, PersonalDataEntity> dataInfoMap, String extraInfo) {
        List<PersonalDataEntity> values = new ArrayList<>(dataInfoMap.values());
        try {
            MailUtil mailUtil = ApplicationContextUtil.get(MailUtil.class);
            // 获取未读邮件数量
            int result = mailUtil.getUnreadCount(extraInfo);
            // 拼装信息
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 未读邮件数量：【unreadCount】
                item.setSubInfo(buildInfo(item.getSubInfo(), extraInfo));
                // 主要信息拼装
                item.setMainInfo(buildInfo(item.getMainInfo(), String.valueOf(result)));
            });

        } catch ( Exception e ) {
            LOGGER.warn("获取未读邮件数量异常", e);
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(buildInfo(item.getSubInfo(), extraInfo));
                // 次要信息拼装
                item.setMainInfo(buildErrorInfo(item.getMainInfo()));
            });
        }
    }

    public static void getCustomInformation (Map<String, PersonalDataEntity> dataInfoMap, UserInfo user) {
        List<PersonalDataEntity> values = new ArrayList<>(dataInfoMap.values());
        PersonalDataEntity information = values.get(0);
        try {
            // 判断是什么插件
            IAuth authPlugin = ApplicationContextUtil.get(AuthUtil.class).getAuthPluginById(information.getSourceKey());
            Map<String, Object> paramsMap = new HashMap<>(16);
            String authConfig = information.getAuthConfig();
            if ( StringUtil.isNotEmpty(authConfig) ) {
                if ( authConfig.contains(USER_ACCOUNT) ) {
                    authConfig = authConfig.replace(USER_ACCOUNT, user.getUserAccount());
                }
                paramsMap = JSONObject.parseObject(authConfig, new TypeReference<Map<String, Object>>(){});
            }
            String authData = authPlugin.getAuthData(paramsMap);
            Assert.hasLength(authData, "获取到的数据信息为空");
            JSONObject resultJson = JSONObject.parseObject(authData);
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                item.setSubInfo(buildInfoWithJson(item.getSubInfo(), resultJson));
                // 次要信息拼装
                item.setMainInfo(buildInfoWithJson(item.getMainInfo(), resultJson));
            });
        } catch ( Exception e ) {
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(buildErrorInfo(item.getSubInfo()));
                // 次要信息拼装
                item.setMainInfo(buildErrorInfo(item.getMainInfo()));
            });
            throw new MinosException("自定义插件获取数据出错", e);
        }
    }

    public static void getXmlInformation (Map<String, PersonalDataEntity> dataInfoMap, UserInfo user) {
        List<PersonalDataEntity> values = new ArrayList<>(dataInfoMap.values());
        PersonalDataEntity information = values.get(0);
        try {
            // 接口地址
            Assert.hasLength(information.getSourceKey(), "接口地址为空");
            StringBuilder url = new StringBuilder(information.getSourceKey());
            JSONObject entityJson = new JSONObject();
            if ( StringUtil.isNotBlank(information.getUserKey()) ) {
                if ( url.toString().contains(URL_PARAM_HOLDER) ) {
                    url.append(URL_PARAM_AND);
                } else {
                    url.append(URL_PARAM_HOLDER);
                }
                url.append(information.getUserKey()).append(SEPARATOR_EQUAL).append(user.getUserAccount());
                entityJson.put(information.getUserKey(), user.getUserAccount());
            }

            String resultStr;
            if ( POST_REQUEST.equals(information.getHttpMethod()) ) {
                resultStr = RestTemplateUtils.post(url.toString(), entityJson, String.class).getBody();
            } else {// get请求
                resultStr = RestTemplateUtils.get(url.toString(), String.class).getBody();
            }
            Assert.hasLength(resultStr, "获取到的数据信息为空");
            Document document = DocumentHelper.parseText(resultStr);
            Element element = document.getRootElement();

            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(buildXmlInfo(item.getSubInfo(), element));
                // 次要信息拼装
                item.setMainInfo(buildXmlInfo(item.getMainInfo(), element));
            });
        } catch ( Exception e ) {
            LOGGER.warn("XML方式获取数据出错", e);
            values.forEach(item -> {
                // 拼接主要信息及次要信息
                // 卡号：【account】
                item.setSubInfo(buildErrorInfo(item.getSubInfo()));
                // 次要信息拼装
                item.setMainInfo(buildErrorInfo(item.getMainInfo()));
            });
        }
    }

    private static String buildXmlInfo (String s, Element element) {
        if ( StringUtil.isNotBlank(s) ) {
            String[] keys = StringUtil.substringsBetween(s, SEPARATOR_LEFT, SEPARATOR_RIGHT);
            if ( null != keys && keys.length > 0 ) {
                for ( String key : keys ) {
                    String value = getInfo(key, element);
                    s = s.replaceFirst(
                            getSeparatorWithContent(key),
                            getSpanWithContent(value));
                }
            }
        }
        return s;
    }

    private static String getInfo (String key, Element element) {
        String[] chKeys = StringUtil.split(key, SEPARATOR_LINE);
        for ( String chKey : chKeys ) {
            if ( chKey.contains(SEPARATOR_ATTR) ) {
                // 获取属性中的值
                String[] attrKeys = StringUtil.split(chKey, SEPARATOR_ATTR);
                String[] childKeys = StringUtil.split(attrKeys[ 0 ], SEPARATOR);

                List list = element.selectNodes(childKeys[ 0 ]);
                int index = childKeys.length > 1 && StringUtil.isNumeric(childKeys[ 1 ]) ? Integer.parseInt(childKeys[ 1 ]) : 0;
                if ( ! CollectionUtils.isEmpty(list) ) {
                    element = ( Element ) list.get(index);
                    return element.attribute(attrKeys[ 1 ]).getText();
                }
                return "-";
            }

            String[] childKeys = StringUtil.split(chKey, SEPARATOR);
            List list = element.selectNodes(childKeys[ 0 ]);
            int index = childKeys.length > 1 && StringUtil.isNumeric(childKeys[ 1 ]) ? Integer.parseInt(childKeys[ 1 ]) : 0;
            if ( ! CollectionUtils.isEmpty(list) ) {
                element = ( Element ) list.get(index);
            }
        }
        // 获取文本内容
        return element.getText();
    }

    private static String buildInfo (String s, Map<String, String> matchInformationMap) {
        if ( StringUtil.isNotBlank(s) ) { // 次要信息不为空
            String[] keys = StringUtil.substringsBetween(s, SEPARATOR_LEFT, SEPARATOR_RIGHT);
            if ( null != keys && keys.length > 0 ) {
                for ( String key : keys ) {
                    String value = StringUtil.isBlank(key) ?
                            NO_DATA : matchInformationMap.get(key.toLowerCase(Locale.US));

                    s = s.replaceAll(
                            getSeparatorWithContent(key),
                            getSpanWithContent(value));
                }
            }
        }
        return s;
    }

    private static String buildInfo (String s, String result) {
        // 信息不为空
        if ( StringUtil.isNotBlank(s) ) {
            String[] keys = StringUtil.substringsBetween(s, SEPARATOR_LEFT, SEPARATOR_RIGHT);
            if ( null != keys && keys.length > 0 ) {
                for ( String key : keys ) {
                    s = s.replaceFirst(
                            getSeparatorWithContent(key),
                            getSpanWithContent(result));
                }
            }
        }
        return s;
    }

    public static String buildErrorInfo (String s) {
        if ( StringUtil.isNotBlank(s) ) { // 信息不为空
            String[] keys = StringUtil.substringsBetween(s, SEPARATOR_LEFT, SEPARATOR_RIGHT);
            if ( null != keys && keys.length > 0 ) {
                for ( String key : keys ) {
                    s = s.replaceFirst(
                            getSeparatorWithContent(key),
                            getSpanWithContent(NO_DATA));
                }
            }
        }
        return s;
    }

    public static String buildInfoWithJson (String s, JSONObject resultJson) {
        if ( StringUtil.isNotBlank(s) ) {
            String[] keys = StringUtil.substringsBetween(s, SEPARATOR_LEFT, SEPARATOR_RIGHT);
            if ( null != keys && keys.length > 0 ) {
                for ( String key : keys ) {
                    String value = getInfoFromJsonObject(key, resultJson);
                    s = s.replaceFirst(
                            getSeparatorWithContent(key),
                            getSpanWithContent(value));
                }
            }
        }
        return s;
    }

    public static String getSpanWithContent (String content) {
        return content;
    }

    /**
     * 【data.forecast~0.high】
     *
     * @param imptKey
     * @param resultJson
     * @return
     */
    private static String getInfoFromJsonObject (String imptKey, JSONObject resultJson) {
        String value;
        if ( imptKey.contains(SEPARATOR_DOT) ) {
            String[] chFirstKeys = StringUtil.split(imptKey, SEPARATOR_DOT);
            for ( int i = 0 ; i < chFirstKeys.length - 1 ; i++ ) {
                // getJSONArray
                if ( chFirstKeys[ i ].contains(SEPARATOR) ) {
                    String[] arrKeys = StringUtil.split(chFirstKeys[ i ], SEPARATOR);
                    JSONArray array = resultJson.getJSONArray(arrKeys[ 0 ]);
                    resultJson = array.getJSONObject(Integer.parseInt(arrKeys[ 1 ]));
                    continue;
                }
                resultJson = resultJson.getJSONObject(chFirstKeys[ i ]);
            }
            value = transValue(resultJson.get(chFirstKeys[ chFirstKeys.length - 1 ]) instanceof JsonNull ?
                    NO_DATA : resultJson.get(chFirstKeys[ chFirstKeys.length - 1 ]).toString());
        } else {
            value = transValue(resultJson.get(imptKey) instanceof JsonNull ?
                    NO_DATA : resultJson.get(imptKey).toString());
        }
        return value;
    }

    public static String getSeparatorWithContent (String content) {
        return SEPARATOR_LEFT + content + SEPARATOR_RIGHT;
    }

    public static String transValue (String value) {
        String value0 = value;
        String value1;
        if ( value.contains(".") ) {
            value1 = value.split("\\.")[ 1 ];
            if ( value1.length() == 1 && "0".equals(value1) ) {
                value0 = value.split("\\.")[ 0 ];
            }
        }
        return value0;
    }
}
