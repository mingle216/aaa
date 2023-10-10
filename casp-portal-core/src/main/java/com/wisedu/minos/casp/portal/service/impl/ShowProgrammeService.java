package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.mapper.ShowProgrammeMapper;
import org.springframework.stereotype.Service;

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
 * @title ProgrammeService
 * @Author: u
 * @Date: 2020/9/21
 */
@Service
public class ShowProgrammeService extends ServiceImpl<ShowProgrammeMapper, ShowProgrammeEntity> {

    public ShowProgrammeEntity getDefaultProgramme(Integer platformType) {
        List<ShowProgrammeEntity> list = this.list(Wrappers.<ShowProgrammeEntity>lambdaQuery()
                .eq(ShowProgrammeEntity::getPageStatus, Global.PageStatus.ENABLE.getId())
                .eq(ShowProgrammeEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .eq(ShowProgrammeEntity::getPlatformType, platformType));
        if (list.isEmpty()) {
            throw new BusinessException(String.format("未获取到平台：%s的默认展示方案", platformType));
        }
        return list.get(0);
    }
}
