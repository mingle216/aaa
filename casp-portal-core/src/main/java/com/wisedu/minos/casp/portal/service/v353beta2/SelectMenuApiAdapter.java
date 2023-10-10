package com.wisedu.minos.casp.portal.service.v353beta2;

import com.wisedu.minos.casp.portal.model.v353beta2.CommonRes;
import com.wisedu.minos.casp.portal.model.v353beta2.EditStatusReq;
import com.wisedu.minos.casp.portal.model.v353beta2.ModelApiResponse;
import com.wisedu.minos.casp.portal.model.v353beta2.QuerySelectMenusRes;
import com.wisedu.minos.casp.portal.model.v353beta2.SaveSelectMenuReq;
import com.wisedu.minos.casp.portal.model.v353beta2.SelectMenuDetailReq;
import com.wisedu.minos.casp.portal.model.v353beta2.SelectMenuDetailRes;
import com.wisedu.minos.casp.portal.model.v353beta2.SortSelectMenuReq;

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


public interface SelectMenuApiAdapter {


    /**
    * 删除下拉菜单
    **/
    public CommonRes deletedSelectMenu(SelectMenuDetailReq body);


    /**
    * 下拉菜单启停用
    **/
    public CommonRes editStatus(EditStatusReq body);


    /**
    * 查询下拉菜单详情
    **/
    public SelectMenuDetailRes querySelectMenuDetail(SelectMenuDetailReq body);


    /**
    * 查询下拉菜单
    **/
    public QuerySelectMenusRes querySelectMenus(String wid);


    /**
    * 保存下拉菜单
    **/
    public CommonRes saveSelectMenu(SaveSelectMenuReq body);


    /**
    * 下拉菜单排序
    **/
    public CommonRes sortSelectMenu(List<SortSelectMenuReq> body);

}
