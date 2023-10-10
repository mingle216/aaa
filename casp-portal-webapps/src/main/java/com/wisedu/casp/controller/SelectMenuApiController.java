package com.wisedu.casp.controller;

import com.wisedu.minos.casp.portal.model.v353beta2.CommonRes;
import com.wisedu.minos.casp.portal.model.v353beta2.EditStatusReq;
import com.wisedu.minos.casp.portal.model.v353beta2.QuerySelectMenusRes;
import com.wisedu.minos.casp.portal.model.v353beta2.SaveSelectMenuReq;
import com.wisedu.minos.casp.portal.model.v353beta2.SelectMenuDetailReq;
import com.wisedu.minos.casp.portal.model.v353beta2.SelectMenuDetailRes;
import com.wisedu.minos.casp.portal.model.v353beta2.SortSelectMenuReq;

import com.wisedu.minos.casp.portal.service.v353beta2.SelectMenuApi;
import com.wisedu.minos.casp.portal.service.v353beta2.SelectMenuApiAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import com.wisedu.minos.gateway.client.util.ApiOperationTypeEnums;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class SelectMenuApiController implements SelectMenuApi {

    @Autowired
    private SelectMenuApiAdapter adapter;


    /**
    * 删除下拉菜单
    **/
    @ManagerGatewayApi(name = "删除下拉菜单",realPath = "/selectMenu/deletedSelectMenu",operationType = ApiOperationTypeEnums.API_OPERATION_DELETE)
    @Override
    public CommonRes deletedSelectMenu( SelectMenuDetailReq body ) {
        return adapter.deletedSelectMenu(body);
    }


    /**
    * 下拉菜单启停用
    **/
    @ManagerGatewayApi(name = "下拉菜单启停用",realPath = "/selectMenu/editStatus",operationType = ApiOperationTypeEnums.API_OPERATION_OTHER)
    @Override
    public CommonRes editStatus( EditStatusReq body ) {
        return adapter.editStatus(body);
    }


    /**
    * 查询下拉菜单详情
    **/
    @ManagerGatewayApi(name = "查询下拉菜单详情",realPath = "/selectMenu/querySelectMenuDetail",operationType = ApiOperationTypeEnums.API_OPERATION_QUERY)
    @Override
    public SelectMenuDetailRes querySelectMenuDetail( SelectMenuDetailReq body ) {
        return adapter.querySelectMenuDetail(body);
    }


    /**
    * 查询下拉菜单
    **/
    @ManagerGatewayApi(name = "查询下拉菜单",realPath = "/selectMenu/querySelectMenus/{wid}",operationType = ApiOperationTypeEnums.API_OPERATION_QUERY)
    @Override
    public QuerySelectMenusRes querySelectMenus( String wid ) {
        return adapter.querySelectMenus(wid);
    }


    /**
    * 保存下拉菜单
    **/
    @ManagerGatewayApi(name = "保存下拉菜单",realPath = "/selectMenu/saveSelectMenu",operationType = ApiOperationTypeEnums.API_OPERATION_MODIFY)
    @Override
    public CommonRes saveSelectMenu( SaveSelectMenuReq body ) {
        return adapter.saveSelectMenu(body);
    }


    /**
    * 下拉菜单排序
    **/
    @ManagerGatewayApi(name = "下拉菜单排序",realPath = "/selectMenu/sortSelectMenu",operationType = ApiOperationTypeEnums.API_OPERATION_OTHER)
    @Override
    public CommonRes sortSelectMenu( List<SortSelectMenuReq> body ) {
        return adapter.sortSelectMenu(body);
    }

}
