package com.wisedu.minos.casp.portal.service;

import com.wisedu.minos.casp.portal.model.InlineResponse200;
import com.wisedu.minos.casp.portal.model.InlineResponse2001;
import com.wisedu.minos.casp.portal.model.InlineResponse2002;
import com.wisedu.minos.casp.portal.model.InlineResponse2003;
import com.wisedu.minos.casp.portal.model.ModelApiResponse;
import com.wisedu.minos.casp.portal.model.QueryVersionsReq;

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


public interface CommonApiAdapter {


    /**
    * 得到卡片/模板配置页面 html渲染字符串
    **/
    public InlineResponse2001 getConfigPage();


    /**
    * 获取minos国际化语言列表
    **/
    public InlineResponse2002 queryLangList();


    /**
    * 获取卡片或模板版本数据
    **/
    public InlineResponse2003 queryVersions(QueryVersionsReq body);


    /**
    * 重启服务
    **/
    public InlineResponse200 restartService();

}
