package com.wisedu.minos.casp.portal.controller.v311beta2;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wisedu.minos.casp.portal.CasProperties;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.dao.entity.InternationalizationEntity;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.model.v311beta2.MultiSitesDto;
import com.wisedu.minos.casp.portal.model.v311beta2.SiteDto;
import com.wisedu.minos.casp.portal.model.v311beta2.UserSiteVo;
import com.wisedu.minos.casp.portal.service.impl.InternationalizationService;
import com.wisedu.minos.casp.portal.service.impl.ShowProgrammeService;
import com.wisedu.minos.casp.portal.service.v311beta2.MultiSitesAdapter;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * //todo 添加描述
 * @date 2021/11/30 19:35
 * @author jszhang@wisedu
 * @version 1.0
 **/
@RestController
public class MultiSiteRouterController {

    @Autowired
    MultiSitesAdapter multiSitesAdapter;
    @Autowired
    UserUtil userUtil;
    @Autowired
    InternationalizationService internationalizationService;
    @Autowired
    ShowProgrammeService showProgrammeService;
    @Autowired
    CasProperties casProperties;
    @Autowired
    HttpServletResponse httpResponse;
    @RequestMapping("/getUserPermissionRouters")
    @SneakyThrows
    public UserSiteVo getUserPermissionRouters(String langCountry) {
        SiteDto siteDto = multiSitesAdapter.getSiteByUserId(userUtil.getCurrentUser());
        Set<MultiSitesDto> userMultiSite = siteDto.getUserMultiSite();

        if (!userMultiSite.isEmpty()) {
            Set<String> userEnableSiteRoutes = userMultiSite.stream().map(MultiSitesDto::getWid).collect(Collectors.toSet());
            //判断这个站点在pc或者h5下面有没有展示方案
            List<ShowProgrammeEntity> list = showProgrammeService.list(Wrappers.<ShowProgrammeEntity>lambdaQuery()
                    .eq(ShowProgrammeEntity::getPageStatus, Global.PageStatus.ENABLE.getId())
                    .eq(ShowProgrammeEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                    .eq(ShowProgrammeEntity::getPlatformType, CommonUtil.isMobileDevice() ? 1 : 0)
                    .in(ShowProgrammeEntity::getSiteWid, userEnableSiteRoutes)
            );
            Set<String> platFormEnableSite = list.stream().map(ShowProgrammeEntity::getSiteWid).collect(Collectors.toSet());

            Set<MultiSitesDto> userPlatformEnableMultiSite =new TreeSet<>(Comparator.comparingInt(MultiSitesDto::getOrderIndex));
            userPlatformEnableMultiSite.addAll( userMultiSite.stream().filter(e->platFormEnableSite.contains(e.getWid())).collect(Collectors.toSet()));

            if (userPlatformEnableMultiSite.isEmpty()) {
                return new UserSiteVo().setData(userPlatformEnableMultiSite);
            }
            Set<String> collect = userPlatformEnableMultiSite.stream().map(MultiSitesDto::getLanguageKey).collect(Collectors.toSet());
            List<InternationalizationEntity> byLangKeyList = internationalizationService.getByLangKeyList(langCountry, collect);
            Map<String, String> map = new HashMap<>();
            byLangKeyList.forEach(e -> map.put(e.getLangKey(), e.getLangValue()));
            userPlatformEnableMultiSite.forEach(e -> {
                String s = map.get(e.getLanguageKey());
                e.setSiteName(StringUtils.isNotEmpty(s) ? s : e.getSiteName());
            });
            return new UserSiteVo().setData(userPlatformEnableMultiSite);

        }
        //游客没权限直接跳转ids登录
        if(userUtil.getCurrentUser()==null &&userMultiSite.isEmpty()){
            String redirectUrl = casProperties.getCas().getServerUrl() + "login?service=" + URLEncoder.encode(casProperties.getModule().getDomain() + Global.LOGIN_URI, "UTF-8");
            httpResponse.setHeader("REDIRECT","REDIRECT");//告诉ajax要重定向
            httpResponse.setHeader("PATH",redirectUrl);
            return null;
        }
        return new UserSiteVo().setData(userMultiSite);
    }
}
