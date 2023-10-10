package com.wisedu.minos.casp.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.wisedu.minos.cache.server.MinosCacheTemplate;
import com.wisedu.minos.casp.portal.PortalManagerProperties;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.utils.*;
import com.wisedu.minos.casp.portal.dao.entity.*;
import com.wisedu.minos.casp.portal.dao.mapper.*;
import com.wisedu.minos.casp.portal.i18n.I18nService;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.service.ProgrammeApiAdapter;
import com.wisedu.minos.casp.portal.spi.itf.ITemplate;
import com.wisedu.minos.casp.portal.utils.*;
import com.wisedu.minos.util.MinosCommonUtil;
import com.wisedu.minos.util.MinosException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.wisedu.minos.casp.portal.common.constant.Global.POP_WINDOW_PREFIX;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ProgrammeApiAdapterImpl
 * @Author: d
 * @Date: 2020/9/17
 */
@Service
public class ProgrammeApiAdapterImpl implements ProgrammeApiAdapter {

    private static Logger logger = LogManager.getLogger(ProgrammeApiAdapterImpl.class);

    @Autowired
    private PageInfoMapper pageInfoMapper;
    @Autowired
    private PageCardConfigMapper pageCardConfigMapper;
    @Autowired
    private PageCardConfigService pageCardConfigService;
    @Autowired
    private SidebarService sidebarService;
    @Autowired
    private ShowProgrammeService showProgrammeService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private PageInfoService pageInfoService;
    @Autowired
    private SysIconMapper sysIconMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private MenuAuthMapper menuAuthMapper;
    @Autowired
    private InternationalizationMapper internationalizationMapper;
    @Autowired
    private InternationalizationService internationalizationService;
    @Autowired
    private MenuAuthService menuAuthService;
    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private MinosApiAdapterImpl minosApi;
    @Autowired
    private HomeService homeService;
    @Autowired
    TemplatePageUtil templatePageUtil;
    @Autowired
    TemplateUtil templateUtil;
    @Autowired
    TemplateService templateService;
    @Autowired
    PopupWindowMapper popupWindowMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    I18nService i18nService;
    @Autowired
    UserUtil userUtil;
    @Autowired
    private MinosCacheTemplate minosCacheTemplate;
    @Autowired
    private CardUtil cardUtil;

    @Autowired
    private SiteMapper siteMapper;

    @Autowired
    private SiteMagMapper siteMagMapper;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private SelectMenuService selectMenuService;
    @Autowired
    ProgramApiServicePlus programApiServicePlus;

    @Override
    @Transactional
    public InlineResponse200 copyProgramme(CopyProgrammeReq body) {
        if (null == body || StringUtil.isEmpty(body.getWid())) {
            return (InlineResponse200) new InlineResponse200().errcode(GlobalEnum.VALIDATION.getCode())
                .errmsg(GlobalEnum.VALIDATION.getMsg());
        }

        //先查出该展示方案信息
        ShowProgrammeEntity showProgrammeEntity = showProgrammeService
                .getOne(new QueryWrapper<ShowProgrammeEntity>().lambda().eq(ShowProgrammeEntity::getWid, body.getWid()));
        List<String> targetSiteWid = com.google.common.collect.Lists.newArrayList(showProgrammeEntity.getSiteWid(),body.getSiteWid());
        MinosConsoleUtil.isRequestRightsLegal(targetSiteWid);
        String programmeId = StringUtil.simpleUuid();
        //修改wid，并插入
        ShowProgrammeEntity newProGramme = new ShowProgrammeEntity();
        ObjectUtil.copyProperties(showProgrammeEntity, newProGramme);
        newProGramme.setWid(programmeId);
        newProGramme.setSiteWid(body.getSiteWid());
        newProGramme.setPageStatus(Global.PageStatus.DISABLE.getId());
        newProGramme.setProgrammeName(StringUtils.isNotEmpty(body.getProgrammeName())
                ? body.getProgrammeName() : showProgrammeEntity.getProgrammeName() + Global.FB);
        newProGramme.setUpdateTime(CommonUtil.getSystemDate());
        showProgrammeService.save(newProGramme);

        // 根据展示方案wid查询页面信息
        saveCopyOfAllProgrammeInfo(true,programmeId, body.getWid(), "", showProgrammeEntity.getPlatformType());

        return new InlineResponse200();
    }

    private void saveCopyOfAllProgrammeInfo(boolean isCopy,String programmeId, String wid, String templateId,Integer platformType) {
        Date date = CommonUtil.getSystemDate();
        // 根据展示方案wid查询页面信息
        // 修改其中展示方案ID
        // 批量新增
        List<PageInfoEntity> pageInfoEntityList = pageInfoService.list(new QueryWrapper<PageInfoEntity>().lambda().eq(PageInfoEntity::getProgrammeId, wid).eq(PageInfoEntity::getIsDeleted, Global.DeleteStatus.NO.getId()).orderByAsc(PageInfoEntity::getCreateTime));
        //查询展示方案下所有的目录信息
        QueryWrapper<MenuEntity> wrapperMenu = new QueryWrapper<>();
        wrapperMenu.eq(DbFieldConstant.PROGRAMME_ID, wid);
        wrapperMenu.eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId());
        List<MenuEntity> menuList =menuService.filterIconUrl(menuMapper.selectList(wrapperMenu));

        //1.将查询结果转化返回类(转换成带有树的结构)
        //2.判断menu中是否配置该页面，如果配置了则菜单列表页面不可选
        List<PageListEntity> pageListObj = transPageInfo(menuList, pageInfoEntityList);
        //将所有的页面递归分类
        List<PageListEntity> pageTreeList = getPageList2(pageListObj);
        //复制得到的list
        List<PageInfoEntity> resultPageInfoEntityList = new ArrayList<>();
        //递归复制页面信息
        copyPageInfo(pageTreeList, menuList, resultPageInfoEntityList, programmeId, date, pageTreeList.get(0).getParentId(),templateId,platformType);
        if (Global.NUMBER_ZERO < resultPageInfoEntityList.size()) {
            pageInfoService.saveBatch(resultPageInfoEntityList);
        }

        // 根据展示方案wid查询菜单信息
        // 修改其中展示方案ID
        // 批量新增
        //            List<MenuEntity> menuEntityList = menuService.list(new QueryWrapper<MenuEntity>().lambda().eq(MenuEntity::getProgrammeId, body.getWid()));
        List<MenuBiz> menuBizList = new ArrayList<>();
        //转成带有树结构的菜单对象
        for (MenuEntity menuEntity : menuList) {
            MenuBiz menuBiz = ObjectUtil.copy(menuEntity, MenuBiz.class);
            menuBizList.add(menuBiz);
        }
        List<MenuEntity> menuEntityList = new ArrayList<>();
        //菜单list转树结构数据
        List<MenuBiz> menuBizTree = menusToTree(menuBizList);
        //递归复制菜单信息
        copyMenuInfo(menuBizTree, programmeId, menuEntityList, menuBizTree.get(0).getParentId());
        if (Global.NUMBER_ZERO < menuEntityList.size()) {
            menuService.saveBatch(menuEntityList);
        }

        // 根据展示方案wid查询侧边栏信息
        // 修改其中展示方案ID
        // 批量新增
        List<SidebarEntity> sidebarEntityList = sidebarService.list(new QueryWrapper<SidebarEntity>().lambda().eq(SidebarEntity::getProgrammeId, wid));
        for (SidebarEntity sidebarEntity : sidebarEntityList) {
            String newWid = StringUtil.simpleUuid();
            //处理授权
            copySidebarAuth(newWid, sidebarEntity.getWid());
            sidebarEntity.setWid(newWid);
            sidebarEntity.setProgrammeId(programmeId);
            //处理国际化
            String langKey = CommunalUtil.getWid();
            copySidebarInfoInternationalizationLang(sidebarEntity, langKey);
            sidebarEntity.setDisplayNameLangKey(langKey);
        }
        if (Global.NUMBER_ZERO < sidebarEntityList.size()) {
            sidebarService.saveBatch(sidebarEntityList);
        }

        // 根据展示方案wid查询配置信息
        // 修改展示方案配置信息
        if(StringUtil.isNotEmpty(templateId)){
            updateProgramme(programmeId,templateId,platformType);
        }

        if(isCopy){
            //复制下拉菜单
            copySelectMenu(wid,programmeId,resultPageInfoEntityList);
        }else{
            //初始化下拉菜单
            if(Global.PlatformType.PC.getType()==platformType){
                loadSelectMenu(programmeId);
            }
        }

    }

    /***
     * @Author jcx
     * @Description 复制下拉菜单
     * @Date 20:57 2022/10/24
     * @Param programmeWid:
     * @Param resultPageInfoEntityList:
     * @return void
     **/
    public void copySelectMenu(String oldProgrammeWid,String programmeWid,List<PageInfoEntity> resultPageInfoEntityList){
        List<SelectMenuEntity> list = selectMenuService.list(new QueryWrapper<SelectMenuEntity>().lambda()
                .eq(SelectMenuEntity::getIsDeleted, Global.NO).eq(SelectMenuEntity::getProgrammeId,oldProgrammeWid));
        if(!CollectionUtils.isEmpty(list)){
            List<String> selectMenuWids = list.stream().map(SelectMenuEntity::getWid).filter(Objects::nonNull).collect(Collectors.toList());
            List<String> selectMenuLangKeys = list.stream().map(SelectMenuEntity::getMenuNameLangKey).filter(Objects::nonNull).collect(Collectors.toList());
            //内部页面
            List<SelectMenuEntity> pageMenus = list.stream().filter(k -> Global.MenuType.INNER_LINK.getId() == k.getMenuType()).collect(Collectors.toList());
            Map<String, String> pageMap =null;
            Map<String, String> newPageMap =null;
            if(CollectionUtils.isNotEmpty(pageMenus)){
                List<String> pageMenuWids = pageMenus.stream().map(SelectMenuEntity::getPageId).filter(Objects::nonNull).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(pageMenuWids)){
                    List<PageInfoEntity> pageInfos = pageInfoService.list(new QueryWrapper<PageInfoEntity>().lambda().select(PageInfoEntity::getPageCode,PageInfoEntity::getWid)
                            .in(PageInfoEntity::getWid, pageMenuWids));
                    pageMap = pageInfos.stream().collect(Collectors.toMap(PageInfoEntity::getWid, PageInfoEntity::getPageCode));
                    if(CollectionUtils.isNotEmpty(resultPageInfoEntityList)){
                        newPageMap = resultPageInfoEntityList.stream().collect(Collectors.toMap(PageInfoEntity::getPageCode, PageInfoEntity::getWid));
                    }
                }
            }
            List<InternationalizationEntity> langData =null;
            if(CollectionUtils.isNotEmpty(selectMenuLangKeys)){
                langData = internationalizationService.list(new QueryWrapper<InternationalizationEntity>().lambda().in(InternationalizationEntity::getLangKey, selectMenuLangKeys));
            }
            Map<String, String> finalPageMap = pageMap;
            Map<String, String> finalNewPageMap = newPageMap;
            AtomicReference<List<MenuAuthEntity>> menuAuthData =new AtomicReference<>();
            List<InternationalizationEntity> finalLangData = langData;
            list.forEach(selectMenu->{
                String selectMenuWid = MinosCommonUtil.getWid();
                String selectMenuLang = MinosCommonUtil.getWid();
                selectMenu.setWid(selectMenuWid);

                selectMenu.setProgrammeId(programmeWid);
                if( Global.MenuType.INNER_LINK.getId() == selectMenu.getMenuType()&&StringUtils.isNotBlank(selectMenu.getPageId())&&
                        MapUtils.isNotEmpty(finalPageMap)&&MapUtils.isNotEmpty(finalNewPageMap)){
                    String pageCode = finalPageMap.get(selectMenu.getPageId());
                    selectMenu.setPageId(StringUtils.isNotBlank(pageCode)&&finalNewPageMap.containsKey(pageCode)?finalNewPageMap.get(pageCode):"");
                }
                if(CollectionUtils.isNotEmpty(finalLangData)){
                for ( int i = 0 ; i < finalLangData.size()  ; i++ ) {
                    InternationalizationEntity internationalizationEntity = finalLangData.get(i);
                    if(selectMenu.getMenuNameLangKey().equals(internationalizationEntity.getLangKey())){
                        internationalizationEntity.setWid(MinosCommonUtil.getWid());
                        internationalizationEntity.setLangKey(selectMenuLang);
                    }
                }
                    selectMenu.setMenuNameLangKey(selectMenuLang);
                }

                if(Global.AuthType.AUTH_VISIBLE.getId()==selectMenu.getAuthType()){
                    menuAuthData.set(menuAuthService.list(new QueryWrapper<MenuAuthEntity>().lambda().in(MenuAuthEntity::getMenuWid, selectMenuWids)));
                    if(CollectionUtils.isNotEmpty(menuAuthData.get())){
                        menuAuthData.get().forEach(k->{
                            k.setWid(MinosCommonUtil.getWid());
                            k.setMenuWid(selectMenuWid);
                        });
                    }
                }
            });
            if(null!=menuAuthData&&CollectionUtils.isNotEmpty(menuAuthData.get())){
                menuAuthService.saveBatch(menuAuthData.get());
            }
            if(CollectionUtils.isNotEmpty(finalLangData)){
                internationalizationService.saveBatch(finalLangData);
            }
            selectMenuService.saveBatch(list);
        }
    }


    /***
     * @Author jcx
     * @Description 初始化下拉菜单
     * @Date 19:27 2022/10/24
     * @return void
     **/
    public void loadSelectMenu(String programmeId){
        //帐号管理
        SystemConfigEntity accountCongfig = systemConfigService.getSystemConfig(PortalManagerProperties.ACCOUNT_MANAGE_URL);
        //融合管控台
        SystemConfigEntity backendManageConfig = systemConfigService.getSystemConfig(PortalManagerProperties.BACKEND_MANAGE_URL);
        //个人中心
        SystemConfigEntity personCenterConfig = systemConfigService.getSystemConfig(PortalManagerProperties.PERSON_CENTER_URL);
        List<ShowProgrammeEntity> list = showProgrammeService.list(new QueryWrapper<ShowProgrammeEntity>().lambda()
                .eq(ShowProgrammeEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .eq(ShowProgrammeEntity::getPlatformType, Global.PlatformType.PC.getType()));
        List<String> programmeWids = StringUtils.isNotBlank(programmeId)?Arrays.asList(programmeId):list.stream().map(ShowProgrammeEntity::getWid).collect(Collectors.toList());
        List<SelectMenuEntity> selectMenus= com.google.common.collect.Lists.newArrayList();
        List<InternationalizationEntity> langEntitys = com.google.common.collect.Lists.newArrayList();
        programmeWids.forEach(k->{
            if(null!=personCenterConfig){
                loadData( personCenterConfig,selectMenus,langEntitys,k,"个人中心",PortalManagerProperties.PERSON_CENTER_URL,1L,1,4,"icon-personCenter");
            }
            if(null!=accountCongfig&&(StringUtil.isNotBlank(accountCongfig.getDefaultValue())||StringUtil.isNotBlank(accountCongfig.getConfigValue()))){
                loadData( accountCongfig,selectMenus,langEntitys,k,"帐号管理",PortalManagerProperties.ACCOUNT_MANAGE_URL,2L,0,1,"icon-accountManage");
            }
            loadData( new SystemConfigEntity(),selectMenus,langEntitys,k,"岗位管理",PortalManagerProperties.POSTITION_MANAGER_URL,3L,1,4,"icon-gangweiguanli");
            if(null!=backendManageConfig){
                loadData( backendManageConfig,selectMenus,langEntitys,k,"后台管理",PortalManagerProperties.BACKEND_MANAGE_URL,4L,1,4,"icon-backstageManage");
            }
            loadData( new SystemConfigEntity(),selectMenus,langEntitys,k,"事项管理",PortalManagerProperties.SIM_MANAGE_URL,5L,1,4,"icon-shixiangguanli");
            loadData( new SystemConfigEntity(),selectMenus,langEntitys,k,"效能看板",PortalManagerProperties.SIM_PORTAL_URL,6L,1,4,"icon-xiaonengkanban");
        });
        if(! org.springframework.util.CollectionUtils.isEmpty(selectMenus)){
            selectMenuService.saveBatch(selectMenus);
        }
        if(! org.springframework.util.CollectionUtils.isEmpty(langEntitys)){
            internationalizationService.saveBatch(langEntitys);
        }
    }
    private void loadData(SystemConfigEntity config,List<SelectMenuEntity> selectMenus,List<InternationalizationEntity> langEntitys,
                          String programmeWid,String menuName,String menuWid,Long orderNumber,int isSystemMenu,int authType,String iconPath){
        SelectMenuEntity selectMenuEntity = new SelectMenuEntity();
        InternationalizationEntity langEntity = new InternationalizationEntity();
        selectMenuEntity.setWid(menuWid);
        selectMenuEntity.setMenuName(menuName);
        selectMenuEntity.setOrderNumber(orderNumber);
        selectMenuEntity.setAuthType(authType);
        selectMenuEntity.setOpenType(1);
        selectMenuEntity.setIconType(0);
        selectMenuEntity.setIconPath(iconPath);
        selectMenuEntity.setIsSystemMenu(isSystemMenu);
        String wid = MinosCommonUtil.getWid();
        selectMenuEntity.setMenuNameLangKey(wid);
        selectMenuEntity.setIsDeleted(0);
        if(StringUtil.isBlank(config.getDefaultValue())&&StringUtil.isBlank(config.getConfigValue())){
            selectMenuEntity.setIsEnabled(0);
        }else{
            selectMenuEntity.setIsEnabled(1);
            selectMenuEntity.setLinkUrl(StringUtil.isNotBlank(config.getConfigValue())?config.getConfigValue():config.getDefaultValue());
        }
        selectMenuEntity.setMenuType(2);
        selectMenuEntity.setProgrammeId(programmeWid);
        selectMenus.add(selectMenuEntity);
        langEntity.setLangKey(wid);
        langEntity.setLangValue(menuName);
        langEntity.setLangCountry(Global.DEFAULT_LANGUAGE);
        langEntitys.add(langEntity);
    }

    private void updateProgramme(String programmeId,String templateId,Integer platformType){
        UpdateWrapper<ShowProgrammeEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(ShowProgrammeEntity::getTemplateConfig, getTemplateDefaultConfig(templateId,platformType))
                .set(ShowProgrammeEntity::getTemplateId,templateId)
                .eq(ShowProgrammeEntity::getWid, programmeId);
        showProgrammeService.update(updateWrapper);
    }

    private String getTemplateDefaultConfig(String templateId,Integer platformType){
        TemplateEntity templateEntity = templateService.getById(templateId);
        ITemplate template = templateUtil.getTemplate(templateEntity.getTemplateId());
        return template.getTemplateConfig(String.valueOf(platformType));
    }

    private String saveCardConfigList(PageInfoEntity pageInfo, Date date, String pageUuid) {
        QueryWrapper<PageCardConfigEntity> wrapper = new QueryWrapper<>();
        PageCardConfigEntity pageCardConfigEntity = new PageCardConfigEntity();
        pageCardConfigEntity.setPageWid(pageInfo.getWid());
        pageCardConfigEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
        wrapper.setEntity(pageCardConfigEntity);
        List<PageCardConfigEntity> cardConfigList = pageCardConfigMapper.selectList(wrapper);

        //解析cardlayout，将其中的cardWid进行修改
        String layout = handleLayoutCardWid(pageInfo, cardConfigList, date, pageUuid);
        return layout;
    }

    //1.将查询结果转化返回类(转换成带有树的结构)
    //2.判断menu中是否配置该页面，如果配置了则菜单列表页面不可选
    private List<PageListEntity> transPageInfo(List<MenuEntity> menuList, List<PageInfoEntity> pageInfoEntityList) {
        List<PageListEntity> pageListObj = new ArrayList<>();
        for (PageInfoEntity pageInfo : pageInfoEntityList) {
            PageListEntity page = ObjectUtil.copyProperties(pageInfo, new PageListEntity());
            page.setFlag(Global.DeleteStatus.NO.getId());

            //判断menu中是否配置该页面，如果配置了则菜单列表页面不可选
            for (MenuEntity menuEntity : menuList) {
                if (null != menuEntity.getPageId()
                        && pageInfo.getWid().equals(menuEntity.getPageId())) {
                    page.setFlag(Global.DeleteStatus.YES.getId());
                }
            }
            pageListObj.add(page);
        }
        return pageListObj;
    }

    private Map<String,Object> getTemplate(String templateId,Integer platformType){
        Map<String, Object> result = new HashMap<>();
        TemplateContent defaultTemplate=null;
        Map<String,TemplateContent > templateMap=null;
        TemplateEntity templateEntity = templateMapper.selectOne(new QueryWrapper<TemplateEntity>().lambda().eq(TemplateEntity::getWid, templateId).eq(TemplateEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));
        if(null!=templateEntity){
            TemplateAjaxRequest templateAjaxRequest = new TemplateAjaxRequest();
            Map param = new HashMap<String,String>();
            param.put("templateId", templateEntity.getTemplateId());
            templateAjaxRequest.setParam(param);
            templateAjaxRequest.setMethod(Global.TemPlateGlobalMethod.GET_TEMPLATE_CONTENT.getMethodName());
            templateAjaxRequest.setPlatformType(platformType);
            List<TemplateContent> templateContents= (List<TemplateContent>) homeService.executeAllTemplateMethod(templateAjaxRequest);
            if(CollectionUtils.isNotEmpty(templateContents)){
                List<TemplateContent> defaultTemplateList=templateContents.stream().filter(templateContent ->templateContent.getDefaultTemplate().equals(true)).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(defaultTemplateList)||defaultTemplateList.size()>1){
                    throw new BusinessException("此模板无默认模板文件或默认模板文件不唯一，请检查");
                }
                defaultTemplate=defaultTemplateList.get(0);
                templateMap= templateContents.stream().collect(
                        Collectors.toMap(TemplateContent::getCode,templateContent -> templateContent));
            }
        }
        result.put("defaultTemplate",defaultTemplate);
        result.put("templateMap",templateMap);
        return result;
    }

    //递归复制页面信息
    public void copyPageInfo(List<PageListEntity> resultList, List<MenuEntity> menuList, List<PageInfoEntity> resultPageInfoEntityList,
                             String programmeId, Date date, String parentId,String templateId,Integer platformType) {
        TemplateContent defaultTemplate=null;
        Map<String,TemplateContent > templateMap=null;
        if(!"".equals(templateId)){
            Map<String, Object> result = getTemplate(templateId, platformType);
            defaultTemplate=null!=result.get("defaultTemplate")? (TemplateContent) result.get("defaultTemplate") :null;
            templateMap=null!=result.get("templateMap")?(Map<String, TemplateContent>) result.get("templateMap"):null;
        }
        for (PageListEntity pageListObjBiz : resultList) {
            String pageUuid = StringUtil.simpleUuid();
            copyUpdatePageId(menuList, pageListObjBiz, pageUuid);
            PageInfoEntity pageInfoEntity = ObjectUtil.copy(pageListObjBiz, PageInfoEntity.class);

            //设置cardLayout
            String cardLayout = saveCardConfigList(pageInfoEntity, date, pageUuid);
            pageInfoEntity.setCardLayout(cardLayout);

            pageInfoEntity.setWid(pageUuid);
            pageInfoEntity.setProgrammeId(programmeId);
            pageInfoEntity.setParentId(parentId);
            pageInfoEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
            if(null!=templateMap){
                if(templateMap.containsKey(pageInfoEntity.getTemplateCode())){
                    pageInfoEntity.setPageConfig(templateMap.get(pageInfoEntity.getTemplateCode()).getDefaultPageConfig());
                }else{
                    pageInfoEntity.setTemplateCode(defaultTemplate.getCode());
                    pageInfoEntity.setPageConfig(defaultTemplate.getDefaultPageConfig());
                }
            }else{

            }
            //复制页面国际化语言
            String langKey = CommunalUtil.getWid();
            copyPageInfoInternationalizationLang(pageInfoEntity, langKey);
            pageInfoEntity.setPageTitleLangKey(langKey);
            resultPageInfoEntityList.add(pageInfoEntity);
            if (Global.NUMBER_ZERO < pageListObjBiz.getPageList().size()) {
                copyPageInfo(pageListObjBiz.getPageList(), menuList, resultPageInfoEntityList, programmeId, date, pageUuid,templateId,platformType);
            }
        }
    }

    //递归复制菜单栏
    public void copyMenuInfo(List<MenuBiz> menuBizTree, String programmeId, List<MenuEntity> menuEntityList, String parentId) {
        for (MenuBiz menuBiz : menuBizTree) {
            String menuUuid = StringUtil.simpleUuid();
            MenuEntity menuEntity = ObjectUtil.copy(menuBiz, MenuEntity.class);
            menuEntity.setWid(menuUuid);
            menuEntity.setProgrammeId(programmeId);
            menuEntity.setParentId(parentId);
            menuEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
            //复制菜单权限
            copyMenuInfoAuth(menuUuid, menuBiz.getWid());
            //复制菜单国际化语言
            String langKey = CommunalUtil.getWid();
            copyMenuInfoInternationalizationLang(menuEntity, langKey);
            menuEntity.setMenuNameLangKey(langKey);
            menuEntityList.add(menuEntity);
            if (Global.NUMBER_ZERO < menuBiz.getMenu().size()) {
                copyMenuInfo(menuBiz.getMenu(), programmeId, menuEntityList, menuUuid);
            }
        }
    }

    //cOPY侧边栏授权信息
    private void copySidebarAuth(String newWid, String menuWid) {
        //根据wid获取到所有的权限
        List<MenuAuthEntity> menuAuthEntityList = menuAuthService.list(new QueryWrapper<MenuAuthEntity>().lambda().eq(MenuAuthEntity::getMenuWid, menuWid));
        if (CollectionUtils.isNotEmpty(menuAuthEntityList)) {
            List<MenuAuthEntity> menuAuthEntities = menuAuthEntityList.stream().map(menuAuthEntity -> {
                menuAuthEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
                menuAuthEntity.setMenuAuthType(Global.MenuMenuAuthType.SIDEBAR.getId());
                menuAuthEntity.setMenuWid(newWid);
                menuAuthEntity.setWid(StringUtil.simpleUuid());
                return menuAuthEntity;
            }).collect(Collectors.toList());
            menuAuthService.saveBatch(menuAuthEntities);
        }
    }

    //cOPY菜单授权信息
    private void copyMenuInfoAuth(String newWid, String menuWid) {
        //根据wid获取到所有的权限
        List<MenuAuthEntity> menuAuthEntityList = menuAuthService.list(new QueryWrapper<MenuAuthEntity>().lambda().eq(MenuAuthEntity::getMenuWid, menuWid));
        if (CollectionUtils.isNotEmpty(menuAuthEntityList)) {
            List<MenuAuthEntity> menuAuthEntities = menuAuthEntityList.stream().map(menuAuthEntity -> {
                menuAuthEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
                menuAuthEntity.setMenuAuthType(Global.MenuMenuAuthType.TOP_MENU.getId());
                menuAuthEntity.setMenuWid(newWid);
                menuAuthEntity.setWid(StringUtil.simpleUuid());
                return menuAuthEntity;
            }).collect(Collectors.toList());
            menuAuthService.saveBatch(menuAuthEntities);
        }
    }

    private void copyUpdatePageId(List<MenuEntity> menuList, PageListEntity pageListObjBiz, String pageUuid) {
        for (MenuEntity menuEntity : menuList) {
            if (null != menuEntity.getPageId()
                    && pageListObjBiz.getWid().equals(menuEntity.getPageId())) {
                menuEntity.setPageId(pageUuid);
            }
        }
    }

    private void copyPageInfoInternationalizationLang(PageInfoEntity pageInfoEntity, String langKey) {
        //通过国际化key查出
        if (StringUtil.isNotEmpty(pageInfoEntity.getPageTitleLangKey())) {
            List<InternationalizationEntity> list = internationalizationMapper.selectList(new QueryWrapper<InternationalizationEntity>().lambda().eq(InternationalizationEntity::getLangKey, pageInfoEntity.getPageTitleLangKey()));
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(internationalizationEntity -> {
                    internationalizationEntity.setWid(StringUtil.simpleUuid());
                    internationalizationEntity.setLangKey(langKey);
                });
                internationalizationService.saveBatch(list);
            }
        }
    }

    private void copySidebarInfoInternationalizationLang(SidebarEntity sidebarEntity, String langKey) {
        //通过国际化key查出
        if (StringUtil.isNotEmpty(sidebarEntity.getDisplayNameLangKey())) {
            List<InternationalizationEntity> list = internationalizationMapper.selectList(new QueryWrapper<InternationalizationEntity>().lambda().eq(InternationalizationEntity::getLangKey, sidebarEntity.getDisplayNameLangKey()));
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(internationalizationEntity -> {
                    internationalizationEntity.setWid(StringUtil.simpleUuid());
                    internationalizationEntity.setLangKey(langKey);
                });
                internationalizationService.saveBatch(list);
            }
        }
    }

    private void copyMenuInfoInternationalizationLang(MenuEntity menuEntity, String langKey) {
        //通过国际化key查出
        if (StringUtil.isNotEmpty(menuEntity.getMenuNameLangKey())) {
            List<InternationalizationEntity> list = internationalizationMapper.selectList(new QueryWrapper<InternationalizationEntity>().lambda().eq(InternationalizationEntity::getLangKey, menuEntity.getMenuNameLangKey()));
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(internationalizationEntity -> {
                    internationalizationEntity.setWid(StringUtil.simpleUuid());
                    internationalizationEntity.setLangKey(langKey);
                });
                internationalizationService.saveBatch(list);
            }
        }
    }

    @Override
    public InlineResponse20011 deleteMenu(List<String> body) {
        if (CollectionUtils.isEmpty(body)) {
            return new InlineResponse20011();
        }
        // 根据menuId获取站点id
        List<MenuEntity> menuList = menuMapper.selectBatchIds(body);
        if ( CollectionUtils.isEmpty(menuList) ) {
            return new InlineResponse20011();
        }

        List<String> collect = menuList.stream().map(MenuEntity::getProgrammeId).distinct().collect(Collectors.toList());
        MinosConsoleUtil.checkSiteAuth(collect);

        String programId = menuList.get(0).getProgrammeId();
        menuList.forEach(menuEntity -> {
                    menuEntity.setIsEnabled(Global.Status.DISABLE.getId());
                    menuEntity.setIsDeleted(Global.DeleteStatus.YES.getId());
                }
        );
        menuService.updateBatchById(menuList);

        if ( StringUtils.isNotEmpty(programId) ) {
            updateProgramTimeByWid(programId);
        }

        return new InlineResponse20011();
    }

    @Override
    public InlineResponse20012 deletePageInfo(List<String> body) {
        //判断参数
        if (CollectionUtils.isEmpty(body)) {
            return (InlineResponse20012) new InlineResponse20012().errmsg("Wid不能为空！");
        }
        PageInfoEntity pageInfoEntity = pageInfoMapper.selectById(body.get(0));
        if ( null == pageInfoEntity ) {
            return (InlineResponse20012) new InlineResponse20012().errmsg("页面不存在！");
        }
        MinosConsoleUtil.checkSiteAuth(Collections.singletonList(pageInfoEntity.getProgrammeId()));

        //查询是否有目录占用
        boolean flag = false;
        for (String wid : body) {
            QueryWrapper<MenuEntity> wrapper = new QueryWrapper<>();
            MenuEntity menuEntity = new MenuEntity();
            menuEntity.setPageId(wid);
            menuEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
            wrapper.setEntity(menuEntity);
            List<MenuEntity> menuList = menuMapper.selectList(wrapper);
            //查询侧边栏是否有占用
            QueryWrapper<SidebarEntity> siderWrapper = new QueryWrapper<>();
            SidebarEntity sidebarEntity = new SidebarEntity();
            sidebarEntity.setPageId(wid);
            sidebarEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
            siderWrapper.setEntity(sidebarEntity);
            List<SidebarEntity> siderList = sidebarService.list(siderWrapper);

            if ((null != menuList && CollectionUtils.isNotEmpty(menuList)) || (null != siderList && CollectionUtils.isNotEmpty(siderList))) {
                flag = true;
            }
        }
        if (flag) {
            return (InlineResponse20012) new InlineResponse20012().errmsg("页面正在使用中，不能删除！");
        }
        //更新页面表状态
        LambdaUpdateWrapper<PageInfoEntity> wrapper;
        for (String wid : body) {
            wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(PageInfoEntity::getWid, wid).set(PageInfoEntity::getIsDeleted, Global.DeleteStatus.YES.getId());
            pageInfoMapper.update(null, wrapper);
        }
        //更新展示方案更新时间
        updateProgramTimeByWid(pageInfoEntity.getProgrammeId());
        return (InlineResponse20012) new InlineResponse20012().errcode(GlobalEnum.SUCCESS.getCode()).errmsg(GlobalEnum.SUCCESS.getMsg());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InlineResponse20019 deleteProgramme(List<String> body) {
        try {
            if (CollectionUtils.isEmpty(body)) {
                return (InlineResponse20019) new InlineResponse20019().errcode(GlobalEnum.VALIDATION.getCode()).errmsg(GlobalEnum.VALIDATION.getMsg());
            }
            List<List<String>> widGroups = CommonUtil.getListArray(body);
            List<ShowProgrammeEntity> showProgrammeEntities = new ArrayList<>();
            for(List<String> widGroup:widGroups){
                List<ShowProgrammeEntity> rst =  showProgrammeService.list(
                        new LambdaQueryWrapper<ShowProgrammeEntity>().eq(ShowProgrammeEntity::getIsDeleted,Global.DeleteStatus.NO.getId()).in(ShowProgrammeEntity::getWid,widGroup)
                );
                if(!CollectionUtils.isEmpty(rst)){
                    showProgrammeEntities.addAll(rst);
                }
            }
            List<String> siteWids = showProgrammeEntities.stream().map(ent->ent.getSiteWid()).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(siteWids)){
                MinosConsoleUtil.isRequestRightsLegal(siteWids);
            }
//            LambdaUpdateWrapper<ShowProgrammeEntity> wrapper;
//            for(String wid : body){
//                wrapper = new LambdaUpdateWrapper<>();
//                wrapper.eq(ShowProgrammeEntity::getWid,wid).set(ShowProgrammeEntity::getIsDeleted,1);
//                showProgrammeMapper.update(null,wrapper);
//            }
            List<ShowProgrammeEntity> showProgrammeEntityList = new ArrayList<>();
            Date now = new Date();
            ShowProgrammeEntity showProgrammeEntity;
            for (String wid : body) {
                showProgrammeEntity = new ShowProgrammeEntity();
                showProgrammeEntity.setWid(wid);
                showProgrammeEntity.setIsDeleted(Global.DeleteStatus.YES.getId());
                showProgrammeEntity.setUpdateTime(now);
                showProgrammeEntityList.add(showProgrammeEntity);
            }
            showProgrammeService.updateBatchById(showProgrammeEntityList);
            //3.5.3   性能优化，添加展示方案删除时，关联的页面和顶头菜单，右上角菜单，侧边栏，都要删除
            programApiServicePlus.deleteProgramRelatedPageAndMenuAndSideBar(body);

            return new InlineResponse20019();
        } catch (MinosException e){
            logger.error("deleteProgramme执行失败", e);
            return (InlineResponse20019) new InlineResponse20019().errcode(e.getCode()).errmsg(e.getMessage());
        }catch (Exception e) {
            logger.error("deleteProgramme执行失败", e);
            return (InlineResponse20019) new InlineResponse20019().errcode(GlobalEnum.ERROR.getCode()).errmsg(GlobalEnum.ERROR.getMsg());
        }
    }

    @Override
    public InlineResponse20016 deleteSidebar(List<String> body) {
        try {
            if (CollectionUtils.isEmpty(body)) {
                return (InlineResponse20016) new InlineResponse20016().errcode(GlobalEnum.VALIDATION.getCode()).errmsg(GlobalEnum.VALIDATION.getMsg());
            }

//            LambdaUpdateWrapper<SidebarEntity> wrapper;
//            for(String wid : body){
//                wrapper = new LambdaUpdateWrapper<>();
//                wrapper.eq(SidebarEntity::getWid,wid).set(SidebarEntity::getIsDeleted,Global.DeleteStatus.YES);
//                sidebarMapper.update(null,wrapper);
//            }

            SidebarEntity oneSidebar = sidebarService.getById(body.get(0));

            List<SidebarEntity> sidebarEntityList = new ArrayList<>();
            SidebarEntity sidebarEntity;
            for (String wid : body) {
                sidebarEntity = new SidebarEntity();
                sidebarEntity.setWid(wid);
                sidebarEntity.setIsDeleted(Global.DeleteStatus.YES.getId());
                sidebarEntityList.add(sidebarEntity);
            }
            sidebarService.updateBatchById(sidebarEntityList);

            List<SidebarEntity> sidebarEntityListNew;
            if(null != oneSidebar){
                sidebarEntityListNew = sidebarService.list(new QueryWrapper<SidebarEntity>().lambda()
                        .eq(SidebarEntity::getPositionType, oneSidebar.getPositionType())
                        .eq(SidebarEntity::getProgrammeId, oneSidebar.getProgrammeId())
                        .eq(SidebarEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                        .orderByAsc(SidebarEntity::getOrderNumber));
                if(CollectionUtils.isNotEmpty(sidebarEntityListNew)){
                    for(int i = 0; i<sidebarEntityListNew.size(); i++){
                        sidebarEntityListNew.get(i).setOrderNumber(Long.valueOf(i+1));
                    }
                    sidebarService.updateBatchById(sidebarEntityListNew);
                }
            }

            //更新展示方案更新时间
            SidebarEntity sidebar = sidebarService.getById(body.get(0));
            updateProgramTimeByWid(sidebar.getProgrammeId());

            return new InlineResponse20016();
        } catch (Exception e) {
            logger.error("deleteSidebar执行失败", e);
            return (InlineResponse20016) new InlineResponse20016().errcode(GlobalEnum.ERROR.getCode()).errmsg(GlobalEnum.ERROR.getMsg());
        }
    }

    @Override
    public InlineResponse20020 getBasicInfo(String code) {

        try {
            if (StringUtil.isEmpty(code)) {
                return (InlineResponse20020) new InlineResponse20020().errcode(GlobalEnum.VALIDATION.getCode()).errmsg(GlobalEnum.VALIDATION.getMsg());
            }

            QueryWrapper<ShowProgrammeEntity> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ShowProgrammeEntity::getWid, code).eq(ShowProgrammeEntity::getIsDeleted, Global.DeleteStatus.NO.getId());

//            ShowProgrammeEntity showProgrammeEntity = showProgrammeMapper.selectOne(wrapper);
            ShowProgrammeEntity showProgrammeEntity = showProgrammeService.getOne(wrapper);

            ProgrammeInfo programmeInfo = null;
            if (null != showProgrammeEntity) {
                programmeInfo = (ProgrammeInfo) ObjectUtil.copyProperties(showProgrammeEntity, new ProgrammeInfo());
            }

            return new InlineResponse20020().data(programmeInfo);
        } catch (Exception e) {
            logger.error("getBasicInfo执行失败", e);
            return (InlineResponse20020) new InlineResponse20020().errcode(GlobalEnum.ERROR.getCode()).errmsg(GlobalEnum.ERROR.getMsg());
        }
    }

    @Override
    public InlineResponse20023 getCardConfig(String code) {
        return null;
    }

    @Override
    public InlineResponse20022 getPageInfo(String wid) {
        //判断wid是否为空
        if (StringUtils.isBlank(wid)) {
            InlineResponse20022 inlineResponse20022 = new InlineResponse20022();
            inlineResponse20022.setErrcode(GlobalEnum.PARAM_FAIL.getCode());
            inlineResponse20022.setErrmsg(GlobalEnum.PARAM_FAIL.getMsg());
            return inlineResponse20022;
        }
        //根据wid去查询页面信息
        PageInfoEntity pageInfoEntity = pageInfoMapper.selectById(wid);

        // 权限校验
        MinosConsoleUtil.checkSiteAuth(Collections.singletonList(pageInfoEntity.getProgrammeId()));

        //根据页面id获取所有运行时配置
        QueryWrapper<PageCardConfigEntity> wrapper = new QueryWrapper<>();
        PageCardConfigEntity vo = new PageCardConfigEntity();
        vo.setPageWid(wid);
        vo.setIsDeleted(Global.DeleteStatus.NO.getId());
        wrapper.setEntity(vo);
        List<PageCardConfigEntity> pageCardList = pageCardConfigMapper.selectList(wrapper);
        //转换页面参数
        PageInfoRes pageInfoRes = (PageInfoRes) ObjectUtil.copyProperties(pageInfoEntity, new PageInfoRes());
        //查询国际化信息
        List<MenuNameLangBiz> langList = queryListLangByLangKey(pageInfoRes.getPageTitleLangKey());
        pageInfoRes.setPageTitleList(langList);
        //转换运行时配置参数
        List<CardConfigObj> cardConfigList = new ArrayList<CardConfigObj>();
        for (int i = 0; i < pageCardList.size(); i++) {
            CardConfigObj cardConfigObj = (CardConfigObj) ObjectUtil.copyProperties(pageCardList.get(i), new CardConfigObj());
            if (!StringUtils.isEmpty(cardConfigObj.getCardConfig())) {
                cardConfigObj.setCardConfig(cardConfigObj.getCardConfig().replaceAll("(\\\r\\\n|\\\r|\\\n|\\\n\\\r)", ""));
                cardConfigList.add(cardConfigObj);
            }
        }
        if (!StringUtils.isEmpty(pageInfoRes.getCardLayout())) {
            pageInfoRes.setCardLayout(pageInfoRes.getCardLayout().replaceAll("(\\\r\\\n|\\\r|\\\n|\\\n\\\r)", ""));
        }
        pageInfoRes.setCardConfig(cardConfigList);
        //返回页面结果
        InlineResponse20022 inlineResponse20022 = new InlineResponse20022();
        inlineResponse20022.setData(pageInfoRes);
        return inlineResponse20022;
    }

    private SidebarInfoResponse getSidebarInfo(SidebarEntity sidebarEntity){
        SidebarInfoResponse sidebarInfoResponse = new SidebarInfoResponse();
        if (null != sidebarEntity) {
            sidebarInfoResponse.setColumnName(sidebarEntity.getColumnName());
            sidebarInfoResponse.setIconUrl(sidebarEntity.getIconUrl());
            sidebarInfoResponse.setIconType(sidebarEntity.getIconType());
            sidebarInfoResponse.setLinkUrl(sidebarEntity.getLinkUrl());
            sidebarInfoResponse.setMenuType(sidebarEntity.getMenuType());
            sidebarInfoResponse.setAuthType(sidebarEntity.getAuthType());
            sidebarInfoResponse.setOpenType(sidebarEntity.getOpenType());
            sidebarInfoResponse.setCountAddress(sidebarEntity.getCountAddress());
            if (StringUtil.isNotEmpty(sidebarEntity.getPageId())) {
                sidebarInfoResponse.setPageId(sidebarEntity.getPageId());
                PageInfoEntity pageInfoEntity = pageInfoService.getById(sidebarEntity.getPageId());
                sidebarInfoResponse.setPageName(pageInfoEntity.getPageName());
            }
        }
        return sidebarInfoResponse;
    }
    @Override
    public InlineResponse20015 getSidebarInfo(String wid) {
        try {
            if (StringUtil.isEmpty(wid)) {
                return (InlineResponse20015) new InlineResponse20015().errcode(GlobalEnum.VALIDATION.getCode()).errmsg(GlobalEnum.VALIDATION.getMsg());
            }
            QueryWrapper<SidebarEntity> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SidebarEntity::getWid, wid);
            SidebarEntity sidebarEntity = sidebarService.getOne(wrapper);
            InlineResponse20015 inlineResponse20015 = new InlineResponse20015();
            SidebarInfoResponse sidebarInfoResponse = getSidebarInfo(sidebarEntity);
            //查询菜单授权表
            QueryWrapper<MenuAuthEntity> menuAuthWrapper = new QueryWrapper<>();
            menuAuthWrapper.eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId());
            List<MenuAuthEntity> menuAuthEntities = menuAuthMapper.selectList(menuAuthWrapper);

            //查询国际化表
            QueryWrapper<InternationalizationEntity> interWrapper = new QueryWrapper<>();
            interWrapper.eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId());
            List<InternationalizationEntity> internList = internationalizationMapper.selectList(interWrapper);

            //查询机构信息
            OrgInfoTreeReq orgInfoTreeReq = new OrgInfoTreeReq();
            orgInfoTreeReq.setIsTree(Global.CONSTANT_NO);
            InlineResponse20029 minosOrg = minosApi.getMinosOrg(orgInfoTreeReq);
            //机构list转map
            Map<String, String> orgMap = getOrgMap(minosOrg.getData());
            //查询域及用户组信息
            FieldGroupsReq fieldGroupsReq = new FieldGroupsReq();
            fieldGroupsReq.setIsTree(Global.CONSTANT_NO);
            InlineResponse20031 fieldGroups = minosApi.getFieldGroups(fieldGroupsReq);
            //域及用户组list转Map
            Map<String, String> fieldGroupMap = getFieldGroupMap(fieldGroups.getData());
            //添加授权信息
            if (CollectionUtils.isNotEmpty(menuAuthEntities)) {
                Map<String, List<MenuAuthEntity>> menuGroupList = menuAuthEntities.stream()
                        .collect(Collectors.groupingBy(MenuAuthEntity::getMenuWid));
                List<MenuAuthEntity> menuAuths = menuGroupList.get(sidebarEntity.getWid());
                if (CollectionUtils.isNotEmpty(menuAuths)) {
                    //list转化list，MenuId特殊处理
                    List<MenuAuthInfoBiz> menuAuthInfoBizs = ObjectUtil.copyListProperties(menuAuths,MenuAuthInfoBiz::new,(menuAuthEntity, menuAuthInfoBiz) ->{
                        menuAuthInfoBiz.setMenuId(menuAuthEntity.getMenuWid());
                    });
                    Map<String, String> finalOrgMap = orgMap;
                    Map<String, String> finalFieldGroupMap = fieldGroupMap;
                    //处理菜单授权，赋值授权名称用以展示
                    setAuthName( menuAuthInfoBizs, finalOrgMap,finalFieldGroupMap);
                    sidebarInfoResponse.setMenuAuthInfo(menuAuthInfoBizs);
                }
            }
            //添加国际化信息
            if (CollectionUtils.isNotEmpty(internList)) {
                Map<String, List<InternationalizationEntity>> interGroupList = internList.stream()
                        .collect(Collectors.groupingBy(InternationalizationEntity::getLangKey));
                List<InternationalizationEntity> entities = interGroupList.get(sidebarEntity.getDisplayNameLangKey());
                if (CollectionUtils.isNotEmpty(entities)) {
                    List<MenuNameLangBiz> copy = ObjectUtil.copy(entities, MenuNameLangBiz.class);
                    sidebarInfoResponse.setDisplayNameLang(copy);
                }
            }

            return inlineResponse20015.data(sidebarInfoResponse);
        } catch (Exception e) {
            logger.error("getSidebarInfo执行失败", e);
            return (InlineResponse20015) new InlineResponse20015().errcode(GlobalEnum.ERROR.getCode()).errmsg(GlobalEnum.ERROR.getMsg());
        }

    }

    @Override
    public InlineResponse20013 previewPage(PreviewPageReq body) {
        return null;
    }

    @Override
    public InlineResponse20021 queryMenuList(MenuListRes body) {
        //判断参数是否为空
        if (StringUtils.isBlank(body.getProgrammeId())) {
            InlineResponse20021 inlineResponse20021 = new InlineResponse20021();
            inlineResponse20021.setErrcode(GlobalEnum.PARAM_FAIL.getCode());
            inlineResponse20021.setErrmsg(GlobalEnum.PARAM_FAIL.getMsg());
            return inlineResponse20021;
        }
        //查询展示方案归属的站点并检查权限
        Collection<ShowProgrammeEntity> programmeEntities =showProgrammeService.listByIds(com.google.common.collect.Lists.newArrayList(body.getProgrammeId()));
        List<String> siteWids = programmeEntities.stream().map(ent->ent.getSiteWid()).collect(Collectors.toList());
        MinosConsoleUtil.isRequestRightsLegal(siteWids);
        //查询展示方案下面所有的菜单信息
        QueryWrapper<MenuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(DbFieldConstant.PROGRAMME_ID, body.getProgrammeId())
                .eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId())
                .orderByAsc(DbFieldConstant.ORDER_NUMBER);
        List<MenuEntity> menuList = menuService.filterIconUrl(menuMapper.selectList(wrapper));
        //查询菜单授权表
        QueryWrapper<MenuAuthEntity> menuAuthWrapper = new QueryWrapper<>();
        menuAuthWrapper.eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId());
        List<MenuAuthEntity> menuAuthEntities = menuAuthMapper.selectList(menuAuthWrapper);
        //查询国际化表
        QueryWrapper<InternationalizationEntity> interWrapper = new QueryWrapper<>();
        interWrapper.eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId());
        List<InternationalizationEntity> internList = internationalizationMapper.selectList(interWrapper);
        //查询页面表
        QueryWrapper<PageInfoEntity> pageWrapper = new QueryWrapper<>();
        pageWrapper.eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId());
        List<PageInfoEntity> pageInfos = pageInfoMapper.selectList(pageWrapper);

        if (CollectionUtils.isNotEmpty(menuList)) {
            return makeMenuList(menuList, internList, menuAuthEntities, pageInfos);
        }
        return new InlineResponse20021();
    }

    //组织机构list转Map
    public Map<String, String> getOrgMap(List<OrgInfosBiz> orgInfosBizs){
        Map<String, String> orgMap=null;
        if (CollectionUtils.isNotEmpty(orgInfosBizs)) {
            orgMap = orgInfosBizs.stream().collect(
                    Collectors.toMap(OrgInfosBiz::getWid,
                            OrgInfosBiz::getName, (oldVal, currVal) -> currVal));
        }
        return orgMap;
    }
    //域及用户组List转map
    public Map<String, String> getFieldGroupMap(List<FieldGroupBiz> fieldGroupBizs){
        Map<String, String> fieldGroupMap=null;
        if (CollectionUtils.isNotEmpty(fieldGroupBizs)) {
            fieldGroupMap = fieldGroupBizs.stream().collect(
                    Collectors.toMap(FieldGroupBiz::getWid,
                            FieldGroupBiz::getName, (oldVal, currVal) -> currVal));
        }
        return fieldGroupMap;
    }

    //页面信息list转Map
    private Map<String, String> getPageInfosMap(List<PageInfoEntity> pageInfos){
        Map<String, String> pageMap=null;
        if (CollectionUtils.isNotEmpty(pageInfos)) {
            pageMap = pageInfos.stream().collect(
                    Collectors.toMap(PageInfoEntity::getWid,
                            PageInfoEntity::getPageName, (oldVal, currVal) -> currVal));
        }
        return pageMap;
    }

    private InlineResponse20021 makeMenuList(List<MenuEntity> menuList, List<InternationalizationEntity> internList,
                                             List<MenuAuthEntity> menuAuthEntities, List<PageInfoEntity> pageInfos) {
        List<MenuBiz> menuBizList = new ArrayList<>();
        //查询机构信息
        OrgInfoTreeReq orgInfoTreeReq = new OrgInfoTreeReq();
        orgInfoTreeReq.setIsTree(Global.CONSTANT_NO);
        InlineResponse20029 minosOrg = minosApi.getMinosOrg(orgInfoTreeReq);
        Map<String, String> orgMap = getOrgMap(minosOrg.getData());
        //查询域及用户组信息
        FieldGroupsReq fieldGroupsReq = new FieldGroupsReq();
        fieldGroupsReq.setIsTree(Global.CONSTANT_NO);
        InlineResponse20031 fieldGroups = minosApi.getFieldGroups(fieldGroupsReq);
        Map<String, String> fieldGroupMap = getFieldGroupMap(fieldGroups.getData());
        Map<String, String> pageMap=getPageInfosMap(pageInfos);
        Map<String, List<MenuAuthEntity>> menuGroupListMap=null;
        if (CollectionUtils.isNotEmpty(menuAuthEntities)) {
            menuGroupListMap = menuAuthEntities.stream()
                    .collect(Collectors.groupingBy(MenuAuthEntity::getMenuWid));
        }
        for (MenuEntity menuEntity : menuList) {
            MenuBiz menuBiz = ObjectUtil.copy(menuEntity, MenuBiz.class);
            //添加页面名称信息
            menuBiz.setPageName(null != pageMap.get(menuBiz.getPageId()) ? pageMap.get(menuBiz.getPageId()) : "");
            //添加授权信息
            if (null!=menuGroupListMap) {
                List<MenuAuthEntity> menuAuths = menuGroupListMap.get(menuEntity.getWid());
                if(CollectionUtils.isNotEmpty(menuAuths)){
                    //list转化list，MenuId特殊处理
                    List<MenuAuthInfoBiz> menuAuthInfoBizs = ObjectUtil.copyListProperties(menuAuths,MenuAuthInfoBiz::new,(menuAuthEntity, menuAuthInfoBiz) ->{
                        menuAuthInfoBiz.setMenuId(menuAuthEntity.getMenuWid());
                    });
                    Map<String, String> finalOrgMap = orgMap;
                    Map<String, String> finalFieldGroupMap = fieldGroupMap;
                    //处理菜单授权，赋值授权名称用以展示
                    setAuthName( menuAuthInfoBizs, finalOrgMap,finalFieldGroupMap);
                    menuBiz.setMenuAuthInfo(menuAuthInfoBizs);
                }
            }
            //添加国际化信息
            if (CollectionUtils.isNotEmpty(internList)) {
                Map<String, List<InternationalizationEntity>> interGroupList = internList.stream()
                        .collect(Collectors.groupingBy(InternationalizationEntity::getLangKey));
                List<InternationalizationEntity> entities = interGroupList.get(menuEntity.getMenuNameLangKey());
                if (CollectionUtils.isNotEmpty(entities)) {
                    List<MenuNameLangBiz> copy = ObjectUtil.copy(entities, MenuNameLangBiz.class);
                    menuBiz.setMenuNameLangKeys(copy);
                }
            }
            menuBizList.add(menuBiz);
        }
        return new InlineResponse20021().data(menusToTree(menuBizList));
    }
    //处理菜单授权，赋值授权名称用以展示
    private void setAuthName(List<MenuAuthInfoBiz> menuAuthInfoBizs,Map<String, String> finalOrgMap,Map<String, String> finalFieldGroupMap){
        menuAuthInfoBizs.forEach(menuAuthInfoBiz -> {
            if (null != finalOrgMap && menuAuthInfoBiz.getAuthType().equals(String.valueOf(Global.MenuAuthType.ORG.getId()))) {
                //组织机构类型 赋值组织机构名称
                menuAuthInfoBiz.setAuthRelName(null != finalOrgMap.get(menuAuthInfoBiz.getAuthRelWid()) ? finalOrgMap.get(menuAuthInfoBiz.getAuthRelWid()) : "");
            } else if (null != finalFieldGroupMap && menuAuthInfoBiz.getAuthType().equals(String.valueOf(Global.MenuAuthType.DOMAIN_AND_GROUP.getId()))) {
                //赋值域及用户组名称
                menuAuthInfoBiz.setAuthRelName(null != finalFieldGroupMap.get(menuAuthInfoBiz.getAuthRelWid()) ? finalFieldGroupMap.get(menuAuthInfoBiz.getAuthRelWid()) : "");
            } else if (menuAuthInfoBiz.getAuthType().equals(String.valueOf(Global.MenuAuthType.USER.getId()))) {
                //赋值用户名
                InlineResponse20028 userDetails = minosApi.getUserDetails(menuAuthInfoBiz.getAuthRelWid());
                menuAuthInfoBiz.setAuthRelName(null != userDetails && userDetails.getData() != null ? userDetails.getData().getUserName() + "(" + userDetails.getData().getUserAccount() + ")" : "");
            }
        });
    }

    @Override
    public InlineResponse20017 queryPageList(ProgrammeReq body) {
        //判断参数是否为空
        if (StringUtils.isBlank(body.getWid())) {
            InlineResponse20017 inlineResponse20017 = new InlineResponse20017();
            inlineResponse20017.setErrcode(GlobalEnum.PARAM_FAIL.getCode());
            inlineResponse20017.setErrmsg(GlobalEnum.PARAM_FAIL.getMsg());
            return inlineResponse20017;
        }
        //查询展示方案归属的站点
        Collection<ShowProgrammeEntity> programmeEntities =showProgrammeService.listByIds(com.google.common.collect.Lists.newArrayList(body.getWid()));
        List<String> siteWids = programmeEntities.stream().map(ent->ent.getSiteWid()).collect(Collectors.toList());
        MinosConsoleUtil.isRequestRightsLegal(siteWids);
        //查询展示方案下面所有的页面信息
        QueryWrapper<PageInfoEntity> wrapper = new QueryWrapper<>();
        PageInfoEntity pageInfoEntity = new PageInfoEntity();
        pageInfoEntity.setProgrammeId(body.getWid());
        pageInfoEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
        wrapper.setEntity(pageInfoEntity);
        wrapper.orderByAsc(Global.CREATE_TIME);
        List<PageInfoEntity> pageList = pageInfoMapper.selectList(wrapper);
        //查询展示方案下所有的目录信息
        QueryWrapper<MenuEntity> wrapperMenu = new QueryWrapper<>();
        wrapperMenu.eq(DbFieldConstant.PROGRAMME_ID, body.getWid());
        wrapperMenu.eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId());
        List<MenuEntity> menuList = menuService.filterIconUrl(menuMapper.selectList(wrapperMenu));
        //将查询结果转化返回类
        List<PageListObj> pageListObj = new ArrayList<>();
        for (PageInfoEntity pageInfo : pageList) {
            PageListObj page = (PageListObj) ObjectUtil.copyProperties(pageInfo, new PageListObj());
            page.setFlag(Global.DeleteStatus.NO.getId());

            //判断menu中是否配置该页面，如果配置了则菜单列表页面不可选
            for (MenuEntity menuEntity : menuList) {
                if (null != menuEntity.getPageId()
                        && pageInfo.getWid().equals(menuEntity.getPageId())) {
                    page.setFlag(Global.DeleteStatus.YES.getId());
                }
            }
            pageListObj.add(page);
        }
        //将所有的页面递归分类
        List<PageListObj> resultList = getPageList(pageListObj);

        return new InlineResponse20017().data((List<PageListObj>) getUsService(resultList,"[a-zA-Z]+"));
    }

    private <T> Object getNewService(List<T> list, String redex) {
        List<PageListObj> result = new ArrayList<>();
        list.forEach(serviceModel -> {
            char[] chars1 = ((PageListObj) serviceModel).getPageCode().toCharArray();
            String c1 = Character.toString(chars1[0]);
            if (c1.matches(redex)) {
                result.add(((PageListObj) serviceModel));
            }
        });
        return result;
    }

    private <T> Object getUsService(List<T> list, String redex) {
        List<PageListObj> result = (List<PageListObj>) getNewService(list,redex);
        Collections.sort(result, new Comparator<PageListObj>() {
            @Override
            public int compare(PageListObj o1, PageListObj o2) {
                return o1.getPageCode().compareToIgnoreCase(o2.getPageCode());
            }
        });
        return result;
    }

    private List<PageListObj> getPageList(List<PageListObj> pageListObj) {
        List<PageListObj> result = new ArrayList<>();
        for (PageListObj page : pageListObj) {
            // 判断是否是根节点，就是 parentId，这里是从 0 开始，如果 parentId 为 0 ，则表示根节点
            if (Global.CONSTANT_NO.equals(page.getParentId())) {
                result.add(page);
            }
        }
        // 遍历根节点数据，为其设置子节点集合
        for (PageListObj pageObj : result) {
            // 获取子节点数据，传入 当前节点 id 和 所有 list
            List<PageListObj> childList = getChildren(pageObj.getWid(), pageListObj);
            // 将获取到的子节点集合添加到根节点里
            pageObj.setPageList(childList);
        }
        return result;
    }

    private List<PageListObj> getChildren(String id, List<PageListObj> pageListObj) {
        // 存在子节点数据
        List<PageListObj> childList = new ArrayList<>();
        // 遍历所有节点数据
        for (PageListObj item : pageListObj) {
            // 如果当前节点 ID 与父节点 ID 一致，表示当前数据是该节点的子节点
            if (item.getParentId().equals(id)) {
                childList.add(item);
            }
        }
        // 重点来了，递归调用
        for (PageListObj item : childList) {
            // 调用自身方法，依次添加子节点数据
            item.setPageList(getChildren(item.getWid(), pageListObj));
        }
        // 如果当前节点无子节点数据添加空数据，递归退出
        if (CollectionUtils.isEmpty(childList)) {
            return new ArrayList<>();
        }
        // 返回最终的子节点数据
        return childList;
    }

    //将所有的页面递归分类
    private List<PageListEntity> getPageList2(List<PageListEntity> pageListObj) {
        List<PageListEntity> result = new ArrayList<>();
        for (PageListEntity page : pageListObj) {
            // 判断是否是根节点，就是 parentId，这里是从 0 开始，如果 parentId 为 0 ，则表示根节点
            if (Global.CONSTANT_NO.equals(page.getParentId())) {
                result.add(page);
            }
        }
        // 遍历根节点数据，为其设置子节点集合
        for (PageListEntity pageObj : result) {
            // 获取子节点数据，传入 当前节点 id 和 所有 list
            List<PageListEntity> childList = getChildren2(pageObj.getWid(), pageListObj);
            // 将获取到的子节点集合添加到根节点里
            pageObj.setPageList(childList);
        }
        return result;
    }

    private List<PageListEntity> getChildren2(String id, List<PageListEntity> pageListObj) {
        // 存在子节点数据
        List<PageListEntity> childList = new ArrayList<>();
        // 遍历所有节点数据
        for (PageListEntity item : pageListObj) {
            // 如果当前节点 ID 与父节点 ID 一致，表示当前数据是该节点的子节点
            if (item.getParentId().equals(id)) {
                childList.add(item);
            }
        }
        // 重点来了，递归调用
        for (PageListEntity item : childList) {
            // 调用自身方法，依次添加子节点数据
            item.setPageList(getChildren2(item.getWid(), pageListObj));
        }
        // 如果当前节点无子节点数据添加空数据，递归退出
        if (CollectionUtils.isEmpty(childList)) {
            return new ArrayList<>();
        }
        // 返回最终的子节点数据
        return childList;
    }

    @Override
    public InlineResponse20018 queryProgrammeList(ProgrammeListReq body) {
        InlineResponse20018 inlineResponse20018;
        try {
            inlineResponse20018 = new InlineResponse20018();
            QueryWrapper<ShowProgrammeEntity> wrapper = new QueryWrapper<>();
            if (null != body && null != body.getPlatformType()) {
                wrapper.lambda().eq(ShowProgrammeEntity::getPlatformType, body.getPlatformType());
            }
            if (StringUtils.isNotEmpty(body.getSiteWid())) {
                // 检查站点管理员权限
                MinosConsoleUtil.isRequestRightsLegal( com.google.common.collect.Lists.newArrayList(body.getSiteWid()));
                wrapper.lambda().eq(ShowProgrammeEntity::getSiteWid, body.getSiteWid());
            }
            wrapper.lambda().eq(ShowProgrammeEntity::getIsDeleted, Global.DeleteStatus.NO.getId()).eq(ShowProgrammeEntity::getInitializeFlag, Global.DeleteStatus.NO.getId()).orderByDesc(ShowProgrammeEntity::getUpdateTime);
//            List<ShowProgrammeEntity> showProgrammeEntities = showProgrammeMapper.selectList(wrapper);
            List<ShowProgrammeEntity> showProgrammeEntities = showProgrammeService.list(wrapper);

            if (null != showProgrammeEntities) {
                for (ShowProgrammeEntity showProgrammeEntity : showProgrammeEntities) {
                    ProgrammeBiz programmeBiz = ObjectUtil.copyProperties(showProgrammeEntity, new ProgrammeBiz());
                    //模板code添加到返回结果中
                    TemplateEntity templateEntity = templateMapper.selectById(programmeBiz.getTemplateId());
                    if (null != templateEntity && StringUtils.isNotEmpty(templateEntity.getTemplateId())) {
                        programmeBiz.setTemplateCode(templateEntity.getTemplateId());
                        programmeBiz.setTemplateName(templateEntity.getTemplateName());
                    }
                    //转时间格式
                    Date updateTime = showProgrammeEntity.getUpdateTime();
                    Date createTime = showProgrammeEntity.getCreateTime();
                    if (null != updateTime && null != createTime) {
                        String updateTimeStr = DateUtil.getStrFromDate(updateTime, DateUtil.DATE_FORMATE_STRING_A);
                        programmeBiz.setUpdateTime(updateTimeStr);
                        String createTimeStr = DateUtil.getStrFromDate(createTime, DateUtil.DATE_FORMATE_STRING_A);
                        programmeBiz.setCreateTime(createTimeStr);
                    }
                    inlineResponse20018.addDataItem(programmeBiz);
                }
            }
            return inlineResponse20018;
        } catch (MinosException e){
            logger.error("queryProgrammeList执行失败", e);
            return (InlineResponse20018) new InlineResponse20018().errcode(e.getCode()).errmsg(e.getMessage());
        } catch (Exception e) {
            logger.error("queryProgrammeList执行失败", e);
            return (InlineResponse20018) new InlineResponse20018().errcode(GlobalEnum.ERROR.getCode()).errmsg(GlobalEnum.ERROR.getMsg());
        }
    }

    @Override
    public InlineResponse20014 querySidebarList(SidebarListReq body) {

        InlineResponse20014 inlineResponse20014;
        try {
            if (null == body || null == body.getPositionType() || StringUtil.isEmpty(body.getProgrammeId())) {
                return (InlineResponse20014) new InlineResponse20014().errcode(GlobalEnum.VALIDATION.getCode()).errmsg(GlobalEnum.VALIDATION.getMsg());
            }
            //查询展示方案归属的站点
            Collection<ShowProgrammeEntity> programmeEntities =showProgrammeService.listByIds(com.google.common.collect.Lists.newArrayList(body.getProgrammeId()));
            List<String> siteWids = programmeEntities.stream().map(ent->ent.getSiteWid()).collect(Collectors.toList());
            MinosConsoleUtil.isRequestRightsLegal(siteWids);

            QueryWrapper<SidebarEntity> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SidebarEntity::getPositionType, body.getPositionType()).eq(SidebarEntity::getProgrammeId, body.getProgrammeId()).eq(SidebarEntity::getIsDeleted, Global.DeleteStatus.NO.getId()).orderByAsc(SidebarEntity::getOrderNumber);

//            List<SidebarEntity> sidebarEntities = sidebarMapper.selectList(wrapper);
            List<SidebarEntity> sidebarEntities = sidebarService.list(wrapper);

            inlineResponse20014 = new InlineResponse20014();
            if (null != sidebarEntities && Global.NUMBER_ZERO < sidebarEntities.size()) {
                for (SidebarEntity sidebarEntity : sidebarEntities) {
                    if(StringUtil.isNotEmpty(sidebarEntity.getIconUrl())){
                        sidebarEntity.setIconUrl(sidebarService.filterMenu(sidebarEntity).getIconUrl());
                    }
                    SidebarInfo sidebarInfo = ObjectUtil.copyProperties(sidebarEntity, new SidebarInfo());
                    inlineResponse20014.addDataItem(sidebarInfo);
                }
            }

            return inlineResponse20014;
        } catch (MinosException e) {
            logger.error("querySidebarList执行失败", e);
            return (InlineResponse20014) new InlineResponse20014().errcode(e.getCode()).errmsg(e.getMessage());
        }catch (Exception e) {
            logger.error("querySidebarList执行失败", e);
            return (InlineResponse20014) new InlineResponse20014().errcode(GlobalEnum.ERROR.getCode()).errmsg(GlobalEnum.ERROR.getMsg());
        }

    }

    @Override
    public InlineResponse20026 querySysIconList() {

        List<SysIconEntity> sysIconEntities = sysIconMapper.selectList(null);

        InlineResponse20026 inlineResponse20026 = new InlineResponse20026();
        for (SysIconEntity entity : sysIconEntities) {
            SysIconInfo sysIconInfo = (SysIconInfo) ObjectUtil.copyProperties(entity, new SysIconInfo());
            inlineResponse20026.addDataItem(sysIconInfo);
        }

        return inlineResponse20026;
    }


    @Override
    public InlineResponse200 saveCopyOfPageInfo(PageInfoRes body) {
        //国际化langkey生成
        String langKey = CommunalUtil.getWid();
        //判断参数是否为空
        if (null == body || StringUtils.isBlank(body.getWid()) || StringUtils.isBlank(body.getPageCode())) {
            return (InlineResponse200) new InlineResponse200().errmsg("参数不能为空！");
        }
        Date date = CommonUtil.getSystemDate();
        //查询page数据
        PageInfoEntity pageInfoEntity = pageInfoMapper.selectById(body.getWid());

        // 权限校验
        MinosConsoleUtil.checkSiteAuth(Collections.singletonList(pageInfoEntity.getProgrammeId()));

        //判断pageCode是否重复
        QueryWrapper<PageInfoEntity> wrapperPage = new QueryWrapper<>();
        PageInfoEntity page = new PageInfoEntity();
        page.setProgrammeId(pageInfoEntity.getProgrammeId());
        page.setIsDeleted(Global.DeleteStatus.NO.getId());
        page.setPageCode(body.getPageCode());
        wrapperPage.setEntity(page);
        List<PageInfoEntity> pageList = pageInfoMapper.selectList(wrapperPage);
        if (CollectionUtils.isNotEmpty(pageList)) {
            return (InlineResponse200) new InlineResponse200().errmsg("pageCode不能重复，请重新编辑！");
        }
        //查询cardConfig数据
        QueryWrapper<PageCardConfigEntity> wrapper = new QueryWrapper<>();
        PageCardConfigEntity pageCardConfigEntity = new PageCardConfigEntity();
        pageCardConfigEntity.setPageWid(body.getWid());
        pageCardConfigEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
        wrapper.setEntity(pageCardConfigEntity);
        List<PageCardConfigEntity> cardConfigList = pageCardConfigMapper.selectList(wrapper);
        //拷贝page数据
        String newPageWid = UUID.randomUUID().toString();
        pageInfoEntity.setCreateTime(date);
        pageInfoEntity.setUpdateTime(date);
        pageInfoEntity.setPageCode(body.getPageCode());
        pageInfoEntity.setWid(newPageWid);
        pageInfoEntity.setPageName(body.getPageName());
        pageInfoEntity.setTemplateCode(body.getTemplateCode());
        pageInfoEntity.setEnabledFlag(Global.DeleteStatus.YES.getId());
        pageInfoEntity.setPageType(Global.PageTypes.FREE_PAGE.getType());
        pageInfoEntity.setPageTitleLangKey(langKey);

        //解析cardlayout，将其中的cardWid进行修改
        String layout = handleLayoutCardWid(pageInfoEntity, cardConfigList, date, newPageWid);
        pageInfoEntity.setCardLayout(layout);

        pageInfoMapper.insert(pageInfoEntity);

        //插入国际化数据
        insertListLangByList(body.getPageTitleList(), langKey);

        //更新展示方案更新时间
        updateProgramTimeByWid(pageInfoEntity.getProgrammeId());
        return (InlineResponse200) new InlineResponse200().errcode(GlobalEnum.SUCCESS.getCode()).errmsg(GlobalEnum.SUCCESS.getMsg());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InlineResponse20024 saveMenuInfo(MenuInfoRes body) {
        Collection<ShowProgrammeEntity> programmeEntities =showProgrammeService.listByIds(com.google.common.collect.Lists.newArrayList(body.getProgrammeId()));
        List<String> siteWids = programmeEntities.stream().map(ent->ent.getSiteWid()).collect(Collectors.toList());
        MinosConsoleUtil.isRequestRightsLegal(siteWids);
        //属性key,雪花算法生成
        String langKey = CommunalUtil.getWid();
        //保存国际化表
        saveInternation(body, langKey);
        //保存菜单表
        MenuEntity menuEntity = saveMenu(body, langKey);
        //保存菜单授权表
        saveMenuAuth(body, menuEntity.getWid());
        //更新展示方案更新时间
        updateProgramTimeByWid(body.getProgrammeId());
        return new InlineResponse20024();
    }

    //保存菜单授权表
    private void saveMenuAuth(MenuInfoRes body, String menuWid) {
        //编辑菜单的情况，先把原先记录删除，再添加
        if (StringUtil.isNotEmpty(body.getWid())) {
            MenuEntity menuEntity = menuMapper.selectById(body.getWid());
            List<MenuAuthEntity> menuAuthEntities = menuAuthMapper.selectList(Wrappers.<MenuAuthEntity>lambdaQuery().eq(MenuAuthEntity::getMenuWid, menuEntity.getWid()));
            if (CollectionUtils.isNotEmpty(menuAuthEntities)) {
                menuAuthEntities.forEach(menuAuthEntity -> menuAuthEntity.setIsDeleted(Global.DeleteStatus.YES.getId()));
                menuAuthService.updateBatchById(menuAuthEntities);
            }
        }
        if (CollectionUtils.isNotEmpty(body.getMenuAuthInfo())) {
            List<MenuAuthEntity> menuAuthEntities = body.getMenuAuthInfo().stream().map(menuAuthInfoBiz -> {
                MenuAuthEntity menuAuthEntity = ObjectUtil.copyProperties(menuAuthInfoBiz, new MenuAuthEntity());
                menuAuthEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
                menuAuthEntity.setMenuAuthType(Global.MenuMenuAuthType.TOP_MENU.getId());
                menuAuthEntity.setMenuWid(menuWid);
                return menuAuthEntity;
            }).collect(Collectors.toList());
            menuAuthService.saveBatch(menuAuthEntities);
        }
    }

    //保存国际化表
    private void saveInternation(MenuInfoRes body, String langKey) {
        //编辑菜单的情况
        if (StringUtil.isNotEmpty(body.getWid())) {
            MenuEntity menuEntity = menuMapper.selectById(body.getWid());
            QueryWrapper<InternationalizationEntity> wrapper = new QueryWrapper<>();
            wrapper.eq(DbFieldConstant.LANG_KEY, menuEntity.getMenuNameLangKey());
            List<InternationalizationEntity> list = internationalizationMapper.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(list) && CollectionUtils.isNotEmpty(body.getMenuNameLangKey())) {
                //有就更新，没有则新增
                updateInternation( body.getMenuNameLangKey(), list);
            }
        } else {
            //新增菜单的情况
            if (CollectionUtils.isNotEmpty(body.getMenuNameLangKey())) {
                List<InternationalizationEntity> list = new ArrayList<>();
                for (MenuNameLangBiz menuNameLangBiz : body.getMenuNameLangKey()) {
                    InternationalizationEntity internationalization = new InternationalizationEntity();
                    ObjectUtil.copyProperties(menuNameLangBiz, internationalization);
                    internationalization.setIsDeleted(Global.DeleteStatus.NO.getId());
                    internationalization.setLangKey(langKey);
                    list.add(internationalization);
                }
                internationalizationService.saveBatch(list);
            }
        }

    }
    //更新菜单国际化信息
    private void updateInternation(List<MenuNameLangBiz> langkeys,List<InternationalizationEntity> list){
        String dbLangKey="";
        fromParams:     for (int i = langkeys.size()-1; i>=0; i--) {
                    MenuNameLangBiz menuNameLangBiz = langkeys.get(i);
                    for (int j = list.size() - 1; j >= 0; j--) {
                        InternationalizationEntity internationalization = list.get(j);
                        if (menuNameLangBiz.getLangCountry().equals(internationalization.getLangCountry())) {
                            dbLangKey=internationalization.getLangKey();
                            internationalization.setLangValue(menuNameLangBiz.getLangValue());
                            internationalization.setIsDeleted(Global.DeleteStatus.NO.getId());
                            internationalizationService.updateById(internationalization);
                            list.remove(j);
                            langkeys.remove(i);
                            continue fromParams;
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(langkeys)) {
                        List<MenuNameLangBiz> menuNameLangKey = langkeys;
                        List<InternationalizationEntity> internationas = new ArrayList<>();
                        String finalDbLangKey = dbLangKey;
                        menuNameLangKey.forEach(menuNameLangBiz->{
                            InternationalizationEntity internationalization = new InternationalizationEntity();
                            ObjectUtil.copyProperties(menuNameLangBiz, internationalization);
                            internationalization.setIsDeleted(Global.DeleteStatus.NO.getId());
                            internationalization.setLangKey(finalDbLangKey);
                            internationas.add(internationalization);
                        });
                        internationalizationService.saveBatch(internationas);
                    }
                if (CollectionUtils.isNotEmpty(list)) {
                    list.forEach(internationalizationEntity -> internationalizationEntity.setIsDeleted(Global.DeleteStatus.YES.getId()));
                    internationalizationService.updateBatchById(list);
                }
    }


    //保存菜单表
    private MenuEntity saveMenu(MenuInfoRes body, String langKey) {
        MenuEntity menuEntity = new MenuEntity();
        ObjectUtil.copyProperties(body, menuEntity);
        if (StringUtils.isNotEmpty(body.getWid())) {
            MenuEntity menu = menuMapper.selectById(body.getWid());
            menuEntity.setIsEnabled(menu.getIsEnabled());
        } else {
            //新增 状态为默认禁用
            menuEntity.setIsEnabled(Global.EnableStatus.DISABLE.getId());
        }
        menuEntity.setOpenType(null != body.getOpenType() ? body.getOpenType() : Global.openType.OPEN_OTHER_WINDOW_PAGE.getType());
        String parentId = StringUtil.isNotEmpty(body.getParentId()) ? body.getParentId() : Global.DEFAULT_MENU_ROOT_PARRNT_ID;
        menuEntity.setParentId(parentId);
        menuEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
        if (StringUtil.isEmpty(body.getWid())) {
            //新增
            menuEntity.setMenuNameLangKey(langKey);
            QueryWrapper<MenuEntity> wrapper = new QueryWrapper<>();
            wrapper.eq(DbFieldConstant.PARENT_ID, parentId);
            List<MenuEntity> menuEntities = menuMapper.selectList(wrapper);
            long orderNum = 0;
            if (CollectionUtils.isNotEmpty(menuEntities)) {
                //当前节点最大orderNum的数据
                MenuEntity menuEntityFir = menuEntities.stream().max(Comparator.comparingLong(MenuEntity::getOrderNumber)).get();
                orderNum = menuEntityFir.getOrderNumber() + 1;
            }
            menuEntity.setOrderNumber(orderNum);
            menuMapper.insert(menuEntity);
        } else {
            //修改
            //当选择内部页面的时候，判断 页面不能挂到两个菜单上
            if (StringUtil.isNotEmpty(body.getPageId())) {
                QueryWrapper<MenuEntity> wrapperFir = new QueryWrapper<>();
                wrapperFir.eq(DbFieldConstant.PAGE_ID, body.getPageId())
                        .eq(DbFieldConstant.IS_ENABLED, Global.EnableStatus.ENABLE.getId()).eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId());
                List<MenuEntity> menus = menuMapper.selectList(wrapperFir);
                if ((CollectionUtils.isNotEmpty(menus) && menus.size() > 1) || (menus.size() == 1 && !menus.get(0).getWid().equals(body.getWid()))) {
                    throw new BusinessException("该对应内部页面已挂载对应菜单,请勿重复挂载！");
                }
            }
            menuMapper.updateById(menuEntity);
        }
        return menuEntity;
    }

    //查询pageCode是否已存在
    //判断pageCode是否重复
    private void checkPageCode(String pageWid,String pagecode,String programmeId){
        //查询pageCode是否已存在
        QueryWrapper<PageInfoEntity> wrapper = new QueryWrapper<>();
        PageInfoEntity pageInfo = new PageInfoEntity();
        pageInfo.setIsDeleted(Global.DeleteStatus.NO.getId());
        pageInfo.setPageCode(pagecode);
        pageInfo.setProgrammeId(programmeId);
        wrapper.setEntity(pageInfo);
        List<PageInfoEntity> pageList = pageInfoMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(pageList)&&StringUtils.isBlank(pageWid)) {
            throw new BusinessException("pageCode不能重复，请重新编辑！");
        }else if(StringUtils.isNotBlank(pageWid)&&CollectionUtils.isNotEmpty(pageList)&&!(pageList.size() == 1 && pageList.get(0).getWid().equals(pageWid))){
            throw new BusinessException("pageCode不能重复，请重新编辑！");
        }
    }

    @Override
    public InlineResponse20025 savePageInfo(PageInfoRes body) {
        //校验DECLARE、CAST 和 EXEC 进行的 SQL 注入
        if(CollectionUtils.isNotEmpty(body.getCardConfig())){
            body.getCardConfig().forEach(k->{
                CommonUtil.checkReqParams(k.getCardConfig());
                CommonUtil.checkReqParams(k.getCardId());
            });
        }
        //定义全局展示方案id
        String programId = "";
        //仅新增时判断
        if(StringUtils.isEmpty(body.getWid())){
            if(StringUtils.isEmpty(body.getPageCode())){
                throw new BusinessException("pageCode不可以为空");
            }
            if(!StrUtil.isLetter(body.getPageCode())){
                throw new BusinessException("pageCode只能为英文大小写字母");
            }
        }

        //新增
        if (StringUtils.isBlank(body.getWid())) {
            //雪花id
            String langKey = CommunalUtil.getWid();
            //查询pageCode是否已存在
            checkPageCode(body.getWid(),body.getPageCode(),body.getProgrammeId());
            //新增必然是页面信息
            PageInfoEntity pageInfoEntity = (PageInfoEntity) ObjectUtil.copyProperties(body, new PageInfoEntity());
            pageInfoEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
            pageInfoEntity.setPageTitleLangKey(langKey);
            pageInfoEntity.setPageTitle(body.getPageTitleList().get(0).getLangValue());

            programId = body.getProgrammeId();
            // 权限校验
            MinosConsoleUtil.checkSiteAuth(Collections.singletonList(programId));
            pageInfoMapper.insert(pageInfoEntity);
            //页面标题国际化表插入
            insertListLangByList(body.getPageTitleList(), langKey);
        } else {
            //定义时间
            Date sysdate = CommonUtil.getSystemDate();
            //编辑
            //判断是保存页面信息还是保存布局配置等信息
            PageInfoEntity pageInfoOra = pageInfoMapper.selectById(body.getWid());
            //展示方案id赋值
            programId = pageInfoOra.getProgrammeId();
            // 权限校验
            MinosConsoleUtil.checkSiteAuth(Collections.singletonList(programId));
            if (StringUtils.isBlank(body.getCardLayout())) {
                //查询原来的数据
                //判断pageCode是否重复
                checkPageCode(body.getWid(),body.getPageCode(),body.getProgrammeId());
                //更新基本信息
                PageInfoEntity pageInfoEntity = ObjectUtil.copyProperties(body, new PageInfoEntity());
                pageInfoEntity.setCardLayout(pageInfoOra.getCardLayout());
                pageInfoEntity.setUpdateTime(sysdate);
                pageInfoMapper.updateById(pageInfoEntity);

                //查询之前的国际化表，循环遍历，有的更新，没有的新增
                savePageInternation(body);
            } else {
                //更新页面表cardLayout
                //查询原来的数据
                pageInfoOra.setUpdateTime(sysdate);
                pageInfoOra.setCardLayout(body.getCardLayout());
                pageInfoMapper.updateById(pageInfoOra);
                //删除所有运行时配置表
                QueryWrapper<PageCardConfigEntity> wrapper = new QueryWrapper<>();
                PageCardConfigEntity pageCardConfigEntity = new PageCardConfigEntity();
                pageCardConfigEntity.setPageWid(body.getWid());
                wrapper.setEntity(pageCardConfigEntity);
                //物理删，否则数据量会巨大
                pageCardConfigMapper.delete(wrapper);
                //新增所有运行时配置
                if (null != body.getCardConfig()) {
                    List<PageCardConfigEntity> pageCardConfigs = new ArrayList<>();
                    List cardList = body.getCardConfig();
                    for (int i = 0; i < cardList.size(); i++) {
                        PageCardConfigEntity pageCardConfig = ObjectUtil.copyProperties(cardList.get(i), new PageCardConfigEntity());
                        pageCardConfig.setIsDeleted(Global.DeleteStatus.NO.getId());
                        pageCardConfig.setPlatformType(body.getPlatformType());
                        pageCardConfig.setPageWid(body.getWid());
                        pageCardConfigs.add(pageCardConfig);
                    }
                    pageCardConfigService.saveBatch(pageCardConfigs);
                }
            }
        }
        //根据展示方案id更新展示方案更新时间
        updateProgramTimeByWid(programId);

        return new InlineResponse20025();
    }

    //保存国际化表
    private void savePageInternation(PageInfoRes body) {
        PageInfoEntity pageInfoEntity = pageInfoMapper.selectById(body.getWid());
        QueryWrapper<InternationalizationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(DbFieldConstant.LANG_KEY, pageInfoEntity.getPageTitleLangKey());
        List<InternationalizationEntity> list = internationalizationMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(list) && CollectionUtils.isNotEmpty(body.getPageTitleList())) {
            //有就更新，没有则新增
            updateInternation( body.getPageTitleList(), list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InlineResponse20020 saveProgrammeInfo(ProgrammeInfoRes body) {
        if(StringUtils.isNotBlank(body.getSiteWid())){
            // 检查站点管理员权限
            MinosConsoleUtil.isRequestRightsLegal( com.google.common.collect.Lists.newArrayList(body.getSiteWid()));
        }

        ShowProgrammeEntity showProgrammeEntity = (ShowProgrammeEntity) ObjectUtil.copyProperties(body, new ShowProgrammeEntity());
        //wid不存在就新增
        if (StringUtil.isEmpty(body.getWid())) {
            //查询初始化展示方案的templete_config
//            ShowProgrammeEntity showProgramme= showProgrammeService.getOne(new QueryWrapper<ShowProgrammeEntity>().lambda().eq(ShowProgrammeEntity::getWid, "2d8296b6d3bc49ec977af23029b3d718"));
            String programeId = StringUtil.simpleUuid();
            showProgrammeEntity.setWid(programeId);
            showProgrammeEntity.setIsDeleted(0);
            showProgrammeEntity.setPageStatus(1);
            showProgrammeEntity.setSiteWid(body.getSiteWid());
//            showProgrammeEntity.setTemplateConfig(showProgramme.getTemplateConfig());
            showProgrammeService.save(showProgrammeEntity);
            //保存初始化菜单、页面、侧边栏信息(从隐藏的展示方案中拿数据)
            saveCopyOfAllProgrammeInfo(false,programeId,StringUtil.isNotEmpty(String.valueOf(body.getPlatformType()))&&String.valueOf(body.getPlatformType()).equals(String.valueOf(Global.PlatformType.MOBILE.getType()))?
                    Global.ShowProgrammeInitWid.OFFICIAL_MOBILE_INTI_PROGRAMME_WID.getId():Global.ShowProgrammeInitWid.OFFICIAL_PC_INTI_PROGRAMME_WID.getId(),body.getTemplateId(),body.getPlatformType());
        } else {
            showProgrammeEntity.setUpdateTime(CommonUtil.getSystemDate());
            ShowProgrammeEntity showProgramme = showProgrammeService.getById(showProgrammeEntity.getWid());
            if(StringUtil.isNotEmpty(showProgrammeEntity.getTemplateId())&&!showProgrammeEntity.getTemplateId().equals(showProgramme.getTemplateId())){
                //TemplateId与数据库中不同，说明切换了模板
                updatePageInfo(showProgramme.getPlatformType(),showProgramme.getWid(),body.getTemplateId());
                String templateDefaultConfig = getTemplateDefaultConfig(showProgrammeEntity.getTemplateId(), showProgrammeEntity.getPlatformType());
                showProgrammeEntity.setTemplateConfig(templateDefaultConfig);
            }
            showProgrammeService.updateById(showProgrammeEntity);
        }

        return new InlineResponse20020();
    }

    //切换模板
    public void updatePageInfo(Integer platformType,String showProgrammeWid,String templateId){
        //需要更新的page
        List<PageInfoEntity> pageInfoEntityList = pageInfoService.list(new QueryWrapper<PageInfoEntity>().lambda().eq(PageInfoEntity::getProgrammeId, showProgrammeWid).orderByAsc(PageInfoEntity::getCreateTime));

        TemplateContent defaultTemplate=null;
        Map<String,TemplateContent > templateMap=null;
        Map<String, Object> result = getTemplate(templateId, platformType);
        defaultTemplate=null!=result.get("defaultTemplate")? (TemplateContent) result.get("defaultTemplate") :null;
        templateMap=null!=result.get("templateMap")?(Map<String, TemplateContent>) result.get("templateMap"):null;
        if(CollectionUtils.isNotEmpty(pageInfoEntityList)&&null!=defaultTemplate&&null!=templateMap){
            Map<String, TemplateContent> finalTemplateMap = templateMap;
            TemplateContent finalDefaultTemplate = defaultTemplate;
            pageInfoEntityList.forEach(pageInfoEntity -> {
                if(finalTemplateMap.containsKey(pageInfoEntity.getTemplateCode())){
                    pageInfoEntity.setPageConfig(finalTemplateMap.get(pageInfoEntity.getTemplateCode()).getDefaultPageConfig());
                }else{
                    pageInfoEntity.setTemplateCode(finalDefaultTemplate.getCode());
                    pageInfoEntity.setPageConfig(finalDefaultTemplate.getDefaultPageConfig());
                }
            });
            pageInfoService.updateBatchById(pageInfoEntityList);
        }
    }

    @Override
    public InlineResponse200 saveSidebarInfo(SidebarInfoBiz body) {

        SidebarEntity sidebarEntity = ObjectUtil.copyProperties(body, new SidebarEntity());

        // 权限校验
        MinosConsoleUtil.checkSiteAuth(Collections.singletonList(body.getProgrammeId()));

        //wid不存在就新增
        if (StringUtil.isEmpty(body.getWid())) {
            //雪花id
            String langKey = CommunalUtil.getWid();
            sidebarEntity.setDisplayNameLangKey(langKey);
            sidebarEntity.setColumnCode(StringUtil.simpleUuid());
            sidebarEntity.setOrderNumber(Long.valueOf(sidebarService.count(new QueryWrapper<SidebarEntity>().lambda().eq(SidebarEntity::getPositionType, body.getPositionType()).eq(SidebarEntity::getProgrammeId, body.getProgrammeId()).eq(SidebarEntity::getIsDeleted, Global.DeleteStatus.NO.getId())) + 1));
            sidebarEntity.setIsDeleted(0);
            sidebarService.save(sidebarEntity);
            //页面标题国际化表插入
            insertListLangByList(body.getMenuNameLang(), langKey);
        } else {
//            SidebarEntity sidebarServiceById = sidebarService.getById(body.getWid());
            sidebarEntity.setUpdateTime(CommonUtil.getSystemDate());
            sidebarService.updateById(sidebarEntity);
//            //删除国际化表
//            deleteInterByLangKey(sidebarServiceById.getDisplayNameLangKey());
//            //新增新的国际化表
//            insertListLangByList(body.getMenuNameLang(), sidebarServiceById.getDisplayNameLangKey());
            //国际化表入库
            saveSiderBarInternation(body);
        }
        //保存侧边栏授权表
        saveSidebarAuth(body, sidebarEntity.getWid());
        //更新展示方案更新时间
        updateProgramTimeByWid(body.getProgrammeId());

        return new InlineResponse200();
    }

    //保存国际化表
    private void saveSiderBarInternation(SidebarInfoBiz body) {
        SidebarEntity sidebarEntity = sidebarService.getById(body.getWid());
        QueryWrapper<InternationalizationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(DbFieldConstant.LANG_KEY, sidebarEntity.getDisplayNameLangKey());
        List<InternationalizationEntity> list = internationalizationMapper.selectList(wrapper);

        //有就更新，没有则新增
        for (MenuNameLangBiz menuNameLangBiz : body.getMenuNameLang()) {
            boolean flag = false;
            for (InternationalizationEntity internationalizationEntity : list) {
                if (menuNameLangBiz.getLangCountry().equals(internationalizationEntity.getLangCountry())) {
                    internationalizationEntity.setLangValue(menuNameLangBiz.getLangValue());
                    flag = true;
                    internationalizationService.updateById(internationalizationEntity);
                }
            }
            if (!flag) {
                InternationalizationEntity internationalization = new InternationalizationEntity();
                ObjectUtil.copyProperties(menuNameLangBiz, internationalization);
                internationalization.setIsDeleted(Global.DeleteStatus.NO.getId());
                internationalization.setLangKey(sidebarEntity.getDisplayNameLangKey());
                internationalizationService.save(internationalization);
            }
        }

    }

    //保存侧边栏授权表
    private void saveSidebarAuth(SidebarInfoBiz body, String menuWid) {
        //编辑侧边栏的情况，先把原先记录删除，再添加
        if (StringUtil.isNotEmpty(body.getWid())) {
            SidebarEntity sidebarEntity = sidebarService.getById(body.getWid());
            menuAuthMapper.delete(Wrappers.<MenuAuthEntity>lambdaQuery().eq(MenuAuthEntity::getMenuWid, sidebarEntity.getWid()));
        }
        if (CollectionUtils.isNotEmpty(body.getMenuAuthInfo())) {
            List<MenuAuthEntity> menuAuthEntities = body.getMenuAuthInfo().stream().map(menuAuthInfoBiz -> {
                MenuAuthEntity menuAuthEntity = ObjectUtil.copyProperties(menuAuthInfoBiz, new MenuAuthEntity());
                menuAuthEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
                menuAuthEntity.setMenuAuthType(Global.MenuMenuAuthType.SIDEBAR.getId());
                menuAuthEntity.setMenuWid(menuWid);
                return menuAuthEntity;
            }).collect(Collectors.toList());
            menuAuthService.saveBatch(menuAuthEntities);
        }
    }

    @Override
    public InlineResponse20010 support() {
        return null;
    }

    @Override
    public InlineResponse200 updateMenuSequence(List<SortInfo> body) {
        if (CollectionUtils.isNotEmpty(body)) {
            List<SortInfo> sortInfoList = getSortInfoList(body);
            List<String> wids = sortInfoList.stream().map(SortInfo::getWid).distinct().collect(Collectors.toList());
            Map<String, String> parentIdMap = sortInfoList.stream().collect(
                    Collectors.toMap(SortInfo::getWid,
                            SortInfo::getParentId, (oldVal, currVal) -> currVal));
            Map<String, String> orderNumMap = sortInfoList.stream().collect(
                    Collectors.toMap(SortInfo::getWid,
                            SortInfo::getOrderNum, (oldVal, currVal) -> currVal));

            QueryWrapper<MenuEntity> wrapper = new QueryWrapper<>();
            wrapper.in(DbFieldConstant.WID, wids);
            List<MenuEntity> list = menuService.list(wrapper);
            // 查询菜单归属的展示方案
            List<String> programmeIds = list.stream().map(ent->ent.getProgrammeId()).collect(Collectors.toList());
            // 查询展示方案归属的站点并检查权限
            Collection<ShowProgrammeEntity> programmeEntities =showProgrammeService.listByIds(com.google.common.collect.Lists.newArrayList(programmeIds));
            List<String> siteWids = programmeEntities.stream().map(ent->ent.getSiteWid()).collect(Collectors.toList());
            MinosConsoleUtil.isRequestRightsLegal(siteWids);
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(menuEntity -> {
                    menuEntity.setParentId(parentIdMap.get(menuEntity.getWid()));
                    menuEntity.setOrderNumber(Long.valueOf(orderNumMap.get(menuEntity.getWid())));
                });
            }
            menuService.updateBatchById(list);
            //更新展示方案更新时间
            updateProgramTimeByWid(list.get(0).getProgrammeId());
        }
        return new InlineResponse200();
    }

    /**
     * @return List<SortInfo>
     * @Author jcx
     * @Description tree转list
     * @Date 18:04 2020/9/25
     * @Param list:
     **/
    public List<SortInfo> getSortInfoList(List<SortInfo> list) {
        List<SortInfo> treeVosFir = new ArrayList<>();
        List<SortInfo> treeVosSec = new ArrayList<>();
        String temp;
        for (SortInfo sortInfo : list) {
            if (CollectionUtils.isNotEmpty(sortInfo.getChildMenu())) {
                treeVosFir = getSortInfoList(sortInfo.getChildMenu());
                temp = JSON.toJSONString(sortInfo).substring(JSON.toJSONString(sortInfo).lastIndexOf("],") + 2);
                temp = "{".concat(temp);
                SortInfo sortInfoFir = JSON.parseObject(temp, SortInfo.class);
                treeVosFir.add(sortInfoFir);
            } else {
                treeVosFir.add(sortInfo);
            }
            for (SortInfo treeVoFir : treeVosFir) {
                int count = 0;
                for (SortInfo treeVoSec : treeVosSec)
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


    @Override
    public InlineResponse200 updateMenuStatus(SidebarStatusReq body) {
        if (StringUtil.isEmpty(body.getWid())) {
            throw new BusinessException(GlobalEnum.VALIDATION.getMsg());
        } else {
            MenuEntity menuEntity = menuMapper.selectById(body.getWid());
            Collection<ShowProgrammeEntity> programmeEntities =showProgrammeService.listByIds(com.google.common.collect.Lists.newArrayList(menuEntity.getProgrammeId()));
            List<String> siteWids = programmeEntities.stream().map(ent->ent.getSiteWid()).collect(Collectors.toList());
            MinosConsoleUtil.isRequestRightsLegal(siteWids);
            if (null != menuEntity) {
                if (body.getEnabledFlag().equals(Global.EnableStatus.ENABLE.getId()) && StringUtil.isNotEmpty(menuEntity.getPageId())) {
                    //启用判断
                    QueryWrapper<MenuEntity> wrapperFir = new QueryWrapper<>();
                    wrapperFir.eq(DbFieldConstant.PAGE_ID, menuEntity.getPageId())
                            .eq(DbFieldConstant.IS_ENABLED, Global.EnableStatus.ENABLE.getId()).eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId());
                    List<MenuEntity> menus = menuMapper.selectList(wrapperFir);
                    if (CollectionUtils.isNotEmpty(menus)) {
                        throw new BusinessException("该对应内部页面已挂载对应菜单,请勿重复挂载！");
                    }
                }
                menuEntity.setIsEnabled(body.getEnabledFlag());

            }
            menuMapper.updateById(menuEntity);
            //更新展示方案更新时间
            updateProgramTimeByWid(menuEntity.getProgrammeId());
        }
        return new InlineResponse200();
    }

    @Override
    public InlineResponse200 updatePageStatusByWid(EnabledReq body) {
        //判断参数
        if (StringUtils.isEmpty(body.getWid()) || null == body.getEnabledFlag()) {
            return (InlineResponse200) new InlineResponse200().errmsg(GlobalEnum.PARAM_FAIL.getMsg()).errcode(GlobalEnum.PARAM_FAIL.getCode());
        }
        PageInfoEntity pageInfo = pageInfoMapper.selectById(body.getWid());
        // 权限校验
        MinosConsoleUtil.checkSiteAuth(Collections.singletonList(pageInfo.getProgrammeId()));

        //如果是停用则需要判断是否有菜单使用
        if (Global.DeleteStatus.NO.getId() == body.getEnabledFlag()) {
            QueryWrapper<MenuEntity> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(MenuEntity::getPageId, body.getWid()).eq(MenuEntity::getIsDeleted, Global.DeleteStatus.NO.getId());
            List<MenuEntity> menuList = menuService.list(wrapper);
            if (CollectionUtils.isNotEmpty(menuList)) {
                return (InlineResponse200) new InlineResponse200().errmsg("页面正在被引用，取消引用才可以停用！").errcode(GlobalEnum.ERROR.getCode());
            }
        }

        //更新入库
        Date date = CommonUtil.getSystemDate();
        pageInfo.setEnabledFlag(body.getEnabledFlag());
        pageInfo.setUpdateTime(date);
        pageInfoMapper.updateById(pageInfo);

        //更新展示方案更新时间
        updateProgramTimeByWid(pageInfo.getProgrammeId());
        return new InlineResponse200();
    }

    @Override
    public InlineResponse200 updateSidebarSequence(List<SidebarSequenceReqObj> body) {
        try {
            if (null == body || Global.NUMBER_ZERO >= body.size()) {
                return (InlineResponse200) new InlineResponse200().errcode(GlobalEnum.VALIDATION.getCode()).errmsg(GlobalEnum.VALIDATION.getMsg());
            }

            List<SidebarEntity> sidebarEntityList = new ArrayList<>();
            for (SidebarSequenceReqObj sidebarSequenceReqObj : body) {
                SidebarEntity sidebarEntity = ObjectUtil.copyProperties(sidebarSequenceReqObj, new SidebarEntity());
                sidebarEntityList.add(sidebarEntity);
            }


            SidebarEntity sidebarEntity = sidebarService.getById(body.get(0).getWid());
            // 权限校验
            MinosConsoleUtil.checkSiteAuth(Collections.singletonList(sidebarEntity.getProgrammeId()));

            //更新展示方案更新时间
            sidebarService.updateBatchById(sidebarEntityList);

            updateProgramTimeByWid(sidebarEntity.getProgrammeId());
            return new InlineResponse200();
        } catch ( MinosException e ) {
            return (InlineResponse200) new InlineResponse200().errcode(GlobalEnum.ERROR.getCode()).errmsg(e.getMessage());
        } catch (Exception e) {
            logger.error("updateSidebarSequence执行失败", e);
            return (InlineResponse200) new InlineResponse200().errcode(GlobalEnum.ERROR.getCode()).errmsg(GlobalEnum.ERROR.getMsg());
        }
    }

    @Override
    public InlineResponse200 updateSidebarStatus(SidebarStatusReq body) {

        try {
            if (null == body || StringUtil.isEmpty(body.getWid()) || null == body.getEnabledFlag()) {
                return (InlineResponse200) new InlineResponse200().errcode(GlobalEnum.VALIDATION.getCode()).errmsg(GlobalEnum.VALIDATION.getMsg());
            }

            UpdateWrapper<SidebarEntity> wrapper = new UpdateWrapper<>();
            wrapper.lambda().eq(SidebarEntity::getWid, body.getWid()).set(SidebarEntity::getEnabledFlag, body.getEnabledFlag()).set(SidebarEntity::getUpdateTime, CommonUtil.getSystemDate());

            SidebarEntity sidebarEntity = sidebarService.getById(body.getWid());
            if (null == sidebarEntity) {
                return (InlineResponse200) new InlineResponse200().errcode(GlobalEnum.GET_SIDEBAR_ERROR.getCode()).errmsg(GlobalEnum.GET_SIDEBAR_ERROR.getMsg());
            }
            Integer positionType = sidebarEntity.getPositionType();
            String programmeId = sidebarEntity.getProgrammeId();

            // 权限校验
            MinosConsoleUtil.checkSiteAuth(Collections.singletonList(programmeId));

            List<SidebarEntity> sidebarEntityList = sidebarService.list(new QueryWrapper<SidebarEntity>().lambda()
                    .eq(SidebarEntity::getPositionType, positionType)
                    .eq(SidebarEntity::getProgrammeId, programmeId)
                    .eq(SidebarEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                    .orderByAsc(SidebarEntity::getOrderNumber));
            if (Global.NUMBER_ZERO < sidebarEntityList.size()) {
                sidebarEntityList.remove(sidebarEntity.getOrderNumber() - 1);
            }
            List<SidebarEntity> enableSidebarList = sidebarEntityList.stream().filter(item -> (item.getEnabledFlag() == 1)).collect(Collectors.toList());
            List<SidebarEntity> disableSidebarList = sidebarEntityList.stream().filter(item -> (item.getEnabledFlag() == 0)).collect(Collectors.toList());
            //当前启用的数量
            int enableCount = enableSidebarList.size();
            if (body.getEnabledFlag() == Global.EnableStatus.ENABLE.getId()) {
                if (Global.NUMBER_ZERO < disableSidebarList.size()) {
                    AtomicLong index = new AtomicLong(1);
                    disableSidebarList.forEach(item -> item.setOrderNumber(enableCount + 1 + index.getAndIncrement()));
                    sidebarService.updateBatchById(disableSidebarList);
                }
            } else {
                if (Global.NUMBER_ZERO < enableSidebarList.size()) {
                    AtomicLong index = new AtomicLong(1);
                    enableSidebarList.forEach(item -> item.setOrderNumber(index.getAndIncrement()));
                    sidebarService.updateBatchById(enableSidebarList);
                }
            }

            wrapper.lambda().set(SidebarEntity::getOrderNumber, enableCount + 1);
            sidebarService.update(wrapper);
            //更新展示方案更新时间
            updateProgramTimeByWid(sidebarEntity.getProgrammeId());
            return new InlineResponse200();
        } catch ( MinosException e ) {
            return (InlineResponse200) new InlineResponse200().errcode(GlobalEnum.ERROR.getCode()).errmsg(e.getMessage());
        } catch (Exception e) {
            logger.error("updateSidebarStatus执行失败", e);
            return (InlineResponse200) new InlineResponse200().errcode(GlobalEnum.ERROR.getCode()).errmsg(GlobalEnum.ERROR.getMsg());
        }
    }

    @Override
    public InlineResponse200 updateStatusByWid(UpdateStatusReq body) {
        try {
            if (null == body || StringUtil.isEmpty(body.getWid()) || null == body.getPlatformType()) {
                return (InlineResponse200) new InlineResponse200().errcode(GlobalEnum.VALIDATION.getCode()).errmsg(GlobalEnum.VALIDATION.getMsg());
            }
            ShowProgrammeEntity programmeEntity = showProgrammeService.getById(body.getWid());
            MinosConsoleUtil.isRequestRightsLegal( com.google.common.collect.Lists.newArrayList(programmeEntity.getSiteWid()));
            /**
             * 停用时校验逻辑,如果站点启用了则不能停用pc端展示方案
             */
            if(Objects.equals(body.getStatus(),0)){
                SiteEntity siteEntity = siteMapper.selectById(programmeEntity.getSiteWid());
                if(Objects.equals(siteEntity.getIsEnabled(),1) && Objects.equals(programmeEntity.getPlatformType(), Global.PlatformType.PC.getType())){
                    return (InlineResponse200) new InlineResponse200().errcode(GlobalEnum.ERROR.getCode())
                            .errmsg("已经启用的站点，无法停用pc端展示方案");
                }
                programmeEntity.setPageStatus(Global.Status.DISABLE.getId());
                showProgrammeService.updateById(programmeEntity);
            }else{
                //将其他展示方案都更新为不启用
                LambdaUpdateWrapper<ShowProgrammeEntity> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(ShowProgrammeEntity::getPlatformType, body.getPlatformType())
                        .notIn(ShowProgrammeEntity::getWid, body.getWid())
                        .eq(ShowProgrammeEntity::getSiteWid,programmeEntity.getSiteWid())
                        .set(ShowProgrammeEntity::getPageStatus, Global.Status.DISABLE.getId());
                showProgrammeService.update(wrapper);

                //更新展示方案启用状态
                LambdaUpdateWrapper<ShowProgrammeEntity> wrapper2 = new LambdaUpdateWrapper<>();
                wrapper2.eq(ShowProgrammeEntity::getWid, body.getWid()).set(ShowProgrammeEntity::getPageStatus, Global.Status.ENABLE.getId());
                showProgrammeService.update(wrapper2);
            }
            return new InlineResponse200();
        } catch (MinosException e){
            logger.info("停用启用展示方案发生异常，原因为：",e);
            return (InlineResponse200) new InlineResponse200().errcode(e.getCode()).errmsg(e.getMessage());
        }catch (Exception e) {
            logger.info("停用启用展示方案发生异常，原因为：",e);
            return (InlineResponse200) new InlineResponse200().errcode(GlobalEnum.ERROR.getCode()).errmsg(GlobalEnum.ERROR.getMsg());
        }
    }

    public ShowProgrammeEntity getDefaultProgramme(String platformType) {
        return null;
//        return this.getOne(new LambdaQueryWrapper<ShowProgrammeEntity>()
//                .eq(ShowProgrammeEntity::getPageStatus, 1)
//                .eq(ShowProgrammeEntity::getPlatformType, platformType));
    }

    /**
     * @return List<MenuBiz>
     * @Author jcx
     * @Description 菜单list转树结构数据
     * @Date 18:05 2020/9/22
     * @Param list:
     **/
    private List<MenuBiz> menusToTree(List<MenuBiz> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            StructureTransitionUtil.listToTree(list, new StructureTransitionUtil
                    .TransformMatcher<MenuBiz>() {
                @Override
                public void transform(MenuBiz parent, MenuBiz child) {
                    if (null == parent.getMenu()) {
                        parent.setMenu(new ArrayList<MenuBiz>());
                    }
                    if (parent.getWid().equals(child.getParentId())) {
                        parent.getMenu().add(child);
                    }
                }

                @Override
                public boolean remove(MenuBiz object) {
                    return !Global.DEFAULT_MENU_ROOT_PARRNT_ID.equals(object.getParentId());
                }
            });
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * @return List<MenuNameLangBiz>
     * @Author jdwan
     * @Description 根据langkey获取所有国际化数据
     * @Date 18:05 2020/9/23
     * @Param langKey:
     **/
    private List<MenuNameLangBiz> queryListLangByLangKey(String langKey) {
        QueryWrapper<InternationalizationEntity> wrapperInter = new QueryWrapper<>();
        InternationalizationEntity interVo = new InternationalizationEntity();
        interVo.setLangKey(langKey);
        interVo.setIsDeleted(Global.DeleteStatus.NO.getId());
        wrapperInter.setEntity(interVo);
        List<InternationalizationEntity> interList = internationalizationMapper.selectList(wrapperInter);
        List<MenuNameLangBiz> langList = new ArrayList<>();
        for (InternationalizationEntity internationalizationEntity : interList) {
            MenuNameLangBiz langObj = (MenuNameLangBiz) ObjectUtil.copyProperties(internationalizationEntity, new MenuNameLangBiz());
            langList.add(langObj);
        }
        return langList;
    }

    /**
     * @return void
     * @Author jdwan
     * @Description 根据列表新增国际化数据
     * @Date 18:05 2020/9/23
     * @Param langKey:List<MenuNameLangBiz>
     **/
    private void insertListLangByList(List<MenuNameLangBiz> langList, String langKey) {
        if (CollectionUtils.isNotEmpty(langList)) {
            List<InternationalizationEntity> internationalizations = new ArrayList<>();
            for (MenuNameLangBiz MenuNameLangBiz : langList) {
                InternationalizationEntity internationalizationEntity = (InternationalizationEntity) ObjectUtil.copyProperties(MenuNameLangBiz, new InternationalizationEntity());
                internationalizationEntity.setLangKey(langKey);
                internationalizationEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
                internationalizations.add(internationalizationEntity);
            }
            internationalizationService.saveBatch(internationalizations);
        }
    }

    /**
     * @return void
     * @Author jdwan
     * @Description 根据langkey删除国际化数据
     * @Date 18:05 2020/9/23
     * @Param langKey:List<MenuNameLangBiz>
     **/
    private void deleteInterByLangKey(String langKey) {
        QueryWrapper<InternationalizationEntity> wrapperInter = new QueryWrapper<>();
        InternationalizationEntity interVo = new InternationalizationEntity();
        interVo.setLangKey(langKey);
        wrapperInter.setEntity(interVo);
        internationalizationMapper.delete(wrapperInter);
    }

    /**
     * @return void
     * @Author jdwan
     * @Description 处理卡片
     * @Date 18:05 2020/9/23
     * @Param langKey:List<MenuNameLangBiz>
     **/
    private String handleLayoutCardWid(PageInfoEntity pageInfoEntity, List<PageCardConfigEntity> cardConfigList, Date date, String pageUuid) {
        String layout = pageInfoEntity.getCardLayout();
        //容器json转list获取所有的cardwid
        List<LayoutRow> rows = JSONObject.parseArray(layout, LayoutRow.class);
        List<String> cardWidList = renderRows(rows);
        //循环替换和保存配置信息
        List<PageCardConfigEntity> pageCardConfigs = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(cardWidList)){
            for (int i = 0; i < cardWidList.size(); i++) {
                String cardOldWid = cardWidList.get(i);
                String cardNewWid = String.valueOf(Math.random()).substring(2);
                layout = layout.replace(cardOldWid, cardNewWid);
                //匹配替换了的卡片配置，并且入库
                if (null != cardConfigList) {
                    for (PageCardConfigEntity pageCardConfig : cardConfigList) {
                        if (cardOldWid.equals(pageCardConfig.getCardWid())) {
                            pageCardConfig.setWid(UUID.randomUUID().toString());
                            pageCardConfig.setPageWid(pageUuid);
                            pageCardConfig.setCardWid(cardNewWid);
                            pageCardConfig.setCreateTime(date);
                            pageCardConfig.setUpdateTime(date);
                            pageCardConfigs.add(pageCardConfig);
                        }
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(pageCardConfigs)){
                pageCardConfigService.saveBatch(pageCardConfigs);
            }
        }
        return layout;
    }

    /**
     * 遍历行元素中的wid
     *
     * @param rows
     * @return
     */
    private List<String> renderRows(List<LayoutRow> rows) {
        List<String> cardWidList = new ArrayList<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(rows)) {
            for (LayoutRow row : rows) {
                if (row != null && !org.springframework.util.CollectionUtils.isEmpty(row.getColumns())) {
                    List<String> widList = renderColumns(row.getColumns());
                    cardWidList.addAll(widList);
                }
            }
        }
        return cardWidList;
    }

    /**
     * 获取列元素中的cardWid
     *
     * @param columns
     * @return
     */
    private List<String> renderColumns(List<LayoutColumn> columns) {
        List<String> cardWidList = new ArrayList<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(columns)) {
            List<String> widList = new ArrayList<>();
            for (LayoutColumn column : columns) {
                if (null != column.getCard()) {
                    widList.add(column.getCard().getCardWid());
                } else if (null != column.getCards()) {
                    for (int i = 0; i < column.getCards().getCards().size(); i++) {
                        widList.add(column.getCards().getCards().get(i).getCardWid());
                    }
                } else if (!org.springframework.util.CollectionUtils.isEmpty(column.getRows())) {
                    widList = renderRows(column.getRows());
                }
                cardWidList.addAll(widList);
            }
        }
        return cardWidList;
    }

    /**
     * 获取列元素中的cardWid
     *
     * @param programId 展示方案id
     * @return
     */
    private void updateProgramTimeByWid(String programId) {
        Date sysdate = CommonUtil.getSystemDate();
        ShowProgrammeEntity showProgrammeEntity = showProgrammeService.getOne(new QueryWrapper<ShowProgrammeEntity>().lambda().eq(ShowProgrammeEntity::getWid, programId));
        showProgrammeEntity.setUpdateTime(sysdate);
        showProgrammeService.updateById(showProgrammeEntity);
    }

    @Override
    public InlineResponse200 savePopupWindow(PopupWindowReq body) {
        // 权限校验
        MinosConsoleUtil.checkSiteAuth(Collections.singletonList(body.getPageWid()));

        PopupWindowEntity popupWindowEntity = new PopupWindowEntity();
        BeanUtils.copyProperties(body, popupWindowEntity);
        popupWindowEntity.setBegTime(DateUtil.asDate(body.getBegTime()));
        popupWindowEntity.setEndTime(DateUtil.asDate(body.getEndTime()));
        String titleLangKey = CommunalUtil.getWid();
        String contentsLangKey = CommunalUtil.getWid();
        body.getPopTitle().forEach(e -> {
            InternationalizationEntity multiLangData = new InternationalizationEntity();
            BeanUtils.copyProperties(e, multiLangData);
            multiLangData.setIsDeleted(0);
            multiLangData.setLangKey(titleLangKey);
            internationalizationService.save(multiLangData);
            if ("zh_CN".equals(e.getLangCountry())) {
                popupWindowEntity.setPopTitle(e.getLangValue());
            }
        });
        String contents= JSON.toJSONString(body.getPopContents());
        popupWindowEntity.setPopContents(contents);
        popupWindowEntity.setTitleLangKey(titleLangKey);
        popupWindowEntity.setContentsLangKey(contentsLangKey);
        if(popupWindowEntity.getIsEnabled()==Global.EnableStatus.ENABLE.getId()){
            String dateStr = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
            String cacheKey = CommonUtil
                    .getCacheKey(Global.REDIS_POPUP_WINDOW_VISIT, dateStr);
            minosCacheTemplate.getIncr(cacheKey, 172800000);
        }
        if (StringUtils.isBlank(popupWindowEntity.getWid())) {
            popupWindowMapper.insert(popupWindowEntity);
        } else {
            popupWindowMapper.updateById(popupWindowEntity);
            Map<String, Object> map = Maps.newHashMap();
            map.put("lang_key", body.getTitleLangKey());
            internationalizationService.removeByMap(map);
        }
        redisTemplate.delete(POP_WINDOW_PREFIX);
        return new InlineResponse200();
    }

    @Override
    public PopupWindowRes queryPopupWindow(PopupWindowReq body) {
        if (org.apache.commons.lang3.StringUtils.isBlank(body.getPageWid())) {
            throw new BusinessException("缺少展示方案id");
        }

        // 权限校验
        MinosConsoleUtil.checkSiteAuth(Collections.singletonList(body.getPageWid()));

        QueryWrapper<PopupWindowEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PopupWindowEntity::getPageWid, body.getPageWid());
        PopupWindowEntity popupWindowEntity = popupWindowMapper.selectOne(queryWrapper);
        PopupWindowRes response = new PopupWindowRes();
        if (null != popupWindowEntity) {
            QueryWrapper<InternationalizationEntity> query = new QueryWrapper<>();
            query.lambda()
                .eq(InternationalizationEntity::getLangKey, popupWindowEntity.getTitleLangKey())
                .eq(InternationalizationEntity::getIsDeleted, 0);
            List<InternationalizationEntity> list = internationalizationMapper.selectList(query);
            List<MultiLangData> multiLangDataList = list.stream().map(e -> {
                MultiLangData multiLangData = new MultiLangData();
                BeanUtils.copyProperties(e, multiLangData);
                return multiLangData;
            }).collect(Collectors.toList());
            List<MultiLangData> contentsMultiLangDataList = Lists.newArrayList();
            try {
                contentsMultiLangDataList = JSON.parseArray(cardUtil.filterHttpOrHttps(popupWindowEntity.getPopContents()), MultiLangData.class);
            } catch (Exception e) {
                logger.error("查询公共弹窗发生异常："+e);
                if(org.apache.commons.lang3.StringUtils.isNotBlank(popupWindowEntity.getContentsLangKey())){
                    QueryWrapper<InternationalizationEntity> queryContents = new QueryWrapper<>();
                    queryContents.lambda()
                        .eq(InternationalizationEntity::getLangKey, popupWindowEntity.getContentsLangKey())
                        .eq(InternationalizationEntity::getIsDeleted, 0);
                    List<InternationalizationEntity> contentList = internationalizationMapper.selectList(queryContents);
                    contentsMultiLangDataList = contentList.stream().map(i -> {
                        MultiLangData multiLangData = new MultiLangData();
                        BeanUtils.copyProperties(i, multiLangData);
                        return multiLangData;
                    }).collect(Collectors.toList());
                }else{
                    MultiLangData multiLangData = new MultiLangData();
                    multiLangData.setLangCountry(Global.DEFAULT_LANGUAGE);
                    multiLangData.setLangValue(cardUtil.filterHttpOrHttps(popupWindowEntity.getPopContents()));
                    contentsMultiLangDataList.add(multiLangData);
                }
            }
            PopupWindowInfo result = new PopupWindowInfo();
            BeanUtils.copyProperties(popupWindowEntity, result);
            result.setBegTime(DateUtil.asLocalDateTime(popupWindowEntity.getBegTime()));
            result.setEndTime(DateUtil.asLocalDateTime(popupWindowEntity.getEndTime()));
            result.setPopTitle(multiLangDataList);
            result.setPopContents(contentsMultiLangDataList);
            response.setData(result);
        }
        return response;
    }

    @Override
    public InlineResponse200 updatePopupWindowStatus(PopupWindowReq body) {
        redisTemplate.opsForSet().add(POP_WINDOW_PREFIX, body.getWid());
        return new InlineResponse200();
    }

    @Override
    public PopupWindowRes queryPopupWindowDisplay(PopupWindowReq body) {
        if (org.apache.commons.lang3.StringUtils.isBlank(body.getPageWid())) {
            throw new BusinessException("缺少展示方案id");
        }
        UserInfo currentUser = userUtil.getCurrentUser();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(body.getWid())) {
            Boolean flag = redisTemplate.opsForSet().isMember(POP_WINDOW_PREFIX, body.getWid());
            if (null != flag && !flag) {
                Date now = new Date();
                QueryWrapper<PopupWindowEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(PopupWindowEntity::getIsEnabled, 1)
                    .eq(PopupWindowEntity::getPageWid, body.getPageWid())
                    .eq(PopupWindowEntity::getIsDeleted, 0);
                List<PopupWindowEntity> list = popupWindowMapper
                    .selectList(queryWrapper);
                // 把时间筛选放在内存中，减少sql的枚举值，有效利用mybatis plus的二级缓存
                list = list.stream().filter( item -> item.getBegTime().before(now) && item.getEndTime().after(now)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(list)) {
                    return getPopupWindowRes( currentUser, list);
                }
            }
        }
        return new PopupWindowRes();
    }

    private PopupWindowRes getPopupWindowRes(UserInfo currentUser,List<PopupWindowEntity> popupWindows){
        String lang=Global.DEFAULT_LANGUAGE;
        if(null!=currentUser){
            lang=currentUser.getPreferredLanguage();
        }
        List<MultiLangData> multiLangDataList = getMultiLangDataList(popupWindows, lang);
        List<MultiLangData> contentsMultiLangDataList = Lists.newArrayList();
        try {
            contentsMultiLangDataList = JSON.parseArray(cardUtil.filterHttpOrHttps(popupWindows.get(0).getPopContents()), MultiLangData.class);
            if(CollectionUtils.isNotEmpty(contentsMultiLangDataList))
            {
                String finalLang = lang;
                contentsMultiLangDataList=contentsMultiLangDataList.stream().filter(
                        e->e.getLangCountry().equals(finalLang)).collect(
                        Collectors.toList());
            }
        } catch (Exception e) {
            logger.error("查询公共弹窗发生异常："+e);
            contentsMultiLangDataList = getMultiLangData(popupWindows, lang,contentsMultiLangDataList);
        }
        PopupWindowInfo result = new PopupWindowInfo();
        BeanUtils.copyProperties(popupWindows.get(0), result);
        result.setPopTitle(multiLangDataList);
        result.setPopContents(contentsMultiLangDataList);
        PopupWindowRes response = new PopupWindowRes();
        response.setData(result);
        return response;
    }

    private List<MultiLangData> getMultiLangDataList(List<PopupWindowEntity> popupWindowEntity,
        String lang) {
        QueryWrapper<InternationalizationEntity> query = new QueryWrapper<>();
        query.lambda().eq(InternationalizationEntity::getLangKey,
            popupWindowEntity.get(0).getTitleLangKey())
            .eq(InternationalizationEntity::getLangCountry, lang)
            .eq(InternationalizationEntity::getIsDeleted, 0);
        List<InternationalizationEntity> list = internationalizationMapper
            .selectList(query);
        return list.stream().map(e -> {
            MultiLangData multiLangData = new MultiLangData();
            BeanUtils.copyProperties(e, multiLangData);
            multiLangData.setLangValue(cardUtil.filterHttpOrHttps(multiLangData.getLangValue()));
            return multiLangData;
        }).collect(Collectors.toList());
    }

    private List<MultiLangData> getMultiLangData(List<PopupWindowEntity> popupWindowEntity,
        String lang, List<MultiLangData> contentsMultiLangDataList) {
        if(org.apache.commons.lang3.StringUtils.isNotBlank(popupWindowEntity.get(0).getContentsLangKey()))
        {
            QueryWrapper<InternationalizationEntity> queryContents = new QueryWrapper<>();
            queryContents.lambda().eq(InternationalizationEntity::getLangKey,
                popupWindowEntity.get(0).getContentsLangKey())
                .eq(InternationalizationEntity::getLangCountry, lang)
                .eq(InternationalizationEntity::getIsDeleted, 0);
            List<InternationalizationEntity> contentsList = internationalizationMapper
                .selectList(queryContents);
            contentsMultiLangDataList = contentsList.stream().map(i -> {
                MultiLangData multiLangData = new MultiLangData();
                multiLangData.setLangValue(cardUtil.filterHttpOrHttps(multiLangData.getLangValue()));
                BeanUtils.copyProperties(i, multiLangData);
                return multiLangData;
            }).collect(Collectors.toList());
        }else{
            MultiLangData multiLangData = new MultiLangData();
            multiLangData.setLangCountry(Global.DEFAULT_LANGUAGE);
            multiLangData.setLangValue(cardUtil.filterHttpOrHttps(popupWindowEntity.get(0).getPopContents()));
            contentsMultiLangDataList.add(multiLangData);
        }
        return contentsMultiLangDataList;
    }

    public String ClobToString(Clob clob) throws SQLException, IOException {
        String reString = "";
        if (null == clob) {
            return reString;
        }
        // 得到流
        Reader is = clob.getCharacterStream();
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuilder sb = new StringBuilder();
        // 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
        while (s != null) {
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        return reString;
    }
}
