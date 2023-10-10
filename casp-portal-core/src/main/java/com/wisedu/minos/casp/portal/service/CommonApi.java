/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.14).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.wisedu.minos.casp.portal.service;

import com.wisedu.minos.casp.portal.model.InlineResponse200;
import com.wisedu.minos.casp.portal.model.InlineResponse2001;
import com.wisedu.minos.casp.portal.model.InlineResponse2002;
import com.wisedu.minos.casp.portal.model.InlineResponse2003;
import com.wisedu.minos.casp.portal.model.ModelApiResponse;
import com.wisedu.minos.casp.portal.model.QueryVersionsReq;
import io.swagger.annotations.*;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Api(value = "common", description = "the common API")
public interface CommonApi {


    /**
     * 得到卡片/模板配置页面 html渲染字符串
     **/
    @RequestMapping(value = "/common/getConfigPage/{wid}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
        InlineResponse2001 getConfigPage();


    /**
     * 获取minos国际化语言列表
     **/
    @RequestMapping(value = "/common/queryLangList",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
        InlineResponse2002 queryLangList();


    /**
     * 获取卡片或模板版本数据
     **/
    @RequestMapping(value = "/common/queryVersions",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
        InlineResponse2003 queryVersions( @Valid @RequestBody QueryVersionsReq body);


    /**
     * 重启服务
     **/
    @RequestMapping(value = "/common/restartService",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
        InlineResponse200 restartService();

}
