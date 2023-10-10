package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.exception.PageCodeNotInShowProgramException;
import com.wisedu.minos.casp.portal.common.exception.PageException;
import com.wisedu.minos.casp.portal.common.exception.PageNotFoundException;
import com.wisedu.minos.casp.portal.common.utils.ObjectUtil;
import com.wisedu.minos.casp.portal.common.utils.RequestUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.MenuEntity;
import com.wisedu.minos.casp.portal.dao.entity.PageInfoEntity;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.mapper.PageInfoMapper;
import com.wisedu.minos.casp.portal.model.MenuBiz;
import com.wisedu.minos.casp.portal.model.PageContext;
import com.wisedu.minos.casp.portal.model.v353beta2.PortalSelectMenusReq;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title PageInfoService
 * @Author: hlchen02
 * @Date: 2020/9/23
 */
@Service
public class PageInfoService extends MyServiceImpl<PageInfoMapper, PageInfoEntity> {

    @Autowired
    private ShowProgrammeService showProgrammeService;


    @Autowired
    private MenuService menuService;
    @Autowired
    private CardUtil cardUtil;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private SelectMenuService selectMenuService;

    /**
     * 功能描述：根据pageCode获取页面信息
     *
     * @title getByPageCode
     * @Author: lhy
     * @Date: 2020/9/27
     */
    public PageInfoEntity getByPageCode(String pageCode, String programmeId) {
        PageInfoEntity pageInfoEntity = this.getOne(Wrappers.<PageInfoEntity>lambdaQuery()
                .eq(PageInfoEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .eq(PageInfoEntity::getEnabledFlag, Global.EnableStatus.ENABLE.getId())
                .eq(PageInfoEntity::getProgrammeId, programmeId)
                .eq(PageInfoEntity::getPageCode, pageCode));
        //如果查询不到则是单独页面，直接根据pageCode查询
        if (null == pageInfoEntity) {
//            List<PageInfoEntity> list = this.list(Wrappers.<PageInfoEntity>lambdaQuery()
//                    .eq(PageInfoEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
//                    .eq(PageInfoEntity::getEnabledFlag, Global.EnableStatus.ENABLE.getId())
//                    .eq(PageInfoEntity::getPageCode, pageCode));
//            if (!list.isEmpty()) {
//                pageInfoEntity = list.get(0);
//            } else {
                throw new PageNotFoundException(String.format("未匹配到页面，pageCode:%s, programmeId:%s", pageCode, programmeId));
//            }
        }
        return pageInfoEntity;
    }
    /**
     * 功能描述：根据pageCode获取页面信息
     *
     * @title getByPageCode
     * @Author: lhy
     * @Date: 2020/9/27
     */
    public List<PageInfoEntity> getByPageCode(String pageCode) {
        List<PageInfoEntity> list = this.list(Wrappers.<PageInfoEntity>lambdaQuery()
                .eq(PageInfoEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .eq(PageInfoEntity::getEnabledFlag, Global.EnableStatus.ENABLE.getId())
                .eq(PageInfoEntity::getPageCode, pageCode));
        if (list.isEmpty()) {
            throw new PageNotFoundException(String.format("未匹配到页面，pageCode:%s", pageCode));
        }
        return list;
    }
    /**
     * getAllParentPage
     * <p/>
     * 获取所有父页面 从一级页面开始到本页面
     *
     * @param pageId
     * @param
     * @return java.util.List<com.wisedu.minos.casp.portal.dao.entity.PageInfoEntity>
     * @throws
     * @date 2020-10-4 20:43
     */
    public List<PageInfoEntity> getAllParentPage(String pageId) {
        List<PageInfoEntity> pages = new LinkedList<>();
        PageInfoEntity pageInfoEntity = this.getById(pageId);
        pages.add(pageInfoEntity);
        getParentPage(pageInfoEntity.getParentId(), pages);
        return pages;
    }

    /**
     * getParentPage
     * <p/>
     * 查询父页面
     *
     * @param parentId
     * @param pages
     * @return void
     * @throws
     * @date 2020-10-4 20:50
     */
    private void getParentPage(String parentId, List<PageInfoEntity> pages) {
        if (Global.ROOT_MENU_ID.equals(parentId)) {
            return;
        }
        PageInfoEntity parent = this.getOne(Wrappers.<PageInfoEntity>lambdaQuery()
                .eq(PageInfoEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .eq(PageInfoEntity::getWid, parentId));
        if (parent != null) {
            pages.add(0, parent);
            getParentPage(parent.getParentId(), pages);
        }
    }

    public PageContext getPageContext(HttpServletRequest request) {
        return getPageContext(request, null,null);
    }

    /**
     * getPageContext
     * <p/>
     * 获取页面信息 包含页面 菜单
     *
     * @param request
     * @return com.wisedu.minos.casp.portal.service.impl.PageInfoService.PageContext
     * @throws
     * @date 2020/10/10 15:55
     */
    public PageContext getPageContext(HttpServletRequest request, String pageCode,String siteWid) {
        // del by zjchen   why?
        /*if (StringUtil.isEmpty(pageCode)) {
            pageCode = request.getRequestURI().replaceAll(Global.PORTAL_URI + "/", "").replaceAll(Global.PORTAL_URI, "");
        }*/
        Global.PlatformType platformType = RequestUtil.getPlatform(request);
        //获取当前展示方案
        //todo 展示方案根站点获取
        ShowProgrammeEntity defaultProgramme = showProgrammeService.getDefaultProgramme(platformType.getType(),siteWid);
        defaultProgramme.setTemplateConfig(cardUtil.filterHttpOrHttps(defaultProgramme.getTemplateConfig()));
        String programmeId = defaultProgramme.getWid();

        //获取当前菜单项
        List<MenuBiz> menus = menuService.getTreeMenu(programmeId);
        PageInfoEntity pageInfo;
        MenuBiz lv1Menu, currentMenu;
        //根据目录查询根目录的wid
        if (StringUtils.isEmpty(pageCode)) {
            //如果pageCode为空则查询匹配到的第一个菜单信息 可能是子菜单
            currentMenu = menuService.findFirstMatchMenu(menus);
            if (currentMenu == null) {
                throw new NoLoginException("至少需要一个内部菜单页面,请检查配置,跳转登录页");
            }
            pageInfo = this.getById(currentMenu.getPageId());
            if( StringUtil.isNotEmpty(pageInfo.getPageConfig()) ){
                pageInfo.setPageConfig(cardUtil.filterHttpOrHttps(pageInfo.getPageConfig()));
            }
            lv1Menu = findLv1Menu(menus, pageInfo.getWid());
        } else {
            //根据目录pageCode查询页面信息
//            pageInfo = this.getByPageCode(pageCode, programmeId);
            List<PageInfoEntity> pages = getByPageCode(pageCode);
            pageInfo = pages.stream().filter(e -> e.getProgrammeId().
                    equals(programmeId)).findAny().orElseThrow(PageCodeNotInShowProgramException::new);
            if( StringUtil.isNotEmpty(pageInfo.getPageConfig()) ) {
                pageInfo.setPageConfig(cardUtil.filterHttpOrHttps(pageInfo.getPageConfig()));
            }
            //获取当前菜单
            MenuEntity menuEntity = menuService.getMenu(programmeId, pageInfo.getWid());
            if (menuEntity == null) {
                //单独页面没挂菜单
                currentMenu = null;
                lv1Menu = null;
            } else {
                currentMenu = ObjectUtil.copyProperties(menuService.filterMenu(menuEntity), new MenuBiz());
                //获取当前菜单对应的1级菜单 TODO 可以优化 从查询树形菜单中获取
//                MenuEntity parentMenu = menuService.getParentMenu(menuEntity);
//                lv1Menu = ObjectUtil.copyProperties(parentMenu, new MenuBiz());
                lv1Menu = menuService.findRootMenu(menus,currentMenu.getWid());
            }
        }
        PortalSelectMenusReq req = new PortalSelectMenusReq();
        req.setLang(null!=userUtil.getCurrentUser()?userUtil.getCurrentUser().getPreferredLanguage():Global.DEFAULT_LANGUAGE);
        req.setProgrammeWid(defaultProgramme.getWid());
        return new PageContext().setShowProgrammeEntity(defaultProgramme)
                .setCurrentLv1Menu(lv1Menu).setCurrentMenu(currentMenu).setMenuTree(menus).setPageInfoEntity(pageInfo).setPortalSelectMenus(selectMenuService.queryPortalSelectMenus(req,request));
    }

    /**
     * findLv1Menu
     * <p/>
     * 获取当前激活的lv1菜单 如果是当前页面对应的是子菜单需要找到对应父菜单
     *
     * @param menus
     * @param pageId
     * @return com.wisedu.minos.casp.portal.model.MenuBiz
     * @throws
     * @date 2020/10/14 13:44
     */
    private MenuBiz findLv1Menu(List<MenuBiz> menus, String pageId) {
        return menus.stream().filter(menuBiz -> pageId.equals(menuBiz.getPageId()))
                .findFirst().orElseThrow(() -> new PageException("未匹配到一级菜单"));
    }


}
