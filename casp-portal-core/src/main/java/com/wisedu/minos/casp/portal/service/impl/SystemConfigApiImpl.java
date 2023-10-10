package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.dao.entity.SystemConfigEntity;
import com.wisedu.minos.casp.portal.model.CommonResponseResult;
import com.wisedu.minos.casp.portal.model.CommonStringInfo;
import com.wisedu.minos.casp.portal.model.configforconsole.ConsoleConfigInfo;
import com.wisedu.minos.casp.portal.service.SystemConfigApi;
import com.wisedu.minos.casp.portal.service.SystemConfigApiAdapter;
import com.wisedu.minos.util.MinosException;
import dm.jdbc.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @title SystemConfigApiImpl
 * @Author: 01319098
 * @Date: 2022/11/21
 */
@Service
public class SystemConfigApiImpl implements SystemConfigApiAdapter {
    @Autowired
    private SystemConfigService systemConfigService;

    @Override
    public CommonResponseResult<List<ConsoleConfigInfo>> searchSysParam (@Valid CommonStringInfo body) {
        QueryWrapper<SystemConfigEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SystemConfigEntity::getIsDeleted, 0).eq(SystemConfigEntity::getPageShow, 1);
        if ( StringUtil.isNotEmpty(body.getDataInfo()) ) {
            queryWrapper.and(e -> {
                CommonUtil.addIlikeCondition(e, Arrays.asList("config_desc", "config_key"), body.getDataInfo());
                return e;
            });
        }
        List<ConsoleConfigInfo> configInfoList = new ArrayList<>();
        List<SystemConfigEntity> configs = systemConfigService.list(queryWrapper);
        for ( SystemConfigEntity con : configs ) {
            ConsoleConfigInfo info = new ConsoleConfigInfo();
            info.setTableName(SystemConfigEntity.class.getAnnotation(TableName.class).value().toUpperCase());
            info.setConfigKey(con.getConfigKey());
            info.setConfigDesc(con.getConfigDesc());
            info.setConfigValue(StringUtil.isEmpty(con.getConfigValue()) ? con.getDefaultValue(): con.getConfigValue());
            configInfoList.add(info);
        }
        CommonResponseResult result = new CommonResponseResult();
        result.setData(configInfoList);
        return result;
    }

    @Override
    public CommonResponseResult modifySysParam (ConsoleConfigInfo body) {
        if ( StringUtils.isBlank(body.getConfigKey()) || StringUtils.isBlank(body.getConfigValue()) ) {
            throw new MinosException("参数异常");
        }
        systemConfigService.update(null, Wrappers.<SystemConfigEntity>lambdaUpdate()
                .eq(SystemConfigEntity::getConfigKey, body.getConfigKey())
                .eq(SystemConfigEntity::getPageShow, 1)
                .set(SystemConfigEntity::getConfigValue, body.getConfigValue()));
        return new CommonResponseResult();
    }
}
