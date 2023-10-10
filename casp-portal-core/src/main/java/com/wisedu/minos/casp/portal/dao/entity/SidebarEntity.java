package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * 侧边栏表
 * </p>
 *
 * @author jdwan
 * @since 2020-09-15
 */
@TableName("t_amp_view_sidebar")
public class SidebarEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 栏目编码,不可重复
     */
    @TableField("column_code")
    private String columnCode;

    /**
     * 栏目名称
     */
    @TableField("column_name")
    private String columnName;

    /**
     * 是否系统自带栏目（0：否；1：是）
     */
    @TableField("is_system")
    private Integer isSystem;

    /**
     * 栏目的图标类型（0：png图标；1：字体图标）
     */
    @TableField("icon_type")
    private Integer iconType;

    /**
     * 授权类别（0：游客可见；1：登录后可见；2：授权后可见）
     */
    @TableField("auth_type")
    private Integer authType;

    /**
     * 是否启用（0：否；1：是）
     */
    @TableField("enabled_flag")
    private Integer enabledFlag;

    /**
     * 排序
     */
    @TableField("order_number")
    private Integer orderNumber;

    /**
     * 栏目链接地址，非特定栏目有值
     */
    @TableField("link_url")
    private String linkUrl;

    /**
     * 栏目的图标样式名称
     */
    @TableField("icon_url")
    private String iconUrl;

    /**
     * 展示名称
     */
    @TableField("display_name")
    private String displayName;

    /**
     * 方位0:(左侧),1:(右侧)
     */
    @TableField("position_type")
    private Integer positionType;

    /**
     * 展示名称国际化id
     */
    @TableField("display_name_lang_key")
    private String displayNameLangKey;

    /**
     * 展示方案id
     */
    @TableField("programme_id")
    private String programmeId;

    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;
    /**
     * 菜单类型0：无链接1：内部页面2：第三方链接
     */
    @TableField("menu_type")
    private Integer menuType;
    /**
     * 对应页面
     */
    @TableField("page_id")
    private String pageId;

    /**
     * 打开方式  0:当前页 1:新开窗口
     */
    @TableField("open_type")
    private Integer openType;

    /**
     * 数量提醒地址
     */
    @TableField("count_address")
    private String countAddress;

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public Integer getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Integer isSystem) {
        this.isSystem = isSystem;
    }
    public Integer getIconType() {
        return iconType;
    }

    public void setIconType(Integer iconType) {
        this.iconType = iconType;
    }
    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getPositionType() {
        return positionType;
    }

    public void setPositionType(Integer positionType) {
        this.positionType = positionType;
    }

    public String getDisplayNameLangKey() {
        return displayNameLangKey;
    }

    public void setDisplayNameLangKey(String displayNameLangKey) {
        this.displayNameLangKey = displayNameLangKey;
    }

    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(String programmeId) {
        this.programmeId = programmeId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public Integer getOpenType () {
        return openType;
    }

    public void setOpenType (Integer openType) {
        this.openType = openType;
    }

    public String getCountAddress() {
        return countAddress;
    }

    public void setCountAddress(String countAddress) {
        this.countAddress = countAddress;
    }
}
