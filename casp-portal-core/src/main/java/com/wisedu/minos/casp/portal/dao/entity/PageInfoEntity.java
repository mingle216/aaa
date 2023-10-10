package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * 页面信息表
 * </p>
 *
 * @author jdwan
 * @since 2020-09-15
 */
@TableName("t_amp_view_page_info")
public class PageInfoEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页面名称
     */
    @TableField("page_name")
    private String pageName;

    /**
     * 页面代码
     */
    @TableField("page_code")
    private String pageCode;

    /**
     * 运行平台 0:PC 1:Mobile
     */
    @TableField("platform_type")
    private Integer platformType;

    /**
     * 背景底色
     */
    @TableField("background_color")
    private String backgroundColor;

    /**
     * 背景图片
     */
    @TableField("background_image")
    private String backgroundImage;

    /**
     * 模板代码
     */
    @TableField("template_code")
    private String templateCode;

    /**
     * 卡片布局数据
     */
    @TableField("card_layout")
    private String cardLayout;

    /**
     * 父级页面ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 背景图片
     */
    @TableField("page_title")
    private String pageTitle;

    /**
     * 展示方案id
     */
    @TableField("programme_id")
    private String programmeId;

    /**
     * 页面类型0：功能页1：自由页
     */
    @TableField("page_type")
    private Integer pageType;

    /**
     * 页面标题国际化id
     */
    @TableField("page_title_lang_key")
    private String pageTitleLangKey;

    /**
     * 页面配置信息
     */
    @TableField("page_config")
    private String pageConfig;

    /**
     * 是否启用（0：否；1：是）
     */
    @TableField("enabled_flag")
    private Integer enabledFlag;
    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;


    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public Integer getPlatformType() {
        return platformType;
    }

    public void setPlatformType(Integer platformType) {
        this.platformType = platformType;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
    public String getCardLayout() {
        return cardLayout;
    }

    public void setCardLayout(String cardLayout) {
        this.cardLayout = cardLayout;
    }
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(String programmeId) {
        this.programmeId = programmeId;
    }
    public Integer getPageType() {
        return pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
    }

    public String getPageTitleLangKey() {
        return pageTitleLangKey;
    }

    public void setPageTitleLangKey(String pageTitleLangKey) {
        this.pageTitleLangKey = pageTitleLangKey;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getPageConfig() {
        return pageConfig;
    }

    public void setPageConfig(String pageConfig) {
        this.pageConfig = pageConfig;
    }
}
