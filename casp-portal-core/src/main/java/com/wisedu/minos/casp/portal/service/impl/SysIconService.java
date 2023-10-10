package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisedu.minos.casp.portal.common.page.PageFactory;
import com.wisedu.minos.casp.portal.common.page.PageResult;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.SysIconEntity;
import com.wisedu.minos.casp.portal.dao.mapper.SysIconMapper;
import com.wisedu.minos.casp.portal.vo.SysInconVo;
import com.wisedu.minos.util.MinosCommonUtil;
import org.springframework.stereotype.Service;

/**
 * SysIconService
 * <p/>
 * 系统图标
 *
 * @author hyluan
 * @date 2020-10-1 19:28
 * Copyright (c) 2018 wisedu
 */
@Service
public class SysIconService extends MyServiceImpl<SysIconMapper, SysIconEntity> {

    /**
     * getSysInconPage
     * <p/>
     * 获取系统图标
     *
     * @param sysInconVo
     * @return com.wisedu.minos.casp.portal.common.page.PageResult<com.wisedu.minos.casp.portal.dao.entity.SysIconEntity>
     * @throws
     * @date 2020-10-1 19:31
     */
    public PageResult<SysIconEntity> getSysInconPage(SysInconVo sysInconVo) {
        //开启分页
        Page<SysIconEntity> page = PageFactory.createPageBegin(sysInconVo);
        LambdaQueryWrapper<SysIconEntity> sysIconQueryWrapper = Wrappers.<SysIconEntity>lambdaQuery().orderByAsc(SysIconEntity::getOrderNumber);
        if( StringUtil.isNotEmpty(sysInconVo.getIconType()) ){
            sysIconQueryWrapper.eq(SysIconEntity::getIconType,Integer.valueOf(sysInconVo.getIconType()));
        }
        if( StringUtil.isNotEmpty(sysInconVo.getIconName()) ){
            sysIconQueryWrapper.apply(MinosCommonUtil.getLikeEscapeSql("icon_name"), MinosCommonUtil.getLikeEscapeValue(sysInconVo.getIconName()));
        }
        return PageFactory.pageResult(baseMapper.selectPage(page,sysIconQueryWrapper));
    }

}
