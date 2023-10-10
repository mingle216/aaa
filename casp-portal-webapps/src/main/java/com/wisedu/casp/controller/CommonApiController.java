package com.wisedu.casp.controller;

import com.wisedu.minos.casp.portal.model.InlineResponse200;
import com.wisedu.minos.casp.portal.model.InlineResponse2001;
import com.wisedu.minos.casp.portal.model.InlineResponse2002;
import com.wisedu.minos.casp.portal.model.InlineResponse2003;
import com.wisedu.minos.casp.portal.model.ModelApiResponse;
import com.wisedu.minos.casp.portal.model.QueryVersionsReq;

import com.wisedu.minos.casp.portal.service.CommonApi;
import com.wisedu.minos.casp.portal.service.CommonApiAdapter;
import com.wisedu.minos.gateway.client.util.ApiOperationTypeEnums;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import com.wisedu.minos.gateway.client.annotation.OpenGatewayApi;
import com.wisedu.minos.annotation.OperationLog;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
public class CommonApiController implements CommonApi {

    @Autowired
    private CommonApiAdapter adapter;


    /**
    * 得到卡片/模板配置页面 html渲染字符串
    **/
    @ManagerGatewayApi(name = "得到卡片/模板配置页面 html渲染字符串",realPath = "/common/getConfigPage/{wid}")
    @OperationLog(id = "20009",name = "得到卡片/模板配置页面 html渲染字符串",operateObject="公共")
    @Override
    public InlineResponse2001 getConfigPage() {
        return adapter.getConfigPage();
    }


    /**
    * 获取minos国际化语言列表
    **/
    @ManagerGatewayApi(name = "获取minos国际化语言列表",realPath = "/common/queryLangList")
    @OperationLog(id = "20077",name = "获取minos国际化语言列表",operateObject="公共")
    @Override
    public InlineResponse2002 queryLangList() {
        return adapter.queryLangList();
    }


    /**
    * 获取卡片或模板版本数据
    **/
    @ManagerGatewayApi(name = "获取卡片或模板版本数据",realPath = "/common/queryVersions")
    @OperationLog(id = "20078",name = "获取卡片或模板版本数据",operateObject="公共")
    @Override
    public InlineResponse2003 queryVersions( QueryVersionsReq body ) {
        return adapter.queryVersions(body);
    }


    /**
    * 重启服务
    **/
    @ManagerGatewayApi(name = "重启服务",realPath = "/common/restartService",operationType = ApiOperationTypeEnums.API_OPERATION_OTHER)
    @OperationLog(id = "20000",name = "重启服务",operateObject="公共")
    @Override
    public InlineResponse200 restartService() {
        return adapter.restartService();
    }

}
