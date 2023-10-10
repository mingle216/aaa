package com.wisedu.minos.casp.portal.spi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wisedu.minos.casp.portal.dao.entity.*;
import com.wisedu.minos.casp.portal.model.MenuBiz;
import com.wisedu.minos.casp.portal.model.SideBarGroup;
import com.wisedu.minos.casp.portal.model.v353beta2.PortalSelectMenusRes;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * TemplateContext
 * <p/>
 * 模板插件上下文
 *
 * @author hyluan
 * @date 2020/9/21 19:55
 * Copyright (c) 2018 wisedu
 */
@NoArgsConstructor
public class TemplateContext{

    /**
     * 当前激活的菜单id
     */
    String activeMenuId;

    PageInfoEntity pageInfoEntity;

    ShowProgrammeEntity showProgrammeEntity;

    TemplateEntity templateEntity;

    List<MenuBiz> treeMenu;

    SideBarGroup sideBarGroup;
    /**
     * 查询历史
     */
    List<ServiceSearchHisEntity> serviceSearchHis;

    /**
     * 下拉菜单
     */
    List<PortalSelectMenusRes> portalSelectMenus;

    public TemplateContext(String activeMenuId, PageInfoEntity pageInfoEntity, ShowProgrammeEntity showProgrammeEntity,
                           TemplateEntity templateEntity, List<MenuBiz> treeMenu, SideBarGroup sideBarGroup, List<ServiceSearchHisEntity> serviceSearchHis,List<PortalSelectMenusRes> portalSelectMenus) {
        this.activeMenuId = activeMenuId;
        this.pageInfoEntity = pageInfoEntity;
        this.showProgrammeEntity = showProgrammeEntity;
        this.templateEntity = templateEntity;
        this.treeMenu = treeMenu;
        this.sideBarGroup = sideBarGroup;
        this.serviceSearchHis = serviceSearchHis;
        this.portalSelectMenus = portalSelectMenus;
    }

    public PageInfoEntity getPageInfoEntity() {
        return pageInfoEntity;
    }

    public ShowProgrammeEntity getShowProgrammeEntity() {
        return showProgrammeEntity;
    }

    public TemplateEntity getTemplateEntity() {
        return templateEntity;
    }

    public List<MenuBiz> getTreeMenu() {
        return treeMenu;
    }

    public SideBarGroup getSideBarGroup() {
        return sideBarGroup;
    }

    public List<ServiceSearchHisEntity> getServiceSearchHis() {
        return serviceSearchHis;
    }

    public void setServiceSearchHis(List<ServiceSearchHisEntity> serviceSearchHis) {
        this.serviceSearchHis = serviceSearchHis;
    }

    public String getActiveMenuId() {
        return activeMenuId;
    }

    public void setActiveMenuId(String activeMenuId) {
        this.activeMenuId = activeMenuId;
    }

    public List<PortalSelectMenusRes> getPortalSelectMenus () {
        return portalSelectMenus;
    }

    public void setPortalSelectMenus (List<PortalSelectMenusRes> portalSelectMenus) {
        this.portalSelectMenus = portalSelectMenus;
    }
}
