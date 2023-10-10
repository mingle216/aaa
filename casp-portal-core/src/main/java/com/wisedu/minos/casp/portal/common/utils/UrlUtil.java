package com.wisedu.minos.casp.portal.common.utils;

import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述：URL处理工具类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title UrlUtil
 * @Author: jcx
 * @Date: 2020/7/29
 */
public class UrlUtil {

    private static final Logger logger = LogManager.getLogger(UrlUtil.class);
    private static final List<String> staticSource = Arrays.asList(".js", ".ico", ".css", ".ico", ".jpg", ".png", ".jpeg", ".svg", ".gif", ".off2", ".woff", ".woff2", ".ttf", ".otf");

    /**
     * @return String
     * @Author jcx
     * @Description url编码  和javascript中的encodeURIComponent方法效果相同
     * @Date 13:23 2020/7/29
     * @Param component:
     **/
    public static String encodeURIComponent (String component) {
        String result = null;
        try {
            result = URLEncoder.encode(component, "UTF-8")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%7E", "~");
        } catch ( UnsupportedEncodingException e ) {
            logger.error("url编码发生异常:" + e);
            result = component;
        }
        return result;
    }

    /**
     * @return Map<String, String>
     * @Author jcx
     * @Description 将URL中的查询参数部分解析成键值对
     * @Date 13:25 2020/7/29
     * @Param queryString: URL中的查询参数部分，不含前缀'?'
     **/
    public static Map<String, String> splitQuery (String queryString) {
        final Map<String, String> queryPairs = new ConcurrentHashMap<String, String>();
        final String[] pairs = queryString.split("&");
        for ( String pair : pairs ) {
            final int idx = pair.indexOf("=");
            String key;
            try {
                key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1),
                        "UTF-8") : null;
                if ( ! key.isEmpty() ) {
                    queryPairs.put(key, value);
                }
            } catch ( UnsupportedEncodingException e ) {
                logger.error("将URL中的查询参数部分解析成键值对发生异常:", e);
                throw new BusinessException("将URL中的查询参数部分解析成键值对发生异常");
            }
        }
        return queryPairs;
    }

    public static boolean isStaticSource(String uri) {
        return staticSource.stream().anyMatch(e -> uri.toLowerCase().endsWith(e.toLowerCase()));
    }
    /***
     * 获取url锚点后的字符串
     * @param url
     * @return java.lang.String
     * @author jszhang
     * @date 2022/6/9 16:43
     */
    public static  String getUrlAnchorString(String url){
        try {
            String newUrl = URLDecoder.decode(url, "utf-8");
            int i = newUrl.indexOf("#");
            if(i>-1){
                return newUrl.substring(i+1);
            }else{
                return newUrl;
            }
        } catch (UnsupportedEncodingException e) {
            logger.warn("url解码异常", e);
        }
        return "";
    }
    
    /**
     * 给原url拼接参数
     * @param oldUrl
     * @param params a=b&c=d
     * @return
     */
    public static String getNewAppendUrl(String oldUrl,String params){
        if( StringUtils.isBlank(params)){
            return oldUrl;
        }
        String linUrl="";
        String finalServiceUrl=oldUrl;
        if(oldUrl.contains("#")){
            linUrl=oldUrl.substring(oldUrl.indexOf("#"));
            finalServiceUrl=oldUrl.substring(0,oldUrl.indexOf("#"));
        }
        StringBuilder newUrlResult = new StringBuilder(finalServiceUrl);
        if(finalServiceUrl.contains("?")){
            newUrlResult.append("&");
        }else{
            newUrlResult.append("?");
        }
        newUrlResult.append(params);
        newUrlResult.append(linUrl);
        return newUrlResult.toString();

    }

}
