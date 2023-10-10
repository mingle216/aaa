package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.MenuAuthInfoBiz;
import com.wisedu.minos.casp.portal.model.MenuNameLangBiz;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SidebarInfoResponse
 */
@Validated

public class SidebarInfoResponse   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("columnName")
  private String columnName = null;

  @JsonProperty("pageId")
  private String pageId = null;

  @JsonProperty("openType")
  private Integer openType = null;

  @JsonProperty("pageName")
  private String pageName = null;

  @JsonProperty("isSystem")
  private Integer isSystem = null;

  @JsonProperty("iconType")
  private Integer iconType = null;

  @JsonProperty("menuType")
  private Integer menuType = null;

  @JsonProperty("authType")
  private Integer authType = null;

  @JsonProperty("iconUrl")
  private String iconUrl = null;

  @JsonProperty("linkUrl")
  private String linkUrl = null;

  @JsonProperty("displayName")
  private String displayName = null;

  @JsonProperty("displayNameLangKey")
  private String displayNameLangKey = null;

  @JsonProperty("countAddress")
  private String countAddress = null;

  @JsonProperty("displayNameLang")
  @Valid
  private List<MenuNameLangBiz> displayNameLang = null;

  @JsonProperty("menuAuthInfo")
  @Valid
  private List<MenuAuthInfoBiz> menuAuthInfo = null;

  public SidebarInfoResponse wid(String wid) {
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

  public SidebarInfoResponse columnName(String columnName) {
    this.columnName = columnName;
    return this;
  }

  /**
   * 栏目名称
   * @return columnName
  **/
  @ApiModelProperty(value = "栏目名称")
  
    public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public SidebarInfoResponse pageId(String pageId) {
    this.pageId = pageId;
    return this;
  }

  /**
   * 对应页面
   * @return pageId
  **/
  @ApiModelProperty(value = "对应页面")
  
    public String getPageId() {
    return pageId;
  }

  public void setPageId(String pageId) {
    this.pageId = pageId;
  }

  public SidebarInfoResponse openType(Integer openType) {
    this.openType = openType;
    return this;
  }

  /**
   * 打开方式  0:当前页 1:新开窗口
   * @return openType
  **/
  @ApiModelProperty(value = "打开方式  0:当前页 1:新开窗口")
  
    public Integer getOpenType() {
    return openType;
  }

  public void setOpenType(Integer openType) {
    this.openType = openType;
  }

  public SidebarInfoResponse pageName(String pageName) {
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

  public SidebarInfoResponse isSystem(Integer isSystem) {
    this.isSystem = isSystem;
    return this;
  }

  /**
   * 是否系统自带栏目（0：否；1：是）
   * @return isSystem
  **/
  @ApiModelProperty(value = "是否系统自带栏目（0：否；1：是）")
  
    public Integer getIsSystem() {
    return isSystem;
  }

  public void setIsSystem(Integer isSystem) {
    this.isSystem = isSystem;
  }

  public SidebarInfoResponse iconType(Integer iconType) {
    this.iconType = iconType;
    return this;
  }

  /**
   * 栏目的图标类型（图标类型  0 字体图标  1 png图标）
   * @return iconType
  **/
  @ApiModelProperty(value = "栏目的图标类型（图标类型  0 字体图标  1 png图标）")
  
    public Integer getIconType() {
    return iconType;
  }

  public void setIconType(Integer iconType) {
    this.iconType = iconType;
  }

  public SidebarInfoResponse menuType(Integer menuType) {
    this.menuType = menuType;
    return this;
  }

  /**
   * 菜单类型0：无链接1：内部页面2：第三方链接
   * @return menuType
  **/
  @ApiModelProperty(value = "菜单类型0：无链接1：内部页面2：第三方链接")
  
    public Integer getMenuType() {
    return menuType;
  }

  public void setMenuType(Integer menuType) {
    this.menuType = menuType;
  }

  public SidebarInfoResponse authType(Integer authType) {
    this.authType = authType;
    return this;
  }

  /**
   * 授权类别（0：游客可见；1：登录后可见；2：授权后可见）
   * @return authType
  **/
  @ApiModelProperty(value = "授权类别（0：游客可见；1：登录后可见；2：授权后可见）")
  
    public Integer getAuthType() {
    return authType;
  }

  public void setAuthType(Integer authType) {
    this.authType = authType;
  }

  public SidebarInfoResponse iconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
    return this;
  }

  /**
   * 图片地址
   * @return iconUrl
  **/
  @ApiModelProperty(value = "图片地址")
  
    public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public SidebarInfoResponse linkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
    return this;
  }

  /**
   * 链接地址
   * @return linkUrl
  **/
  @ApiModelProperty(value = "链接地址")
  
    public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
  }

  @ApiModelProperty(value = "数量提醒地址")
  public String getCountAddress() {
    return countAddress;
  }

  public void setCountAddress(String countAddress) {
    this.countAddress = countAddress;
  }

  public SidebarInfoResponse displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * 展示名称
   * @return displayName
  **/
  @ApiModelProperty(value = "展示名称")
  
    public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public SidebarInfoResponse displayNameLangKey(String displayNameLangKey) {
    this.displayNameLangKey = displayNameLangKey;
    return this;
  }

  /**
   * 侧边栏标题国际化id
   * @return displayNameLangKey
  **/
  @ApiModelProperty(value = "侧边栏标题国际化id")
  
    public String getDisplayNameLangKey() {
    return displayNameLangKey;
  }

  public void setDisplayNameLangKey(String displayNameLangKey) {
    this.displayNameLangKey = displayNameLangKey;
  }

  public SidebarInfoResponse displayNameLang(List<MenuNameLangBiz> displayNameLang) {
    this.displayNameLang = displayNameLang;
    return this;
  }

  public SidebarInfoResponse addDisplayNameLangItem(MenuNameLangBiz displayNameLangItem) {
    if (this.displayNameLang == null) {
      this.displayNameLang = new ArrayList<MenuNameLangBiz>();
    }
    this.displayNameLang.add(displayNameLangItem);
    return this;
  }

  /**
   * Get displayNameLang
   * @return displayNameLang
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<MenuNameLangBiz> getDisplayNameLang() {
    return displayNameLang;
  }

  public void setDisplayNameLang(List<MenuNameLangBiz> displayNameLang) {
    this.displayNameLang = displayNameLang;
  }

  public SidebarInfoResponse menuAuthInfo(List<MenuAuthInfoBiz> menuAuthInfo) {
    this.menuAuthInfo = menuAuthInfo;
    return this;
  }

  public SidebarInfoResponse addMenuAuthInfoItem(MenuAuthInfoBiz menuAuthInfoItem) {
    if (this.menuAuthInfo == null) {
      this.menuAuthInfo = new ArrayList<MenuAuthInfoBiz>();
    }
    this.menuAuthInfo.add(menuAuthInfoItem);
    return this;
  }

  /**
   * 授权范围
   * @return menuAuthInfo
  **/
  @ApiModelProperty(value = "授权范围")
      @Valid
    public List<MenuAuthInfoBiz> getMenuAuthInfo() {
    return menuAuthInfo;
  }

  public void setMenuAuthInfo(List<MenuAuthInfoBiz> menuAuthInfo) {
    this.menuAuthInfo = menuAuthInfo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SidebarInfoResponse sidebarInfoResponse = (SidebarInfoResponse) o;
    return Objects.equals(this.wid, sidebarInfoResponse.wid) &&
        Objects.equals(this.columnName, sidebarInfoResponse.columnName) &&
        Objects.equals(this.pageId, sidebarInfoResponse.pageId) &&
        Objects.equals(this.openType, sidebarInfoResponse.openType) &&
        Objects.equals(this.pageName, sidebarInfoResponse.pageName) &&
        Objects.equals(this.isSystem, sidebarInfoResponse.isSystem) &&
        Objects.equals(this.iconType, sidebarInfoResponse.iconType) &&
        Objects.equals(this.menuType, sidebarInfoResponse.menuType) &&
        Objects.equals(this.authType, sidebarInfoResponse.authType) &&
        Objects.equals(this.iconUrl, sidebarInfoResponse.iconUrl) &&
        Objects.equals(this.linkUrl, sidebarInfoResponse.linkUrl) &&
        Objects.equals(this.displayName, sidebarInfoResponse.displayName) &&
        Objects.equals(this.displayNameLangKey, sidebarInfoResponse.displayNameLangKey) &&
        Objects.equals(this.displayNameLang, sidebarInfoResponse.displayNameLang) &&
        Objects.equals(this.menuAuthInfo, sidebarInfoResponse.menuAuthInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, columnName, pageId, openType, pageName, isSystem, iconType, menuType, authType, iconUrl, linkUrl, displayName, displayNameLangKey, displayNameLang, menuAuthInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SidebarInfoResponse {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    columnName: ").append(toIndentedString(columnName)).append("\n");
    sb.append("    pageId: ").append(toIndentedString(pageId)).append("\n");
    sb.append("    openType: ").append(toIndentedString(openType)).append("\n");
    sb.append("    pageName: ").append(toIndentedString(pageName)).append("\n");
    sb.append("    isSystem: ").append(toIndentedString(isSystem)).append("\n");
    sb.append("    iconType: ").append(toIndentedString(iconType)).append("\n");
    sb.append("    menuType: ").append(toIndentedString(menuType)).append("\n");
    sb.append("    authType: ").append(toIndentedString(authType)).append("\n");
    sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
    sb.append("    linkUrl: ").append(toIndentedString(linkUrl)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    displayNameLangKey: ").append(toIndentedString(displayNameLangKey)).append("\n");
    sb.append("    displayNameLang: ").append(toIndentedString(displayNameLang)).append("\n");
    sb.append("    menuAuthInfo: ").append(toIndentedString(menuAuthInfo)).append("\n");
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
