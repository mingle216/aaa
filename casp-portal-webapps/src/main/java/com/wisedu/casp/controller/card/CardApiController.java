package com.wisedu.casp.controller.card;

import com.wisedu.minos.casp.portal.model.CommonQueryListReq;
import com.wisedu.minos.casp.portal.model.CommonStringInfo;
import com.wisedu.minos.casp.portal.model.InlineResponse2004;
import com.wisedu.minos.casp.portal.model.InlineResponse2005;
import com.wisedu.minos.casp.portal.model.InlineResponse2006;
import com.wisedu.minos.casp.portal.model.ModelApiResponse;

import com.wisedu.minos.casp.portal.service.CardApi;
import com.wisedu.minos.casp.portal.service.CardApiAdapter;
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
public class CardApiController implements CardApi {

    @Autowired
    private CardApiAdapter adapter;


    /**
    * 删除部分卡片
    **/
    @ManagerGatewayApi(name = "删除部分卡片",realPath = "/card/deleteCard",operationType = ApiOperationTypeEnums.API_OPERATION_DELETE)
    @OperationLog(id = "20003",name = "删除部分卡片",operateObject="卡片")
    @Override
    public InlineResponse2006 deleteCard( List<String> body ) {
        return adapter.deleteCard(body);
    }


    /**
    * 卡片管理--根据卡片WId获取卡片内容详情
    **/
    @ManagerGatewayApi(name = "卡片管理--根据卡片WId获取卡片内容详情",realPath = "/card/getCardInfo/{wid}")
    @OperationLog(id = "20002",name = "卡片管理--根据卡片WId获取卡片内容详情",operateObject="卡片")
    @Override
    public InlineResponse2005 getCardInfo( String wid ) {
        return adapter.getCardInfo(wid);
    }


    /**
    * 卡片管理--获取卡片列表(有分页)
    **/
    @ManagerGatewayApi(name = "卡片管理--获取卡片列表(有分页)",realPath = "/card/queryCardList")
    @OperationLog(id = "20001",name = "获取卡片列表(有分页)",operateObject="卡片")
    @Override
    public InlineResponse2004 queryCardList( CommonQueryListReq body ) {
        return adapter.queryCardList(body);
    }


    /**
    * 保存卡片配置
    **/
    @ManagerGatewayApi(name = "保存卡片配置",realPath = "/card/saveCardConfig")
    @OperationLog(id = "20004",name = "保存卡片配置",operateObject="卡片")
    @Override
    public InlineResponse2005 saveCardConfig( CommonStringInfo body ) {
        return adapter.saveCardConfig(body);
    }

}
