package com.wisedu.amp3compatible.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 周全 modify by Michael Liu
 * @ClassName FastJsonUtils
 * @Description 阿里json工具类
 * @date 2015年3月16日
 */
public class FastJsonUtils {
    private FastJsonUtils() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FastJsonUtils.class);

    /**
     * 使用FastJson判断字符串是否是Json
     *
     * @param json
     * @return
     */
    public static boolean isJson(String json) {
        try {
            JSON.parseObject(json);
            return true;
        } catch (Exception e) {
            LOGGER.error("json format error ", e);
            return false;
        }
    }

    /**
     * 使用FastJson 将 obj转换成普通的json
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return JSON.toJSONString(obj, false);
    }

    /**
     * 使用FastJson 将 obj转换成格式化的json
     *
     * @param obj
     * @return
     */
    public static String toFormatJson(Object obj) {
        return JSON.toJSONString(obj, true);
    }

    /**
     * 使用FastJson除去json中的某个属性
     *
     * @param json
     * @param propertyName 属性的名字
     * @return
     */
    public static String removePropertyOfJson(String json, String propertyName) {
        JSONObject obj = JSON.parseObject(json);
        Set<String> set = obj.keySet();
        set.remove(propertyName);
        return obj.toString();
    }

    /**
     * 使用FastJson向json中添加属性
     *
     * @param json
     * @param propertyName  属性的名字
     * @param propertyValue 属性的值
     * @return
     */
    public static String addPropertyOfJson(String json, String propertyName,
                                           Object propertyValue) {
        JSONObject obj = JSON.parseObject(json);
        obj.put(propertyName, propertyValue instanceof String ? propertyValue
                .toString() : toJson(propertyValue));
        return obj.toString();
    }

    /**
     * @param json
     * @param propertyName
     * @return String
     * @throws
     * @Title: getPropertyOfJson
     * @Description: 根据json中属性取对应的值
     */
    public static String getPropertyOfJson(String json, String propertyName) {
        JSONObject obj = JSON.parseObject(json);
        return obj.getString(propertyName);
    }

    /**
     * 使用FastJson修改json中某个属性的值
     *
     * @param json
     * @param propertyName  属性的名字
     * @param propertyValue 属性的值
     * @return
     */
    public static String changePropertyOfjson(String json, String propertyName,
                                              Object propertyValue) {
        JSONObject obj = JSON.parseObject(json);
        Set<String> set = obj.keySet();
        if (set.contains(propertyName))
            obj.put(propertyName,
                    propertyValue instanceof String ? propertyValue.toString()
                            : toJson(propertyValue));
        return obj.toString();
    }

    /**
     * 使用FastJson判断Json中是否有该属性
     *
     * @param json
     * @param propertyName 属性的名字
     * @return
     */
    public static boolean hasProperty(String json, String propertyName) {
        try {
            JSONObject obj = JSON.parseObject(json);
            Set<String> set = obj.keySet();
            return set.contains(propertyName);
        } catch (Exception e) {
            LOGGER.error("check property name failed. ", e);
            return false;
        }
    }

    /**
     * 使用FastJson将json装换成实体类
     *
     * @param <T>
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T json2Bean(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 使用FastJson将json串转换成List<String>，json 格式是[{......},{......}]
     *
     * @param json
     * @return
     */
    public static List<String> json2StringList(String json) {
        JSONArray array = JSON.parseArray(json);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < array.size(); i++)
            list.add(array.get(i).toString());
        return list;
    }

    /**
     * 使用FastJson将json转换成实体类的List
     *
     * @param <T>
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> json2BeanList(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * 将json转换成Map<String,Stirng>
     *
     * @param json
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> json2Map(String json) {
        Map<String, String> map = new HashMap<String, String>();
        JSONObject obj = JSON.parseObject(json);
        Set<Map.Entry<String, Object>> set = obj.entrySet();
        for (Map.Entry entry : set) {
            map.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return map;
    }

    /**
     * 使用FastJson将json串转换成json串的数组，json 格式是[{......},{......}]， 返回的是String的数组
     *
     * @param json
     * @return
     */
    public static String[] json2StringArray(String json) {
        JSONArray array = JSON.parseArray(json);
        String[] str = new String[array.size()];
        for (int i = 0; i < array.size(); i++)
            str[i] = array.get(i).toString();
        return str;
    }

    /**
     * 转换字段相同类型不同的对象
     *
     * @param toTransObj 要被转换的对象
     * @param clazz      转换后的对象类型
     * @return
     */
    public static <T> Object transformObject(Object toTransObj, Class<T> clazz) {
        String json = FastJsonUtils.toJson(toTransObj);
        return FastJsonUtils.json2Bean(json, clazz);
    }

    /**
     * 使用FastJson向JSONObject中添加属性
     *
     * @param obj
     * @param propertyName  属性的名字
     * @param propertyValue 属性的值
     * @return
     */
    public static JSONObject addPropertyOfJsonObject(JSONObject obj, String propertyName, Object propertyValue) {
        if (null == obj) {
            obj = new JSONObject();
        }
        obj.put(propertyName, propertyValue);
        return obj;
    }

    public static String beanListToJson(List beans) {
        StringBuilder rest = new StringBuilder();
        rest.append("[");
        int size = beans.size();
        for (int i = 0; i < size; i++) {
            rest.append(toJson(beans.get(i)) + ((i < size - 1) ? "," : ""));
        }
        rest.append("]");
        return rest.toString();
    }
}
