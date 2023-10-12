package com.wisedu.casp.controller.template;

import com.wisedu.minos.casp.portal.model.CommonQueryListReq;
import com.wisedu.minos.casp.portal.model.CommonStringInfo;
import com.wisedu.minos.casp.portal.model.InlineResponse2007;
import com.wisedu.minos.casp.portal.model.InlineResponse2008;
import com.wisedu.minos.casp.portal.model.InlineResponse2009;
import com.wisedu.minos.casp.portal.model.ModelApiResponse;

import com.wisedu.minos.casp.portal.service.TemplateApi;
import com.wisedu.minos.casp.portal.service.TemplateApiAdapter;
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
public class TemplateApiController implements TemplateApi {

    @Autowired
    private TemplateApiAdapter adapter;


    /**
    * 删除部分模板
    **/
    @ManagerGatewayApi(name = "删除部分模板",realPath = "/template/deleteTemplate")
    @OperationLog(id = "20007",name = "删除部分模板",operateObject="模板")
    @Override
    public InlineResponse2009 deleteTemplate( List<String> body ) {
        return adapter.deleteTemplate(body);
    }


    /**
    * 模板管理--根据模板WId获取模板内容详情
    **/
    @ManagerGatewayApi(name = "模板管理--根据模板WId获取模板内容详情",realPath = "/template/getTemplateInfo/{wid}")
    @OperationLog(id = "20006",name = "模板管理--根据模板WId获取模板内容详情",operateObject="模板")
    @Override
    public InlineResponse2008 getTemplateInfo( String wid ) {
        return adapter.getTemplateInfo(wid);
    }


    /**
    * 模板管理--获取模板列表(有分页)
    **/
    @ManagerGatewayApi(name = "模板管理--获取模板列表(有分页)",realPath = "/template/queryTemplates")
    @OperationLog(id = "20005",name = "获取模板列表(有分页)",operateObject="模板")
    @Override
    public InlineResponse2007 queryTemplates( CommonQueryListReq body ) {
        return adapter.queryTemplates(body);
    }


    /**
    * 保存模板配置
    **/
    @ManagerGatewayApi(name = "保存模板配置",realPath = "/template/saveTemplateConfig")
    @OperationLog(id = "20008",name = "保存模板配置",operateObject="模板")
    @Override
    public InlineResponse2008 saveTemplateConfig( CommonStringInfo body ) {
        return adapter.saveTemplateConfig(body);
    }

}
