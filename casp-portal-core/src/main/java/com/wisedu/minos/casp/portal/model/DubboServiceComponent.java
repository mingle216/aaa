package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.api.config.SystemToolsService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title DubboServiceComponent
 * @Author: 01319098
 * @Date: 2021/3/4
 */
@Component
public class DubboServiceComponent {
    @DubboReference
    private SystemToolsService systemToolsService;

    public SystemToolsService getSystemToolsService() {
        return systemToolsService;
    }
}
