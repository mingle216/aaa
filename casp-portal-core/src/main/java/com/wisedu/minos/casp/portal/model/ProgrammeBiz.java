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
 * ProgrammeBiz
 */
@Validated

public class ProgrammeBiz   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("programmeName")
  private String programmeName = null;

  @JsonProperty("platformType")
  private Integer platformType = null;

  @JsonProperty("templateId")
  private String templateId = null;

  @JsonProperty("templateCode")
  private String templateCode = null;

  @JsonProperty("templateName")
  private String templateName = null;

  @JsonProperty("pageAmount")
  private Integer pageAmount = null;

  @JsonProperty("pageStatus")
  private Integer pageStatus = null;

  @JsonProperty("initializeFlag")
  private Integer initializeFlag = null;

  @JsonProperty("isShowPcService")
  private Integer isShowPcService = null;

  @JsonProperty("updateTime")
  private String updateTime = null;

  @JsonProperty("createTime")
  private String createTime = null;

  public ProgrammeBiz wid(String wid) {
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

  public ProgrammeBiz programmeName(String programmeName) {
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

  public ProgrammeBiz platformType(Integer platformType) {
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

  public ProgrammeBiz templateId(String templateId) {
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

  public ProgrammeBiz templateCode(String templateCode) {
    this.templateCode = templateCode;
    return this;
  }

  /**
   * 模板code
   * @return templateCode
  **/
  @ApiModelProperty(value = "模板code")
  
    public String getTemplateCode() {
    return templateCode;
  }

  public void setTemplateCode(String templateCode) {
    this.templateCode = templateCode;
  }

  public ProgrammeBiz templateName(String templateName) {
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

  public ProgrammeBiz pageAmount(Integer pageAmount) {
    this.pageAmount = pageAmount;
    return this;
  }

  /**
   * 页面数量
   * @return pageAmount
  **/
  @ApiModelProperty(value = "页面数量")
  
    public Integer getPageAmount() {
    return pageAmount;
  }

  public void setPageAmount(Integer pageAmount) {
    this.pageAmount = pageAmount;
  }

  public ProgrammeBiz pageStatus(Integer pageStatus) {
    this.pageStatus = pageStatus;
    return this;
  }

  /**
   * 状态0：启用1：停用
   * @return pageStatus
  **/
  @ApiModelProperty(value = "状态0：启用1：停用")
  
    public Integer getPageStatus() {
    return pageStatus;
  }

  public void setPageStatus(Integer pageStatus) {
    this.pageStatus = pageStatus;
  }

  public ProgrammeBiz initializeFlag(Integer initializeFlag) {
    this.initializeFlag = initializeFlag;
    return this;
  }

  /**
   * 是否初始化方案（初始化展示方案隐藏）：0否  1是
   * @return initializeFlag
  **/
  @ApiModelProperty(value = "是否初始化方案（初始化展示方案隐藏）：0否  1是")
  
    public Integer getInitializeFlag() {
    return initializeFlag;
  }

  public void setInitializeFlag(Integer initializeFlag) {
    this.initializeFlag = initializeFlag;
  }

  public ProgrammeBiz isShowPcService(Integer isShowPcService) {
    this.isShowPcService = isShowPcService;
    return this;
  }

  /**
   * 是否展示PC端服务 1:显示 0:隐藏
   * @return isShowPcService
  **/
  @ApiModelProperty(value = "是否展示PC端服务 1:显示 0:隐藏")
  
    public Integer getIsShowPcService() {
    return isShowPcService;
  }

  public void setIsShowPcService(Integer isShowPcService) {
    this.isShowPcService = isShowPcService;
  }

  public ProgrammeBiz updateTime(String updateTime) {
    this.updateTime = updateTime;
    return this;
  }

  /**
   * 更新时间
   * @return updateTime
  **/
  @ApiModelProperty(value = "更新时间")
  
    public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public ProgrammeBiz createTime(String createTime) {
    this.createTime = createTime;
    return this;
  }

  /**
   * 创建时间
   * @return createTime
  **/
  @ApiModelProperty(value = "创建时间")
  
    public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgrammeBiz programmeBiz = (ProgrammeBiz) o;
    return Objects.equals(this.wid, programmeBiz.wid) &&
        Objects.equals(this.programmeName, programmeBiz.programmeName) &&
        Objects.equals(this.platformType, programmeBiz.platformType) &&
        Objects.equals(this.templateId, programmeBiz.templateId) &&
        Objects.equals(this.templateCode, programmeBiz.templateCode) &&
        Objects.equals(this.templateName, programmeBiz.templateName) &&
        Objects.equals(this.pageAmount, programmeBiz.pageAmount) &&
        Objects.equals(this.pageStatus, programmeBiz.pageStatus) &&
        Objects.equals(this.initializeFlag, programmeBiz.initializeFlag) &&
        Objects.equals(this.isShowPcService, programmeBiz.isShowPcService) &&
        Objects.equals(this.updateTime, programmeBiz.updateTime) &&
        Objects.equals(this.createTime, programmeBiz.createTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, programmeName, platformType, templateId, templateCode, templateName, pageAmount, pageStatus, initializeFlag, isShowPcService, updateTime, createTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProgrammeBiz {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    programmeName: ").append(toIndentedString(programmeName)).append("\n");
    sb.append("    platformType: ").append(toIndentedString(platformType)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    templateCode: ").append(toIndentedString(templateCode)).append("\n");
    sb.append("    templateName: ").append(toIndentedString(templateName)).append("\n");
    sb.append("    pageAmount: ").append(toIndentedString(pageAmount)).append("\n");
    sb.append("    pageStatus: ").append(toIndentedString(pageStatus)).append("\n");
    sb.append("    initializeFlag: ").append(toIndentedString(initializeFlag)).append("\n");
    sb.append("    isShowPcService: ").append(toIndentedString(isShowPcService)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
    sb.append("    createTime: ").append(toIndentedString(createTime)).append("\n");
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
