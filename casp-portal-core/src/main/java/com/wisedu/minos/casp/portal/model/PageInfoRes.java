package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.CardConfigObj;
import com.wisedu.minos.casp.portal.model.MenuNameLangBiz;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PageInfoRes
 */
@Validated

public class PageInfoRes   {
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

  @JsonProperty("pageTitleLangKey")
  private String pageTitleLangKey = null;

  @JsonProperty("pageTitleList")
  @Valid
  private List<MenuNameLangBiz> pageTitleList = null;

  @JsonProperty("programmeId")
  private String programmeId = null;

  @JsonProperty("pageType")
  private String pageType = null;

  @JsonProperty("pageConfig")
  private String pageConfig = null;

  @JsonProperty("cardConfig")
  @Valid
  private List<CardConfigObj> cardConfig = null;

  public PageInfoRes wid(String wid) {
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

  public PageInfoRes pageName(String pageName) {
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

  public PageInfoRes pageCode(String pageCode) {
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

  public PageInfoRes platformType(Integer platformType) {
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

  public PageInfoRes templateCode(String templateCode) {
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

  public PageInfoRes cardLayout(String cardLayout) {
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

  public PageInfoRes parentId(String parentId) {
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

  public PageInfoRes pageTitle(String pageTitle) {
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

  public PageInfoRes pageTitleLangKey(String pageTitleLangKey) {
    this.pageTitleLangKey = pageTitleLangKey;
    return this;
  }

  /**
   * 页面标题国际化id
   * @return pageTitleLangKey
  **/
  @ApiModelProperty(value = "页面标题国际化id")
  
    public String getPageTitleLangKey() {
    return pageTitleLangKey;
  }

  public void setPageTitleLangKey(String pageTitleLangKey) {
    this.pageTitleLangKey = pageTitleLangKey;
  }

  public PageInfoRes pageTitleList(List<MenuNameLangBiz> pageTitleList) {
    this.pageTitleList = pageTitleList;
    return this;
  }

  public PageInfoRes addPageTitleListItem(MenuNameLangBiz pageTitleListItem) {
    if (this.pageTitleList == null) {
      this.pageTitleList = new ArrayList<MenuNameLangBiz>();
    }
    this.pageTitleList.add(pageTitleListItem);
    return this;
  }

  /**
   * 页面标题传输列表
   * @return pageTitleList
  **/
  @ApiModelProperty(value = "页面标题传输列表")
      @Valid
    public List<MenuNameLangBiz> getPageTitleList() {
    return pageTitleList;
  }

  public void setPageTitleList(List<MenuNameLangBiz> pageTitleList) {
    this.pageTitleList = pageTitleList;
  }

  public PageInfoRes programmeId(String programmeId) {
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

  public PageInfoRes pageType(String pageType) {
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

  public PageInfoRes pageConfig(String pageConfig) {
    this.pageConfig = pageConfig;
    return this;
  }

  /**
   * 页面配置
   * @return pageConfig
  **/
  @ApiModelProperty(value = "页面配置")
  
    public String getPageConfig() {
    return pageConfig;
  }

  public void setPageConfig(String pageConfig) {
    this.pageConfig = pageConfig;
  }

  public PageInfoRes cardConfig(List<CardConfigObj> cardConfig) {
    this.cardConfig = cardConfig;
    return this;
  }

  public PageInfoRes addCardConfigItem(CardConfigObj cardConfigItem) {
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
    PageInfoRes pageInfoRes = (PageInfoRes) o;
    return Objects.equals(this.wid, pageInfoRes.wid) &&
        Objects.equals(this.pageName, pageInfoRes.pageName) &&
        Objects.equals(this.pageCode, pageInfoRes.pageCode) &&
        Objects.equals(this.platformType, pageInfoRes.platformType) &&
        Objects.equals(this.templateCode, pageInfoRes.templateCode) &&
        Objects.equals(this.cardLayout, pageInfoRes.cardLayout) &&
        Objects.equals(this.parentId, pageInfoRes.parentId) &&
        Objects.equals(this.pageTitle, pageInfoRes.pageTitle) &&
        Objects.equals(this.pageTitleLangKey, pageInfoRes.pageTitleLangKey) &&
        Objects.equals(this.pageTitleList, pageInfoRes.pageTitleList) &&
        Objects.equals(this.programmeId, pageInfoRes.programmeId) &&
        Objects.equals(this.pageType, pageInfoRes.pageType) &&
        Objects.equals(this.pageConfig, pageInfoRes.pageConfig) &&
        Objects.equals(this.cardConfig, pageInfoRes.cardConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, pageName, pageCode, platformType, templateCode, cardLayout, parentId, pageTitle, pageTitleLangKey, pageTitleList, programmeId, pageType, pageConfig, cardConfig);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageInfoRes {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    pageName: ").append(toIndentedString(pageName)).append("\n");
    sb.append("    pageCode: ").append(toIndentedString(pageCode)).append("\n");
    sb.append("    platformType: ").append(toIndentedString(platformType)).append("\n");
    sb.append("    templateCode: ").append(toIndentedString(templateCode)).append("\n");
    sb.append("    cardLayout: ").append(toIndentedString(cardLayout)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    pageTitle: ").append(toIndentedString(pageTitle)).append("\n");
    sb.append("    pageTitleLangKey: ").append(toIndentedString(pageTitleLangKey)).append("\n");
    sb.append("    pageTitleList: ").append(toIndentedString(pageTitleList)).append("\n");
    sb.append("    programmeId: ").append(toIndentedString(programmeId)).append("\n");
    sb.append("    pageType: ").append(toIndentedString(pageType)).append("\n");
    sb.append("    pageConfig: ").append(toIndentedString(pageConfig)).append("\n");
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
