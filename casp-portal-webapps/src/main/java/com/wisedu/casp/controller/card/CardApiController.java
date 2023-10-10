package com.wisedu.casp.controller.card;

import com.wisedu.minos.annotation.OperationLog;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.service.CardApi;
import com.wisedu.minos.casp.portal.service.CardApiAdapter;
import com.wisedu.minos.annotation.ConsoleRightCheck;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import com.wisedu.minos.gateway.client.util.ApiOperationTypeEnums;
import com.wisedu.minos.util.MenuEditTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_SET, menu = MenuEditTypeEnum.MENU_ID_PROGRAMME_MANAGE)
    public InlineResponse2006 deleteCard(List<String> body ) {
        return adapter.deleteCard(body);
    }


    /**
    * 卡片管理--根据卡片WId获取卡片内容详情
    **/
    @ManagerGatewayApi(name = "卡片管理--根据卡片WId获取卡片内容详情",realPath = "/card/getCardInfo/{wid}")
    @OperationLog(id = "20002",name = "卡片管理--根据卡片WId获取卡片内容详情",operateObject="卡片")
    @Override
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_LOOK, menu = MenuEditTypeEnum.MENU_ID_PROGRAMME_MANAGE)
    public InlineResponse2005 getCardInfo( String wid ) {
        return adapter.getCardInfo(wid);
    }


    /**
    * 卡片管理--获取卡片列表(有分页)
    **/
    @ManagerGatewayApi(name = "卡片管理--获取卡片列表(有分页)",realPath = "/card/queryCardList")
    @OperationLog(id = "20001",name = "获取卡片列表(有分页)",operateObject="卡片")
    @Override
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_LOOK, menu = MenuEditTypeEnum.MENU_ID_PROGRAMME_MANAGE)
    public InlineResponse2004 queryCardList(CommonQueryListReq body ) {
        return adapter.queryCardList(body);
    }


    /**
    * 保存卡片配置
    **/
    @ManagerGatewayApi(name = "保存卡片配置",realPath = "/card/saveCardConfig")
    @OperationLog(id = "20004",name = "保存卡片配置",operateObject="卡片")
    @Override
    @ConsoleRightCheck(roleType = MenuEditTypeEnum.MENU_EDIT_TYPE_SET, menu = MenuEditTypeEnum.MENU_ID_PROGRAMME_MANAGE)
    public InlineResponse2005 saveCardConfig(CommonStringInfo body ) {
        return adapter.saveCardConfig(body);
    }

}
