package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TemplateInfo
 */
@Validated

public class TemplateInfo   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("templateId")
  private String templateId = null;

  @JsonProperty("templateSystemType")
  private Integer templateSystemType = null;

  @JsonProperty("templateName")
  private String templateName = null;

  @JsonProperty("templateImgUrl")
  private String templateImgUrl = null;

  @JsonProperty("templateImgMobileUrl")
  private String templateImgMobileUrl = null;

  @JsonProperty("templateDesc")
  private String templateDesc = null;

  @JsonProperty("configureContent")
  private String configureContent = null;

  @JsonProperty("configurableFlag")
  private Integer configurableFlag = null;

  @JsonProperty("configurableRuntimeFlag")
  private Integer configurableRuntimeFlag = null;

  @JsonProperty("platformType")
  private String platformType = null;

  @JsonProperty("templateStatus")
  private Integer templateStatus = null;

  @JsonProperty("versionNumber")
  private String versionNumber = null;

  @JsonProperty("updateTime")
  private String updateTime = null;

  @JsonProperty("refPage")
  private Boolean refPage = null;

  public TemplateInfo wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 主键wid
   * @return wid
  **/
  @ApiModelProperty(value = "主键wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public TemplateInfo templateId(String templateId) {
    this.templateId = templateId;
    return this;
  }

  /**
   * 模板id
   * @return templateId
  **/
  @ApiModelProperty(value = "模板id")
  
    public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public TemplateInfo templateSystemType(Integer templateSystemType) {
    this.templateSystemType = templateSystemType;
    return this;
  }

  /**
   * 模板系统类型0：系统模板1：自定义模板
   * @return templateSystemType
  **/
  @ApiModelProperty(value = "模板系统类型0：系统模板1：自定义模板")
  
    public Integer getTemplateSystemType() {
    return templateSystemType;
  }

  public void setTemplateSystemType(Integer templateSystemType) {
    this.templateSystemType = templateSystemType;
  }

  public TemplateInfo templateName(String templateName) {
    this.templateName = templateName;
    return this;
  }

  /**
   * 模板名称
   * @return templateName
  **/
  @ApiModelProperty(value = "模板名称")
  
    public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public TemplateInfo templateImgUrl(String templateImgUrl) {
    this.templateImgUrl = templateImgUrl;
    return this;
  }

  /**
   * 模板展示图URL
   * @return templateImgUrl
  **/
  @ApiModelProperty(value = "模板展示图URL")
  
    public String getTemplateImgUrl() {
    return templateImgUrl;
  }

  public void setTemplateImgUrl(String templateImgUrl) {
    this.templateImgUrl = templateImgUrl;
  }

  public TemplateInfo templateImgMobileUrl(String templateImgMobileUrl) {
    this.templateImgMobileUrl = templateImgMobileUrl;
    return this;
  }

  /**
   * 模板移动端展示图URL
   * @return templateImgMobileUrl
  **/
  @ApiModelProperty(value = "模板移动端展示图URL")
  
    public String getTemplateImgMobileUrl() {
    return templateImgMobileUrl;
  }

  public void setTemplateImgMobileUrl(String templateImgMobileUrl) {
    this.templateImgMobileUrl = templateImgMobileUrl;
  }

  public TemplateInfo templateDesc(String templateDesc) {
    this.templateDesc = templateDesc;
    return this;
  }

  /**
   * 模板描述
   * @return templateDesc
  **/
  @ApiModelProperty(value = "模板描述")
  
    public String getTemplateDesc() {
    return templateDesc;
  }

  public void setTemplateDesc(String templateDesc) {
    this.templateDesc = templateDesc;
  }

  public TemplateInfo configureContent(String configureContent) {
    this.configureContent = configureContent;
    return this;
  }

  /**
   * 配置信息
   * @return configureContent
  **/
  @ApiModelProperty(value = "配置信息")
  
    public String getConfigureContent() {
    return configureContent;
  }

  public void setConfigureContent(String configureContent) {
    this.configureContent = configureContent;
  }

  public TemplateInfo configurableFlag(Integer configurableFlag) {
    this.configurableFlag = configurableFlag;
    return this;
  }

  /**
   * 是否支持可配置 0:不支持 1:支持
   * @return configurableFlag
  **/
  @ApiModelProperty(value = "是否支持可配置 0:不支持 1:支持")
  
    public Integer getConfigurableFlag() {
    return configurableFlag;
  }

  public void setConfigurableFlag(Integer configurableFlag) {
    this.configurableFlag = configurableFlag;
  }

  public TemplateInfo configurableRuntimeFlag(Integer configurableRuntimeFlag) {
    this.configurableRuntimeFlag = configurableRuntimeFlag;
    return this;
  }

  /**
   * 是否支持运行时可配置 0:不支持 1:支持
   * @return configurableRuntimeFlag
  **/
  @ApiModelProperty(value = "是否支持运行时可配置 0:不支持 1:支持")
  
    public Integer getConfigurableRuntimeFlag() {
    return configurableRuntimeFlag;
  }

  public void setConfigurableRuntimeFlag(Integer configurableRuntimeFlag) {
    this.configurableRuntimeFlag = configurableRuntimeFlag;
  }

  public TemplateInfo platformType(String platformType) {
    this.platformType = platformType;
    return this;
  }

  /**
   * 运行平台 0:PC 1:Mobile（1是0否多位连接，例00 都不支持）
   * @return platformType
  **/
  @ApiModelProperty(value = "运行平台 0:PC 1:Mobile（1是0否多位连接，例00 都不支持）")
  
    public String getPlatformType() {
    return platformType;
  }

  public void setPlatformType(String platformType) {
    this.platformType = platformType;
  }

  public TemplateInfo templateStatus(Integer templateStatus) {
    this.templateStatus = templateStatus;
    return this;
  }

  /**
   * 状态0正常1有更新2不可用
   * @return templateStatus
  **/
  @ApiModelProperty(value = "状态0正常1有更新2不可用")
  
    public Integer getTemplateStatus() {
    return templateStatus;
  }

  public void setTemplateStatus(Integer templateStatus) {
    this.templateStatus = templateStatus;
  }

  public TemplateInfo versionNumber(String versionNumber) {
    this.versionNumber = versionNumber;
    return this;
  }

  /**
   * 版本号
   * @return versionNumber
  **/
  @ApiModelProperty(value = "版本号")
  
    public String getVersionNumber() {
    return versionNumber;
  }

  public void setVersionNumber(String versionNumber) {
    this.versionNumber = versionNumber;
  }

  public TemplateInfo updateTime(String updateTime) {
    this.updateTime = updateTime;
    return this;
  }

  /**
   * 最近一次更新时间
   * @return updateTime
  **/
  @ApiModelProperty(value = "最近一次更新时间")
  
    public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  /**
   * 关联状态 true关联 false未关联
   *
   * @return cardStatus
   **/
  @ApiModelProperty(value = "关联状态 true关联 false未关联")
  public Boolean getRefPage() {
    return refPage;
  }

  public void setRefPage(Boolean refPage) {
    this.refPage = refPage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TemplateInfo templateInfo = (TemplateInfo) o;
    return Objects.equals(this.wid, templateInfo.wid) &&
        Objects.equals(this.templateId, templateInfo.templateId) &&
        Objects.equals(this.templateSystemType, templateInfo.templateSystemType) &&
        Objects.equals(this.templateName, templateInfo.templateName) &&
        Objects.equals(this.templateImgUrl, templateInfo.templateImgUrl) &&
        Objects.equals(this.templateImgMobileUrl, templateInfo.templateImgMobileUrl) &&
        Objects.equals(this.templateDesc, templateInfo.templateDesc) &&
        Objects.equals(this.configureContent, templateInfo.configureContent) &&
        Objects.equals(this.configurableFlag, templateInfo.configurableFlag) &&
        Objects.equals(this.configurableRuntimeFlag, templateInfo.configurableRuntimeFlag) &&
        Objects.equals(this.platformType, templateInfo.platformType) &&
        Objects.equals(this.templateStatus, templateInfo.templateStatus) &&
        Objects.equals(this.versionNumber, templateInfo.versionNumber) &&
        Objects.equals(this.refPage, templateInfo.refPage) &&
        Objects.equals(this.updateTime, templateInfo.updateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, templateId, templateSystemType, templateName, templateImgUrl, templateImgMobileUrl, templateDesc, configureContent, configurableFlag, configurableRuntimeFlag, platformType, templateStatus, versionNumber, updateTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplateInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    templateSystemType: ").append(toIndentedString(templateSystemType)).append("\n");
    sb.append("    templateName: ").append(toIndentedString(templateName)).append("\n");
    sb.append("    templateImgUrl: ").append(toIndentedString(templateImgUrl)).append("\n");
    sb.append("    templateImgMobileUrl: ").append(toIndentedString(templateImgMobileUrl)).append("\n");
    sb.append("    templateDesc: ").append(toIndentedString(templateDesc)).append("\n");
    sb.append("    configureContent: ").append(toIndentedString(configureContent)).append("\n");
    sb.append("    configurableFlag: ").append(toIndentedString(configurableFlag)).append("\n");
    sb.append("    configurableRuntimeFlag: ").append(toIndentedString(configurableRuntimeFlag)).append("\n");
    sb.append("    platformType: ").append(toIndentedString(platformType)).append("\n");
    sb.append("    templateStatus: ").append(toIndentedString(templateStatus)).append("\n");
    sb.append("    versionNumber: ").append(toIndentedString(versionNumber)).append("\n");
    sb.append("    refPage: ").append(toIndentedString(refPage)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
