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
 * SidebarInfoBiz
 */
@Validated

public class SidebarInfoBiz   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("columnName")
  private String columnName = null;

  @JsonProperty("programmeId")
  private String programmeId = null;

  @JsonProperty("positionType")
  private Integer positionType = null;

  @JsonProperty("pageId")
  private String pageId = null;

  @JsonProperty("openType")
  private Integer openType = null;

  @JsonProperty("linkUrl")
  private String linkUrl = null;

  @JsonProperty("menuType")
  private Integer menuType = null;

  @JsonProperty("iconUrl")
  private String iconUrl = null;

  @JsonProperty("iconType")
  private Integer iconType = null;

  @JsonProperty("authType")
  private Integer authType = null;

  @JsonProperty("menuNameLangKey")
  private String menuNameLangKey = null;

  @JsonProperty("menuNameLang")
  @Valid
  private List<MenuNameLangBiz> menuNameLang = null;

  @JsonProperty("menuAuthInfo")
  @Valid
  private List<MenuAuthInfoBiz> menuAuthInfo = null;

  @JsonProperty("countAddress")
  private String countAddress = null;

  public SidebarInfoBiz wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 菜单WID
   * @return wid
  **/
  @ApiModelProperty(value = "菜单WID")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public SidebarInfoBiz columnName(String columnName) {
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

  public SidebarInfoBiz programmeId(String programmeId) {
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

  public SidebarInfoBiz positionType(Integer positionType) {
    this.positionType = positionType;
    return this;
  }

  /**
   * 方位0:(左侧),1:(右侧)
   * @return positionType
  **/
  @ApiModelProperty(value = "方位0:(左侧),1:(右侧)")
  
    public Integer getPositionType() {
    return positionType;
  }

  public void setPositionType(Integer positionType) {
    this.positionType = positionType;
  }

  public SidebarInfoBiz pageId(String pageId) {
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

  public SidebarInfoBiz openType(Integer openType) {
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

  public SidebarInfoBiz linkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
    return this;
  }

  /**
   * 栏目链接地址，非特定栏目有值
   * @return linkUrl
  **/
  @ApiModelProperty(value = "栏目链接地址，非特定栏目有值")
  
    public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
  }

  public SidebarInfoBiz menuType(Integer menuType) {
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

  public SidebarInfoBiz iconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
    return this;
  }

  /**
   * 菜单图标URL
   * @return iconUrl
  **/
  @ApiModelProperty(value = "菜单图标URL")
  
    public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public SidebarInfoBiz iconType(Integer iconType) {
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

  public SidebarInfoBiz authType(Integer authType) {
    this.authType = authType;
    return this;
  }

  /**
   * 0:游客可见 1:登录后可见  2:游客及登录后可见 3:授权后可见
   * @return authType
  **/
  @ApiModelProperty(value = "0:游客可见 1:登录后可见  2:游客及登录后可见 3:授权后可见")
  
    public Integer getAuthType() {
    return authType;
  }

  public void setAuthType(Integer authType) {
    this.authType = authType;
  }

  @ApiModelProperty(value = "数量提醒地址")
  public String getCountAddress() {
    return countAddress;
  }

  public void setCountAddress(String countAddress) {
    this.countAddress = countAddress;
  }

  public SidebarInfoBiz menuNameLangKey(String menuNameLangKey) {
    this.menuNameLangKey = menuNameLangKey;
    return this;
  }

  /**
   * 国际化key
   * @return menuNameLangKey
  **/
  @ApiModelProperty(value = "国际化key")
  
    public String getMenuNameLangKey() {
    return menuNameLangKey;
  }

  public void setMenuNameLangKey(String menuNameLangKey) {
    this.menuNameLangKey = menuNameLangKey;
  }

  public SidebarInfoBiz menuNameLang(List<MenuNameLangBiz> menuNameLang) {
    this.menuNameLang = menuNameLang;
    return this;
  }

  public SidebarInfoBiz addMenuNameLangItem(MenuNameLangBiz menuNameLangItem) {
    if (this.menuNameLang == null) {
      this.menuNameLang = new ArrayList<MenuNameLangBiz>();
    }
    this.menuNameLang.add(menuNameLangItem);
    return this;
  }

  /**
   * 国际化信息
   * @return menuNameLang
  **/
  @ApiModelProperty(value = "国际化信息")
      @Valid
    public List<MenuNameLangBiz> getMenuNameLang() {
    return menuNameLang;
  }

  public void setMenuNameLang(List<MenuNameLangBiz> menuNameLang) {
    this.menuNameLang = menuNameLang;
  }

  public SidebarInfoBiz menuAuthInfo(List<MenuAuthInfoBiz> menuAuthInfo) {
    this.menuAuthInfo = menuAuthInfo;
    return this;
  }

  public SidebarInfoBiz addMenuAuthInfoItem(MenuAuthInfoBiz menuAuthInfoItem) {
    if (this.menuAuthInfo == null) {
      this.menuAuthInfo = new ArrayList<MenuAuthInfoBiz>();
    }
    this.menuAuthInfo.add(menuAuthInfoItem);
    return this;
  }

  /**
   * Get menuAuthInfo
   * @return menuAuthInfo
  **/
  @ApiModelProperty(value = "")
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
    SidebarInfoBiz sidebarInfoBiz = (SidebarInfoBiz) o;
    return Objects.equals(this.wid, sidebarInfoBiz.wid) &&
        Objects.equals(this.columnName, sidebarInfoBiz.columnName) &&
        Objects.equals(this.programmeId, sidebarInfoBiz.programmeId) &&
        Objects.equals(this.positionType, sidebarInfoBiz.positionType) &&
        Objects.equals(this.pageId, sidebarInfoBiz.pageId) &&
        Objects.equals(this.openType, sidebarInfoBiz.openType) &&
        Objects.equals(this.linkUrl, sidebarInfoBiz.linkUrl) &&
        Objects.equals(this.menuType, sidebarInfoBiz.menuType) &&
        Objects.equals(this.iconUrl, sidebarInfoBiz.iconUrl) &&
        Objects.equals(this.iconType, sidebarInfoBiz.iconType) &&
        Objects.equals(this.authType, sidebarInfoBiz.authType) &&
        Objects.equals(this.menuNameLangKey, sidebarInfoBiz.menuNameLangKey) &&
        Objects.equals(this.menuNameLang, sidebarInfoBiz.menuNameLang) &&
        Objects.equals(this.menuAuthInfo, sidebarInfoBiz.menuAuthInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, columnName, programmeId, positionType, pageId, openType, linkUrl, menuType, iconUrl, iconType, authType, menuNameLangKey, menuNameLang, menuAuthInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SidebarInfoBiz {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    columnName: ").append(toIndentedString(columnName)).append("\n");
    sb.append("    programmeId: ").append(toIndentedString(programmeId)).append("\n");
    sb.append("    positionType: ").append(toIndentedString(positionType)).append("\n");
    sb.append("    pageId: ").append(toIndentedString(pageId)).append("\n");
    sb.append("    openType: ").append(toIndentedString(openType)).append("\n");
    sb.append("    linkUrl: ").append(toIndentedString(linkUrl)).append("\n");
    sb.append("    menuType: ").append(toIndentedString(menuType)).append("\n");
    sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
    sb.append("    iconType: ").append(toIndentedString(iconType)).append("\n");
    sb.append("    authType: ").append(toIndentedString(authType)).append("\n");
    sb.append("    menuNameLangKey: ").append(toIndentedString(menuNameLangKey)).append("\n");
    sb.append("    menuNameLang: ").append(toIndentedString(menuNameLang)).append("\n");
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
