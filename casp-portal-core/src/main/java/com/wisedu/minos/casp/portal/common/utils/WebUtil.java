/**
 *
 */
package com.wisedu.minos.casp.portal.common.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 类名称：web相关工具类
 * <p>应用管理平台</p>
 * <p>江苏金智教育股份有限公司</p>
 * <p>类说明：</p>
 *
 * @author vector
 * @version 1.0    创建时间：2015年7月17日下午5:07:12	vector	发布
 */
public abstract class WebUtil {
    private WebUtil() {
    }

    public static final int HTTP_SOCKET_TIMEOUT = 5000;
    public static final int HTTP_CONNECT_TIMEOUT = 3000;
    public static final int HTTP_CONNECT_REQUEST_TIMEOUT = 5000;

    public static final String PROTOCOL_HTTP = "http";
    public static final String PROTOCOL_HTTPS = "https";

    private static final Logger LOGGER = LoggerFactory.getLogger(WebUtil.class);

    /**
     * 方法名：获得请求的查询字符串
     * <p>功能说明：</p>
     *
     * @param request
     * @return 存在查询条件返回以"?"开始的拼接url字符串，不存在返回空字符串
     */
    public static String getRequestQueryString(HttpServletRequest request) {
        if (null == request)
            return "";

        String queryString = request.getQueryString();
        return StringUtil.isEmpty(queryString) ? "" : "?" + queryString;
    }

    /**
     * 方法名：获得客户端主机IP
     * <p>功能说明：</p>
     *
     * @param request
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        if (null == request)
            return "";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    public static String getLastPath(HttpServletRequest request) {
        String simpleUri = request.getRequestURI().substring(request.getContextPath().length());
        if (StringUtil.isEmpty(simpleUri) || "/".equals(simpleUri))
            return "/";

        int lastPathPos = simpleUri.lastIndexOf("/");
        if (lastPathPos <= 0)
            return simpleUri;
        return simpleUri.substring(lastPathPos, simpleUri.length());
    }

    public static String getRequestRootPath(HttpServletRequest request) {
        if (request == null)
            return "";

        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        requestURL.delete(requestURL.length() - request.getRequestURI().length(), requestURL.length())
                .append(request.getContextPath());
        return requestURL.append("/").toString();
    }

    /**
     * 方法名：检查refer来源
     * <p>功能说明：</p>
     *
     * @param request
     * @param allowNullRefer 允许空refer
     * @return
     */
    public static boolean checkRequestRefer(HttpServletRequest request, boolean allowNullRefer) {
        String refer = request.getHeader("referer");
        if (StringUtil.isEmpty(refer))
            return allowNullRefer;

        return refer.contains(request.getServerName());
    }

    public static void redirect(HttpServletRequest request, HttpServletResponse response, String targerUrl) {
        try {
            response.setStatus(302);
            if (StringUtil.isEmpty(targerUrl))
//                response.sendRedirect("/");
                response.setHeader("location", request.getContextPath() + "/");

//            response.sendRedirect(response.encodeRedirectURL(targerUrl));
            response.setHeader("location", response.encodeRedirectURL(targerUrl));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("rawtypes")
	public static String appendUrlParameters(String url, Map<String, ? extends Object> parameters) {
        String tempUrl=url;
        if (StringUtil.isEmpty(tempUrl)) {
            return "";
        }
        if (MapUtils.isEmpty(parameters)) {
            return tempUrl == null ? "" : tempUrl;
        }

        int hashIdx = tempUrl.indexOf("#");
        String hashValue = "";
        if (hashIdx != -1) {
            hashValue = tempUrl.substring(hashIdx);
            tempUrl = tempUrl.substring(0, hashIdx);
        }

        boolean hasParameter = tempUrl.contains("?");
        StringBuilder urlBuilder = new StringBuilder(tempUrl);
        for (Map.Entry keyEntry : parameters.entrySet()) {
            if (StringUtil.isEmpty(keyEntry.getKey().toString())) {
                continue;
            }
            Object value = parameters.get(keyEntry.getKey());
            String linkChar = hasParameter ? "&" : "?";
            urlBuilder.append(linkChar)
                    .append(keyEntry.getKey())
                    .append("=")
                    .append(StringUtil.getString(value));
            hasParameter = true;
        }
        if (hashIdx != -1) {
            urlBuilder.append(hashValue);
        }
        return urlBuilder.toString();
    }

    private final static int[] IP_ADDRESS_WIGHT_TABLE = new int[]{
            256 * 256 * 256, 256 * 256, 256, 1
    };

    /**
     * 方法名：
     * <p>功能说明：</p>
     *
     * @param ipAddress
     * @return
     */
    public static long getIpValue(String ipAddress) {
        if (StringUtil.isEmpty(ipAddress))
            return -1;

        String[] addressArray = ipAddress.split("[.]");
        if (ArrayUtil.isEmpty(addressArray) || addressArray.length != 4)
            return -1;

        long value = 0;
        for (int idx = 0; idx < 4; ++idx) {
            long thisAddressValue = Integer.parseInt(addressArray[idx]);
            if (thisAddressValue < 0)
                return -1;
            value += thisAddressValue * IP_ADDRESS_WIGHT_TABLE[idx];
        }
        return value;
    }

    public static String getRequestParamMapJsonString(HttpServletRequest request) {
        if (null == request) {
            return "";
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        try {
            return new ObjectMapper().writeValueAsString(parameterMap);
        } catch (JsonProcessingException e) {
            LOGGER.error("get request param failed. ", e);
            return parameterMap.toString();
        }
    }



    public static String parseDomainName(String url) {
        if (StringUtil.isEmpty(url)) {
            return null;
        }
        int startPos = url.indexOf("//");
        if (startPos < 0) {
            return null;
        }
        StringBuilder domainBuilder = new StringBuilder();
        for (int i = startPos + 2; i < url.length(); ++i) {
            char charAt = url.charAt(i);
            if (charAt != '/') {
                domainBuilder.append(charAt);
            } else {
                break;
            }
        }
        return domainBuilder.toString();
    }


    /**
     * 设置cookie
     *
     * @param response
     * @param name     cookie名字
     * @param value    cookie值
     * @param maxAge   cookie生命周期  以秒为单位
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(true);
        if (maxAge > 0)
            cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 通过设置Set-Cookie指令删除某一个Cookie
     *
     *
     * @param name    Cookie的名称
     */
    public static void deleteCookieByNameBySetHeader(HttpServletResponse response, String name) {
//		response.setHeader("Set-Cookie",name+"=null;max-age=0");expires=Thu, 01-Jan-1970 00:00:01 GMT
        response.setHeader("Set-Cookie", name + "=null;expires=Thu, 01-Jan-1970 00:00:01 GMT");
    }

    /**
     * 根据名字获取cookie
     *
     * @param request
     * @param name    cookie名字
     * @return
     */
    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }


    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */
    private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }


}
