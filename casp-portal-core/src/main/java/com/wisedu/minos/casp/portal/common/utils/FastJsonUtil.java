package com.wisedu.minos.casp.portal.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;

/**
 * 功能描述：基于FastJson的Json操作工具
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title FastJsonUtil
 * @Author: jcx
 * @Date: 2020/7/30
 */
public class FastJsonUtil {
    private static final SerializeConfig config;

    static {
        config = new SerializeConfig();
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
    }

    private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };

    /**
     * @Author jcx
     * @Description Object转Json(有特殊处理)
     * @Date 17:05 2020/7/30
     * @Param object:
     * @return String
     **/
    public static String convertObjectToJSON(Object object) {
        return JSON.toJSONString(object, config, features);
    }

    /**
     * @Author jcx
     * @Description Object转Json(无特殊处理)
     * @Date 17:05 2020/7/30
     * @Param object:
     * @return String
     **/
    public static String toJSONNoFeatures(Object object) {
        return JSON.toJSONString(object, config);
    }

    /**
     * 将json字符串转为Object实例
     *
     * @param json
     * @return
     */
    public static Object parse(String json) {
        return JSON.parse(json);
    }
    /**
     * 将json字符串转为指定类型的实例
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T parse(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    /**
     * @Author jcx
     * @Description 转换为数组
     * @Date 17:10 2020/7/30
     * @Param text:
     * @return Object
     **/
    public static <T> Object[] toArray(String text) {
        return toArray(text, null);
    }

    /**
     * @Author jcx
     * @Description 转换为数组
     * @Date 17:10 2020/7/30
     * @Param text:
     * @Param clazz:
     * @return Object
     **/
    public static <T> Object[] toArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz).toArray();
    }

    /**
     * @Author jcx
     * @Description 将json转为指定类型的List
     * @Date 17:11 2020/7/30
     * @Param text:
     * @Param clazz:
     * @return List<T>
     **/
    public static <T> List<T> toList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    /**
     * 将json转为Map
     * @param json
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> toMap(String json) {
        return (Map<String, T>) JSONObject.parseObject(json);
    }
}
