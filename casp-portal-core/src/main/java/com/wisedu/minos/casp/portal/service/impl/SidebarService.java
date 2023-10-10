package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.CollectionUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.MenuAuthEntity;
import com.wisedu.minos.casp.portal.dao.entity.MenuEntity;
import com.wisedu.minos.casp.portal.dao.entity.SidebarEntity;
import com.wisedu.minos.casp.portal.dao.mapper.SidebarMapper;
import com.wisedu.minos.casp.portal.model.SideBarGroup;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title SidebarService
 * @Author: u
 * @Date: 2020/9/21
 */
@Service
public class SidebarService extends MyServiceImpl<SidebarMapper, SidebarEntity> {

    @Autowired
    MenuAuthService menuAuthService;

    @Autowired
    UserUtil userUtil;
    @Autowired
    CardUtil cardUtil;

    @Value("${minos.oss.server.addresses}")
    private String minosOssAddResses;

    /**
     * getSidebars
     * <p/>
     * 获取所有侧边栏
     *
     * @param showProgrammeId
     * @param authTypes
     * @return java.util.List<com.wisedu.minos.casp.portal.dao.entity.SidebarEntity>
     * @throws
     * @date 2020/9/22 18:11
     */
    public List<SidebarEntity> getSiderbars(String showProgrammeId, List<Global.AuthType> authTypes) {
        return filterIconUrl(this.baseMapper.selectList(Wrappers.<SidebarEntity>lambdaQuery()
                .eq(SidebarEntity::getProgrammeId, showProgrammeId)
                .in(SidebarEntity::getAuthType, authTypes.stream().map(Global.AuthType::getId).collect(Collectors.toList()))
                .eq(SidebarEntity::getEnabledFlag, Global.EnableStatus.ENABLE.getId())
                .eq(SidebarEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .orderByAsc(SidebarEntity::getOrderNumber)));
    }

    /**
     * getSidebarGroup
     * <p/>
     * 获取侧边栏组 分左右
     *
     * @param programmeId
     * @return com.wisedu.minos.casp.portal.service.impl.SidebarService.SideBarGroup
     * @throws
     * @date 2020/9/22 18:34
     */
    public SideBarGroup getSidebarGroup(String programmeId) {
        List<SidebarEntity> sidebars;
        UserInfo userInfo = userUtil.getCurrentUser();
        //1 查询游客可见菜单
        if (userInfo == null) {
            sidebars = this.getSiderbars(programmeId, Arrays.asList(Global.AuthType.VISITOR_VISIBLE, Global.AuthType.VISITOR_AND_LOGIN_VISIBLE));
        } else {
            //查询所有菜单
            List<SidebarEntity> siderbarsAll = this.getSiderbars(programmeId, Arrays.asList(Global.AuthType.VISITOR_VISIBLE, Global.AuthType.LOGIN_VISIBLE, Global.AuthType.VISITOR_AND_LOGIN_VISIBLE, Global.AuthType.AUTH_VISIBLE));
            //查询登陆后可见菜单
            List<SidebarEntity> loginVisibleMenus = this.getSiderbars(programmeId, Arrays.asList(Global.AuthType.LOGIN_VISIBLE, Global.AuthType.VISITOR_AND_LOGIN_VISIBLE));

            //查看所有授权后可见菜单
            List<SidebarEntity> allAuthVisibleMenus = this.getSiderbars(programmeId, Collections.singletonList(Global.AuthType.AUTH_VISIBLE));

            //查询用户拥有的菜单权限关系
            List<MenuAuthEntity> authedMenuAuths = menuAuthService.getAuthedMenus(Global.MenuMenuAuthType.SIDEBAR);

            //查看当前登陆人有权限的菜单
            List<SidebarEntity> authedMenus = allAuthVisibleMenus.stream()
                    .filter(menuEntity -> filterAuthSidebars(menuEntity,authedMenuAuths)).collect(Collectors.toList());

            //合并登陆后 和 授权后可见菜单
            List<SidebarEntity> loginSidebars = new ArrayList<>(loginVisibleMenus);
            loginSidebars.addAll(authedMenus);
            //之所以不直接返回loginSidebars，是因为loginSidebars是无序的，还需过滤处理一下
            sidebars = siderbarsAll.stream().filter(
                    (sidebarEntity) ->loginSidebars.stream().map(SidebarEntity::getWid).collect(Collectors.toList()).contains(sidebarEntity.getWid())
            ).collect(Collectors.toList());

        }
        //限制最大20个
        List<SidebarEntity> leftSidebarList = sidebars.stream()
                .filter(sidebarEntity -> Global.SidebarPositionType.LEFT.getId() == sidebarEntity.getPositionType()).limit(20).collect(Collectors.toList());
        List<SidebarEntity> rightSidebarList = sidebars.stream()
                .filter(sidebarEntity -> Global.SidebarPositionType.RIGHT.getId() == sidebarEntity.getPositionType()).limit(20).collect(Collectors.toList());
        return new SideBarGroup().setLeftSidebarList(leftSidebarList).setRightSidebarList(rightSidebarList);
    }

    /***
     * @Author jcx
     * @Description 判断菜单iconurl http是否和门户保持一致
     * @Date 14:48 2022/1/19
     * @Param menuEntities:
     * @return List<MenuEntity>
     **/
    public List<SidebarEntity> filterIconUrl(List<SidebarEntity> menuEntities){
        if(! CollectionUtils.isEmpty(menuEntities)){
            menuEntities.forEach(menu->{
                filterMenu(menu);
            });
        }
        return menuEntities;
    }

    public SidebarEntity filterMenu(SidebarEntity menu){
        if(null!=menu){
            if( StringUtil.isNotEmpty(menu.getIconUrl())&&menu.getIconUrl().startsWith("http")){
                menu.setIconUrl(cardUtil.filterHttpOrHttps(menu.getIconUrl()));
            }
        }
        return menu;
    }

    /**
     * filterAuthSidebars
     * <p/>
     * 过滤授权的侧边栏
     *
     * @param sidebarEntity
     * @param authedMenuAuths
     * @return boolean
     * @throws
     * @date 2020-10-3 16:06
     */
    private boolean filterAuthSidebars(SidebarEntity sidebarEntity,List<MenuAuthEntity> authedMenuAuths) {
        return authedMenuAuths.stream()
                .map(MenuAuthEntity::getMenuWid).collect(Collectors.toList()).contains(sidebarEntity.getWid());
    }

}
