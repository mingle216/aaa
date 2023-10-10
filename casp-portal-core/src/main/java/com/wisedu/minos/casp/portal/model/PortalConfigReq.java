package com.wisedu.minos.casp.portal.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述：获取门户配置入参
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title PortalConfigReq
 * @Author: 01120034
 * @Date: 2021/7/29
 */
@Getter
@Setter
public class PortalConfigReq {
    private String key;
    private String value;
    private String defaultValue;
}
