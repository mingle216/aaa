package com.wisedu.minos.casp.portal.service;

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


public interface MinosApiAdapter {


    /**
    * Minos接口--获取Minos域及用户组信息
    **/
    public InlineResponse20031 getFieldGroups(FieldGroupsReq body);


    /**
    * 机构管理--获取Minos机构树信息
    **/
    public InlineResponse20029 getMinosOrg(OrgInfoTreeReq body);


    /**
    * 用户管理--根据用户WId获取用户详情
    **/
    public InlineResponse20028 getUserDetails(String wid);


    /**
    * 用户管理--获取用户列表(有分页)
    **/
    public InlineResponse20027 searchUser(UserInfoReq body);


    /**
    * 更新用户信息
    **/
    public InlineResponse20030 updateUserInfo(DubboUserInfo body);

}
