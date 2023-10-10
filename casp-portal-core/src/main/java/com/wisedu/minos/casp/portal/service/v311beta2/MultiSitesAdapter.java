package com.wisedu.minos.casp.portal.service.v311beta2;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.HttpUtil;
import com.wisedu.minos.casp.portal.dao.entity.SiteAuthEntity;
import com.wisedu.minos.casp.portal.dao.entity.SiteEntity;
import com.wisedu.minos.casp.portal.model.DubboGroupBeanInfo;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.v311beta2.MultiSitesDto;
import com.wisedu.minos.casp.portal.model.v311beta2.SiteDto;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 *  多站点操作
 * @date 2021/11/29 18:21
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Component
public class MultiSitesAdapter {
    @Autowired
    private SiteViewMapperService siteViewMapperService;
    @Autowired
    private SiteAuthMapperService siteAuthMapperService;
    @Autowired
    private UserUtil userUtil;

    /***
     *  获取用户的站点
     * @param userInfo
     * @return java.util.List<java.lang.String>
     * @author jszhang
     * @date 2021/11/29 18:23
     */
    public SiteDto getSiteByUserId(UserInfo userInfo) {
        SiteDto siteDto = new SiteDto();
        //查询所有的站点
        Set<MultiSitesDto> list = new HashSet<>();
        List<SiteEntity> allSites = siteViewMapperService.list(new QueryWrapper<SiteEntity>()
                .lambda().eq(SiteEntity::getIsDeleted, 0).eq(SiteEntity::getIsEnabled, 1));
        siteDto.setAllEnableSiteRoutes(allSites.stream().map(SiteEntity::getSiteRoute).collect(Collectors.toSet()));
        //登陆前可见
        if (userInfo == null) {
            List<Integer> beforeLogin = Arrays.asList(0, 2);
            list.addAll(allSites.stream().filter(e -> beforeLogin.contains(e.getAuthType())).map(e ->
                    new MultiSitesDto().setSiteRoute(e.getSiteRoute())
                            .setSiteName(e.getSiteName())
                            .setLanguageKey(e.getSiteNameLangKey())
                            .setIsMaster(e.getIsMaster())
                            .setOrderIndex(e.getOrderIndex())
                            .setWid(e.getWid())).collect(Collectors.toSet()));
        } else {
            //登陆后可见
            List<Integer> afterLogin = Arrays.asList(1, 2);
            Set<MultiSitesDto> collect = allSites.stream().filter(e -> afterLogin.contains(e.getAuthType()))
                    .map(e ->
                            new MultiSitesDto().setSiteRoute(e.getSiteRoute())
                                    .setSiteName(e.getSiteName())
                                    .setLanguageKey(e.getSiteNameLangKey())
                                    .setIsMaster(e.getIsMaster())
                                    .setOrderIndex(e.getOrderIndex())
                                    .setWid(e.getWid())).collect(Collectors.toSet());
            list.addAll(collect);
            //获取授权可见
            Set<String> userGroupIds = CollectionUtils.isEmpty(userInfo.getGroups())? Collections.emptySet():
                    userInfo.getGroups().stream().map(DubboGroupBeanInfo::getWid).collect(Collectors.toSet());
            Set<String> userOrgIds = CollectionUtils.isEmpty(userInfo.getAllParentOrgIncludeSelf())?Collections.emptySet():
                    userInfo.getAllParentOrgIncludeSelf();
            List<SiteAuthEntity> authList = siteAuthMapperService.list(new QueryWrapper<SiteAuthEntity>().lambda()
                    .eq(SiteAuthEntity::getIsDeleted, 0));
            List<String> authFilterList = authList.stream().filter(item -> {
                boolean flag;
                if (Global.MenuAuthType.USER.getId() == (item.getAuthType())
                        && userInfo.getWid().equals(item.getAuthRelWid())) {
                    flag = true;
                }else if (CollectionUtils.isNotEmpty(userGroupIds) && Global.MenuAuthType.DOMAIN_AND_GROUP.getId() == (item.getAuthType())
                        && userGroupIds.contains(item.getAuthRelWid())) {
                    flag = true;
                }else if (CollectionUtils.isNotEmpty(userOrgIds) && (Global.MenuAuthType.ORG.getId()) == (item.getAuthType())
                        && userOrgIds.contains(item.getAuthRelWid())|| Global.ORG_SCHOOL.equals(item.getAuthRelWid())) {
                    flag = true;
                }else {
                    flag = false;
                }
                return flag;
            }).map(SiteAuthEntity::getSiteWid).collect(Collectors.toList());
            if (!authFilterList.isEmpty()) {
                Set<MultiSitesDto> multiSitesDtos = allSites.stream().filter(e -> 3 == e.getAuthType() && authFilterList.contains(e.getWid()))
                        .map(e ->
                                new MultiSitesDto().setSiteRoute(e.getSiteRoute())
                                        .setSiteName(e.getSiteName())
                                        .setLanguageKey(e.getSiteNameLangKey())
                                        .setIsMaster(e.getIsMaster())
                                        .setOrderIndex(e.getOrderIndex())
                                        .setWid(e.getWid()))
                        .collect(Collectors.toSet());
                list.addAll(multiSitesDtos);
            }
        }
        siteDto.setUserMultiSite(list);
        siteDto.setIsMasterPermission(list.stream().anyMatch(e -> e.getIsMaster() == 1));
        siteDto.setUserEnableSiteRoutes(list.stream().map(MultiSitesDto::getSiteRoute).collect(Collectors.toSet()));
        return siteDto;
    }

    public Set<String> getAllSitesRoutes(){
      return siteViewMapperService.list(new QueryWrapper<SiteEntity>()
                .lambda().eq(SiteEntity::getIsDeleted, 0).eq(SiteEntity::getIsEnabled, 1)).stream().map(SiteEntity::getSiteRoute).collect(Collectors.toSet());
    }

    /***
     * 获取请求中的路由信息
     * @param request
     * @return java.lang.String
     * @author jszhang
     * @date 2021/12/2 18:56
     */
    public String getRouteByRequest(HttpServletRequest request){
        String routeWid = HttpUtil.getValueFromSessionAndCookie(Global.SITE_WID, request);
        if(StringUtils.isBlank(routeWid)){
            return "";
        }
        SiteEntity siteEntity = siteViewMapperService.getById(routeWid);
        String siteRoute = "";
        if (siteEntity != null) {
            siteRoute = "/" + siteEntity.getSiteRoute();
        }
        return siteRoute;
    }


}
