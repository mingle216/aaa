package com.wisedu.minos.casp.portal.model.v353beta2;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title SystemMenuEnable
 * @Author: 01120034
 * @Date: 2022/10/8
 */
@Getter
@Setter
public class SystemMenuEnable {
    //个人中心
    private boolean personCenter =false;
    private String personCenterUrl;
    //岗位管理
    private boolean postitionManager =false;
    private String postitionManagerUrl;
    //后台管理
    private boolean backendManager =false;
    private String backendManagerUrl;
    //事项管理
    private boolean simManager =false;
    private String simManagerUrl;
    //效能看板
    private boolean simPortal =false;
    private String simPortalUrl;
}
