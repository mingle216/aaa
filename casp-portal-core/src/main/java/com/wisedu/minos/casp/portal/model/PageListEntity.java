package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.PageListEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PageListEntity
 */
@Validated

public class PageListEntity   {
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

  @JsonProperty("pageTitle")
  private String pageTitle = null;

  @JsonProperty("parentId")
  private String parentId = null;

  @JsonProperty("pageType")
  private Integer pageType = null;

  @JsonProperty("pageTitleLangKey")
  private String pageTitleLangKey = null;

  @JsonProperty("pageConfig")
  private String pageConfig = null;

  @JsonProperty("cardConfig")
  private List cardConfig = null;

  @JsonProperty("flag")
  private Integer flag = null;

  @JsonProperty("enabledFlag")
  private Integer enabledFlag = null;

  @JsonProperty("pageList")
  @Valid
  private List<PageListEntity> pageList = null;

  public PageListEntity wid(String wid) {
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

  public PageListEntity pageName(String pageName) {
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

  public PageListEntity pageCode(String pageCode) {
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

  public PageListEntity platformType(Integer platformType) {
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

  public PageListEntity backgroundColor(String backgroundColor) {
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

  public PageListEntity backgroundImage(String backgroundImage) {
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

  public PageListEntity templateCode(String templateCode) {
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

  public PageListEntity cardLayout(String cardLayout) {
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

  public PageListEntity pageTitle(String pageTitle) {
    this.pageTitle = pageTitle;
    return this;
  }

  /**
   * 浏览器标题
   * @return pageTitle
  **/
  @ApiModelProperty(value = "浏览器标题")
  
    public String getPageTitle() {
    return pageTitle;
  }

  public void setPageTitle(String pageTitle) {
    this.pageTitle = pageTitle;
  }

  public PageListEntity parentId(String parentId) {
    this.parentId = parentId;
    return this;
  }

  /**
   * 父级id
   * @return parentId
  **/
  @ApiModelProperty(value = "父级id")
  
    public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public PageListEntity pageType(Integer pageType) {
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

  public PageListEntity pageTitleLangKey(String pageTitleLangKey) {
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

  public PageListEntity pageConfig(String pageConfig) {
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

  public PageListEntity cardConfig(List cardConfig) {
    this.cardConfig = cardConfig;
    return this;
  }

  /**
   * 卡片配置
   * @return cardConfig
  **/
  @ApiModelProperty(value = "卡片配置")
  
    @Valid
    public List getCardConfig() {
    return cardConfig;
  }

  public void setCardConfig(List cardConfig) {
    this.cardConfig = cardConfig;
  }

  public PageListEntity flag(Integer flag) {
    this.flag = flag;
    return this;
  }

  /**
   * 是否可选0是 1否
   * @return flag
  **/
  @ApiModelProperty(value = "是否可选0是 1否")
  
    public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  public PageListEntity enabledFlag(Integer enabledFlag) {
    this.enabledFlag = enabledFlag;
    return this;
  }

  /**
   * 是否启用（0：否；1：是）
   * @return enabledFlag
  **/
  @ApiModelProperty(value = "是否启用（0：否；1：是）")
  
    public Integer getEnabledFlag() {
    return enabledFlag;
  }

  public void setEnabledFlag(Integer enabledFlag) {
    this.enabledFlag = enabledFlag;
  }

  public PageListEntity pageList(List<PageListEntity> pageList) {
    this.pageList = pageList;
    return this;
  }

  public PageListEntity addPageListItem(PageListEntity pageListItem) {
    if (this.pageList == null) {
      this.pageList = new ArrayList<PageListEntity>();
    }
    this.pageList.add(pageListItem);
    return this;
  }

  /**
   * 卡片配置
   * @return pageList
  **/
  @ApiModelProperty(value = "卡片配置")
      @Valid
    public List<PageListEntity> getPageList() {
    return pageList;
  }

  public void setPageList(List<PageListEntity> pageList) {
    this.pageList = pageList;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PageListEntity pageListEntity = (PageListEntity) o;
    return Objects.equals(this.wid, pageListEntity.wid) &&
        Objects.equals(this.pageName, pageListEntity.pageName) &&
        Objects.equals(this.pageCode, pageListEntity.pageCode) &&
        Objects.equals(this.platformType, pageListEntity.platformType) &&
        Objects.equals(this.backgroundColor, pageListEntity.backgroundColor) &&
        Objects.equals(this.backgroundImage, pageListEntity.backgroundImage) &&
        Objects.equals(this.templateCode, pageListEntity.templateCode) &&
        Objects.equals(this.cardLayout, pageListEntity.cardLayout) &&
        Objects.equals(this.pageTitle, pageListEntity.pageTitle) &&
        Objects.equals(this.parentId, pageListEntity.parentId) &&
        Objects.equals(this.pageType, pageListEntity.pageType) &&
        Objects.equals(this.pageTitleLangKey, pageListEntity.pageTitleLangKey) &&
        Objects.equals(this.pageConfig, pageListEntity.pageConfig) &&
        Objects.equals(this.cardConfig, pageListEntity.cardConfig) &&
        Objects.equals(this.flag, pageListEntity.flag) &&
        Objects.equals(this.enabledFlag, pageListEntity.enabledFlag) &&
        Objects.equals(this.pageList, pageListEntity.pageList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, pageName, pageCode, platformType, backgroundColor, backgroundImage, templateCode, cardLayout, pageTitle, parentId, pageType, pageTitleLangKey, pageConfig, cardConfig, flag, enabledFlag, pageList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageListEntity {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    pageName: ").append(toIndentedString(pageName)).append("\n");
    sb.append("    pageCode: ").append(toIndentedString(pageCode)).append("\n");
    sb.append("    platformType: ").append(toIndentedString(platformType)).append("\n");
    sb.append("    backgroundColor: ").append(toIndentedString(backgroundColor)).append("\n");
    sb.append("    backgroundImage: ").append(toIndentedString(backgroundImage)).append("\n");
    sb.append("    templateCode: ").append(toIndentedString(templateCode)).append("\n");
    sb.append("    cardLayout: ").append(toIndentedString(cardLayout)).append("\n");
    sb.append("    pageTitle: ").append(toIndentedString(pageTitle)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    pageType: ").append(toIndentedString(pageType)).append("\n");
    sb.append("    pageTitleLangKey: ").append(toIndentedString(pageTitleLangKey)).append("\n");
    sb.append("    pageConfig: ").append(toIndentedString(pageConfig)).append("\n");
    sb.append("    cardConfig: ").append(toIndentedString(cardConfig)).append("\n");
    sb.append("    flag: ").append(toIndentedString(flag)).append("\n");
    sb.append("    enabledFlag: ").append(toIndentedString(enabledFlag)).append("\n");
    sb.append("    pageList: ").append(toIndentedString(pageList)).append("\n");
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
