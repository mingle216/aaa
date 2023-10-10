package com.wisedu.minos.casp.portal.common.utils;

import com.wisedu.minos.casp.portal.common.constant.Global;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 功能描述：Request相关的工具类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title RequestUtil
 * @Author: jcx
 * @Date: 2020/7/29
 */
public class RequestUtil {
    private static final Logger LOGGER = LogManager.getLogger(RequestUtil.class);

    // 浏览器类型 TODO 前期不确定浏览器情况，可能有误判，要放到系统参数表便于调整
//    private static final String[] mobileAgents = {"iphone", "android", "phone", "mobile", "wap", "netfront", "java",
//            "opera mobi", "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry",
//            "dopod", "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
//            "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
//            "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem", "wellcom",
//            "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos", "pantech", "gionee",
//            "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320", "240x320", "176x220", "w3c ",
//            "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac", "blaz", "brew", "cell", "cldc",
//            "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs", "kddi", "keji", "leno", "lg-c",
//            "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi", "mot-", "moto", "mwbp", "nec-",
//            "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port", "prox", "qwap", "sage", "sams",
//            "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem", "smal", "smar", "sony", "sph-",
//            "symb", "t-mo", "teli", "tim-", "tsm-", "upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi",
//            "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-", "Googlebot-Mobile"};

    /**
     * 将所有的请求参数转换字符串
     * 例如:
     * uri?param1=111&param2=111,222
     *
     * @param request
     * @return
     */
    public static String requestParamsToString(ServletRequest request) {
        StringBuilder sbuf = new StringBuilder();
        Map<String, String[]> map = request.getParameterMap();
        if (map == null || map.size() == 0) {
            return "";
        }

        String arrayJoiner = ",";
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String joiner = "&";
            String key = entry.getKey();
            String[] value = entry.getValue();
            if (value != null && value.length == 1) {
                sbuf.append(joiner).append(key).append("=").append(value[0]);
            } else {
                sbuf.append(joiner).append(key).append("=").append(StringUtil.join(value, arrayJoiner));
            }
        }
        return sbuf.toString();
    }


    /**
     * 判断是否为ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(header);
    }

    /**
     * 获取客户端请求的IP
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取请求参数
     *
     * @param request
     * @return
     */
    public static Map<String, Object> convertDataMap(HttpServletRequest request) {
        Map<String, String[]> properties = request.getParameterMap();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Iterator<?> entries = properties.entrySet().iterator();
        Map.Entry<?, ?> entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry<?, ?>) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    /**
     * 获取请求参数
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> map = new HashMap();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }

    /**
     * 获取请求参数
     *
     * @param request
     * @return
     */
    public static Map<String, Object> paramMaps(HttpServletRequest request) {
        Map<String, String[]> properties = request.getParameterMap();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Iterator<?> entries = properties.entrySet().iterator();
        Map.Entry<?, ?> entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry<?, ?>) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
//            name = StringUtil.xssEncode(name);
//            value = StringUtil.xssEncode(value);
            returnMap.put(name, value);
        }
        return returnMap;
    }

    /**
     * 获取整型参数
     *
     * @param request
     * @param paramName
     * @return
     */
    public static Integer paramInteger(HttpServletRequest request, String paramName) {
        String parameter = request.getParameter(paramName);
        Integer value = null;
        try {
            value = Integer.parseInt(parameter);
        } catch (Exception e) {
            LOGGER.error("参数类型转换异常", e);
        }
        return value;
    }


    /**
     * getPlatform
     * <p/>
     * 判断当前平台类型
     *
     * @param request
     * @return void
     * @throws
     * @date 2020/9/22 19:55
     */
    public static Global.PlatformType getPlatform(HttpServletRequest request) {
        return CommonUtil.isMobileDevice(request)?Global.PlatformType.MOBILE:Global.PlatformType.PC;
    }

    public static boolean isMobileDevice(HttpServletRequest request) {
        return getPlatform(request).getType() == 1;
    }



}
