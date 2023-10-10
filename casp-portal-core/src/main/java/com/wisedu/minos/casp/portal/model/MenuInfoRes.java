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
 * MenuInfoRes
 */
@Validated

public class MenuInfoRes   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("menuName")
  private String menuName = null;

  @JsonProperty("iconUrl")
  private String iconUrl = null;

  @JsonProperty("authType")
  private Integer authType = null;

  @JsonProperty("openType")
  private Integer openType = null;

  @JsonProperty("linkUrl")
  private String linkUrl = null;

  @JsonProperty("parentId")
  private String parentId = null;

  @JsonProperty("programmeId")
  private String programmeId = null;

  @JsonProperty("menuType")
  private Integer menuType = null;

  @JsonProperty("pageId")
  private String pageId = null;

  @JsonProperty("iconType")
  private Integer iconType = null;

  @JsonProperty("menuNameLangKey")
  @Valid
  private List<MenuNameLangBiz> menuNameLangKey = null;

  @JsonProperty("menuAuthInfo")
  @Valid
  private List<MenuAuthInfoBiz> menuAuthInfo = null;

  @JsonProperty("countAddress")
  private String countAddress = null;

  public MenuInfoRes wid(String wid) {
    this.wid = wid;
    return this;
  }

  @ApiModelProperty(value = "数量提醒地址")
  public String getCountAddress() {
    return countAddress;
  }

  public void setCountAddress(String countAddress) {
    this.countAddress = countAddress;
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

  public MenuInfoRes menuName(String menuName) {
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

  public MenuInfoRes iconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
    return this;
  }

  /**
   * 图标
   * @return iconUrl
  **/
  @ApiModelProperty(value = "图标")
  
    public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public MenuInfoRes authType(Integer authType) {
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

  public MenuInfoRes openType(Integer openType) {
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

  public MenuInfoRes linkUrl(String linkUrl) {
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

  public MenuInfoRes parentId(String parentId) {
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

  public MenuInfoRes programmeId(String programmeId) {
    this.programmeId = programmeId;
    return this;
  }

  /**
   * 展示方案Id
   * @return programmeId
  **/
  @ApiModelProperty(value = "展示方案Id")
  
    public String getProgrammeId() {
    return programmeId;
  }

  public void setProgrammeId(String programmeId) {
    this.programmeId = programmeId;
  }

  public MenuInfoRes menuType(Integer menuType) {
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

  public MenuInfoRes pageId(String pageId) {
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

  public MenuInfoRes iconType(Integer iconType) {
    this.iconType = iconType;
    return this;
  }

  /**
   * 图标类型0：png1：字体图标
   * @return iconType
  **/
  @ApiModelProperty(value = "图标类型0：png1：字体图标")
  
    public Integer getIconType() {
    return iconType;
  }

  public void setIconType(Integer iconType) {
    this.iconType = iconType;
  }

  public MenuInfoRes menuNameLangKey(List<MenuNameLangBiz> menuNameLangKey) {
    this.menuNameLangKey = menuNameLangKey;
    return this;
  }

  public MenuInfoRes addMenuNameLangKeyItem(MenuNameLangBiz menuNameLangKeyItem) {
    if (this.menuNameLangKey == null) {
      this.menuNameLangKey = new ArrayList<MenuNameLangBiz>();
    }
    this.menuNameLangKey.add(menuNameLangKeyItem);
    return this;
  }

  /**
   * Get menuNameLangKey
   * @return menuNameLangKey
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<MenuNameLangBiz> getMenuNameLangKey() {
    return menuNameLangKey;
  }

  public void setMenuNameLangKey(List<MenuNameLangBiz> menuNameLangKey) {
    this.menuNameLangKey = menuNameLangKey;
  }

  public MenuInfoRes menuAuthInfo(List<MenuAuthInfoBiz> menuAuthInfo) {
    this.menuAuthInfo = menuAuthInfo;
    return this;
  }

  public MenuInfoRes addMenuAuthInfoItem(MenuAuthInfoBiz menuAuthInfoItem) {
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
    MenuInfoRes menuInfoRes = (MenuInfoRes) o;
    return Objects.equals(this.wid, menuInfoRes.wid) &&
        Objects.equals(this.menuName, menuInfoRes.menuName) &&
        Objects.equals(this.iconUrl, menuInfoRes.iconUrl) &&
        Objects.equals(this.authType, menuInfoRes.authType) &&
        Objects.equals(this.openType, menuInfoRes.openType) &&
        Objects.equals(this.linkUrl, menuInfoRes.linkUrl) &&
        Objects.equals(this.parentId, menuInfoRes.parentId) &&
        Objects.equals(this.programmeId, menuInfoRes.programmeId) &&
        Objects.equals(this.menuType, menuInfoRes.menuType) &&
        Objects.equals(this.pageId, menuInfoRes.pageId) &&
        Objects.equals(this.iconType, menuInfoRes.iconType) &&
        Objects.equals(this.menuNameLangKey, menuInfoRes.menuNameLangKey) &&
        Objects.equals(this.menuAuthInfo, menuInfoRes.menuAuthInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, menuName, iconUrl, authType, openType, linkUrl, parentId, programmeId, menuType, pageId, iconType, menuNameLangKey, menuAuthInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MenuInfoRes {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    menuName: ").append(toIndentedString(menuName)).append("\n");
    sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
    sb.append("    authType: ").append(toIndentedString(authType)).append("\n");
    sb.append("    openType: ").append(toIndentedString(openType)).append("\n");
    sb.append("    linkUrl: ").append(toIndentedString(linkUrl)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    programmeId: ").append(toIndentedString(programmeId)).append("\n");
    sb.append("    menuType: ").append(toIndentedString(menuType)).append("\n");
    sb.append("    pageId: ").append(toIndentedString(pageId)).append("\n");
    sb.append("    iconType: ").append(toIndentedString(iconType)).append("\n");
    sb.append("    menuNameLangKey: ").append(toIndentedString(menuNameLangKey)).append("\n");
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
