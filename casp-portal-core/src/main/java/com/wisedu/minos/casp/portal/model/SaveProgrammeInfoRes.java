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
 * SaveProgrammeInfoRes
 */
@Validated

public class SaveProgrammeInfoRes   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("programmeName")
  private String programmeName = null;

  @JsonProperty("platformType")
  private Integer platformType = null;

  @JsonProperty("templateId")
  private String templateId = null;

  @JsonProperty("pageAmount")
  private String pageAmount = null;

  @JsonProperty("pageStatus")
  private String pageStatus = null;

  @JsonProperty("logoUrl")
  private String logoUrl = null;

  @JsonProperty("templateConfig")
  private String templateConfig = null;

  @JsonProperty("sideFlag")
  private Integer sideFlag = null;

  public SaveProgrammeInfoRes wid(String wid) {
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

  public SaveProgrammeInfoRes programmeName(String programmeName) {
    this.programmeName = programmeName;
    return this;
  }

  /**
   * 展示方案名称
   * @return programmeName
  **/
  @ApiModelProperty(value = "展示方案名称")
  
    public String getProgrammeName() {
    return programmeName;
  }

  public void setProgrammeName(String programmeName) {
    this.programmeName = programmeName;
  }

  public SaveProgrammeInfoRes platformType(Integer platformType) {
    this.platformType = platformType;
    return this;
  }

  /**
   * 适配终端0：PC1：移动端
   * @return platformType
  **/
  @ApiModelProperty(value = "适配终端0：PC1：移动端")
  
    public Integer getPlatformType() {
    return platformType;
  }

  public void setPlatformType(Integer platformType) {
    this.platformType = platformType;
  }

  public SaveProgrammeInfoRes templateId(String templateId) {
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

  public SaveProgrammeInfoRes pageAmount(String pageAmount) {
    this.pageAmount = pageAmount;
    return this;
  }

  /**
   * 页面数量
   * @return pageAmount
  **/
  @ApiModelProperty(value = "页面数量")
  
    public String getPageAmount() {
    return pageAmount;
  }

  public void setPageAmount(String pageAmount) {
    this.pageAmount = pageAmount;
  }

  public SaveProgrammeInfoRes pageStatus(String pageStatus) {
    this.pageStatus = pageStatus;
    return this;
  }

  /**
   * 状态0：启用1：停用
   * @return pageStatus
  **/
  @ApiModelProperty(value = "状态0：启用1：停用")
  
    public String getPageStatus() {
    return pageStatus;
  }

  public void setPageStatus(String pageStatus) {
    this.pageStatus = pageStatus;
  }

  public SaveProgrammeInfoRes logoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
    return this;
  }

  /**
   * 校徽&LOGO地址
   * @return logoUrl
  **/
  @ApiModelProperty(value = "校徽&LOGO地址")
  
    public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public SaveProgrammeInfoRes templateConfig(String templateConfig) {
    this.templateConfig = templateConfig;
    return this;
  }

  /**
   * 模板配置
   * @return templateConfig
  **/
  @ApiModelProperty(value = "模板配置")
  
    public String getTemplateConfig() {
    return templateConfig;
  }

  public void setTemplateConfig(String templateConfig) {
    this.templateConfig = templateConfig;
  }

  public SaveProgrammeInfoRes sideFlag(Integer sideFlag) {
    this.sideFlag = sideFlag;
    return this;
  }

  /**
   * 是否展示左/右侧栏0都不展示 1只展示左侧栏2只展示右侧栏3、都展示
   * @return sideFlag
  **/
  @ApiModelProperty(value = "是否展示左/右侧栏0都不展示 1只展示左侧栏2只展示右侧栏3、都展示")
  
    public Integer getSideFlag() {
    return sideFlag;
  }

  public void setSideFlag(Integer sideFlag) {
    this.sideFlag = sideFlag;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SaveProgrammeInfoRes saveProgrammeInfoRes = (SaveProgrammeInfoRes) o;
    return Objects.equals(this.wid, saveProgrammeInfoRes.wid) &&
        Objects.equals(this.programmeName, saveProgrammeInfoRes.programmeName) &&
        Objects.equals(this.platformType, saveProgrammeInfoRes.platformType) &&
        Objects.equals(this.templateId, saveProgrammeInfoRes.templateId) &&
        Objects.equals(this.pageAmount, saveProgrammeInfoRes.pageAmount) &&
        Objects.equals(this.pageStatus, saveProgrammeInfoRes.pageStatus) &&
        Objects.equals(this.logoUrl, saveProgrammeInfoRes.logoUrl) &&
        Objects.equals(this.templateConfig, saveProgrammeInfoRes.templateConfig) &&
        Objects.equals(this.sideFlag, saveProgrammeInfoRes.sideFlag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, programmeName, platformType, templateId, pageAmount, pageStatus, logoUrl, templateConfig, sideFlag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SaveProgrammeInfoRes {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    programmeName: ").append(toIndentedString(programmeName)).append("\n");
    sb.append("    platformType: ").append(toIndentedString(platformType)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    pageAmount: ").append(toIndentedString(pageAmount)).append("\n");
    sb.append("    pageStatus: ").append(toIndentedString(pageStatus)).append("\n");
    sb.append("    logoUrl: ").append(toIndentedString(logoUrl)).append("\n");
    sb.append("    templateConfig: ").append(toIndentedString(templateConfig)).append("\n");
    sb.append("    sideFlag: ").append(toIndentedString(sideFlag)).append("\n");
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
