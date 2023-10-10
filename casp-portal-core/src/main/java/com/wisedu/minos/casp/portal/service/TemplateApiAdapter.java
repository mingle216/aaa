package com.wisedu.minos.casp.portal.service;

import com.wisedu.minos.casp.portal.model.CommonQueryListReq;
import com.wisedu.minos.casp.portal.model.CommonStringInfo;
import com.wisedu.minos.casp.portal.model.InlineResponse2007;
import com.wisedu.minos.casp.portal.model.InlineResponse2008;
import com.wisedu.minos.casp.portal.model.InlineResponse2009;
import com.wisedu.minos.casp.portal.model.ModelApiResponse;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface TemplateApiAdapter {


    /**
    * 删除部分模板
    **/
    public InlineResponse2009 deleteTemplate(List<String> body);


    /**
    * 模板管理--根据模板WId获取模板内容详情
    **/
    public InlineResponse2008 getTemplateInfo(String wid);


    /**
    * 模板管理--获取模板列表(有分页)
    **/
    public InlineResponse2007 queryTemplates(CommonQueryListReq body);


    /**
    * 保存模板配置
    **/
    public InlineResponse2008 saveTemplateConfig(CommonStringInfo body);

}
