package com.wisedu.amp.card.cus.personalinfo.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonNull;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;

public class SourceTypeUtil {
    //    private final static Logger LOGGER = LogManager.getLogger(SourceTypeUtil.class);

    private SourceTypeUtil () {
    }

    //0: 静态文字
    public static final int STATIC_TEXT = 0;
    //1: JSON接口方式
    public static final int JSON = 1;
    //2: MINOS数据源方式
    public static final int MINOS_DB = 2;
    //3: 邮箱
    public static final int MAIL = 3;
    //4: 定制插件
    public static final int CUSTOM_PLUGIN = 4;
    //5: XML
    public static final int XML = 5;

    public static final String SEPARATOR_LEFT = "【";
    public static final String SEPARATOR_RIGHT = "】";
    public static final String SEPARATOR = "~";
    public static final String SEPARATOR_DOT = ".";
    public static final String SEPARATOR_LINE = "/";
    public static final String NO_DATA = "-";
    public static final String SEPARATOR_ATTR = "@";
    public static final String SEPARATOR_JING = "#";
    public static final Integer GET_REQUEST = 0;
    public static final Integer POST_REQUEST = 1;
    public static final String SEPARATOR_EQUAL = "=";
    public static final String URL_PARAM_HOLDER = "?";
    public static final String URL_PARAM_AND = "&";
    public static final String USER_ACCOUNT = "【userAccount】";


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


//    private static boolean isInteger (String input) {
//        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
//        return mer.find();
//    }

    public static String getSeparatorWithContent (String content) {
        return SourceTypeUtil.SEPARATOR_LEFT + content + SourceTypeUtil.SEPARATOR_RIGHT;
    }

    public static String getSpanWithContent (String content) {
        return content;
    }

    public static String buildErrorInfo (String s) {
        if ( StringUtil.isNotBlank(s) ) { // 信息不为空
            String[] keys = StringUtil.substringsBetween(s, SourceTypeUtil.SEPARATOR_LEFT, SourceTypeUtil.SEPARATOR_RIGHT);
            if ( null != keys && keys.length > 0 ) {
                for ( String key : keys ) {
                    s = s.replaceFirst(
                            SourceTypeUtil.getSeparatorWithContent(key),
                            SourceTypeUtil.getSpanWithContent(SourceTypeUtil.NO_DATA));
                }
            }
        }
        return s;
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
        if ( imptKey.contains(SourceTypeUtil.SEPARATOR_DOT) ) {
            String[] chFirstKeys = StringUtil.split(imptKey, SourceTypeUtil.SEPARATOR_DOT);
            for ( int i = 0 ; i < chFirstKeys.length - 1 ; i++ ) {
                // getJSONArray
                if ( chFirstKeys[ i ].contains(SourceTypeUtil.SEPARATOR) ) {
                    String[] arrKeys = StringUtil.split(chFirstKeys[ i ], SourceTypeUtil.SEPARATOR);
                    JSONArray array = resultJson.getJSONArray(arrKeys[ 0 ]);
                    resultJson = array.getJSONObject(Integer.parseInt(arrKeys[ 1 ]));
                    continue;
                }
                resultJson = resultJson.getJSONObject(chFirstKeys[ i ]);
            }
            value = SourceTypeUtil.transValue(resultJson.get(chFirstKeys[ chFirstKeys.length - 1 ]) instanceof JsonNull ?
                    SourceTypeUtil.NO_DATA : resultJson.get(chFirstKeys[ chFirstKeys.length - 1 ]).toString());
        } else {
            value = SourceTypeUtil.transValue(resultJson.get(imptKey) instanceof JsonNull ?
                    SourceTypeUtil.NO_DATA : resultJson.get(imptKey).toString());
        }
        return value;
    }

    public static String buildInfoWithJson (String s, JSONObject resultJson) {
        if ( StringUtil.isNotBlank(s) ) {
            String[] keys = StringUtil.substringsBetween(s, SourceTypeUtil.SEPARATOR_LEFT, SourceTypeUtil.SEPARATOR_RIGHT);
            if ( null != keys && keys.length > 0 ) {
                for ( String key : keys ) {
                    String value = getInfoFromJsonObject(key, resultJson);
                    s = s.replaceFirst(
                            SourceTypeUtil.getSeparatorWithContent(key),
                            SourceTypeUtil.getSpanWithContent(value));
                }
            }
        }
        return s;
    }

}
