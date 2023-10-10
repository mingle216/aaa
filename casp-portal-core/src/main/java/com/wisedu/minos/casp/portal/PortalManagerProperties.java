package com.wisedu.minos.casp.portal;

import com.wisedu.minos.api.data.UserService;
import com.wisedu.minos.casp.portal.dao.entity.SystemConfigEntity;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * PortalManagerProperties
 * <p/>
 * 门户管理员配置
 *
 * @author hyluan
 * @date 2020/10/10 19:29
 * Copyright (c) 2018 wisedu
 */
@Configuration
@Data
@Slf4j
public class PortalManagerProperties {

    private static final String PERSON_CENTER_URL = "personCenterUrl";
    private static final String ACCOUNT_MANAGE_URL = "accountManageUrl";
    private static final String BACKEND_MANAGE_URL = "backendManageUrl";


    @Autowired
    SystemConfigService systemConfigService;

    @DubboReference
    UserService userService;

    @Autowired
    UserUtil util;

    /**
     * 个人中心地址
     */
    String personCenterUrl;
    /**
     * 帐号管理地址
     */
    String accountManageUrl;

    /**
     * 融合管控台地址
     */
    String backendManageUrl;

    /**
     * <p/>
     *
     * @param
     * @return
     * @throws
     * @date 2020-11-1 12:07
     */
    public PortalManagerProperties loadData() {
        PortalManagerProperties portalManagerProperties = new PortalManagerProperties();
        //是否管控台管理员
        Boolean flag = userService.checkManagerUser(util.getUserAccount());
        List<String> list = Lists.newArrayList();
        list.add(PERSON_CENTER_URL);
        list.add(ACCOUNT_MANAGE_URL);
        if(flag){
            LOGGER.info(util.getUserAccount()+" 是管控台管理员，具有后台管理菜单");
            list.add(BACKEND_MANAGE_URL);
        }
        List<SystemConfigEntity> systemConfigs = systemConfigService.getSystemConfigs(list);
        for (SystemConfigEntity systemConfig : systemConfigs) {
            switch (systemConfig.getConfigKey()) {
                case PERSON_CENTER_URL:
                    portalManagerProperties.personCenterUrl = getValue(systemConfig);
                    break;
                case ACCOUNT_MANAGE_URL:
                    portalManagerProperties.accountManageUrl = getValue(systemConfig);
                    break;
                case BACKEND_MANAGE_URL:
                    portalManagerProperties.backendManageUrl = getValue(systemConfig);
                    break;
                default:
                    break;
            }
        }
        LOGGER.info("PortalManagerProperties 配置属性：" + this.toString());
        return portalManagerProperties;
    }

    private String getValue(SystemConfigEntity item) {
        return StringUtils.isEmpty(item.getConfigValue()) ? item.getDefaultValue() : item.getConfigValue();
    }
}
