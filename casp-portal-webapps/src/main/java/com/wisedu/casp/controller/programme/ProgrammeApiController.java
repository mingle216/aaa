package com.wisedu.casp.controller.programme;

import com.wisedu.minos.casp.portal.model.CommonReq;
import com.wisedu.minos.casp.portal.model.EnabledReq;
import com.wisedu.minos.casp.portal.model.InlineResponse200;
import com.wisedu.minos.casp.portal.model.InlineResponse20010;
import com.wisedu.minos.casp.portal.model.InlineResponse20011;
import com.wisedu.minos.casp.portal.model.InlineResponse20012;
import com.wisedu.minos.casp.portal.model.InlineResponse20013;
import com.wisedu.minos.casp.portal.model.InlineResponse20014;
import com.wisedu.minos.casp.portal.model.InlineResponse20015;
import com.wisedu.minos.casp.portal.model.InlineResponse20016;
import com.wisedu.minos.casp.portal.model.InlineResponse20017;
import com.wisedu.minos.casp.portal.model.InlineResponse20018;
import com.wisedu.minos.casp.portal.model.InlineResponse20019;
import com.wisedu.minos.casp.portal.model.InlineResponse20020;
import com.wisedu.minos.casp.portal.model.InlineResponse20021;
import com.wisedu.minos.casp.portal.model.InlineResponse20022;
import com.wisedu.minos.casp.portal.model.InlineResponse20023;
import com.wisedu.minos.casp.portal.model.InlineResponse20024;
import com.wisedu.minos.casp.portal.model.InlineResponse20025;
import com.wisedu.minos.casp.portal.model.InlineResponse20026;
import com.wisedu.minos.casp.portal.model.MenuInfoRes;
import com.wisedu.minos.casp.portal.model.MenuListRes;
import com.wisedu.minos.casp.portal.model.PageInfoRes;
import com.wisedu.minos.casp.portal.model.PopupWindowReq;
import com.wisedu.minos.casp.portal.model.PopupWindowRes;
import com.wisedu.minos.casp.portal.model.PreviewPageReq;
import com.wisedu.minos.casp.portal.model.ProgrammeInfoRes;
import com.wisedu.minos.casp.portal.model.ProgrammeListReq;
import com.wisedu.minos.casp.portal.model.ProgrammeReq;
import com.wisedu.minos.casp.portal.model.SidebarInfoBiz;
import com.wisedu.minos.casp.portal.model.SidebarListReq;
import com.wisedu.minos.casp.portal.model.SidebarSequenceReqObj;
import com.wisedu.minos.casp.portal.model.SidebarStatusReq;
import com.wisedu.minos.casp.portal.model.SortInfo;
import com.wisedu.minos.casp.portal.model.UpdateStatusReq;
import com.wisedu.minos.casp.portal.service.ProgrammeApi;
import com.wisedu.minos.casp.portal.service.ProgrammeApiAdapter;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import com.wisedu.minos.annotation.OperationLog;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ProgrammeApiController implements ProgrammeApi {

    @Autowired
    private ProgrammeApiAdapter adapter;


    /**
    * 复制展示方案
    **/
    @ManagerGatewayApi(name = "复制展示方案",realPath = "/programme/copyProgramme")
    @OperationLog(id = "20049",name = "复制展示方案",operateObject="展示方案")
    @Override
    public InlineResponse200 copyProgramme( CommonReq body ) {
        return adapter.copyProgramme(body);
    }


    /**
    * 展示方案--删除菜单
    **/
    @ManagerGatewayApi(name = "展示方案--删除菜单",realPath = "/programme/deleteMenu")
    @OperationLog(id = "20021",name = "删除目录",operateObject="展示方案")
    @Override
    public InlineResponse20011 deleteMenu( List<String> body ) {
        return adapter.deleteMenu(body);
    }


    /**
    * 展示方案--删除页面
    **/
    @ManagerGatewayApi(name = "展示方案--删除页面",realPath = "/programme/deletePageInfo")
    @OperationLog(id = "20022",name = "展示方案--删除页面",operateObject="展示方案")
    @Override
    public InlineResponse20012 deletePageInfo( List<String> body ) {
        return adapter.deletePageInfo(body);
    }


    /**
    * 删除展示方案,支持批量删除应用
    **/
    @ManagerGatewayApi(name = "删除展示方案,支持批量删除应用",realPath = "/programme/deleteProgramme")
    @OperationLog(id = "20042",name = "删除部分展示方案",operateObject="展示方案")
    @Override
    public InlineResponse20019 deleteProgramme( List<String> body ) {
        return adapter.deleteProgramme(body);
    }


    /**
    * 根据主键删除侧边栏
    **/
    @ManagerGatewayApi(name = "根据主键删除侧边栏",realPath = "/programme/deleteSidebar")
    @OperationLog(id = "20029",name = "根据主键删除侧边栏",operateObject="展示方案")
    @Override
    public InlineResponse20016 deleteSidebar( List<String> body ) {
        return adapter.deleteSidebar(body);
    }


    /**
    * 根据展示方案id获取基本信息
    **/
    @ManagerGatewayApi(name = "根据展示方案id获取基本信息",realPath = "/programme/getBasicInfo/{code}")
    @OperationLog(id = "20043",name = "根据展示方案id获取基本信息",operateObject="展示方案")
    @Override
    public InlineResponse20020 getBasicInfo( String code ) {
        return adapter.getBasicInfo(code);
    }


    /**
    * 根据卡片运行时ID查询卡片配置信息
    **/
    @ManagerGatewayApi(name = "根据卡片运行时ID查询卡片配置信息",realPath = "/programme/getCardConfig/{code}")
    @Override
    public InlineResponse20023 getCardConfig( String code ) {
        return adapter.getCardConfig(code);
    }


    /**
    * 根据页面wid查询页面信息
    **/
    @ManagerGatewayApi(name = "根据页面wid查询页面信息",realPath = "/programme/getPageInfo/{wid}")
    @Override
    public InlineResponse20022 getPageInfo( String wid ) {
        return adapter.getPageInfo(wid);
    }


    /**
    * 获取侧边栏详情
    **/
    @ManagerGatewayApi(name = "获取侧边栏详情",realPath = "/programme/getSidebarInfo/{wid}")
    @OperationLog(id = "20026",name = "展示方案--获取侧边栏详情",operateObject="展示方案")
    @Override
    public InlineResponse20015 getSidebarInfo( String wid ) {
        return adapter.getSidebarInfo(wid);
    }


    /**
    * 展示方案--预览页面
    **/
    @ManagerGatewayApi(name = "展示方案--预览页面",realPath = "/programme/previewPage")
    @OperationLog(id = "20023",name = "展示方案--预览页面",operateObject="展示方案")
    @Override
    public InlineResponse20013 previewPage( PreviewPageReq body ) {
        return adapter.previewPage(body);
    }


    /**
    * 根据展示方案id查询菜单信息
    **/
    @ManagerGatewayApi(name = "根据展示方案id查询菜单信息",realPath = "/programme/queryMenuList")
    @Override
    public InlineResponse20021 queryMenuList( MenuListRes body ) {
        return adapter.queryMenuList(body);
    }


    /**
    * 展示方案-根据展示方案id查询页面列表
    **/
    @ManagerGatewayApi(name = "展示方案-根据展示方案id查询页面列表",realPath = "/programme/queryPageList")
    @OperationLog(id = "20031",name = "展示方案-根据展示方案id查询页面列表",operateObject="展示方案")
    @Override
    public InlineResponse20017 queryPageList( ProgrammeReq body ) {
        return adapter.queryPageList(body);
    }


    /**
    * 查询展示方案列表
    **/
    @ManagerGatewayApi(name = "查询展示方案列表",realPath = "/programme/queryProgrammeList")
    @Override
    public InlineResponse20018 queryProgrammeList( ProgrammeListReq body ) {
        return adapter.queryProgrammeList(body);
    }


    /**
    * 展示方案--侧边栏列表
    **/
    @ManagerGatewayApi(name = "展示方案--侧边栏列表",realPath = "/programme/querySidebarList")
    @OperationLog(id = "20024",name = "展示方案--侧边栏列表",operateObject="卡片")
    @Override
    public InlineResponse20014 querySidebarList( SidebarListReq body ) {
        return adapter.querySidebarList(body);
    }


    /**
    * 查询系统图标库列表
    **/
    @ManagerGatewayApi(name = "查询系统图标库列表",realPath = "/programme/querySysIconList")
    @Override
    public InlineResponse20026 querySysIconList() {
        return adapter.querySysIconList();
    }


    /**
    * 展示方案-复制页面信息
    **/
    @ManagerGatewayApi(name = "展示方案-复制页面信息",realPath = "/programme/saveCopyOfPageInfo")
    @OperationLog(id = "20030",name = "展示方案-复制页面信息",operateObject="展示方案")
    @Override
    public InlineResponse200 saveCopyOfPageInfo( PageInfoRes body ) {
        return adapter.saveCopyOfPageInfo(body);
    }


    /**
    * 新增修改菜单信息
    **/
    @ManagerGatewayApi(name = "新增修改菜单信息",realPath = "/programme/saveMenuInfo")
    @OperationLog(id = "20044",name = "新增修改菜单信息",operateObject="菜单信息")
    @Override
    public InlineResponse20024 saveMenuInfo( MenuInfoRes body ) {
        return adapter.saveMenuInfo(body);
    }


    /**
    * 新增修改页面信息
    **/
    @ManagerGatewayApi(name = "新增修改页面信息",realPath = "/programme/savePageInfo")
    @OperationLog(id = "20045",name = "新增修改页面信息",operateObject="页面信息")
    @Override
    public InlineResponse20025 savePageInfo( PageInfoRes body ) {
        return adapter.savePageInfo(body);
    }


    /**
    * 新增修改展示方案信息
    **/
    @ManagerGatewayApi(name = "新增修改展示方案信息",realPath = "/programme/saveProgrammeInfo")
    @OperationLog(id = "20046",name = "新增修改展示方案信息",operateObject="展示方案信息")
    @Override
    public InlineResponse20020 saveProgrammeInfo( ProgrammeInfoRes body ) {
        return adapter.saveProgrammeInfo(body);
    }


    /**
    * 新增修改侧边栏信息
    **/
    @ManagerGatewayApi(name = "新增修改侧边栏信息",realPath = "/programme/saveSidebarInfo")
    @OperationLog(id = "20048",name = "新增修改侧边栏信息",operateObject="侧边栏信息")
    @Override
    public InlineResponse200 saveSidebarInfo( SidebarInfoBiz body ) {
        return adapter.saveSidebarInfo(body);
    }


    /**
    * 获取支持创建的展示方案类型，目前包含pc门户和移动h5
    **/
    @ManagerGatewayApi(name = "获取支持创建的展示方案类型，目前包含pc门户和移动h5",realPath = "/programme/support")
    @OperationLog(id = "20010",name = "获取支持创建的展示方案类型",operateObject="展示方案")
    @Override
    public InlineResponse20010 support() {
        return adapter.support();
    }


    /**
    * 修改菜单排序
    **/
    @ManagerGatewayApi(name = "修改菜单排序",realPath = "/programme/updateMenuSequence")
    @OperationLog(id = "20047",name = "修改菜单排序",operateObject="菜单排序")
    @Override
    public InlineResponse200 updateMenuSequence( List<SortInfo> body ) {
        return adapter.updateMenuSequence(body);
    }


    /**
    * 展示方案菜单启用禁用
    **/
    @ManagerGatewayApi(name = "展示方案菜单启用禁用",realPath = "/programme/updateMenuStatus")
    @OperationLog(id = "20050",name = "展示方案菜单启用禁用",operateObject="展示方案")
    @Override
    public InlineResponse200 updateMenuStatus( SidebarStatusReq body ) {
        return adapter.updateMenuStatus(body);
    }


    /**
    * 停用启用页面信息
    **/
    @ManagerGatewayApi(name = "停用启用页面信息",realPath = "/programme/updatePageStatusByWid")
    @OperationLog(id = "20042",name = "停用启用页面信息",operateObject="展示方案")
    @Override
    public InlineResponse200 updatePageStatusByWid( EnabledReq body ) {
        return adapter.updatePageStatusByWid(body);
    }


    /**
    * 侧边栏排序
    **/
    @ManagerGatewayApi(name = "侧边栏排序",realPath = "/programme/updateSidebarSequence")
    @OperationLog(id = "20028",name = "侧边栏排序",operateObject="展示方案")
    @Override
    public InlineResponse200 updateSidebarSequence( List<SidebarSequenceReqObj> body ) {
        return adapter.updateSidebarSequence(body);
    }


    /**
    * 侧边栏启用禁用
    **/
    @ManagerGatewayApi(name = "侧边栏启用禁用",realPath = "/programme/updateSidebarStatus")
    @OperationLog(id = "20025",name = "侧边栏启用禁用",operateObject="展示方案")
    @Override
    public InlineResponse200 updateSidebarStatus( SidebarStatusReq body ) {
        return adapter.updateSidebarStatus(body);
    }


    /**
    * 停用启用展示方案
    **/
    @ManagerGatewayApi(name = "停用启用展示方案",realPath = "/programme/updateStatusByWid")
    @OperationLog(id = "20041",name = "停用启用展示方案",operateObject="展示方案")
    @Override
    public InlineResponse200 updateStatusByWid( UpdateStatusReq body ) {
        return adapter.updateStatusByWid(body);
    }

    @ManagerGatewayApi(name = "保存系统弹窗",realPath = "/programme/savePopupWindow")
    @OperationLog(id = "20099",name = "保存系统弹窗",operateObject="保存系统弹窗")
    @Override
    public InlineResponse200 savePopupWindow(@Valid PopupWindowReq body) {
        return adapter.savePopupWindow(body);
    }

    @ManagerGatewayApi(name = "查询系统弹窗",realPath = "/programme/queryPopupWindow")
    @Override
    public PopupWindowRes queryPopupWindow(@Valid PopupWindowReq body) {
        return adapter.queryPopupWindow(body);
    }

    @ManagerGatewayApi(name = "不再显示系统弹窗",realPath = "/programme/updatePopupWindowStatus")
    @Override
    public InlineResponse200 updatePopupWindowStatus(@Valid PopupWindowReq body) {
        return adapter.updatePopupWindowStatus(body);
    }

    @ManagerGatewayApi(name = "门户前端查询弹窗",realPath = "/programme/queryPopupWindowDisplay")
    @Override
    public PopupWindowRes queryPopupWindowDisplay(@Valid PopupWindowReq body) {
        return adapter.queryPopupWindowDisplay(body);
    }
}
