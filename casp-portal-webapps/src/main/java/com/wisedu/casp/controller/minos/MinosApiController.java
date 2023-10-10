package com.wisedu.casp.controller.minos;

import com.wisedu.minos.api.model.DubboUserInfo;
import com.wisedu.minos.casp.portal.model.DubboUserInfoReq;
import com.wisedu.minos.casp.portal.model.FieldGroupsReq;
import com.wisedu.minos.casp.portal.model.InlineResponse20027;
import com.wisedu.minos.casp.portal.model.InlineResponse20028;
import com.wisedu.minos.casp.portal.model.InlineResponse20029;
import com.wisedu.minos.casp.portal.model.InlineResponse20030;
import com.wisedu.minos.casp.portal.model.InlineResponse20031;
import com.wisedu.minos.casp.portal.model.ModelApiResponse;
import com.wisedu.minos.casp.portal.model.OrgInfoTreeReq;
import com.wisedu.minos.casp.portal.model.UserInfoReq;

import com.wisedu.minos.casp.portal.service.MinosApi;
import com.wisedu.minos.casp.portal.service.MinosApiAdapter;
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
    * 用户管理--根据用户WId获取用户详情
    **/
    @ManagerGatewayApi(name = "用户管理--根据用户WId获取用户详情",realPath = "/minos/getUserDetails/{wid}")
    @OperationLog(id = "20053",name = "用户管理--根据用户WId获取用户详情",operateObject="用户管理")
    @Override
    public InlineResponse20028 getUserDetails( String wid ) {
        return adapter.getUserDetails(wid);
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
