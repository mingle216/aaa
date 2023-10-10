package com.wisedu.minos.casp.portal.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wisedu.minos.casp.portal.base.BasicEntity;

import java.io.Serializable;

/**
 * <p>
 * 展示方案表
 * </p>
 *
 * @author jdwan
 * @since 2020-09-15
 */
@TableName("t_amp_view_show_programme")
public class ShowProgrammeEntity extends BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 展示方案名称
     */
    @TableField("programme_name")
    private String programmeName;

    /**
     * 适配终端0：PC1：移动端
     */
    @TableField("platform_type")
    private Integer platformType;

    /**
     * 模板id
     */
    @TableField("template_id")
    private String templateId;

    /**
     * 页面数量
     */
    @TableField("page_amount")
    private Integer pageAmount;

    /**
     * 状态0：启用1：停用
     */
    @TableField("page_status")
    private Integer pageStatus;

    /**
     * 校徽/LOGO地址
     */
    @TableField("logo_url")
    private String logoUrl;

    /**
     * 模板配置
     */
    @TableField("template_config")
    private String templateConfig;

    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;
    /**
     * 是否展示左/右侧栏0都不展示 1只展示左侧栏2只展示右侧栏3、都展示
     */
    @TableField("side_flag")
    private Integer sideFlag;
    /**
     * 是否初始化方案（初始化展示方案隐藏）：0否  1是
     */
    @TableField("initialize_flag")
    private Integer initializeFlag;
    /**
     * 是否显示PC端服务  1:显示  0:隐藏
     */
    @TableField("is_show_pc_service")
    private Integer isShowPcService;

    public Integer getIsShowPcService() {
        return isShowPcService;
    }

    public void setIsShowPcService(Integer isShowPcService) {
        this.isShowPcService = isShowPcService;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public Integer getPlatformType() {
        return platformType;
    }

    public void setPlatformType(Integer platformType) {
        this.platformType = platformType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    public Integer getPageAmount() {
        return pageAmount;
    }

    public void setPageAmount(Integer pageAmount) {
        this.pageAmount = pageAmount;
    }

    public Integer getPageStatus() {
        return pageStatus;
    }

    public void setPageStatus(Integer pageStatus) {
        this.pageStatus = pageStatus;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    public String getTemplateConfig() {
        return templateConfig;
    }

    public void setTemplateConfig(String templateConfig) {
        this.templateConfig = templateConfig;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getSideFlag() {
        return sideFlag;
    }

    public void setSideFlag(Integer sideFlag) {
        this.sideFlag = sideFlag;
    }

    public Integer getInitializeFlag() {
        return initializeFlag;
    }

    public void setInitializeFlag(Integer initializeFlag) {
        this.initializeFlag = initializeFlag;
    }
}
