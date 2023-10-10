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
 * SaveMenuInfoRes
 */
@Validated

public class SaveMenuInfoRes   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("menuName")
  private String menuName = null;

  @JsonProperty("iconUrl")
  private String iconUrl = null;

  @JsonProperty("orderNumber")
  private Integer orderNumber = null;

  @JsonProperty("authType")
  private Integer authType = null;

  @JsonProperty("isEnabled")
  private Integer isEnabled = null;

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
  private String menuNameLangKey = null;

  public SaveMenuInfoRes wid(String wid) {
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

  public SaveMenuInfoRes menuName(String menuName) {
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

  public SaveMenuInfoRes iconUrl(String iconUrl) {
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

  public SaveMenuInfoRes orderNumber(Integer orderNumber) {
    this.orderNumber = orderNumber;
    return this;
  }

  /**
   * 排序
   * @return orderNumber
  **/
  @ApiModelProperty(value = "排序")
  
    public Integer getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(Integer orderNumber) {
    this.orderNumber = orderNumber;
  }

  public SaveMenuInfoRes authType(Integer authType) {
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

  public SaveMenuInfoRes isEnabled(Integer isEnabled) {
    this.isEnabled = isEnabled;
    return this;
  }

  /**
   * 是否启用 0:停用 1:启用
   * @return isEnabled
  **/
  @ApiModelProperty(value = "是否启用 0:停用 1:启用")
  
    public Integer getIsEnabled() {
    return isEnabled;
  }

  public void setIsEnabled(Integer isEnabled) {
    this.isEnabled = isEnabled;
  }

  public SaveMenuInfoRes openType(Integer openType) {
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

  public SaveMenuInfoRes linkUrl(String linkUrl) {
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

  public SaveMenuInfoRes parentId(String parentId) {
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

  public SaveMenuInfoRes programmeId(String programmeId) {
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

  public SaveMenuInfoRes menuType(Integer menuType) {
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

  public SaveMenuInfoRes pageId(String pageId) {
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

  public SaveMenuInfoRes iconType(Integer iconType) {
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

  public SaveMenuInfoRes menuNameLangKey(String menuNameLangKey) {
    this.menuNameLangKey = menuNameLangKey;
    return this;
  }

  /**
   * 菜单名称国际化id
   * @return menuNameLangKey
  **/
  @ApiModelProperty(value = "菜单名称国际化id")
  
    public String getMenuNameLangKey() {
    return menuNameLangKey;
  }

  public void setMenuNameLangKey(String menuNameLangKey) {
    this.menuNameLangKey = menuNameLangKey;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SaveMenuInfoRes saveMenuInfoRes = (SaveMenuInfoRes) o;
    return Objects.equals(this.wid, saveMenuInfoRes.wid) &&
        Objects.equals(this.menuName, saveMenuInfoRes.menuName) &&
        Objects.equals(this.iconUrl, saveMenuInfoRes.iconUrl) &&
        Objects.equals(this.orderNumber, saveMenuInfoRes.orderNumber) &&
        Objects.equals(this.authType, saveMenuInfoRes.authType) &&
        Objects.equals(this.isEnabled, saveMenuInfoRes.isEnabled) &&
        Objects.equals(this.openType, saveMenuInfoRes.openType) &&
        Objects.equals(this.linkUrl, saveMenuInfoRes.linkUrl) &&
        Objects.equals(this.parentId, saveMenuInfoRes.parentId) &&
        Objects.equals(this.programmeId, saveMenuInfoRes.programmeId) &&
        Objects.equals(this.menuType, saveMenuInfoRes.menuType) &&
        Objects.equals(this.pageId, saveMenuInfoRes.pageId) &&
        Objects.equals(this.iconType, saveMenuInfoRes.iconType) &&
        Objects.equals(this.menuNameLangKey, saveMenuInfoRes.menuNameLangKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, menuName, iconUrl, orderNumber, authType, isEnabled, openType, linkUrl, parentId, programmeId, menuType, pageId, iconType, menuNameLangKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SaveMenuInfoRes {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    menuName: ").append(toIndentedString(menuName)).append("\n");
    sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
    sb.append("    orderNumber: ").append(toIndentedString(orderNumber)).append("\n");
    sb.append("    authType: ").append(toIndentedString(authType)).append("\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
    sb.append("    openType: ").append(toIndentedString(openType)).append("\n");
    sb.append("    linkUrl: ").append(toIndentedString(linkUrl)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    programmeId: ").append(toIndentedString(programmeId)).append("\n");
    sb.append("    menuType: ").append(toIndentedString(menuType)).append("\n");
    sb.append("    pageId: ").append(toIndentedString(pageId)).append("\n");
    sb.append("    iconType: ").append(toIndentedString(iconType)).append("\n");
    sb.append("    menuNameLangKey: ").append(toIndentedString(menuNameLangKey)).append("\n");
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
