package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * 顶部菜单表
 * </p>
 *
 * @author jdwan
 * @since 2020-09-15
 */
@TableName("t_amp_view_menu")
public class MenuEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 图标
     */
    @TableField("icon_url")
    private String iconUrl;

    /**
     * 排序
     */
    @TableField("order_number")
    private Integer orderNumber;

    /**
     * 0:游客可见 1:登录后可见  2:游客及登录后可见 3:授权后可见
     */
    @TableField("auth_type")
    private Integer authType;

    /**
     * 是否启用 0:停用 1:启用
     */
    @TableField("is_enabled")
    private Integer isEnabled;

    /**
     * 打开方式  0:当前页 1:新开窗口
     */
    @TableField("open_type")
    private Integer openType;

    /**
     * 菜单链接
     */
    @TableField("link_url")
    private String linkUrl;

    /**
     * 父级菜单ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 展示方案Id
     */
    @TableField("programme_id")
    private String programmeId;

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
     * 图标类型0：png1：字体图标
     */
    @TableField("icon_type")
    private Integer iconType;

    /**
     * 菜单名称国际化id
     */
    @TableField("menu_name_lang_key")
    private String menuNameLangKey;

    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }
    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }
    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(String programmeId) {
        this.programmeId = programmeId;
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
    public Integer getIconType() {
        return iconType;
    }

    public void setIconType(Integer iconType) {
        this.iconType = iconType;
    }

    public String getMenuNameLangKey() {
        return menuNameLangKey;
    }

    public void setMenuNameLangKey(String menuNameLangKey) {
        this.menuNameLangKey = menuNameLangKey;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
