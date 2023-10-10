package com.wisedu.minos.casp.portal.vo;

import com.wisedu.minos.casp.portal.common.page.PageBase;

/**
 * 功能描述：系统预置图标数据传参Vo
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title SysInconVo
 * @Author: jcx
 * @Date: 2020/8/28
 */
public class SysInconVo extends PageBase {
    /**
     *图标名称
     */
    private String iconName;
    /**
     * 图标描述
     */
    private String iconDesc;
    /**
     * 图标类型  0 字体图标  1 png图标
     */
    private String iconType;

    public String getIconType () {
        return iconType;
    }

    public void setIconType (String iconType) {
        this.iconType = iconType;
    }

    public String getIconName () {
        return iconName;
    }

    public void setIconName (String iconName) {
        this.iconName = iconName;
    }

    public String getIconDesc () {
        return iconDesc;
    }

    public void setIconDesc (String iconDesc) {
        this.iconDesc = iconDesc;
    }
}
