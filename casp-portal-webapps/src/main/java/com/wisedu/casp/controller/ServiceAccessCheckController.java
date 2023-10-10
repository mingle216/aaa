package com.wisedu.casp.controller;

import com.wisedu.minos.api.data.AppServiceService;
import com.wisedu.minos.api.model.DubboServiceHealthInfo;
import com.wisedu.minos.casp.portal.model.CommonResponseResult;
import com.wisedu.minos.casp.portal.model.serviceCheck.ServiceCheckUrl;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @DubboReference
    AppServiceService appServiceService;

    @RequestMapping("service/getHealthInfo")
    public CommonResponseResult<DubboServiceHealthInfo> getHealthInfo(@RequestParam("serviceWid") String serviceWid) {
        CommonResponseResult<DubboServiceHealthInfo> res = new CommonResponseResult<>();
        res.setData(appServiceService.getServiceHealthInfo(serviceWid));
        return res;
    }

}
