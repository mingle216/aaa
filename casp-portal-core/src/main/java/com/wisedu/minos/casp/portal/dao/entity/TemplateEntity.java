package com.wisedu.minos.casp.portal.dao.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wisedu.minos.casp.portal.base.BasicEntity;
import java.sql.Clob;
import java.io.Serializable;
/**
 * <p>
 * 模板表
 * </p>
 *
 * @author jcx
 * @since 2020-09-15
 */
@TableName("t_amp_view_template")
public class TemplateEntity extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 模板id
     */
    @TableField("template_id")
    private String templateId;
    /**
     * 模板系统类型0：系统模板1：自定义模板
     */
    @TableField("template_system_type")
    private Integer templateSystemType;
    /**
     * 模板名称
     */
    @TableField("template_name")
    private String templateName;
    /**
     * 模板展示图URL
     */
    @TableField("template_img_url")
    private String templateImgUrl;
    /**
     * 模板描述
     */
    @TableField("template_desc")
    private String templateDesc;
    /**
     * 数据源配置信息
     */
    @TableField("configure_content")
    private String configureContent;
    /**
     * 是否支持可配置 0:不支持 1:支持
     */
    @TableField("configurable_flag")
    private Integer configurableFlag;
    /**
     * 是否支持运行时可配置 0:不支持 1:支持
     */
    @TableField("configurable_runtime_flag")
    private Integer configurableRuntimeFlag;
    /**
     * 运行平台 0:PC 1:Mobile（1是0否多位连接，例00 都不支持）
     */
    @TableField("platform_type")
    private String platformType;
    /**
     * 状态0正常1有更新2不可用
     */
    @TableField("template_status")
    private Integer templateStatus;
    /**
     * 版本号
     */
    @TableField("version_number")
    private String versionNumber;
    /**
     * 是否已经删除 0:否 1:是
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 学校代码
     */
    @TableField(exist = false)
    private String schoolId;

    /**
     * 移动端模板主图地址
     */
    @TableField("template_img_mobile_url")
    private String templateImgMobileUrl;

    public String getTemplateImgMobileUrl() {
        return templateImgMobileUrl;
    }

    public void setTemplateImgMobileUrl(String templateImgMobileUrl) {
        this.templateImgMobileUrl = templateImgMobileUrl;
    }

    public String getSchoolId () {
        return schoolId;
    }

    public void setSchoolId (String schoolId) {
        this.schoolId = schoolId;
    }

    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    public Integer getTemplateSystemType() {
        return templateSystemType;
    }
    public void setTemplateSystemType(Integer templateSystemType) {
        this.templateSystemType = templateSystemType;
    }
    public String getTemplateName() {
        return templateName;
    }
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    public String getTemplateImgUrl() {
        return templateImgUrl;
    }
    public void setTemplateImgUrl(String templateImgUrl) {
        this.templateImgUrl = templateImgUrl;
    }
    public String getTemplateDesc() {
        return templateDesc;
    }
    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    public String getConfigureContent() {
        return configureContent;
    }

    public void setConfigureContent(String configureContent) {
        this.configureContent = configureContent;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public Integer getTemplateStatus() {
        return templateStatus;
    }

    public void setTemplateStatus(Integer templateStatus) {
        this.templateStatus = templateStatus;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Integer getConfigurableFlag() {
        return configurableFlag;
    }

    public void setConfigurableFlag(Integer configurableFlag) {
        this.configurableFlag = configurableFlag;
    }

    public Integer getConfigurableRuntimeFlag() {
        return configurableRuntimeFlag;
    }

    public void setConfigurableRuntimeFlag(Integer configurableRuntimeFlag) {
        this.configurableRuntimeFlag = configurableRuntimeFlag;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}