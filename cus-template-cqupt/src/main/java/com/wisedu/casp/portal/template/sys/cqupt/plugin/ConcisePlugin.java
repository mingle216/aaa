package com.wisedu.casp.portal.template.sys.cqupt.plugin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wisedu.casp.portal.template.sys.cqupt.model.ConfigResult;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.dao.entity.PersonalDataEntity;
import com.wisedu.minos.casp.portal.dao.mapper.PersonalDataMapper;
import com.wisedu.minos.casp.portal.model.TemplateAjaxRequest;
import com.wisedu.minos.casp.portal.model.TemplateConfigRequest;
import com.wisedu.minos.casp.portal.model.configcomponent.AbstractComponent;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractTemplate;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2022/2/24
 */

@MinosSPI
public class ConcisePlugin extends AbstractTemplate{

    private final static Logger logger = LogManager.getLogger(ConcisePlugin.class);

    @Override
    protected Object doExecDispatcher(TemplateAjaxRequest request) {
        return null;
    }

    @Override
    public String getTemplateId() {
        return ConciseVariable.TEMPLATE_ID;
    }

    @Override
    public String getTemplateConfig(String platformType) {
        return StringUtil.isNotEmpty(platformType)&&platformType.equals(String.valueOf(Global.PlatformType.MOBILE.getType()))?ConciseVariable.DEFAULT_CONCISE_MOBILE_CONFIG:ConciseVariable.DEFAULT_CONCISE_TEMPLATE_CONFIG;
    }

    @Override
    public ComponentContainer getConfig(TemplateConfigRequest templateConfigRequest) {
        super.getConfig(templateConfigRequest);
        ComponentContainer componentContainer = templateConfigRequest.getComponentContainer();
        List<AbstractComponent> components = componentContainer.getComponents();
        if (components != null) {
            AbstractComponent dataSourceComponent = components.stream().filter(e -> "dataSourceConfig".equals(e.getKey()))
                    .findFirst().orElse(null);
            if (dataSourceComponent != null) {
                LambdaQueryWrapper<PersonalDataEntity> queryWrapper = new LambdaQueryWrapper<PersonalDataEntity>()
                        .eq(PersonalDataEntity::getPersonalData, 1)
                        .eq(PersonalDataEntity::getIsDeleted, 0);
                List<PersonalDataEntity> personalDataEntities = ApplicationContextUtil.get(PersonalDataMapper.class).selectList(queryWrapper);
                List<ConfigResult> personalTitle = personalDataEntities.stream().map(personalDataEntity -> {
                    ConfigResult configResult = new ConfigResult();
                    configResult.setWid(personalDataEntity.getWid());
                    configResult.setData(personalDataEntity.getPersonalTitle());
                    return configResult;
                }).collect(Collectors.toList());
                dataSourceComponent.setDatas(personalTitle);
            }
        }
        return componentContainer;
    }

    @Override
    public void destroy() {

    }
}
