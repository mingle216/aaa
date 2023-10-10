package com.wisedu.minos.casp.portal.vo;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title SwitchVersionVo
 * @Author: jcx
 * @Date: 2020/10/10
 */
public class SwitchVersionVo {

    private String wid;//卡片或者模板wid
    private String foreignKey;
    private String versionNumWid;//选择要切换的版本表wid
    private String versionNumber;//当前启用的包的版本号
    private String switchType;//类型  0：模板  1：卡片
    private String isRestart;//是否重启门户引擎 1:是  0:否

    public String getForeignKey () {
        return foreignKey;
    }

    public void setForeignKey (String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getIsRestart () {
        return isRestart;
    }

    public void setIsRestart (String isRestart) {
        this.isRestart = isRestart;
    }

    public String getSwitchType () {
        return switchType;
    }

    public void setSwitchType (String switchType) {
        this.switchType = switchType;
    }

    public String getWid () {
        return wid;
    }

    public void setWid (String wid) {
        this.wid = wid;
    }

    public String getVersionNumWid () {
        return versionNumWid;
    }

    public void setVersionNumWid (String versionNumWid) {
        this.versionNumWid = versionNumWid;
    }

    public String getVersionNumber () {
        return versionNumber;
    }

    public void setVersionNumber (String versionNumber) {
        this.versionNumber = versionNumber;
    }
}
