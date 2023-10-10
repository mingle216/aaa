package com.wisedu.minos.casp.portal.service.v311beta2;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wisedu.minos.api.data.UserService;
import com.wisedu.minos.api.model.DubboIds;
import com.wisedu.minos.api.model.DubboUserInfo;
import com.wisedu.minos.api.model.DubboUserInfoResp;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.ObjectUtil;
import com.wisedu.minos.casp.portal.common.utils.SiteBeanUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.controller.v311beta2.SiteApiAdapter;
import com.wisedu.minos.casp.portal.dao.entity.*;
import com.wisedu.minos.casp.portal.dao.mapper.InternationalizationMapper;
import com.wisedu.minos.casp.portal.dao.mapper.MenuAuthMapper;
import com.wisedu.minos.casp.portal.dao.mapper.SiteMagMapper;
import com.wisedu.minos.casp.portal.dao.mapper.SiteMapper;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.model.v311beta2.*;
import com.wisedu.minos.casp.portal.service.ProgrammeApiAdapter;
import com.wisedu.minos.casp.portal.service.impl.InternationalizationService;
import com.wisedu.minos.casp.portal.service.impl.MinosApiAdapterImpl;
import com.wisedu.minos.casp.portal.service.impl.ShowProgrammeService;
import com.wisedu.minos.casp.portal.utils.CommunalUtil;
import com.wisedu.minos.casp.portal.utils.MinosConsoleUtil;
import com.wisedu.minos.casp.portal.utils.StrUtil;
import com.wisedu.minos.util.MinosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.wisedu.minos.casp.portal.common.constant.Global.MASTER_SITE_WID;

@Service
@Slf4j
public class SiteApiAdapterImpl implements SiteApiAdapter {

    @Autowired
    private MenuAuthMapper menuAuthMapper;
    @Autowired
    private InternationalizationMapper internationalizationMapper;
    @Autowired
    private InternationalizationService internationalizationService;

    @Autowired
    private SiteMapper siteMapper;

    @Autowired
    private SiteMagMapper siteMagMapper;

    @Autowired
    private SiteMagMapperService siteMagMapperService;

    @Autowired
    private SiteAuthMapperService siteAuthMapperService;

    @Autowired
    private MinosApiAdapterImpl minosApi;

    @DubboReference
    private UserService userService;

    @Autowired
    private ShowProgrammeService showProgrammeService;
    @Autowired
    private   ProgrammeApiAdapter programmeApiAdapter;

    @Override
    public OneSiteResponse changeEnableStatus(PortalSiteInfo body) {
        // 首先验证权限
        if(!body.isIsSysAdmin()){
            List<SiteMagEntity> siteMags = siteMagMapper.selectList(
                    new QueryWrapper<SiteMagEntity>().lambda()
                            .eq(SiteMagEntity::getIsDeleted,0)
                            .and(wrapper->wrapper.eq(SiteMagEntity::getMagRelWid,body.getUserWid()).eq(SiteMagEntity::getMagType,1))
                            .or(CollectionUtils.isNotEmpty(body.getRoleWids()),wrapper->wrapper.in(SiteMagEntity::getMagRelWid,body.getRoleWids()).eq(SiteMagEntity::getMagType,0))
            );
            if(CollectionUtils.isEmpty(siteMags)){
                throw new MinosException("没有权限操作该站点");
            }
            Set<String> operateableSites = siteMags.stream().map(siteMag->siteMag.getSiteWid()).collect(Collectors.toSet());
            if(!operateableSites.contains(body.getWid())){
                throw new MinosException("没有权限操作该站点");
            }
        }
        SiteEntity siteEntity = siteMapper.selectById(body.getWid());
        if (Objects.isNull(siteEntity)) {
            throw new MinosException("当前数据不存在");
        }
        //停用
        if (Objects.equals(body.getIsEnabled(), 0)) {
            if(Objects.equals(siteEntity.getIsMaster(),1)){
                throw new MinosException("预置站点无法停用");
            }
            siteEntity.setIsEnabled(0);
            siteMapper.updateById(siteEntity);
        }
        //启用
        if (Objects.equals(body.getIsEnabled(), 1)) {
            //判断是否有启用的pc端展示方案
            QueryWrapper<ShowProgrammeEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(ShowProgrammeEntity::getSiteWid,body.getWid())
                    .eq(ShowProgrammeEntity::getIsDeleted,Global.DeleteStatus.NO.getId())
                    .eq(ShowProgrammeEntity::getPlatformType, Global.PlatformType.PC.getType())
                    .eq(ShowProgrammeEntity::getPageStatus, Global.PageStatus.ENABLE.getId());
            if(showProgrammeService.count(queryWrapper)  == 0){
                QueryWrapper<ShowProgrammeEntity> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(ShowProgrammeEntity::getSiteWid,body.getWid())
                        .eq(ShowProgrammeEntity::getIsDeleted,Global.DeleteStatus.NO.getId())
                        .eq(ShowProgrammeEntity::getPlatformType, Global.PlatformType.PC.getType());
                if(showProgrammeService.count(queryWrapper2)  == 0){
                    throw new MinosException(" 站点下至少启用一个PC方案才能开启，当前站点下无PC方案");
                }
                throw new MinosException("站点下至少启用一个PC方案才能开启，当前站点下PC方案未启用");
            }
            siteEntity.setIsEnabled(1);
            siteMapper.updateById(siteEntity);
        }
        return new OneSiteResponse();
    }

    @Override
    public CheckRouteResponse checkRoute(PortalSiteInfo body) {
        if (StringUtils.isEmpty(body.getSiteRoute())) {
            throw new MinosException("站点路由不可以为空");
        }
        if (!StrUtil.validRoute(body.getSiteRoute())) {
            throw new MinosException("站点路由只能包含字母、数字、-");
        }
        CheckRouteResponse response = new CheckRouteResponse();

        QueryWrapper<SiteEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SiteEntity::getIsDeleted, Global.DeleteStatus.YES.getId());
        queryWrapper.lambda().eq(SiteEntity::getSiteRoute, body.getSiteRoute());
        if (StringUtils.isNotEmpty(body.getWid())) {
            queryWrapper.lambda().ne(SiteEntity::getWid, body.getWid());
        }
        List<SiteEntity> siteEntities = querySameRoute(body.getWid(), body.getSiteRoute());
        if (CollectionUtils.isNotEmpty(siteEntities)) {
            response.setCheckSuccess(false);
            response.setCheckErrorMsg("该站点路由已存在");
        } else {
            response.setCheckSuccess(true);
        }
        return response;
    }

    private List<SiteEntity> querySameRoute(String siteWid, String route) {
        QueryWrapper<SiteEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SiteEntity::getIsDeleted, Global.DeleteStatus.NO.getId());
        queryWrapper.lambda().eq(SiteEntity::getSiteRoute, route);
        if (StringUtils.isNotEmpty(siteWid)) {
            queryWrapper.lambda().ne(SiteEntity::getWid, siteWid);
        }

        return siteMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SiteDeleteResponse deleteSite(DeleteSiteRequest body) {
        // 首先验证权限
        List<String> siteWids = body.getSiteWids();
        if(!body.isIsSysAdmin()){
            List<SiteMagEntity> siteMags = siteMagMapper.selectList(
                    new QueryWrapper<SiteMagEntity>().lambda()
                            .eq(SiteMagEntity::getIsDeleted,0)
                            .and(wrapper->wrapper.eq(SiteMagEntity::getMagRelWid,body.getUserWid()).eq(SiteMagEntity::getMagType,1))
                            .or(CollectionUtils.isNotEmpty(body.getRoleWids()),wrapper->wrapper.in(SiteMagEntity::getMagRelWid,body.getRoleWids()).eq(SiteMagEntity::getMagType,0))
            );
            if(CollectionUtils.isEmpty(siteMags)){
                throw new MinosException("没有权限删除该站点");
            }
            Set<String> operateableSites = siteMags.stream().map(siteMag->siteMag.getSiteWid()).collect(Collectors.toSet());
            if(!operateableSites.containsAll(siteWids)){
                throw new MinosException("没有权限删除该站点");
            }
        }
        //一般不会有批量删除情况，一个个删除
        for (String wid : siteWids) {
            SiteEntity siteEntity = siteMapper.selectById(wid);
            if (Objects.isNull(siteEntity)) {
                throw new MinosException("要删除的数据不存在");
            }
            if(StringUtils.equals(siteEntity.getWid(),MASTER_SITE_WID)){
                throw new MinosException("主站点不可以删除");
            }
            siteEntity.setIsDeleted(Global.DeleteStatus.YES.getId());
            siteMapper.updateById(siteEntity);

            //删除授权
            deleteSiteAuth(wid);
            //删除管理员
            deleteSiteMag(wid);
        }
        List<List<String>> siteWidArray = CommonUtil.getListArray(siteWids);
        siteWidArray.forEach(siteWid->{
            List<ShowProgrammeEntity> programmeEntities = showProgrammeService.list(new QueryWrapper<ShowProgrammeEntity>().lambda().select()
                    .in(ShowProgrammeEntity::getSiteWid, siteWid)
                    .eq(ShowProgrammeEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
            );
            List<String> collect = programmeEntities.stream().map(e -> e.getWid()).collect(Collectors.toList());
            programmeApiAdapter.deleteProgramme(collect);
        });
        return new SiteDeleteResponse();
    }

    @Override
    public OneSiteResponse detailSite(PortalSiteInfo body) {
        SiteEntity siteEntity = siteMapper.selectById(body.getWid());
        if (Objects.isNull(siteEntity)) {
            throw new MinosException("当前数据不存在");
        }
        // 检查站点管理员权限
        MinosConsoleUtil.isRequestRightsLegal(Lists.newArrayList(siteEntity.getWid()));

        OneSiteResponse response = new OneSiteResponse();
        PortalSiteInfo siteInfo = new PortalSiteInfo();
        BeanUtils.copyProperties(siteEntity, siteInfo);

        siteInfo.setSiteNameLangList(getSiteNameLangListBySiteEntity(siteEntity));
        siteInfo.setAuthList(getAuthListBySiteWid(body.getWid()));
        siteInfo.setSiteManagers(getManagerListBySiteWid(body.getWid()));

        response.setData(siteInfo);
        return response;
    }

    private List<PortalSiteManagerInfo> getManagerListBySiteWid(String wid) {
        QueryWrapper<SiteMagEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SiteMagEntity::getIsDeleted, Global.DeleteStatus.NO.getId());
        wrapper.lambda().eq(SiteMagEntity::getSiteWid, wid);
        List<SiteMagEntity> list = siteMagMapperService.list(wrapper);

        List<PortalSiteManagerInfo> result = new ArrayList<>();
        for (SiteMagEntity magEntity : list) {
            PortalSiteManagerInfo siteManagerInfo = new PortalSiteManagerInfo();
            BeanUtils.copyProperties(magEntity, siteManagerInfo);
            result.add(siteManagerInfo);
        }
        return result;
    }

    private List<PortalSiteAuthInfo> getAuthListBySiteWid(String wid) {
        QueryWrapper<SiteAuthEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SiteAuthEntity::getIsDeleted, Global.DeleteStatus.NO.getId());
        wrapper.lambda().eq(SiteAuthEntity::getSiteWid, wid);
        List<SiteAuthEntity> list = siteAuthMapperService.list(wrapper);

        List<PortalSiteAuthInfo> result = new ArrayList<>();
        for (SiteAuthEntity siteAuthEntity : list) {
            PortalSiteAuthInfo siteAuthInfo = new PortalSiteAuthInfo();
            BeanUtils.copyProperties(siteAuthEntity, siteAuthInfo);
            result.add(siteAuthInfo);
        }
        //设置authRelName值
        setAuthRelNames(result);
        //如果没有找到名称则删除
        result.removeIf(authInfo -> StringUtils.isEmpty(authInfo.getAuthRelName()));
        return result;
    }

    private void setAuthRelNames(List<PortalSiteAuthInfo> result) {

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

        //查询所有的用户信息
        List<String> userWids = result.stream()
                .filter(item -> item.getAuthType().equals(Global.MenuAuthType.USER.getId()))
                .map(PortalSiteAuthInfo::getAuthRelWid).distinct().collect(Collectors.toList());

        List<List<String>> userWidsArray = CommonUtil.getListArray(userWids);
        Map<String, DubboUserInfo> userInfoMap = getUserInfoMap(userWidsArray);

        for (PortalSiteAuthInfo authInfo : result) {
            if (null != orgMap && authInfo.getAuthType().equals(Global.MenuAuthType.ORG.getId())) {
                //组织机构类型 赋值组织机构名称
                authInfo.setAuthRelName(null != orgMap.get(authInfo.getAuthRelWid()) ? orgMap.get(authInfo.getAuthRelWid()) : "");
            } else if (null != fieldGroupMap && authInfo.getAuthType().equals(Global.MenuAuthType.DOMAIN_AND_GROUP.getId())) {
                //赋值域及用户组名称
                authInfo.setAuthRelName(null != fieldGroupMap.get(authInfo.getAuthRelWid()) ? fieldGroupMap.get(authInfo.getAuthRelWid()) : "");
            } else if (authInfo.getAuthType().equals(Global.MenuAuthType.USER.getId())) {
                //赋值用户名
                authInfo.setAuthRelName(null != userInfoMap.get(authInfo.getAuthRelWid()) ? userInfoMap.get(authInfo.getAuthRelWid()).getUserName() : "");
            }
        }
    }

    private Map<String, DubboUserInfo> getUserInfoMap(List<List<String>> userWidsArray){
        Map<String, DubboUserInfo> userInfoMap = new HashMap<>();
        for (List<String> wids : userWidsArray) {
            DubboIds dubboIds = new DubboIds();
            dubboIds.setWid(wids);
            try {
                DubboUserInfoResp resp = userService.getUserByIds(dubboIds);
                userInfoMap.putAll(resp.getData().stream().collect(
                        Collectors.toMap(DubboUserInfo::getWid,
                                item -> item, (oldVal, currVal) -> currVal)));
            } catch (Exception e) {
                LOGGER.info("调用getUserByIds获取用户详情失败，原因为：", e);
            }
        }

        return userInfoMap;
    }

    //组织机构list转Map
    private Map<String, String> getOrgMap(List<OrgInfosBiz> orgInfosBizs) {
        Map<String, String> orgMap = null;
        if (CollectionUtils.isNotEmpty(orgInfosBizs)) {
            orgMap = orgInfosBizs.stream().collect(
                    Collectors.toMap(OrgInfosBiz::getWid,
                            OrgInfosBiz::getName, (oldVal, currVal) -> currVal));
        }
        return orgMap;
    }

    //域及用户组List转map
    private Map<String, String> getFieldGroupMap(List<FieldGroupBiz> fieldGroupBizs) {
        Map<String, String> fieldGroupMap = null;
        if (CollectionUtils.isNotEmpty(fieldGroupBizs)) {
            fieldGroupMap = fieldGroupBizs.stream().collect(
                    Collectors.toMap(FieldGroupBiz::getWid,
                            FieldGroupBiz::getName, (oldVal, currVal) -> currVal));
        }
        return fieldGroupMap;
    }

    private List<PortalSiteLangInfo> getSiteNameLangListBySiteEntity(SiteEntity siteEntity) {
        QueryWrapper<InternationalizationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(DbFieldConstant.LANG_KEY, siteEntity.getSiteNameLangKey());
        wrapper.eq(DbFieldConstant.IS_DELETED, Global.DeleteStatus.NO.getId());
        List<InternationalizationEntity> list = internationalizationMapper.selectList(wrapper);

        List<PortalSiteLangInfo> result = new ArrayList<>();
        for (InternationalizationEntity internationalizationEntity : list) {
            PortalSiteLangInfo siteLangInfo = new PortalSiteLangInfo();
            siteLangInfo.setLangCountry(internationalizationEntity.getLangCountry());
            siteLangInfo.setLangValue(internationalizationEntity.getLangValue());
            siteLangInfo.setSourceType(internationalizationEntity.getSourceType());
            result.add(siteLangInfo);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OneSiteResponse saveSite(PortalSiteInfo body) {
        if(StringUtils.isNotBlank(body.getWid())){
            // 检查站点管理员权限
            MinosConsoleUtil.isRequestRightsLegal(Lists.newArrayList(body.getWid()));
        }
        List<SiteEntity> sameSiteEntities = querySameRoute(body.getWid(), body.getSiteRoute());
        if (CollectionUtils.isNotEmpty(sameSiteEntities)) {
            throw new MinosException("该站点路由已存在,无法保存");
        }
        if (!StrUtil.validRoute(body.getSiteRoute())) {
            throw new MinosException("站点路由只能包含字母、数字、-");
        }
        //校验站点路由
        if (StringUtils.contains(body.getSiteRoute(),"/")) {
            throw new MinosException("站点路由不可以包含/字符");
        }

        String langKey = null;
        if (StringUtil.isNotEmpty(body.getWid())) {
            SiteEntity siteEntity = siteMapper.selectById(body.getWid());
            langKey = siteEntity.getSiteNameLangKey();
        }else{
            langKey = CommunalUtil.getWid();
        }
        //保存国际化表
        saveInternation(body, langKey);
        //保存站点
        SiteEntity siteEntity = doSaveSite(body, langKey);
        //保存授权表
        saveSiteAuth(body, siteEntity.getWid());
        //保存管理员
        saveSiteMag(body, siteEntity.getWid());
        return new OneSiteResponse();
    }

    private void saveSiteMag(PortalSiteInfo body, String wid) {
        //编辑的情况，先把原先记录删除，再添加
        if (StringUtil.isNotEmpty(body.getWid())) {
            deleteSiteMag(wid);
        }
        if (CollectionUtils.isNotEmpty(body.getSiteManagers())) {
            List<SiteMagEntity> authEntities = body.getSiteManagers().stream().map(item -> {
                SiteMagEntity siteMagEntity = ObjectUtil.copyProperties(item, new SiteMagEntity());
                siteMagEntity.setWid(CommunalUtil.getWid());
                siteMagEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
                siteMagEntity.setSiteWid(wid);
                return siteMagEntity;
            }).collect(Collectors.toList());
            siteMagMapperService.saveBatch(authEntities);
        }
    }

    private void deleteSiteMag(String siteWid) {
        SiteEntity siteEntity = siteMapper.selectById(siteWid);
        List<SiteMagEntity> magEntities = siteMagMapperService.list(Wrappers.<SiteMagEntity>lambdaQuery()
                .eq(SiteMagEntity::getSiteWid, siteEntity.getWid())
                .eq(SiteMagEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));
        if (CollectionUtils.isNotEmpty(magEntities)) {
            magEntities.forEach((item) -> {
                item.setIsDeleted(Global.DeleteStatus.YES.getId());
            });
            siteMagMapperService.updateBatchById(magEntities);
        }
    }

    private void saveSiteAuth(PortalSiteInfo body, String wid) {
        //编辑的情况，先把原先记录删除，再添加
        if (StringUtil.isNotEmpty(body.getWid())) {
            deleteSiteAuth(wid);
        }
        if (CollectionUtils.isNotEmpty(body.getAuthList())) {
            List<SiteAuthEntity> authEntities = body.getAuthList().stream().map(item -> {
                SiteAuthEntity siteAuthEntity = ObjectUtil.copyProperties(item, new SiteAuthEntity());
                siteAuthEntity.setWid(CommunalUtil.getWid());
                siteAuthEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
                siteAuthEntity.setSiteWid(wid);
                return siteAuthEntity;
            }).collect(Collectors.toList());
            siteAuthMapperService.saveBatch(authEntities);
        }
    }

    private void deleteSiteAuth(String siteWid) {
        SiteEntity siteEntity = siteMapper.selectById(siteWid);
        List<SiteAuthEntity> authEntities = siteAuthMapperService.list(Wrappers.<SiteAuthEntity>lambdaQuery()
                .eq(SiteAuthEntity::getSiteWid, siteEntity.getWid())
                .eq(SiteAuthEntity::getIsDeleted, Global.DeleteStatus.NO.getId()));
        if (CollectionUtils.isNotEmpty(authEntities)) {
            authEntities.forEach((item) -> {
                item.setIsDeleted(Global.DeleteStatus.YES.getId());
            });
            siteAuthMapperService.updateBatchById(authEntities);
        }
    }

    private SiteEntity doSaveSite(PortalSiteInfo body, String langKey) {
        SiteEntity siteEntity = new SiteEntity();
        ObjectUtil.copyProperties(body, siteEntity);
        siteEntity.setSiteNameLangKey(langKey);
        if (StringUtils.isEmpty(body.getWid())) {
            siteEntity.setWid(CommunalUtil.getWid());
            siteEntity.setIsMaster(0);
            siteEntity.setIsEnabled(0);
            //设置排序值
            siteEntity.setOrderIndex(getSiteNextOrderIndex());
        }
        siteEntity.setIsDeleted(Global.DeleteStatus.NO.getId());
        if (StringUtils.isEmpty(body.getWid())) {
            siteMapper.insert(siteEntity);
        } else {
            siteMapper.updateById(siteEntity);
        }
        return siteEntity;
    }

    private int getSiteNextOrderIndex() {
        QueryWrapper<SiteEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SiteEntity::getIsDeleted, Global.DeleteStatus.NO.getId());
        queryWrapper.lambda().orderByDesc(SiteEntity::getOrderIndex);
        Page<SiteEntity> ipage = new Page<>();
        ipage.setSize(1);
        ipage.setCurrent(1);
        IPage<SiteEntity> eList = siteMapper.selectPage(ipage, queryWrapper);

        if (CollectionUtils.isNotEmpty(eList.getRecords())) {
            return eList.getRecords().get(0).getOrderIndex() + 1;
        } else {
            return 1;
        }
    }

    //保存国际化表
    private void saveInternation(PortalSiteInfo body, String langKey) {
        //编辑菜单的情况
        if (StringUtil.isNotEmpty(body.getWid())) {
            QueryWrapper<InternationalizationEntity> wrapper = new QueryWrapper<>();
            wrapper.eq(DbFieldConstant.LANG_KEY, langKey);
            List<InternationalizationEntity> list = internationalizationMapper.selectList(wrapper);
            //有就更新，没有则新增
            updateInternation(body.getSiteNameLangList(), list,langKey);
        } else {
            //新增菜单的情况
            if (CollectionUtils.isNotEmpty(body.getSiteNameLangList())) {
                List<InternationalizationEntity> list = new ArrayList<>();
                for (PortalSiteLangInfo langInfo : body.getSiteNameLangList()) {
                    InternationalizationEntity internationalization = new InternationalizationEntity();
                    ObjectUtil.copyProperties(langInfo, internationalization);
                    internationalization.setIsDeleted(Global.DeleteStatus.NO.getId());
                    internationalization.setLangKey(langKey);
                    list.add(internationalization);
                }
                internationalizationService.saveBatch(list);
            }
        }

    }

    //更新菜单国际化信息
    private void updateInternation(List<PortalSiteLangInfo> langkeys, List<InternationalizationEntity> list, String langKey) {
        fromParams:
        for (int i = langkeys.size() - 1; i >= 0; i--) {
            PortalSiteLangInfo langInfo = langkeys.get(i);
            for (int j = list.size() - 1; j >= 0; j--) {
                InternationalizationEntity internationalization = list.get(j);
                if (langInfo.getLangCountry().equals(internationalization.getLangCountry())) {
                    internationalization.setLangValue(langInfo.getLangValue());
                    internationalization.setIsDeleted(Global.DeleteStatus.NO.getId());
                    internationalizationService.updateById(internationalization);
                    list.remove(j);
                    langkeys.remove(i);
                    continue fromParams;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(langkeys)) {
            List<InternationalizationEntity> internationas = new ArrayList<>();
            String finalDbLangKey = langKey;
            langkeys.forEach(menuNameLangBiz -> {
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

    @Override
    public SearchSiteApiResponse searchSite(SearchSiteRequest body) {
        QueryWrapper<SiteEntity> queryWrapper = SiteBeanUtil.searchObjectCondition(body.getSearchCriteria(), SiteEntity.class);
        queryWrapper.orderByAsc("order_index");
        queryWrapper.orderByDesc(DbFieldConstant.CREATE_TIME);

        //非管理员进行过滤  mag_type 授权类型 0 角色 1 个人
        if(!body.isIsSysAdmin()){
            if(CollectionUtils.isNotEmpty(body.getRoleWids())){
                StringBuilder inSql = new StringBuilder();
                for (int i = 0; i < body.getRoleWids().size(); i++) {
                    inSql.append("{").append(i).append("},");
                }
                queryWrapper.and(x->x.apply("exists (select 1 from t_amp_view_site_mag mg where mg.site_wid = t_amp_view_site.wid " +
                        " and mg.is_deleted = 0 and mg.mag_type = 1 and mg.mag_rel_wid = {0} )",body.getUserWid())
                        .or().apply("exists (select 1 from t_amp_view_site_mag mg where mg.site_wid = t_amp_view_site.wid " +
                                        " and mg.is_deleted = 0  and mg.mag_type = 0 and mg.mag_rel_wid in ("+StringUtils.removeEnd(inSql.toString(),",")+") )",
                                body.getRoleWids().toArray()));
            }else{
                queryWrapper.apply("exists (select 1 from t_amp_view_site_mag mg where mg.site_wid = t_amp_view_site.wid " +
                        "  and mg.is_deleted = 0 and mg.mag_type = 1 and mg.mag_rel_wid = {0} )",body.getUserWid());
            }
        }

        List<SiteEntity> siteEntities = siteMapper.selectList(queryWrapper);

        SearchSiteApiResponse response = new SearchSiteApiResponse();
        response.setData(new ArrayList<>());

        for (SiteEntity siteEntity : siteEntities) {
            PortalSiteInfo siteInfo = new PortalSiteInfo();
            BeanUtils.copyProperties(siteEntity, siteInfo);
            response.getData().add(siteInfo);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SortSitesResponse sortSites(PortalSortSitesRequest body) {
        if(CollectionUtils.isEmpty(body.getData())){
            throw new MinosException("要排序的数据不能为空");
        }
        List<String> siteWids = body.getData().stream().map(ent->ent.getWid()).collect(Collectors.toList());
        // 站点权限检查
        MinosConsoleUtil.isRequestRightsLegal(siteWids);
        for (PortalSiteInfo siteInfo : body.getData()) {
            SiteEntity siteEntity = new SiteEntity();
            siteEntity.setWid(siteInfo.getWid());
            siteEntity.setOrderIndex(siteInfo.getOrderIndex());
            siteMapper.updateById(siteEntity);
        }
        return new SortSitesResponse();
    }
}
