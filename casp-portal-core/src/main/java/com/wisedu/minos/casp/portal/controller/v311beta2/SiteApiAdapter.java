package com.wisedu.minos.casp.portal.controller.v311beta2;

import com.wisedu.minos.casp.portal.model.DeleteSiteRequest;
import com.wisedu.minos.casp.portal.model.v311beta2.CheckRouteResponse;
import com.wisedu.minos.casp.portal.model.v311beta2.ModelApiResponse;
import com.wisedu.minos.casp.portal.model.v311beta2.OneSiteResponse;
import com.wisedu.minos.casp.portal.model.v311beta2.PortalSiteInfo;
import com.wisedu.minos.casp.portal.model.v311beta2.PortalSortSitesRequest;
import com.wisedu.minos.casp.portal.model.v311beta2.SearchSiteApiResponse;
import com.wisedu.minos.casp.portal.model.v311beta2.SearchSiteRequest;
import com.wisedu.minos.casp.portal.model.v311beta2.SiteDeleteResponse;
import com.wisedu.minos.casp.portal.model.v311beta2.SortSitesResponse;

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


public interface SiteApiAdapter {


    /**
    * 查询单个站点信息
    **/
    public OneSiteResponse changeEnableStatus(PortalSiteInfo body);


    /**
    * 判断站点路由是否已经存在
    **/
    public CheckRouteResponse checkRoute(PortalSiteInfo body);


    /**
     * 删除站点
     **/
    public SiteDeleteResponse deleteSite(DeleteSiteRequest body);


    /**
    * 查询单个站点信息
    **/
    public OneSiteResponse detailSite(PortalSiteInfo body);


    /**
    * 保存站点
    **/
    public OneSiteResponse saveSite(PortalSiteInfo body);


    /**
    * 搜索站点
    **/
    public SearchSiteApiResponse searchSite(SearchSiteRequest body);


    /**
    * 站点排序
    **/
    public SortSitesResponse sortSites(PortalSortSitesRequest body);

}
