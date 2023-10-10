package com.wisedu.minos.casp.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.exception.PageNotFoundException;
import com.wisedu.minos.casp.portal.common.utils.ObjectUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.MenuAuthEntity;
import com.wisedu.minos.casp.portal.dao.entity.MenuEntity;
import com.wisedu.minos.casp.portal.dao.mapper.MenuMapper;
import com.wisedu.minos.casp.portal.model.MenuBiz;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MenuService
 * <p/>
 * 顶部菜单服务
 *
 * @author hyluan
 * @date 2020/9/21 20:04
 * Copyright (c) 2018 wisedu
 */
@Service
public class MenuService extends MyServiceImpl<MenuMapper, MenuEntity> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    MenuAuthService menuAuthService;

    @Autowired
    UserUtil userUtil;
    @Autowired
    CardUtil cardUtil;
    @Value("${minos.oss.server.addresses}")
    private String minosOssAddResses;

    /**
     * @return void
     * @Author jdwan
     * @Description 获取菜单树第一个可用的菜单
     * @Date 16:12 2020/9/27
     * @Param response:
     * @Param list: 数据
     **/
    public MenuBiz findFirstMatchMenu(List<MenuBiz> menuList) {
        MenuBiz menuBiz = null;
        for (MenuBiz menu : menuList) {
            if (Global.MenuType.INNER_LINK == Global.MenuType.getById(menu.getMenuType()) && StringUtil.isNotEmpty(menu.getPageId())) {
                menuBiz = menu;
                break;
            }
        }
        if (menuBiz == null) {
            for (MenuBiz biz : menuList) {
                if (!CollectionUtils.isEmpty(biz.getMenu())) {
                    return findFirstMatchMenu(biz.getMenu());
                }
            }
        }
        return menuBiz;
    }

    /**
     * findMenu
     * <p/>
     * 从菜单树中查找对应菜单
     *
     * @param menuList
     * @param menuWid
     * @return com.wisedu.minos.casp.portal.model.MenuBiz
     * @throws
     * @date 2020/11/3 12:17
     */
    public MenuBiz findMenu(List<MenuBiz> menuList, String menuWid) {
        MenuBiz menuBiz = null;
        for (MenuBiz menu : menuList) {
            if (menu.getWid().equals(menuWid)) {
                menuBiz = menu;
                break;
            }
        }
//        if (menuBiz == null) {
//            for (MenuBiz biz : menuList) {
//                if (!CollectionUtils.isEmpty(biz.getMenu())) {
//                    return findFirstMatchMenu(biz.getMenu());
//                }
//            }
//        }
        return menuBiz;
    }
    /***
     * @Author jcx
     * @Description 菜单树转list
     * @Date 14:54 2021/1/13
     * @Param list:
     * @return List<MenuBiz>
     **/
    public List<MenuBiz> getMenuBizList(List<MenuBiz> list) {
        List<MenuBiz> treeVosFir = new ArrayList<>();
        List<MenuBiz> treeVosSec = new ArrayList<>();
        String temp;
        for (MenuBiz menuBiz : list) {
            if ( org.apache.commons.collections.CollectionUtils.isNotEmpty(menuBiz.getMenu())) {
                treeVosFir = getMenuBizList(menuBiz.getMenu());
                temp = JSON.toJSONString(menuBiz).substring(JSON.toJSONString(menuBiz).lastIndexOf("],") + 2);
                temp = "{".concat(temp);
                MenuBiz menuBizFir = JSON.parseObject(temp, MenuBiz.class);
                treeVosFir.add(menuBizFir);
            } else {
                treeVosFir.add(menuBiz);
            }
            for (MenuBiz treeVoFir : treeVosFir) {
                int count = 0;
                for (MenuBiz treeVoSec : treeVosSec)
                    if (!treeVoSec.getWid().equals(treeVoFir.getWid())) {
                        count++;
                    }
                if (count == treeVosSec.size()) {
                    treeVosSec.add(treeVoFir);
                }
            }
        }
        return treeVosSec;
    }

    /**
     * findRootMenu
     * <p/>
     * 从菜单树中查找给定菜单的根菜单
     *
     * @param menuList
     * @param menuWid
     * @return com.wisedu.minos.casp.portal.model.MenuBiz
     * @throws
     * @date 2020/11/3 12:18
     */
    public MenuBiz findRootMenu(List<MenuBiz> menuList, String menuWid) {
        MenuBiz menu = findMenu(getMenuBizList(menuList), menuWid);
        if (menu == null) {
            //当前菜单在菜单树找不到，由于未登录引起
            throw new NoLoginException(String.format("访问菜单：%s 为登陆后菜单", menuWid));
        }
        if (Global.ROOT_MENU_ID.equals(menu.getParentId())) {
            return menu;
        }
        return findRootMenu(menuList, menu.getParentId());
    }

    /**
     * getAuthedMenus
     * <p/>
     * 获取所有菜单
     *
     * @param showProgrammeId 展示方案id
     * @param authTypes       授权类型列表
     * @return java.util.List<com.wisedu.minos.casp.portal.dao.entity.MenuEntity>
     * @throws
     * @date 2020/9/21 20:07
     */
    public List<MenuEntity> getMenus(String showProgrammeId, List<Global.AuthType> authTypes) {
        return filterIconUrl(this.baseMapper.selectList(Wrappers.<MenuEntity>lambdaQuery()
                .eq(MenuEntity::getProgrammeId, showProgrammeId)
                .in(MenuEntity::getAuthType, authTypes.stream().map(Global.AuthType::getId).collect(Collectors.toList()))
                .eq(MenuEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .eq(MenuEntity::getIsEnabled, Global.Status.DISABLE.getId()).orderByAsc(MenuEntity::getOrderNumber)));
    }

    /***
     * @Author jcx
     * @Description 判断菜单iconurl http是否和门户保持一致
     * @Date 14:48 2022/1/19
     * @Param menuEntities:
     * @return List<MenuEntity>
     **/
    public List<MenuEntity> filterIconUrl(List<MenuEntity> menuEntities){
        if(!CollectionUtils.isEmpty(menuEntities)){
            menuEntities.forEach(menu->{
                filterMenu(menu);
            });
        }
        return menuEntities;
    }

    public MenuEntity filterMenu(MenuEntity menu){
        if(null!=menu){
            if(StringUtil.isNotEmpty(menu.getIconUrl())&&menu.getIconUrl().startsWith("http")){
                menu.setIconUrl(cardUtil.filterHttpOrHttps(menu.getIconUrl()));
            }
        }
        return menu;
    }

    /**
     * getTreeMenu
     * <p/>
     * 获取树形菜单 根据菜单权限判断
     *
     * @param showProgrammeId
     * @return java.util.List<com.wisedu.minos.casp.portal.model.MenuBiz>
     * @throws
     * @date 2020/9/29 18:34
     */
    public List<MenuBiz> getTreeMenu(String showProgrammeId) {
        UserInfo currentUser = userUtil.getCurrentUser();
        List<MenuEntity> allMenus;
        //1 查询游客可见菜单
        if (currentUser == null) {
            allMenus = this.getMenus(showProgrammeId, Arrays.asList(Global.AuthType.VISITOR_VISIBLE, Global.AuthType.VISITOR_AND_LOGIN_VISIBLE));
            if(org.apache.commons.collections.CollectionUtils.isEmpty(allMenus)){
                throw new NoLoginException("当前用户未登陆");
            }
        } else {
            LOGGER.debug("当前用户:{}，获取菜单", currentUser.getWid());
            //查询登陆后可见菜单
            List<MenuEntity> loginVisibleMenus = this.getMenus(showProgrammeId, Arrays.asList(Global.AuthType.LOGIN_VISIBLE, Global.AuthType.VISITOR_AND_LOGIN_VISIBLE));

            //查看所有授权后可见菜单
            List<MenuEntity> allAuthVisibleMenus = this.getMenus(showProgrammeId, Collections.singletonList(Global.AuthType.AUTH_VISIBLE));

            //查询用户拥有的菜单权限关系
            List<MenuAuthEntity> authedMenuAuths = menuAuthService.getAuthedMenus(Global.MenuMenuAuthType.TOP_MENU);

            //查看当前登陆人有权限的菜单
            List<MenuEntity> authedMenus = allAuthVisibleMenus.stream()
                    .filter(menuEntity -> filterAuthMenus(menuEntity, authedMenuAuths)).collect(Collectors.toList());

            //合并登陆后 和 授权后可见菜单
            allMenus = new ArrayList<>(loginVisibleMenus);
            allMenus.addAll(authedMenus);
            allMenus.sort(Comparator.comparingLong(MenuEntity::getOrderNumber));
        }
        if (allMenus.isEmpty()) {
            throw new PageNotFoundException(String.format("当前为%s状态，对应菜单为空，请检查配置", currentUser == null ? "游客" : "登录"));
        }
        //构建为树形菜单
        return tranToTree(allMenus);
    }
//    public List<MenuBiz> getTreeMenu(String showProgrammeId) {
//        List<MenuEntity> allMenus=this.list(new QueryWrapper<MenuEntity>().lambda().eq(MenuEntity::getProgrammeId, showProgrammeId)
//                .eq(MenuEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
//                .eq(MenuEntity::getIsEnabled, Global.Status.DISABLE.getId()).orderByAsc(MenuEntity::getOrderNumber))
//
//    }
    private List<MenuBiz> tranToTree(List<MenuEntity> allMenus) {
        List<MenuBiz> treeMenus = allMenus.stream()
                .filter(menuEntity -> menuEntity.getParentId() == null || Global.ROOT_MENU_ID.equals(menuEntity.getParentId()))
                .map(menu -> ObjectUtil.copyProperties(menu, new MenuBiz())).collect(Collectors.toList());
        findChildren(treeMenus, allMenus);
        return treeMenus;
    }

    private boolean filterAuthMenus(MenuEntity menuEntity, List<MenuAuthEntity> authedMenus) {
        return authedMenus.stream()
                .map(MenuAuthEntity::getMenuWid).collect(Collectors.toList()).contains(menuEntity.getWid());
    }

    private void findChildren(List<MenuBiz> treeMenus, List<MenuEntity> menus) {
        for (MenuBiz menuBiz : treeMenus) {
            List<MenuBiz> children = new ArrayList<>();
            for (MenuEntity dept : menus) {
                if (menuBiz.getWid() != null && menuBiz.getWid().equals(dept.getParentId())) {
                    children.add(ObjectUtil.copyProperties(dept, new MenuBiz()));
                }
            }
            menuBiz.setMenu(children);
            findChildren(children, menus);
        }
    }

    /**
     * getMenu
     * <p/>
     * 获取菜单
     *
     * @param showProgrammeId
     * @param pageId
     * @return com.wisedu.minos.casp.portal.dao.entity.MenuEntity
     * @throws
     * @date 2020/10/14 11:05
     */
    public MenuEntity getMenu(String showProgrammeId, String pageId) {
        return filterMenu(getOne(Wrappers.<MenuEntity>lambdaQuery().eq(MenuEntity::getPageId, pageId)
                .eq(MenuEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .eq(MenuEntity::getIsEnabled, Global.EnableStatus.ENABLE.getId())
                .eq(MenuEntity::getProgrammeId, showProgrammeId)));
    }

}
