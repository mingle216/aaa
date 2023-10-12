package com.wisedu.amp.card.sys.seumyfavoritesx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.wisedu.minos.casp.portal.common.resttemplate.RestTemplateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.lang.reflect.Type;

//@Component
public class RestTemplateUtil {

    private final static Logger LOGGER = LogManager.getLogger(RestTemplateUtil.class);

    public static  String httpRequestForBody(HttpEntity httpEntity, String url, HttpMethod httpMethod) {
        ResponseEntity<String> exchange = null;
        LOGGER.debug("------执行rest请求,url:{},请求头:{},body:{},请求方式:{}------", url,
                JSONObject.toJSONString(httpEntity.getHeaders()),
                JSONObject.toJSONString(httpEntity.getBody()), httpMethod.name());
        try {
            exchange = RestTemplateUtils.exchange(url, httpMethod, httpEntity, String.class);
            LOGGER.debug("请求url:{}，返回结果：{}", url, JSON.toJSONString(exchange));
        } catch (RestClientException e) {
            LOGGER.error("httpclient调用接口" + url + "失败", e);
        }
        if (exchange != null && HttpStatus.OK.equals(exchange.getStatusCode())) {
            return exchange.getBody();
        }
        return null;
    }

    public static <T> T httpRequestForObject(HttpEntity httpEntity, String url, HttpMethod httpMethod,
                                      Type... types) {
        String body = httpRequestForBody(httpEntity, url, httpMethod);
        if (body != null) {
            try {
                return JSON.parseObject(body, buildType(types));
            } catch (Exception e) {
                LOGGER.error("请求url:{}，返回结果：{}，转json失败", url, body, e);
            }
        }
        return null;
    }

    private static Type buildType(Type... types) {
        if (types.length == 1) {
            return types[0];
        }
        ParameterizedTypeImpl beforeType = null;
        if (types.length > 0) {
            for (int i = types.length - 1; i > 0; i--) {
                beforeType = new ParameterizedTypeImpl(new Type[]{beforeType == null ? types[i] : beforeType}, null, types[i - 1]);
            }
        }
        return beforeType;
    }
}
