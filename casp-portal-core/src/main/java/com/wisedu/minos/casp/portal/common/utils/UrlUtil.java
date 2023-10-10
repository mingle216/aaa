package com.wisedu.minos.casp.portal.common.utils;

import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
}
