package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisedu.casp.sim.api.DubboServiceItemService;
import com.wisedu.casp.sim.model.DubboResponse;
import com.wisedu.minos.api.data.UserService;
import com.wisedu.minos.casp.portal.PortalManagerProperties;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.ObjectUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.*;
import com.wisedu.minos.casp.portal.dao.mapper.SelectMenuMapper;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.v353beta2.*;
import com.wisedu.minos.casp.portal.service.v311beta2.MultiSitesAdapter;
import com.wisedu.minos.casp.portal.service.v353beta2.SelectMenuApiAdapter;
import com.wisedu.minos.casp.portal.utils.MinosConsoleUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.oss.client.bean.OSSFileBean;
import com.wisedu.minos.oss.client.util.OSSClientUtil;
import com.wisedu.minos.util.MinosCommonUtil;
import com.wisedu.minos.util.MinosException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
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
 * @title SelectMenuService
 * @Author: 01120034
 * @Date: 2022/9/28
 */
@Service
public class SelectMenuService extends MyServiceImpl<SelectMenuMapper, SelectMenuEntity> implements SelectMenuApiAdapter  {
    private static Logger logger = LogManager.getLogger(SelectMenuService.class);
    @Autowired
    private InternationalizationService langService;
    @Autowired
    private PageInfoService pageInfoService;
    @Autowired
    private MenuAuthService menuAuthService;
    @Autowired
    private MinosApiAdapterImpl minosApi;
    @Autowired
    private ProgrammeApiAdapterImpl programmeApiAdapter;
    @Autowired
    private OSSClientUtil ossClientUtil;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private MultiSitesAdapter multiSitesAdapter;
    @Autowired
    private PortalManagerProperties portalManagerProperties;
    @DubboReference
    private UserService userService;
    @DubboReference
    private DubboServiceItemService dubboServiceItemService;
    @Value("${module.domain:http://127.0.0.1:8116}")
    private String moudleDomain;
    @Override
    public CommonRes deletedSelectMenu (SelectMenuDetailReq body) {
        checkParams(body.getWid(),body.getProgrammeWid());
        // 权限校验
        MinosConsoleUtil.checkSiteAuth(Collections.singletonList(body.getProgrammeWid()));

        SelectMenuEntity selectMenuEntity = this.getOne(new QueryWrapper<SelectMenuEntity>().lambda().eq(SelectMenuEntity::getProgrammeId, body.getProgrammeWid()).eq(SelectMenuEntity::getWid, body.getWid()));
        if(null!=selectMenuEntity){
            selectMenuEntity.setIsDeleted(Global.DeleteStatus.YES.getId());
            this.update(selectMenuEntity,new QueryWrapper<SelectMenuEntity>().lambda().eq(SelectMenuEntity::getProgrammeId,selectMenuEntity.getProgrammeId()).eq(SelectMenuEntity::getWid,selectMenuEntity.getWid()));
            langService.remove(new QueryWrapper<InternationalizationEntity>().lambda().eq(InternationalizationEntity::getLangKey,selectMenuEntity.getMenuNameLangKey()));
            menuAuthService.remove(new QueryWrapper<MenuAuthEntity>().lambda().eq(MenuAuthEntity::getMenuWid,selectMenuEntity.getWid()));
        }
        return new CommonRes();
    }

    @Override
    public CommonRes editStatus (EditStatusReq body) {
        checkParams(body.getWid(),body.getProgrammeWid());
        if(Global.EnableStatus.ENABLE.getId()!=body.getIsEnabled()&&Global.EnableStatus.DISABLE.getId()!=body.getIsEnabled()){
            throw new MinosException("参数错误");
        }
        // 权限校验
        MinosConsoleUtil.checkSiteAuth(Collections.singletonList(body.getProgrammeWid()));

        SelectMenuEntity selectMenuEntity = this.getOne(new QueryWrapper<SelectMenuEntity>().lambda().eq(SelectMenuEntity::getProgrammeId, body.getProgrammeWid()).eq(SelectMenuEntity::getWid, body.getWid()));
        if(null!=selectMenuEntity){
            selectMenuEntity.setIsEnabled(body.getIsEnabled());
            this.update(selectMenuEntity,new QueryWrapper<SelectMenuEntity>().lambda().eq(SelectMenuEntity::getProgrammeId,selectMenuEntity.getProgrammeId()).eq(SelectMenuEntity::getWid,selectMenuEntity.getWid()));
        }
        return new CommonRes();
    }

    private void checkParams(String wid,String programmeWid){
        if(StringUtil.isBlank(wid)||StringUtil.isBlank(programmeWid)){
            throw new MinosException("参数错误");
        }
    }

    /***
     * @Author jcx
     * @Description 门户前台查询下拉菜单
     * @Date 15:17 2022/10/11
     * @Param req:
     * @Param request:
     * @return List<PortalSelectMenusRes>
     **/
    public List<PortalSelectMenusRes> queryPortalSelectMenus(PortalSelectMenusReq req,HttpServletRequest request){
        List<PortalSelectMenusRes> result = Lists.newArrayList();
        try {
            List<SelectMenuEntity> list = this.list(new QueryWrapper<SelectMenuEntity>().lambda().eq(SelectMenuEntity::getProgrammeId, req.getProgrammeWid())
                    .eq(SelectMenuEntity::getIsDeleted, Global.DeleteStatus.NO.getId()).eq(SelectMenuEntity::getIsEnabled,Global.EnableStatus.ENABLE.getId()).orderByAsc(SelectMenuEntity::getOrderNumber));
            if(CollectionUtils.isNotEmpty(list)){
                List<String> langKeys = list.stream().map(SelectMenuEntity::getMenuNameLangKey).collect(Collectors.toList());
                List<InternationalizationEntity> langData = langService.list(new QueryWrapper<InternationalizationEntity>().lambda().eq(InternationalizationEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                        .in(InternationalizationEntity::getLangKey, langKeys));
                Map<String, List<InternationalizationEntity>> langMap = langData.stream().collect(Collectors.groupingBy(InternationalizationEntity::getLangKey));
                Map<String, String> urlMap = filterMenuByLicense(list, true);
                if(CollectionUtils.isNotEmpty(list)){
                    //内部页面
                    Map<String, String> pageMap=null;
                    List<SelectMenuEntity> innerPageList = list.stream().filter(k -> Global.MenuType.INNER_LINK.getId() == k.getMenuType()).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(innerPageList)){
                        List<PageInfoEntity> pageList = pageInfoService.list(new QueryWrapper<PageInfoEntity>().lambda().in(PageInfoEntity::getWid,
                                innerPageList.stream().map(SelectMenuEntity::getPageId).collect(Collectors.toList()))
                                .eq(PageInfoEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));
                        pageMap = pageList.stream().collect(Collectors.toMap(PageInfoEntity::getWid, PageInfoEntity::getPageCode));
                    }
                    Map<String, String> finalPageMap = pageMap;
                    return ObjectUtil.copyListProperties(list, PortalSelectMenusRes::new, (entity, res) ->{
                        if(Global.MenuType.INNER_LINK.getId() == entity.getMenuType()&&MapUtils.isNotEmpty(finalPageMap)){
                            //内部页面
                            String siteRoute = multiSitesAdapter.getRouteByRequest(request);
                            res.setUrl(moudleDomain+siteRoute + Global.INDEX_URI + "#/"+finalPageMap.get(entity.getPageId()));
                        }else if(Global.MenuType.OUTER_LINK.getId() == entity.getMenuType()){
                            //第三方链接
                            res.setUrl(entity.getLinkUrl());
                            if(Global.YES==entity.getIsSystemMenu()){
                                //系统菜单
                                if(StringUtils.isBlank(entity.getLinkUrl())){
                                    if(entity.getWid().equals(PortalManagerProperties.PERSON_CENTER_URL)){
                                        //个人中心
                                        res.setUrl(PortalManagerProperties.getPersonCenterUrlSelect());
                                    }else if(entity.getWid().equals(PortalManagerProperties.POSTITION_MANAGER_URL)){
                                        //岗位中心
                                        res.setUrl(urlMap.get(Global.POSITION_URL));
                                    }else if(entity.getWid().equals(PortalManagerProperties.BACKEND_MANAGE_URL)){
                                        //管控台
                                        res.setUrl(PortalManagerProperties.getBackendManageUrlSelect());
                                    }else if(entity.getWid().equals(PortalManagerProperties.SIM_MANAGE_URL)){
                                        //事项管理
                                        res.setUrl(urlMap.get(Global.COOSK_CONSOLE_URL));
                                    }else if(entity.getWid().equals(PortalManagerProperties.SIM_PORTAL_URL)){
                                        //效能看板
                                        res.setUrl(urlMap.get(Global.COOSK_PORTAL_URL));
                                    }
                                }
                            }
                        }
                        if(Global.SelectMenuIconType.PNG.getId()==entity.getIconType()&&StringUtils.isNotBlank(entity.getIconPath())){
                            List<OSSFileBean> beans = new ArrayList<>();
                            OSSFileBean bean = new OSSFileBean();
                            bean.setDir(entity.getIconPath());
                            bean.setObjectName(entity.getIconName());
                            beans.add(bean);
                            beans = ossClientUtil.getFileUrlsWithLocal(Global.OSS_FILE_BACKET_USERICON, beans);
                            beans.get(0).setObjectName(entity.getMenuName());
                            res.setIconUrl(beans.get(0).getIconUrl());
                        }
                        if(Global.SelectMenuIconType.FONT.getId()==entity.getIconType()&&StringUtils.isNotBlank(entity.getIconPath())){
                            res.setIconUrl(entity.getIconPath());
                        }
                        Map<String, String> menuLangMap=langMap.containsKey(entity.getMenuNameLangKey())&&null!=langMap.get(entity.getMenuNameLangKey())?
                                langMap.get(entity.getMenuNameLangKey()).stream().collect(HashMap::new,(m, v)->m.put(v.getLangCountry(), v.getLangValue()), HashMap::putAll):Collections.emptyMap();
                        res.setMenuName(MapUtils.isNotEmpty(menuLangMap)&&menuLangMap.containsKey(req.getLang())&&null!=menuLangMap.get(req.getLang())?menuLangMap.get(req.getLang()):entity.getMenuName());
                    });
                }
            }
        } catch ( Exception e ) {
            logger.error("queryPortalSelectMenus查询发生错误：{}",e);
        }
        return result;
    }

    /***
     * @Author jcx
     * @Description 根据license、授权类型 过滤下拉菜单
     * @Date 16:29 2022/10/8
     * @Param list:
     * @Param ischeckUserLogin 是否校验登录规则
     * @return void
     **/
    private Map<String, String> filterMenuByLicense(List<SelectMenuEntity> list,boolean ischeckUserLogin){
        Map<String, String> result= Maps.newHashMap();
        List<MenuAuthEntity> authedMenuAuths = menuAuthService.getAuthedMenus(Global.MenuMenuAuthType.SELECT_MENU);
        UserInfo currentUser = userUtil.getCurrentUser();
        SystemMenuEnable systemMenuEnable = PortalManagerProperties.getSystemMenuEnable();
        String positionUrl ="";
        String cooskConsoleUrl ="";
        String cooskPortalUrl ="";
        for(int i=list.size()-1;i>=0;i--){
            SelectMenuEntity selectMenuEntity = list.get(i);
            //系统菜单检查是否可用
            if((selectMenuEntity.getWid().equals(PortalManagerProperties.PERSON_CENTER_URL)&&!systemMenuEnable.getPersonCenter())||
                    (selectMenuEntity.getWid().equals(PortalManagerProperties.POSTITION_MANAGER_URL)&&!systemMenuEnable.getPostitionManager())||
                    (selectMenuEntity.getWid().equals(PortalManagerProperties.BACKEND_MANAGE_URL)&&!systemMenuEnable.getBackendManager())||
                    (selectMenuEntity.getWid().equals(PortalManagerProperties.SIM_MANAGE_URL)&&!systemMenuEnable.getSimManager())||
                    (selectMenuEntity.getWid().equals(PortalManagerProperties.SIM_PORTAL_URL)&&!systemMenuEnable.getSimPortal())){
                list.remove(selectMenuEntity);
                continue;
            }
            if(ischeckUserLogin){
                if(null!=currentUser){
                    //登录状态
                    if(Global.AuthType.VISITOR_VISIBLE.getId()==selectMenuEntity.getAuthType()){
                        //移除游客可见
                        list.remove(selectMenuEntity);
                    }else if(Global.AuthType.AUTH_VISIBLE.getId()==selectMenuEntity.getAuthType()){
                        //授权后可见，判断是否在范围，不在则移除
                        if(CollectionUtils.isNotEmpty(authedMenuAuths)){
                            List<String> collect = authedMenuAuths.stream().map(MenuAuthEntity::getMenuWid).distinct().collect(Collectors.toList());
                            if(collect.contains(selectMenuEntity.getWid())){
                                continue;
                            }
                        }
                        list.remove(selectMenuEntity);
                    }

                    boolean authFlag = true;
                    if(selectMenuEntity.getWid().equals(PortalManagerProperties.BACKEND_MANAGE_URL)){
                        //检查是否有管控台权限
                        authFlag = userService.checkManagerUser(currentUser.getUserAccount());
                    }else if(selectMenuEntity.getWid().equals(PortalManagerProperties.POSTITION_MANAGER_URL)){
                        //检查是否是岗位中心管理员
                        positionUrl = portalManagerProperties.getPositionUrl(currentUser.getUserAccount());
                        authFlag = StringUtils.isNotBlank(positionUrl);
                    }else if(selectMenuEntity.getWid().equals(PortalManagerProperties.SIM_MANAGE_URL)){
                        //检查是否有coosk管控台权限
                        DubboResponse<String> cooskConsoleAuth = dubboServiceItemService.checkUserConsoleAuth(userUtil.getUserAccount());
                        if("0".equals(cooskConsoleAuth.getErrcode())&&StringUtils.isNotBlank(cooskConsoleAuth.getData())){
                            cooskConsoleUrl=cooskConsoleAuth.getData();
                        }
                        authFlag = StringUtils.isNotBlank(cooskConsoleUrl);
                    }else if(selectMenuEntity.getWid().equals(PortalManagerProperties.SIM_PORTAL_URL)){
                        //检查是否有coosk看板权限
                        DubboResponse<String> cooskPortalAuth = dubboServiceItemService.checkUserPortalAuth(userUtil.getUserAccount());
                        if("0".equals(cooskPortalAuth.getErrcode())&&StringUtils.isNotBlank(cooskPortalAuth.getData())){
                            cooskPortalUrl=cooskPortalAuth.getData();
                        }
                        authFlag = StringUtils.isNotBlank(cooskPortalUrl);
                    }
                    if(!authFlag){
                        list.remove(selectMenuEntity);
                    }
                }else{
                    //游客状态
                    if(Global.AuthType.LOGIN_VISIBLE.getId()==selectMenuEntity.getAuthType()||Global.AuthType.AUTH_VISIBLE.getId()==selectMenuEntity.getAuthType()){
                        //移除登录后可见、授权可见类型
                        list.remove(selectMenuEntity);
                    }
                    if(selectMenuEntity.getWid().equals(PortalManagerProperties.PERSON_CENTER_URL)||
                            selectMenuEntity.getWid().equals(PortalManagerProperties.BACKEND_MANAGE_URL)||
                            selectMenuEntity.getWid().equals(PortalManagerProperties.POSTITION_MANAGER_URL)||
                            selectMenuEntity.getWid().equals(PortalManagerProperties.SIM_PORTAL_URL)||
                            selectMenuEntity.getWid().equals(PortalManagerProperties.SIM_MANAGE_URL) ){
                        //个人中心、岗位管理、管控台、事项管理、效能看板在登录状态可见
                        list.remove(selectMenuEntity);
                    }
                }
            }
        }
        result.put(Global.POSITION_URL,positionUrl);
        result.put(Global.COOSK_CONSOLE_URL,cooskConsoleUrl);
        result.put(Global.COOSK_PORTAL_URL,cooskPortalUrl);
        return result;
    }

    @Override
    public SelectMenuDetailRes querySelectMenuDetail (SelectMenuDetailReq body) {
        // 权限校验
        MinosConsoleUtil.checkSiteAuth(Collections.singletonList(body.getProgrammeWid()));

        SelectMenuDetailRes selectMenuDetailRes = new SelectMenuDetailRes();
        SelectMenuEntity selectMenuEntity = this.getOne(new QueryWrapper<SelectMenuEntity>().lambda()
                .eq(SelectMenuEntity::getWid,body.getWid()).eq(SelectMenuEntity::getProgrammeId,body.getProgrammeWid()).eq(SelectMenuEntity::getIsDeleted,Global.DeleteStatus.NO.getId()));
        if(null!=selectMenuEntity){
            SelectMenuInfo selectMenuInfo = ObjectUtil.copyProperties(selectMenuEntity, new SelectMenuInfo());
            List<InternationalizationEntity> langData = langService.list(new QueryWrapper<InternationalizationEntity>().lambda().eq(InternationalizationEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                    .eq(InternationalizationEntity::getLangKey, selectMenuEntity.getMenuNameLangKey()));
            if(CollectionUtils.isNotEmpty(langData)){
                selectMenuInfo.setNameLang(ObjectUtil.copyListProperties(langData, CommonLang::new));
            }
            //内部页面
            if(Global.MenuType.INNER_LINK.getId()==selectMenuInfo.getMenuType()){
                PageInfoEntity pageInfoEntity = pageInfoService.getOne(new QueryWrapper<PageInfoEntity>().lambda().eq(PageInfoEntity::getWid, selectMenuEntity.getPageId())
                        .eq(PageInfoEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));
                if(null!=pageInfoEntity){
                    selectMenuInfo.setPageName(pageInfoEntity.getPageName());
                }else{
                    selectMenuInfo.setPageName(selectMenuEntity.getPageId());
                }
            }
            //授权后可用
            if(Global.AuthType.AUTH_VISIBLE.getId()==selectMenuEntity.getAuthType()){
                List<MenuAuthEntity> list = menuAuthService.list(new QueryWrapper<MenuAuthEntity>().lambda().eq(MenuAuthEntity::getMenuWid, selectMenuEntity.getWid())
                        .eq(MenuAuthEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));
                if(CollectionUtils.isNotEmpty(list)){
                    List<MenuAuthDto> menuAuthDtos = ObjectUtil.copyListProperties(list,MenuAuthDto::new,(entity, dto) ->{
                        dto.setAuthType(Integer.valueOf(entity.getAuthType()));
                    });
                    //查询机构信息
                    OrgInfoTreeReq orgInfoTreeReq = new OrgInfoTreeReq();
                    orgInfoTreeReq.setIsTree(Global.CONSTANT_NO);
                    InlineResponse20029 minosOrg = minosApi.getMinosOrg(orgInfoTreeReq);
                    //机构list转map
                    Map<String, String> orgMap = programmeApiAdapter.getOrgMap(minosOrg.getData());
                    //查询域及用户组信息
                    FieldGroupsReq fieldGroupsReq = new FieldGroupsReq();
                    fieldGroupsReq.setIsTree(Global.CONSTANT_NO);
                    InlineResponse20031 fieldGroups = minosApi.getFieldGroups(fieldGroupsReq);
                    //域及用户组list转Map
                    Map<String, String> fieldGroupMap = programmeApiAdapter.getFieldGroupMap(fieldGroups.getData());
                    setAuthName(menuAuthDtos, orgMap, fieldGroupMap);
                    selectMenuInfo.setMenuAuthInfo(menuAuthDtos);
                }
            }
            //png图标特殊处理
            if(Global.SelectMenuIconType.PNG.getId()==selectMenuEntity.getIconType()&&StringUtils.isNotBlank(selectMenuEntity.getIconPath())){
                List<OSSFileBean> beans = new ArrayList<>();
                OSSFileBean bean = new OSSFileBean();
                bean.setDir(selectMenuEntity.getIconPath());
                bean.setObjectName(selectMenuEntity.getIconName());
                beans.add(bean);
                beans = ossClientUtil.getFileUrlsWithLocal(Global.OSS_FILE_BACKET_USERICON, beans);
                beans.get(0).setObjectName(selectMenuEntity.getMenuName());
                selectMenuInfo.setIconUrl(beans.get(0).getIconUrl());
            }else if(Global.SelectMenuIconType.FONT.getId()==selectMenuEntity.getIconType()){
                selectMenuInfo.setIconUrl(selectMenuInfo.getIconPath());
            }
            selectMenuDetailRes.setData(selectMenuInfo);
        }
        return selectMenuDetailRes;
    }

    //处理菜单授权，赋值授权名称用以展示
    private void setAuthName(List<MenuAuthDto> menuAuthInfoBizs, Map<String, String> finalOrgMap, Map<String, String> finalFieldGroupMap){
        menuAuthInfoBizs.forEach(menuAuthInfoBiz -> {
            if (null != finalOrgMap && menuAuthInfoBiz.getAuthType()==Global.MenuAuthType.ORG.getId()) {
                //组织机构类型 赋值组织机构名称
                menuAuthInfoBiz.setAuthRelName(null != finalOrgMap.get(menuAuthInfoBiz.getAuthRelWid()) ? finalOrgMap.get(menuAuthInfoBiz.getAuthRelWid()) : "");
            } else if (null != finalFieldGroupMap && menuAuthInfoBiz.getAuthType()==Global.MenuAuthType.DOMAIN_AND_GROUP.getId()) {
                //赋值域及用户组名称
                menuAuthInfoBiz.setAuthRelName(null != finalFieldGroupMap.get(menuAuthInfoBiz.getAuthRelWid()) ? finalFieldGroupMap.get(menuAuthInfoBiz.getAuthRelWid()) : "");
            } else if (menuAuthInfoBiz.getAuthType()==Global.MenuAuthType.USER.getId()) {
                //赋值用户名
                InlineResponse20028 userDetails = minosApi.getUserDetails(menuAuthInfoBiz.getAuthRelWid());
                menuAuthInfoBiz.setAuthRelName(null != userDetails && userDetails.getData() != null ? userDetails.getData().getUserName() + "(" + userDetails.getData().getUserAccount() + ")" : "");
            }
        });
    }

    @Override
    public QuerySelectMenusRes querySelectMenus (String wid) {
        MinosConsoleUtil.checkSiteAuth(Collections.singletonList(wid));
        QuerySelectMenusRes querySelectMenusRes = new QuerySelectMenusRes();
        List<SelectMenuEntity> list = this.list(new QueryWrapper<SelectMenuEntity>().lambda().select(SelectMenuEntity::getWid,SelectMenuEntity::getMenuName,
                SelectMenuEntity::getOrderNumber,SelectMenuEntity::getIsSystemMenu,SelectMenuEntity::getIsEnabled,SelectMenuEntity::getMenuType,SelectMenuEntity::getPageId)
                .eq(SelectMenuEntity::getIsDeleted, Global.DeleteStatus.NO.getId()).eq(SelectMenuEntity::getProgrammeId,wid).orderByAsc(SelectMenuEntity::getOrderNumber));
        if( CollectionUtils.isNotEmpty(list) ){
            try {
                filterMenuByLicense(list,false);
            } catch ( Exception e ) {
                logger.error("===========querySelectMenus发生异常:{}",e);
            }
            if(CollectionUtils.isNotEmpty(list)){
                querySelectMenusRes.setData(ObjectUtil.copyListProperties(list,SelectMenusDto::new,(entity, dto) ->{
                    if(Global.MenuType.INNER_LINK.getId()==entity.getMenuType()&&StringUtils.isBlank(entity.getPageId())){
                        dto.setIsAllRequired(Global.NO);
                    }
                }));
            }
        }
        return querySelectMenusRes;
    }

    @Override
    public CommonRes saveSelectMenu (SaveSelectMenuReq body) {
        if(CollectionUtils.isEmpty(body.getNameLang())||StringUtil.isBlank(body.getMenuName())||StringUtil.isBlank(body.getProgrammeId())){
            throw new MinosException("参数错误");
        }

        // 权限校验
        MinosConsoleUtil.checkSiteAuth(Collections.singletonList(body.getProgrammeId()));

        SelectMenuEntity selectMenuEntity = ObjectUtil.copyProperties(body, new SelectMenuEntity());
        String oldLangKey= StringUtils.isNotBlank(selectMenuEntity.getMenuNameLangKey())?selectMenuEntity.getMenuNameLangKey():"";
        String langKey = MinosCommonUtil.getWid();
        List<InternationalizationEntity> langData = ObjectUtil.copyListProperties(body.getNameLang(), InternationalizationEntity::new, (dto, entity) -> {
            entity.setLangKey(langKey);
        });
        selectMenuEntity.setMenuNameLangKey(langKey);
        List<MenuAuthEntity> menuAuths = Lists.newArrayList();
        List<SelectMenuEntity> list = this.list(new QueryWrapper<SelectMenuEntity>().lambda().eq(SelectMenuEntity::getProgrammeId,body.getProgrammeId()));

        if( StringUtil.isBlank(body.getWid()) ){
            //新增
            SelectMenuEntity menuEntityFir = null;
            Optional<SelectMenuEntity> max = list.stream().filter(k -> null != k.getOrderNumber()).max(Comparator.comparingLong(SelectMenuEntity::getOrderNumber));
            if ( max.isPresent() ) {
                menuEntityFir = max.get();
            }
            selectMenuEntity.setOrderNumber(null!=menuEntityFir?menuEntityFir.getOrderNumber()+1:1L);
            this.save(selectMenuEntity);
        }else{
            SelectMenuEntity one = this.getOne(new QueryWrapper<SelectMenuEntity>().lambda().eq(SelectMenuEntity::getProgrammeId, body.getProgrammeId())
                    .eq(SelectMenuEntity::getWid, body.getWid()));
            if(Global.YES==one.getIsSystemMenu()){
                //系统菜单不需要修改linkUrl、menuType、openType
                selectMenuEntity.setLinkUrl(one.getLinkUrl());
                selectMenuEntity.setOpenType(one.getOpenType());
                selectMenuEntity.setMenuType(one.getMenuType());
            }
            //编辑
            if(StringUtils.isNotBlank(oldLangKey)){
                langService.remove(new QueryWrapper<InternationalizationEntity>().lambda().eq(InternationalizationEntity::getLangKey,oldLangKey));
            }
            if(Global.SelectMenuIconType.NO_PNG.getId()==selectMenuEntity.getIconType()){
                selectMenuEntity.setIconPath(null);
                selectMenuEntity.setIconName(null);
            }
            if(Global.SelectMenuIconType.FONT.getId()==selectMenuEntity.getIconType()){
                selectMenuEntity.setIconPath(body.getIconUrl());
                selectMenuEntity.setIconName(null);
            }
            this.update(selectMenuEntity,new QueryWrapper<SelectMenuEntity>().lambda().eq(SelectMenuEntity::getProgrammeId,body.getProgrammeId()).eq(SelectMenuEntity::getWid,body.getWid()));
        }
        //授权后可用
        menuAuthService.remove(new QueryWrapper<MenuAuthEntity>().lambda().eq(MenuAuthEntity::getMenuWid,selectMenuEntity.getWid()));
        if(Global.AuthType.AUTH_VISIBLE.getId()==selectMenuEntity.getAuthType()&&CollectionUtils.isNotEmpty(body.getMenuAuthInfo())){
            body.getMenuAuthInfo().forEach(k->{
                MenuAuthEntity menuAuthEntity = new MenuAuthEntity();
                menuAuthEntity.setMenuWid(selectMenuEntity.getWid());
                menuAuthEntity.setAuthType(String.valueOf(k.getAuthType()));
                menuAuthEntity.setAuthRelWid(k.getAuthRelWid());
                menuAuthEntity.setMenuAuthType(Global.MenuMenuAuthType.SELECT_MENU.getId());
                menuAuths.add(menuAuthEntity);
            });
            menuAuthService.saveBatch(menuAuths);
        }
        langService.saveBatch(langData);
        return new CommonRes();
    }

    @Override
    public CommonRes sortSelectMenu (List<SortSelectMenuReq> body) {
        if(CollectionUtils.isEmpty(body)){
            throw new MinosException("参数错误");
        }

        // 权限校验
        List<String> programmeIds = body.stream().map(SortSelectMenuReq::getProgrammeWid).distinct().collect(Collectors.toList());
        MinosConsoleUtil.checkSiteAuth(programmeIds);

        List<String> wids = body.stream().map(SortSelectMenuReq::getWid).collect(Collectors.toList());
        Map<String, Long> bodyMap = body.stream().collect(Collectors.toMap(SortSelectMenuReq::getWid, SortSelectMenuReq::getOrderNumber));

        List<SelectMenuEntity> list = this.list(new QueryWrapper<SelectMenuEntity>().lambda().eq(SelectMenuEntity::getProgrammeId, body.get(0).getProgrammeWid())
                .in(SelectMenuEntity::getWid, wids));
        if(CollectionUtils.isNotEmpty(list)){
            list.forEach(k->{
                if(bodyMap.containsKey(k.getWid())){
                    k.setOrderNumber(bodyMap.get(k.getWid()));
                }
                this.update(k,new QueryWrapper<SelectMenuEntity>().lambda().eq(SelectMenuEntity::getProgrammeId,k.getProgrammeId()).eq(SelectMenuEntity::getWid,k.getWid()));
            });
        }
        return new CommonRes();
    }
}
