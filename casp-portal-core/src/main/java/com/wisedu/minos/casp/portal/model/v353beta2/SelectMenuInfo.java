package com.wisedu.minos.casp.portal.model.v353beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.v353beta2.CommonLang;
import com.wisedu.minos.casp.portal.model.v353beta2.MenuAuthDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 下拉菜单详情信息
 */
@ApiModel(description = "下拉菜单详情信息")
@Validated

public class SelectMenuInfo   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("menuName")
  private String menuName = null;

  @JsonProperty("nameLang")
  @Valid
  private List<CommonLang> nameLang = null;

  @JsonProperty("isSystemMenu")
  private Integer isSystemMenu = null;

  @JsonProperty("menuType")
  private Integer menuType = null;

  @JsonProperty("pageId")
  private String pageId = null;

  @JsonProperty("pageName")
  private String pageName = null;

  @JsonProperty("openType")
  private Integer openType = null;

  @JsonProperty("linkUrl")
  private String linkUrl = null;

  @JsonProperty("authType")
  private Integer authType = null;

  @JsonProperty("menuAuthInfo")
  @Valid
  private List<MenuAuthDto> menuAuthInfo = null;

  @JsonProperty("iconType")
  private Integer iconType = null;

  @JsonProperty("iconUrl")
  private String iconUrl = null;

  @JsonProperty("iconPath")
  private String iconPath = null;

  @JsonProperty("iconName")
  private String iconName = null;

  public SelectMenuInfo wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * wid
   * @return wid
  **/
  @ApiModelProperty(value = "wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public SelectMenuInfo menuName(String menuName) {
    this.menuName = menuName;
    return this;
  }

  /**
   * 菜单名称
   * @return menuName
  **/
  @ApiModelProperty(value = "菜单名称")
  
    public String getMenuName() {
    return menuName;
  }

  public void setMenuName(String menuName) {
    this.menuName = menuName;
  }

  public SelectMenuInfo nameLang(List<CommonLang> nameLang) {
    this.nameLang = nameLang;
    return this;
  }

  public SelectMenuInfo addNameLangItem(CommonLang nameLangItem) {
    if (this.nameLang == null) {
      this.nameLang = new ArrayList<CommonLang>();
    }
    this.nameLang.add(nameLangItem);
    return this;
  }

  /**
   * 菜单名称多语言
   * @return nameLang
  **/
  @ApiModelProperty(value = "菜单名称多语言")
      @Valid
    public List<CommonLang> getNameLang() {
    return nameLang;
  }

  public void setNameLang(List<CommonLang> nameLang) {
    this.nameLang = nameLang;
  }

  public SelectMenuInfo isSystemMenu(Integer isSystemMenu) {
    this.isSystemMenu = isSystemMenu;
    return this;
  }

  /**
   * 是否预置菜单 0:否 1:是
   * @return isSystemMenu
  **/
  @ApiModelProperty(value = "是否预置菜单 0:否 1:是")
  
    public Integer getIsSystemMenu() {
    return isSystemMenu;
  }

  public void setIsSystemMenu(Integer isSystemMenu) {
    this.isSystemMenu = isSystemMenu;
  }

  public SelectMenuInfo menuType(Integer menuType) {
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

  public SelectMenuInfo pageId(String pageId) {
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

  public SelectMenuInfo pageName(String pageName) {
    this.pageName = pageName;
    return this;
  }

  /**
   * 对应页面名称
   * @return pageName
  **/
  @ApiModelProperty(value = "对应页面名称")
  
    public String getPageName() {
    return pageName;
  }

  public void setPageName(String pageName) {
    this.pageName = pageName;
  }

  public SelectMenuInfo openType(Integer openType) {
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

  public SelectMenuInfo linkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
    return this;
  }

  /**
   * 菜单链接
   * @return linkUrl
  **/
  @ApiModelProperty(value = "菜单链接")
  
    public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
  }

  public SelectMenuInfo authType(Integer authType) {
    this.authType = authType;
    return this;
  }

  /**
   * 0:游客可见 1:登录后可见  2:游客及登录后可见 3:授权后可见 4:可用即可见
   * @return authType
  **/
  @ApiModelProperty(value = "0:游客可见 1:登录后可见  2:游客及登录后可见 3:授权后可见 4:可用即可见")
  
    public Integer getAuthType() {
    return authType;
  }

  public void setAuthType(Integer authType) {
    this.authType = authType;
  }

  public SelectMenuInfo menuAuthInfo(List<MenuAuthDto> menuAuthInfo) {
    this.menuAuthInfo = menuAuthInfo;
    return this;
  }

  public SelectMenuInfo addMenuAuthInfoItem(MenuAuthDto menuAuthInfoItem) {
    if (this.menuAuthInfo == null) {
      this.menuAuthInfo = new ArrayList<MenuAuthDto>();
    }
    this.menuAuthInfo.add(menuAuthInfoItem);
    return this;
  }

  /**
   * 菜单授权范围
   * @return menuAuthInfo
  **/
  @ApiModelProperty(value = "菜单授权范围")
      @Valid
    public List<MenuAuthDto> getMenuAuthInfo() {
    return menuAuthInfo;
  }

  public void setMenuAuthInfo(List<MenuAuthDto> menuAuthInfo) {
    this.menuAuthInfo = menuAuthInfo;
  }

  public SelectMenuInfo iconType(Integer iconType) {
    this.iconType = iconType;
    return this;
  }

  /**
   * 图标类型  0 字体图标  1 png图标 2不使用图标
   * @return iconType
  **/
  @ApiModelProperty(value = "图标类型  0 字体图标  1 png图标 2不使用图标")
  
    public Integer getIconType() {
    return iconType;
  }

  public void setIconType(Integer iconType) {
    this.iconType = iconType;
  }

  public SelectMenuInfo iconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
    return this;
  }

  /**
   * 图标URL
   * @return iconUrl
  **/
  @ApiModelProperty(value = "图标URL")
  
    public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public SelectMenuInfo iconPath(String iconPath) {
    this.iconPath = iconPath;
    return this;
  }

  /**
   * 图标路径
   * @return iconPath
  **/
  @ApiModelProperty(value = "图标路径")
  
    public String getIconPath() {
    return iconPath;
  }

  public void setIconPath(String iconPath) {
    this.iconPath = iconPath;
  }

  public SelectMenuInfo iconName(String iconName) {
    this.iconName = iconName;
    return this;
  }

  /**
   * 图标名称
   * @return iconName
  **/
  @ApiModelProperty(value = "图标名称")
  
    public String getIconName() {
    return iconName;
  }

  public void setIconName(String iconName) {
    this.iconName = iconName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SelectMenuInfo selectMenuInfo = (SelectMenuInfo) o;
    return Objects.equals(this.wid, selectMenuInfo.wid) &&
        Objects.equals(this.menuName, selectMenuInfo.menuName) &&
        Objects.equals(this.nameLang, selectMenuInfo.nameLang) &&
        Objects.equals(this.isSystemMenu, selectMenuInfo.isSystemMenu) &&
        Objects.equals(this.menuType, selectMenuInfo.menuType) &&
        Objects.equals(this.pageId, selectMenuInfo.pageId) &&
        Objects.equals(this.pageName, selectMenuInfo.pageName) &&
        Objects.equals(this.openType, selectMenuInfo.openType) &&
        Objects.equals(this.linkUrl, selectMenuInfo.linkUrl) &&
        Objects.equals(this.authType, selectMenuInfo.authType) &&
        Objects.equals(this.menuAuthInfo, selectMenuInfo.menuAuthInfo) &&
        Objects.equals(this.iconType, selectMenuInfo.iconType) &&
        Objects.equals(this.iconUrl, selectMenuInfo.iconUrl) &&
        Objects.equals(this.iconPath, selectMenuInfo.iconPath) &&
        Objects.equals(this.iconName, selectMenuInfo.iconName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, menuName, nameLang, isSystemMenu, menuType, pageId, pageName, openType, linkUrl, authType, menuAuthInfo, iconType, iconUrl, iconPath, iconName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SelectMenuInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    menuName: ").append(toIndentedString(menuName)).append("\n");
    sb.append("    nameLang: ").append(toIndentedString(nameLang)).append("\n");
    sb.append("    isSystemMenu: ").append(toIndentedString(isSystemMenu)).append("\n");
    sb.append("    menuType: ").append(toIndentedString(menuType)).append("\n");
    sb.append("    pageId: ").append(toIndentedString(pageId)).append("\n");
    sb.append("    pageName: ").append(toIndentedString(pageName)).append("\n");
    sb.append("    openType: ").append(toIndentedString(openType)).append("\n");
    sb.append("    linkUrl: ").append(toIndentedString(linkUrl)).append("\n");
    sb.append("    authType: ").append(toIndentedString(authType)).append("\n");
    sb.append("    menuAuthInfo: ").append(toIndentedString(menuAuthInfo)).append("\n");
    sb.append("    iconType: ").append(toIndentedString(iconType)).append("\n");
    sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
    sb.append("    iconPath: ").append(toIndentedString(iconPath)).append("\n");
    sb.append("    iconName: ").append(toIndentedString(iconName)).append("\n");
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
