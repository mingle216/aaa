package com.wisedu.minos.casp.portal.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.entity.SiteMagEntity;
import com.wisedu.minos.casp.portal.dao.mapper.SiteMagMapper;
import com.wisedu.minos.casp.portal.model.SiteReqHeader;
import com.wisedu.minos.casp.portal.service.impl.ShowProgrammeService;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.MinosException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

public class MinosConsoleUtil {
    private static final Logger logger = LogManager.getLogger(MinosConsoleUtil.class);
    private static final String UID = "uid";
    private static final String UNAME = "uname";
    private static final String ISSUPER = "isSupper";

    public static String getLoginUserId(HttpServletRequest request) {
        String uid = request.getHeader(UID);
        return uid;
    }

    public static String getLoginUserName(HttpServletRequest request) {
        String uname = request.getHeader(UNAME);
        if (StringUtil.isNotEmpty(uname)) {
            try {
                uname = new String(Base64.getDecoder().decode(uname));
            } catch (Exception e) {
                logger.warn("用户名[" + uname + "]base64解码异常", e);
            }
        }
        return uname;
    }

    public static boolean getIsSupper(HttpServletRequest request) {
        String isSuper = request.getHeader(ISSUPER);
        if (StringUtil.isNotEmpty(isSuper)) {
            try {
                isSuper = new String(Base64.getDecoder().decode(isSuper));
            } catch (Exception e) {
                logger.warn("是否超管标识[" + isSuper + "]base64解码异常", e);
            }
        }
        return null != isSuper && "true".equals(isSuper);
    }

    public static boolean getIsSupperManager(HttpServletRequest request) {
        String userId = getLoginUserId(request);
        return null != userId && "sysadmin".equals(userId.toLowerCase());
    }

    public static void isRequestRightsLegal(List<String> siteWids){
        //获取当前的request
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String consoleAdminRightsInfo = request.getHeader("consoleAdminRightsInfo");
        if ( StringUtils.isBlank(consoleAdminRightsInfo) ) {
            return;
        }
        SiteReqHeader consoleAdmin = JSONObject.parseObject(HtmlUtils.htmlUnescape(consoleAdminRightsInfo), SiteReqHeader.class);
        if ( consoleAdmin.getIsSysAdmin()) {
            return;
        }

        SiteMagMapper siteMagMapper = ApplicationContextUtil.get(SiteMagMapper.class);
        // 获取管理员授权的站点
        List<SiteMagEntity> siteMags = siteMagMapper.selectList(
                new QueryWrapper<SiteMagEntity>().lambda()
                        .eq(SiteMagEntity::getIsDeleted,0)
                        .and(wrapper->wrapper.eq(SiteMagEntity::getMagRelWid,consoleAdmin.getUserWid()).eq(SiteMagEntity::getMagType,1))
                        .or(CollectionUtils.isNotEmpty(consoleAdmin.getRoleWids()), wrapper->wrapper.in(SiteMagEntity::getMagRelWid,consoleAdmin.getRoleWids()).eq(SiteMagEntity::getMagType,0))
        );
        if(CollectionUtils.isEmpty(siteMags)){
            throw new MinosException("没有权限操作该站点");
        }
        Set<String> operateableSites = siteMags.stream().map(siteMag->siteMag.getSiteWid()).collect(Collectors.toSet());
        if(!operateableSites.containsAll(siteWids)){
            throw new MinosException("没有权限操作该站点");
        }




    }

    public static void checkSiteAuth(List<String> programmeIds) {
        if ( CollectionUtils.isEmpty(programmeIds) ) {
            return;
        }
        ShowProgrammeService showProgrammeService = ApplicationContextUtil.get(ShowProgrammeService.class);
        // 根据栏目Id 获取站点id
        QueryWrapper<ShowProgrammeEntity> programmeWrapper = new QueryWrapper<>();
        programmeWrapper.lambda().select(ShowProgrammeEntity::getSiteWid)
                .eq(ShowProgrammeEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .in(ShowProgrammeEntity::getWid, programmeIds);

        List<ShowProgrammeEntity> programmeList = showProgrammeService.list(programmeWrapper);
        if ( CollectionUtils.isNotEmpty(programmeList)) {
            List<String> siteWids = programmeList.stream().map(ShowProgrammeEntity::getSiteWid).distinct().collect(Collectors.toList());
            isRequestRightsLegal(siteWids);
        }
    }

}
