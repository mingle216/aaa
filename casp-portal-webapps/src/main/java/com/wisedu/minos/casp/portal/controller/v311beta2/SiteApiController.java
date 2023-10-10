package com.wisedu.minos.casp.portal.controller.v311beta2;

import com.wisedu.minos.annotation.OperationLog;
import com.wisedu.minos.casp.portal.model.DeleteSiteRequest;
import com.wisedu.minos.casp.portal.model.v311beta2.*;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import com.wisedu.minos.gateway.client.util.ApiOperationTypeEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class SiteApiController implements SiteApi {

    @Autowired
    private SiteApiAdapter adapter;


    /**
    * 查询单个站点信息
    **/
    @ManagerGatewayApi(name = "查询单个站点信息",realPath = "/site/changeEnableStatus",operationType = ApiOperationTypeEnums.API_OPERATION_MODIFY)
    @OperationLog(id = "20001",name = "启用、停用站点",operateObject="启用、停用站点")
    @Override
    public OneSiteResponse changeEnableStatus( PortalSiteInfo body ) {
        return adapter.changeEnableStatus(body);
    }


    /**
    * 判断站点路由是否已经存在
    **/
    @ManagerGatewayApi(name = "判断站点路由是否已经存在",realPath = "/site/checkRoute")
    @Override
    public CheckRouteResponse checkRoute( PortalSiteInfo body ) {
        return adapter.checkRoute(body);
    }


    /**
     * 删除站点
     **/
    @ManagerGatewayApi(name = "删除站点",realPath = "/site/deleteSite" ,operationType=ApiOperationTypeEnums.API_OPERATION_DELETE )
    @OperationLog(id = "20000",name = "删除站点",operateObject="删除站点")
    @Override
    public SiteDeleteResponse deleteSite( DeleteSiteRequest body ) {
        return adapter.deleteSite(body);
    }


    /**
    * 查询单个站点信息
    **/
    @ManagerGatewayApi(name = "查询单个站点信息",realPath = "/site/detailSite")
    @OperationLog(id = "20001",name = "查询单个站点信息",operateObject="查询单个站点信息")
    @Override
    public OneSiteResponse detailSite( PortalSiteInfo body ) {
        return adapter.detailSite(body);
    }


    /**
    * 保存站点
    **/
    @ManagerGatewayApi(name = "保存站点",realPath = "/site/saveSite",operationType = ApiOperationTypeEnums.API_OPERATION_MODIFY)
    @OperationLog(id = "20000",name = "保存站点",operateObject="保存站点")
    @Override
    public OneSiteResponse saveSite( PortalSiteInfo body ) {
        return adapter.saveSite(body);
    }


    /**
    * 搜索站点
    **/
    @ManagerGatewayApi(name = "搜索站点",realPath = "/site/searchSite")
    @OperationLog(id = "20001",name = "搜索站点",operateObject="搜索站点")
    @Override
    public SearchSiteApiResponse searchSite( SearchSiteRequest body ) {
        return adapter.searchSite(body);
    }


    /**
    * 站点排序
    **/
    @ManagerGatewayApi(name = "站点排序",realPath = "/site/sortSites",operationType = ApiOperationTypeEnums.API_OPERATION_MODIFY)
    @OperationLog(id = "20001",name = "站点排序",operateObject="站点排序")
    @Override
    public SortSitesResponse sortSites( PortalSortSitesRequest body ) {
        return adapter.sortSites(body);
    }






}
