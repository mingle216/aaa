package com.wisedu.minos.casp.portal.model.v311beta2;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 *
 * 站点传输对象
 * @date 2021/11/30 16:54
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Data
@Accessors(chain = true)
public class SiteDto {
    /**
     *  所有可用的站点路由
     */
    private Set<String> allEnableSiteRoutes;
    /**
     *  用户的站点路由
     */
    private Set<String> userEnableSiteRoutes;
    /**
     *  用户的站点路由
     */
    private Set<MultiSitesDto> userMultiSite;
    /**
     * 是否有主站点权限
     */
    private boolean isMasterPermission;
}
