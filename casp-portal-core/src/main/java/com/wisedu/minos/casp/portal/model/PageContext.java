package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.dao.entity.PageInfoEntity;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.entity.TemplateEntity;
import lombok.Data;

import java.util.List;

/**
 * PageContext
 * <p/>
 * 当前页面信息容器 
 * @author hyluan
 * @date 2020/10/14 12:54
 * Copyright (c) 2018 wisedu
 */
@Data
public class PageContext {
    /**
     * 展示方案信息
     */
    ShowProgrammeEntity showProgrammeEntity;
    /**
     * 页面菜单信息
     */
    PageInfoEntity pageInfoEntity;
    /**
     * 当前页面对应菜单
     */
    MenuBiz currentMenu;
    /**
     * 当前页面顶级菜单
     */
    MenuBiz currentLv1Menu;
    /**
     * 页面菜单树
     */
    List<MenuBiz> menuTree;

    TemplateEntity templateEntity;

}
