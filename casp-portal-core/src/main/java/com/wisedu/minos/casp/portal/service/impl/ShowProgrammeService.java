package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.mapper.ShowProgrammeMapper;
import com.wisedu.minos.casp.portal.utils.CardUtil;
import com.wisedu.minos.gateway.client.bean.LicenseInfo;
import com.wisedu.minos.gateway.client.license.MinosLicenseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
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
 * @title ProgrammeService
 * @Author: u
 * @Date: 2020/9/21
 */
@Service
@Slf4j

public class ShowProgrammeService extends MyServiceImpl<ShowProgrammeMapper, ShowProgrammeEntity> {

    @Autowired
    private CardUtil cardUtil;
    @Autowired
    Environment environment;
    public ShowProgrammeEntity getDefaultProgramme(Integer platformType,String siteWid) {
        List<ShowProgrammeEntity> list = this.list(Wrappers.<ShowProgrammeEntity>lambdaQuery()
                .eq(ShowProgrammeEntity::getPageStatus, Global.PageStatus.ENABLE.getId())
                .eq(ShowProgrammeEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .eq(ShowProgrammeEntity::getSiteWid, siteWid)
        );
        List<ShowProgrammeEntity> collect = list.stream().filter(e -> e.getPlatformType().equals(platformType)).collect(Collectors.toList());
        //获取license信息
        LicenseInfo licenseInfo = MinosLicenseManager.getLicenseInfo();
        List<LicenseInfo.App> apps = licenseInfo.getApps();
//        AtomicBoolean flag = new AtomicBoolean(true);
        AtomicBoolean flag = new AtomicBoolean(false);
        apps.forEach(app->{
            List<LicenseInfo.Module> modules = app.getModules();
            if("CASP".equals(app.getId())){
                modules.forEach(module -> {
                    if("casp-mobile".equals(module.getName())){
                        flag.set(true);
                    }
                });
            }
        });
        if("true".equals(environment.getProperty("portal.ignore.mobileLicense"))){
            flag.set(true);
        }
        if(!flag.get()&&platformType==Global.PlatformType.MOBILE.getType()){
            return list.stream().filter(e -> e.getPlatformType()
                    .equals(Global.PlatformType.PC.getType())).collect(Collectors.toList()).get(0);
        }
        if(!collect.isEmpty()){
            ShowProgrammeEntity showProgrammeEntity = collect.get(0);
            if( StringUtil.isNotEmpty(showProgrammeEntity.getTemplateConfig())){
                showProgrammeEntity.setTemplateConfig(cardUtil.filterHttpOrHttps(showProgrammeEntity.getTemplateConfig()));
            }
            return collect.get(0);
        }
        if (list.isEmpty()) {
            throw new BusinessException(String.format("未获取到平台：%s的默认展示方案", platformType));
        }
        return list.get(0);
    }
}
