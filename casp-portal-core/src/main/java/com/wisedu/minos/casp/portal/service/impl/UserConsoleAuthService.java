package com.wisedu.minos.casp.portal.service.impl;

import com.wisedu.minos.api.config.SystemConfigService;
import com.wisedu.minos.util.MenuEditTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
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
 * @title UserConsoleAuthService
 * @Author: 01319098
 * @Date: 2022/3/17
 */
@Service
public class UserConsoleAuthService {
    @DubboReference
    private SystemConfigService systemConfigService;

    public boolean isUserEditAuthority (List<MenuEditTypeEnum> editTypes, String menuId) {
        if ( CollectionUtils.isEmpty(editTypes) || StringUtils.isBlank(menuId) ) {
            return true;
        }
        //是否开启菜单权限校验
        String roleEnabled = systemConfigService.getConfigValue("system", "config.check.role.edit.enabled", "1");
        if ( ! "1".equals(roleEnabled) ) {
            return true;
        }
        return MenuEditTypeEnum.isEditRole(editTypes, menuId);
    }
}
