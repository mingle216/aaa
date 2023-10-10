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
 * PageInfo
 */
@Validated

public class PageInfo   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("pageName")
  private String pageName = null;

  @JsonProperty("pageCode")
  private String pageCode = null;

  @JsonProperty("platformType")
  private Integer platformType = null;

  @JsonProperty("backgroundColor")
  private String backgroundColor = null;

  @JsonProperty("backgroundImage")
  private String backgroundImage = null;

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
  private Integer pageType = null;

  @JsonProperty("pageTitleId")
  private String pageTitleId = null;

  public PageInfo wid(String wid) {
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

  public PageInfo pageName(String pageName) {
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

  public PageInfo pageCode(String pageCode) {
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

  public PageInfo platformType(Integer platformType) {
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

  public PageInfo backgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  /**
   * 背景底色
   * @return backgroundColor
  **/
  @ApiModelProperty(value = "背景底色")
  
    public String getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public PageInfo backgroundImage(String backgroundImage) {
    this.backgroundImage = backgroundImage;
    return this;
  }

  /**
   * 背景图片
   * @return backgroundImage
  **/
  @ApiModelProperty(value = "背景图片")
  
    public String getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(String backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public PageInfo templateCode(String templateCode) {
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

  public PageInfo cardLayout(String cardLayout) {
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

  public PageInfo parentId(String parentId) {
    this.parentId = parentId;
    return this;
  }

  /**
   * 父级菜单ID
   * @return parentId
  **/
  @ApiModelProperty(value = "父级菜单ID")
  
    public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public PageInfo pageTitle(String pageTitle) {
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

  public PageInfo programmeId(String programmeId) {
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

  public PageInfo pageType(Integer pageType) {
    this.pageType = pageType;
    return this;
  }

  /**
   * 页面类型0：功能页1：自由页
   * @return pageType
  **/
  @ApiModelProperty(value = "页面类型0：功能页1：自由页")
  
    public Integer getPageType() {
    return pageType;
  }

  public void setPageType(Integer pageType) {
    this.pageType = pageType;
  }

  public PageInfo pageTitleId(String pageTitleId) {
    this.pageTitleId = pageTitleId;
    return this;
  }

  /**
   * 页面标题国际化id
   * @return pageTitleId
  **/
  @ApiModelProperty(value = "页面标题国际化id")
  
    public String getPageTitleId() {
    return pageTitleId;
  }

  public void setPageTitleId(String pageTitleId) {
    this.pageTitleId = pageTitleId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PageInfo pageInfo = (PageInfo) o;
    return Objects.equals(this.wid, pageInfo.wid) &&
        Objects.equals(this.pageName, pageInfo.pageName) &&
        Objects.equals(this.pageCode, pageInfo.pageCode) &&
        Objects.equals(this.platformType, pageInfo.platformType) &&
        Objects.equals(this.backgroundColor, pageInfo.backgroundColor) &&
        Objects.equals(this.backgroundImage, pageInfo.backgroundImage) &&
        Objects.equals(this.templateCode, pageInfo.templateCode) &&
        Objects.equals(this.cardLayout, pageInfo.cardLayout) &&
        Objects.equals(this.parentId, pageInfo.parentId) &&
        Objects.equals(this.pageTitle, pageInfo.pageTitle) &&
        Objects.equals(this.programmeId, pageInfo.programmeId) &&
        Objects.equals(this.pageType, pageInfo.pageType) &&
        Objects.equals(this.pageTitleId, pageInfo.pageTitleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, pageName, pageCode, platformType, backgroundColor, backgroundImage, templateCode, cardLayout, parentId, pageTitle, programmeId, pageType, pageTitleId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    pageName: ").append(toIndentedString(pageName)).append("\n");
    sb.append("    pageCode: ").append(toIndentedString(pageCode)).append("\n");
    sb.append("    platformType: ").append(toIndentedString(platformType)).append("\n");
    sb.append("    backgroundColor: ").append(toIndentedString(backgroundColor)).append("\n");
    sb.append("    backgroundImage: ").append(toIndentedString(backgroundImage)).append("\n");
    sb.append("    templateCode: ").append(toIndentedString(templateCode)).append("\n");
    sb.append("    cardLayout: ").append(toIndentedString(cardLayout)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    pageTitle: ").append(toIndentedString(pageTitle)).append("\n");
    sb.append("    programmeId: ").append(toIndentedString(programmeId)).append("\n");
    sb.append("    pageType: ").append(toIndentedString(pageType)).append("\n");
    sb.append("    pageTitleId: ").append(toIndentedString(pageTitleId)).append("\n");
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
