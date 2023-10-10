package com.wisedu.minos.casp.portal.service;

import com.wisedu.minos.casp.portal.model.*;

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


public interface ProgrammeApiAdapter {


    /**
    * 复制展示方案
    **/
    public InlineResponse200 copyProgramme(CopyProgrammeReq body);


    /**
    * 展示方案--删除菜单
    **/
    public InlineResponse20011 deleteMenu(List<String> body);


    /**
    * 展示方案--删除页面
    **/
    public InlineResponse20012 deletePageInfo(List<String> body);


    /**
    * 删除展示方案,支持批量删除应用
    **/
    public InlineResponse20019 deleteProgramme(List<String> body);


    /**
    * 根据主键删除侧边栏
    **/
    public InlineResponse20016 deleteSidebar(List<String> body);


    /**
    * 根据展示方案id获取基本信息
    **/
    public InlineResponse20020 getBasicInfo(String code);


    /**
    * 根据卡片运行时ID查询卡片配置信息
    **/
    public InlineResponse20023 getCardConfig(String code);


    /**
    * 根据页面wid查询页面信息
    **/
    public InlineResponse20022 getPageInfo(String wid);


    /**
    * 获取侧边栏详情
    **/
    public InlineResponse20015 getSidebarInfo(String wid);


    /**
    * 展示方案--预览页面
    **/
    public InlineResponse20013 previewPage(PreviewPageReq body);


    /**
    * 根据展示方案id查询菜单信息
    **/
    public InlineResponse20021 queryMenuList(MenuListRes body);


    /**
    * 展示方案-根据展示方案id查询页面列表
    **/
    public InlineResponse20017 queryPageList(ProgrammeReq body);


    /**
    * 查询展示方案列表
    **/
    public InlineResponse20018 queryProgrammeList(ProgrammeListReq body);


    /**
    * 展示方案--侧边栏列表
    **/
    public InlineResponse20014 querySidebarList(SidebarListReq body);


    /**
    * 查询系统图标库列表
    **/
    public InlineResponse20026 querySysIconList();


    /**
    * 展示方案-复制页面信息
    **/
    public InlineResponse200 saveCopyOfPageInfo(PageInfoRes body);


    /**
    * 新增修改菜单信息
    **/
    public InlineResponse20024 saveMenuInfo(MenuInfoRes body);


    /**
    * 新增修改页面信息
    **/
    public InlineResponse20025 savePageInfo(PageInfoRes body);


    /**
    * 新增修改展示方案信息
    **/
    public InlineResponse20020 saveProgrammeInfo(ProgrammeInfoRes body);


    /**
    * 新增修改侧边栏信息
    **/
    public InlineResponse200 saveSidebarInfo(SidebarInfoBiz body);


    /**
    * 获取支持创建的展示方案类型，目前包含pc门户和移动h5
    **/
    public InlineResponse20010 support();


    /**
    * 修改菜单排序
    **/
    public InlineResponse200 updateMenuSequence(List<SortInfo> body);


    /**
    * 展示方案菜单启用禁用
    **/
    public InlineResponse200 updateMenuStatus(SidebarStatusReq body);


    /**
    * 停用启用页面信息
    **/
    public InlineResponse200 updatePageStatusByWid(EnabledReq body);


    /**
    * 侧边栏排序
    **/
    public InlineResponse200 updateSidebarSequence(List<SidebarSequenceReqObj> body);


    /**
    * 侧边栏启用禁用
    **/
    public InlineResponse200 updateSidebarStatus(SidebarStatusReq body);


    /**
    * 停用启用展示方案
    **/
    public InlineResponse200 updateStatusByWid(UpdateStatusReq body);

    /**
     * 保存系统弹窗
     **/
    public InlineResponse200 savePopupWindow(PopupWindowReq body);

    /**
     * 查询系统弹窗
     **/
    public PopupWindowRes queryPopupWindow(PopupWindowReq body);

    /**
     * 不再显示系统弹窗
     **/
    public InlineResponse200 updatePopupWindowStatus(PopupWindowReq body);

    /**
     * 前端查询系统弹窗
     **/
    public PopupWindowRes queryPopupWindowDisplay(PopupWindowReq body);

}
