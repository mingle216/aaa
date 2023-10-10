package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.CardConfigObj;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SavePageInfoRes
 */
@Validated

public class SavePageInfoRes   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("pageName")
  private String pageName = null;

  @JsonProperty("pageCode")
  private String pageCode = null;

  @JsonProperty("platformType")
  private Integer platformType = null;

  @JsonProperty("templateCode")
  private String templateCode = null;

  @JsonProperty("cardLayout")
  private String cardLayout = null;

  @JsonProperty("parentId")
  private String parentId = null;

  @JsonProperty("pageTitle")
  private String pageTitle = null;

  @JsonProperty("programmeId")
  private String programmeId = null;

  @JsonProperty("pageType")
  private String pageType = null;

  @JsonProperty("cardConfig")
  @Valid
  private List<CardConfigObj> cardConfig = null;

  public SavePageInfoRes wid(String wid) {
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

  public SavePageInfoRes pageName(String pageName) {
    this.pageName = pageName;
    return this;
  }

  /**
   * 页面名称
   * @return pageName
  **/
  @ApiModelProperty(value = "页面名称")
  
    public String getPageName() {
    return pageName;
  }

  public void setPageName(String pageName) {
    this.pageName = pageName;
  }

  public SavePageInfoRes pageCode(String pageCode) {
    this.pageCode = pageCode;
    return this;
  }

  /**
   * 页面代码
   * @return pageCode
  **/
  @ApiModelProperty(value = "页面代码")
  
    public String getPageCode() {
    return pageCode;
  }

  public void setPageCode(String pageCode) {
    this.pageCode = pageCode;
  }

  public SavePageInfoRes platformType(Integer platformType) {
    this.platformType = platformType;
    return this;
  }

  /**
   * 运行平台 0:PC 1:Mobile
   * @return platformType
  **/
  @ApiModelProperty(value = "运行平台 0:PC 1:Mobile")
  
    public Integer getPlatformType() {
    return platformType;
  }

  public void setPlatformType(Integer platformType) {
    this.platformType = platformType;
  }

  public SavePageInfoRes templateCode(String templateCode) {
    this.templateCode = templateCode;
    return this;
  }

  /**
   * 模板代码
   * @return templateCode
  **/
  @ApiModelProperty(value = "模板代码")
  
    public String getTemplateCode() {
    return templateCode;
  }

  public void setTemplateCode(String templateCode) {
    this.templateCode = templateCode;
  }

  public SavePageInfoRes cardLayout(String cardLayout) {
    this.cardLayout = cardLayout;
    return this;
  }

  /**
   * 卡片布局数据
   * @return cardLayout
  **/
  @ApiModelProperty(value = "卡片布局数据")
  
    public String getCardLayout() {
    return cardLayout;
  }

  public void setCardLayout(String cardLayout) {
    this.cardLayout = cardLayout;
  }

  public SavePageInfoRes parentId(String parentId) {
    this.parentId = parentId;
    return this;
  }

  /**
   * 父级页面ID
   * @return parentId
  **/
  @ApiModelProperty(value = "父级页面ID")
  
    public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public SavePageInfoRes pageTitle(String pageTitle) {
    this.pageTitle = pageTitle;
    return this;
  }

  /**
   * 页面标题
   * @return pageTitle
  **/
  @ApiModelProperty(value = "页面标题")
  
    public String getPageTitle() {
    return pageTitle;
  }

  public void setPageTitle(String pageTitle) {
    this.pageTitle = pageTitle;
  }

  public SavePageInfoRes programmeId(String programmeId) {
    this.programmeId = programmeId;
    return this;
  }

  /**
   * 展示方案id
   * @return programmeId
  **/
  @ApiModelProperty(value = "展示方案id")
  
    public String getProgrammeId() {
    return programmeId;
  }

  public void setProgrammeId(String programmeId) {
    this.programmeId = programmeId;
  }

  public SavePageInfoRes pageType(String pageType) {
    this.pageType = pageType;
    return this;
  }

  /**
   * 页面类型0：功能页1：自由页
   * @return pageType
  **/
  @ApiModelProperty(value = "页面类型0：功能页1：自由页")
  
    public String getPageType() {
    return pageType;
  }

  public void setPageType(String pageType) {
    this.pageType = pageType;
  }

  public SavePageInfoRes cardConfig(List<CardConfigObj> cardConfig) {
    this.cardConfig = cardConfig;
    return this;
  }

  public SavePageInfoRes addCardConfigItem(CardConfigObj cardConfigItem) {
    if (this.cardConfig == null) {
      this.cardConfig = new ArrayList<CardConfigObj>();
    }
    this.cardConfig.add(cardConfigItem);
    return this;
  }

  /**
   * 卡片配置
   * @return cardConfig
  **/
  @ApiModelProperty(value = "卡片配置")
      @Valid
    public List<CardConfigObj> getCardConfig() {
    return cardConfig;
  }

  public void setCardConfig(List<CardConfigObj> cardConfig) {
    this.cardConfig = cardConfig;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SavePageInfoRes savePageInfoRes = (SavePageInfoRes) o;
    return Objects.equals(this.wid, savePageInfoRes.wid) &&
        Objects.equals(this.pageName, savePageInfoRes.pageName) &&
        Objects.equals(this.pageCode, savePageInfoRes.pageCode) &&
        Objects.equals(this.platformType, savePageInfoRes.platformType) &&
        Objects.equals(this.templateCode, savePageInfoRes.templateCode) &&
        Objects.equals(this.cardLayout, savePageInfoRes.cardLayout) &&
        Objects.equals(this.parentId, savePageInfoRes.parentId) &&
        Objects.equals(this.pageTitle, savePageInfoRes.pageTitle) &&
        Objects.equals(this.programmeId, savePageInfoRes.programmeId) &&
        Objects.equals(this.pageType, savePageInfoRes.pageType) &&
        Objects.equals(this.cardConfig, savePageInfoRes.cardConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, pageName, pageCode, platformType, templateCode, cardLayout, parentId, pageTitle, programmeId, pageType, cardConfig);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SavePageInfoRes {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    pageName: ").append(toIndentedString(pageName)).append("\n");
    sb.append("    pageCode: ").append(toIndentedString(pageCode)).append("\n");
    sb.append("    platformType: ").append(toIndentedString(platformType)).append("\n");
    sb.append("    templateCode: ").append(toIndentedString(templateCode)).append("\n");
    sb.append("    cardLayout: ").append(toIndentedString(cardLayout)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    pageTitle: ").append(toIndentedString(pageTitle)).append("\n");
    sb.append("    programmeId: ").append(toIndentedString(programmeId)).append("\n");
    sb.append("    pageType: ").append(toIndentedString(pageType)).append("\n");
    sb.append("    cardConfig: ").append(toIndentedString(cardConfig)).append("\n");
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
