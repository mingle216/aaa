package com.wisedu.casp.controller.minos;

import com.wisedu.minos.annotation.OperationLog;
import com.wisedu.minos.api.model.DubboUserInfo;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.service.MinosApi;
import com.wisedu.minos.casp.portal.service.MinosApiAdapter;
import com.wisedu.minos.gateway.client.annotation.IgnoreGatewaySecurity;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MinosApiController implements MinosApi {

    @Autowired
    private MinosApiAdapter adapter;


    /**
    * Minos接口--获取Minos域及用户组信息
    **/
    @ManagerGatewayApi(name = "Minos接口--获取Minos域及用户组信息",realPath = "/minos/getFieldGroups")
    @OperationLog(id = "20055",name = "Minos接口--获取Minos域及用户组信息",operateObject="Minos接口")
    @Override
    public InlineResponse20031 getFieldGroups( FieldGroupsReq body ) {
        return adapter.getFieldGroups(body);
    }


    /**
    * 机构管理--获取Minos机构树信息
    **/
    @ManagerGatewayApi(name = "机构管理--获取Minos机构树信息",realPath = "/minos/getMinosOrg")
    @OperationLog(id = "20054",name = "机构管理--获取Minos机构树信息",operateObject="机构管理")
    @Override
    public InlineResponse20029 getMinosOrg( OrgInfoTreeReq body ) {
        return adapter.getMinosOrg(body);
    }



    /**
    * 用户管理--获取用户列表(有分页)
    **/
    @ManagerGatewayApi(name = "用户管理--获取用户列表(有分页)",realPath = "/minos/searchUser")
    @OperationLog(id = "20051",name = "获取用户列表(有分页)",operateObject="卡片")
    @Override
    public InlineResponse20027 searchUser( UserInfoReq body ) {
        return adapter.searchUser(body);
    }


    /**
    * 更新用户信息
    **/
    @ManagerGatewayApi(name = "更新用户信息",realPath = "/minos/updateUserInfo")
    @OperationLog(id = "202101",name = "更新用户信息",operateObject="更新用户信息")
    @Override
    public InlineResponse20030 updateUserInfo( DubboUserInfo body ) {
        return adapter.updateUserInfo(body);
    }

}
