package com.wisedu.minos.casp.portal.model.v353beta2;

import com.wisedu.minos.casp.portal.common.constant.Global;
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
 * @title PortalSelectMenusReq
 * @Author: 01120034
 * @Date: 2022/10/8
 */
@Getter
@Setter
public class PortalSelectMenusReq {
    private String programmeWid;
    private String lang = Global.DEFAULT_LANGUAGE;
}
