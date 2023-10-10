package com.wisedu.minos.casp.portal.model.v354Beta2;

import lombok.Getter;
import lombok.Setter;

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
 * @title CommonConfig
 * @Author: 01120034
 * @Date: 2022/11/14
 */
@Getter
@Setter
public class CommonConfig {
    //pc是否开启黑白模式 1:是  0:否
    private String isPcBlackAndWhite="0";
    //h5是否开启黑白模式 1:是  0:否
    private String isH5BlackAndWhite="0";
    //门户多站点方案  0:匹配站点地址   1：随机展示站点
    private String siteSwitchRule="0";
    //浏览器兼容提示是否开启 1:是  0:否
    private String isBrowserCompatible="0";
    //兼容浏览器 0：360极速模式、1：Safari、2：Chrome、3：Firefox、4：IE11及以上、5：Edge     英文逗号分隔
    private String compatibleBrowser="0,1,2,3,4,5";
    //兼容提示文案
    private List<SysConfigLang> compatibleInfo;
    //提示文案是否修改
    private String isChanged="0";
    //登录后自定义跳转
    private String isCustomJump="0";
    private String customJumpUrl;
}
