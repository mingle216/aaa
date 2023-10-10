package com.wisedu.minos.casp.portal.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.dao.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 3.5.3 性能优化调整
 * 本次增加  展示方案删除时，关联的页面和顶头菜单，右上角菜单，侧边栏，都要删除
 * @date 2022/11/4 13:43
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Service
public class ProgramApiServicePlus {
    @Autowired
    private SidebarService sidebarService;
    @Autowired
    private ShowProgrammeService showProgrammeService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private PageInfoService pageInfoService;
    @Autowired
    private SelectMenuService selectMenuService;
    @Autowired
    private InternationalizationService internationalizationService;

    /***
     * 根据展示方案id 删除展示方案下面，关联的页面和顶头菜单，右上角菜单，侧边栏
     * @param programIds
     * @return void
     * @author jszhang
     * @date 2022/11/4 14:56
     */
    @Transactional(rollbackFor = Exception.class)

    public void deleteProgramRelatedPageAndMenuAndSideBar(List<String> programIds) {
        List<List<String>> programIdsList = CommonUtil.getListArray(programIds);
        Date now = new Date();
        List<String> deleteLangKey=new ArrayList<>();
        programIdsList.forEach(e -> {
            //页面信息
            List<String> pageLangKey = setPageInfoDeleteAndReturnLangKey(now, e);
            deleteLangKey.addAll(pageLangKey);

            //侧边栏
            List<String> sideBarLangKey = setSideBarDeletedAndReturnLangKeys(now, e);
            deleteLangKey.addAll(sideBarLangKey);
            // 菜单
            List<String> menuLangKey = setMenuDeletedAndReturnLangKeys(now, e);
            deleteLangKey.addAll(menuLangKey);
            //菜单2
            List<String> selectMenuLangKey = setSelectMenuDeletedAndReturnLangKeys(now, e);
            deleteLangKey.addAll(selectMenuLangKey);
        });
        if(CollectionUtil.isNotEmpty(deleteLangKey)) {
            List<List<String>> deleteLangKeyList = CommonUtil.getListArray(deleteLangKey);
            deleteLangKeyList.forEach(e -> {
                internationalizationService.update(new UpdateWrapper<InternationalizationEntity>().lambda()
                        .set(InternationalizationEntity::getIsDeleted, Global.DeleteStatus.YES.getId())
                        .set(InternationalizationEntity::getUpdateTime,now)
                        .in(InternationalizationEntity::getLangKey, e));

            });
        }
    }
    /***
     * 将下拉菜单置为删除，并且返回langkey
     * @param now
     * @param e
     * @return void
     * @author jszhang
     * @date 2022/11/4 14:40
     */
    private List<String> setSelectMenuDeletedAndReturnLangKeys(Date now, List<String> e) {
        List<SelectMenuEntity> selectMenuEntityList = selectMenuService.list(new QueryWrapper<SelectMenuEntity>().lambda()
                .eq(SelectMenuEntity::getIsDeleted, Global.NO)
                .in(SelectMenuEntity::getProgrammeId, e));
        if( CollectionUtils.isNotEmpty(selectMenuEntityList) ){
            selectMenuEntityList.forEach(k->{
                k.setIsDeleted(Global.DeleteStatus.YES.getId());
                selectMenuService.update(k,new QueryWrapper<SelectMenuEntity>().lambda().eq(SelectMenuEntity::getProgrammeId,k.getProgrammeId()).
                        eq(SelectMenuEntity::getWid,k.getWid()));
            });
        }

        List<String> collect = selectMenuEntityList.stream().map(SelectMenuEntity::getMenuNameLangKey).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return collect;
    }

    /***
     * 将菜单置为删除，并且返回多语言信息方便后续删除
     * @param now 更新时间
     * @param e  展示方案id
     * @return void
     * @author jszhang
     * @date 2022/11/4 14:33
     */
    private List<String> setMenuDeletedAndReturnLangKeys(Date now, List<String> e) {
        List<MenuEntity> menuEntityList = menuService.list(new QueryWrapper<MenuEntity>().lambda()
                .in(MenuEntity::getProgrammeId, e)
                .eq(MenuEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));

        List<String> wids = menuEntityList.stream().map(MenuEntity::getWid).collect(Collectors.toList());
        List<List<String>> widsList = CommonUtil.getListArray(wids);
        widsList.forEach(wid->{
            UpdateWrapper<MenuEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda()
                    .set(MenuEntity::getIsDeleted,Global.DeleteStatus.YES.getId())
                    .set(MenuEntity::getUpdateTime,now)
                    .in(MenuEntity::getWid, wid);
            menuService.update(updateWrapper);
        });
        List<String> collect = menuEntityList.stream().map(MenuEntity::getMenuNameLangKey).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return collect;
    }

    /***
     * 将侧边栏置为删除，并且返回多语言信息方便后续删除
     * @param now 更新时间
     * @param e  展示方案id
     * @return void
     * @author jszhang
     * @date 2022/11/4 14:33
     */
    private List<String> setSideBarDeletedAndReturnLangKeys(Date now, List<String> e) {
        List<SidebarEntity> sidebarEntityList = sidebarService.list(new QueryWrapper<SidebarEntity>().lambda()
                .eq(SidebarEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .in(SidebarEntity::getProgrammeId, e));
        List<String> wids = sidebarEntityList.stream().map(SidebarEntity::getWid).collect(Collectors.toList());
        List<List<String>> widsList = CommonUtil.getListArray(wids);
        widsList.forEach(wid->{
            UpdateWrapper<SidebarEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda()
                    .set(SidebarEntity::getIsDeleted,Global.DeleteStatus.YES.getId())
                    .set(SidebarEntity::getUpdateTime,now)
                    .in(SidebarEntity::getWid, wid);
            sidebarService.update(updateWrapper);
        });
        List<String> collect = sidebarEntityList.stream().map(SidebarEntity::getDisplayNameLangKey).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return collect;
    }

    /***
     * 将页面信息置为删除，并且返回多语言信息方便后续删除
     * @param  e 展示方案id
     * @param  now  更新时间
     * @return java.util.List<java.lang.String>
     * @author jszhang
     * @date 2022/11/4 14:27
     */
    public List<String> setPageInfoDeleteAndReturnLangKey(Date now, List<String> e) {
        List<PageInfoEntity> pageInfoEntityList = pageInfoService.list(new QueryWrapper<PageInfoEntity>().lambda()
                .in(PageInfoEntity::getProgrammeId, e)
                .eq(PageInfoEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));

        List<String> wids = pageInfoEntityList.stream().map(PageInfoEntity::getWid).collect(Collectors.toList());
        List<List<String>> widsList = CommonUtil.getListArray(wids);
        widsList.forEach(wid->{
            UpdateWrapper<PageInfoEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda()
                    .set(PageInfoEntity::getIsDeleted,Global.DeleteStatus.YES.getId())
                    .set(PageInfoEntity::getUpdateTime,now)
                    .in(PageInfoEntity::getWid, wid);
            pageInfoService.update(updateWrapper);
        });
        List<String> collect = pageInfoEntityList.stream().map(PageInfoEntity::getPageTitleLangKey).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return collect;
    }
}
