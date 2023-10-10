package com.wisedu.casp.controller;

import com.wisedu.minos.casp.portal.model.CommonResponseResult;
import com.wisedu.minos.casp.portal.model.serviceCheck.ServiceCheckUrl;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * 在服务跳转之前是否要做可访问的校验
 * @date 2022/5/18 14:26
 * @author jszhang@wisedu
 * @version 1.0
 **/
@RestController
public class ServiceAccessCheckController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAccessCheckController.class);
    @Autowired
    SystemConfigService systemConfigService;

    @RequestMapping("service/serviceAccessCheck/config")
    public CommonResponseResult<String> getAccessTokenOn() {
        String accessCheck = systemConfigService.getSystemConfigValue("service_access_check");
        CommonResponseResult<String> objectCommonResponseResult = new CommonResponseResult<>();
        return objectCommonResponseResult.setData(accessCheck);
    }
    @RequestMapping("service/serviceAccessCheck/result")
    public CommonResponseResult<Boolean> serviceAccessResult(@RequestBody ServiceCheckUrl url) {
        CommonResponseResult<Boolean> res = new CommonResponseResult<>();
        res.setData(urlAccess(url.getUrl()));
        return res;
    }

    /***
     * 访问url返回5xx或者404就认为是异常
     * @param url
     * @return boolean
     * @author jszhang
     * @date 2022/5/18 15:04
     */
    public boolean urlAccess(String url) {
        if(StringUtils.isBlank(url)){
         return false;
        }
        try {
            URL urlResource = new URL(url);
            URLConnection rulConnection = urlResource.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
            httpUrlConnection.setConnectTimeout(3000);
            httpUrlConnection.setReadTimeout(3000);
            httpUrlConnection.connect();
            String code = httpUrlConnection.getResponseCode() + "";
            LOGGER.debug("访问url：{}的状态码{}",url,code);
            return !(code.indexOf("5") ==0|| "404".equals(code));
        }catch (Exception e){
            LOGGER.error("访问url：{}异常",url,e);
        }
        return false;
    }
}
