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
 * SidebarInfo
 */
@Validated

public class SidebarInfo   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("columnCode")
  private String columnCode = null;

  @JsonProperty("columnName")
  private String columnName = null;

  @JsonProperty("isSystem")
  private Integer isSystem = null;

  @JsonProperty("iconType")
  private Integer iconType = null;

  @JsonProperty("authType")
  private Integer authType = null;

  @JsonProperty("enabledFlag")
  private Integer enabledFlag = null;

  @JsonProperty("orderNumber")
  private Integer orderNumber = null;

  @JsonProperty("linkUrl")
  private String linkUrl = null;

  @JsonProperty("iconUrl")
  private String iconUrl = null;

  @JsonProperty("displayName")
  private String displayName = null;

  @JsonProperty("positionType")
  private Integer positionType = null;

  @JsonProperty("displayNameLangKey")
  private String displayNameLangKey = null;

  @JsonProperty("programmeId")
  private String programmeId = null;

  @JsonProperty("menuType")
  private Integer menuType = null;

  @JsonProperty("pageId")
  private String pageId = null;

  @JsonProperty("openType")
  private Integer openType = null;

  @JsonProperty("countAddress")
  private String countAddress = null;

  public SidebarInfo wid(String wid) {
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

  public SidebarInfo columnCode(String columnCode) {
    this.columnCode = columnCode;
    return this;
  }

  /**
   * 栏目编码,不可重复
   * @return columnCode
  **/
  @ApiModelProperty(value = "栏目编码,不可重复")
  
    public String getColumnCode() {
    return columnCode;
  }

  public void setColumnCode(String columnCode) {
    this.columnCode = columnCode;
  }

  public SidebarInfo columnName(String columnName) {
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

  public SidebarInfo isSystem(Integer isSystem) {
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

  public SidebarInfo iconType(Integer iconType) {
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

  public SidebarInfo authType(Integer authType) {
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

  public SidebarInfo enabledFlag(Integer enabledFlag) {
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

  public SidebarInfo orderNumber(Integer orderNumber) {
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

  public SidebarInfo linkUrl(String linkUrl) {
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

  public SidebarInfo iconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
    return this;
  }

  /**
   * 栏目的图标样式名称
   * @return iconUrl
  **/
  @ApiModelProperty(value = "栏目的图标样式名称")
  
    public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public SidebarInfo displayName(String displayName) {
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

  @ApiModelProperty(value = "数量提醒地址")
  public String getCountAddress() {
    return countAddress;
  }

  public void setCountAddress(String countAddress) {
    this.countAddress = countAddress;
  }

  public SidebarInfo positionType(Integer positionType) {
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

  public SidebarInfo displayNameLangKey(String displayNameLangKey) {
    this.displayNameLangKey = displayNameLangKey;
    return this;
  }

  /**
   * 页面标题国际化id
   * @return displayNameLangKey
  **/
  @ApiModelProperty(value = "页面标题国际化id")
  
    public String getDisplayNameLangKey() {
    return displayNameLangKey;
  }

  public void setDisplayNameLangKey(String displayNameLangKey) {
    this.displayNameLangKey = displayNameLangKey;
  }

  public SidebarInfo programmeId(String programmeId) {
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

  public SidebarInfo menuType(Integer menuType) {
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

  public SidebarInfo pageId(String pageId) {
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

  public SidebarInfo openType(Integer openType) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SidebarInfo sidebarInfo = (SidebarInfo) o;
    return Objects.equals(this.wid, sidebarInfo.wid) &&
        Objects.equals(this.columnCode, sidebarInfo.columnCode) &&
        Objects.equals(this.columnName, sidebarInfo.columnName) &&
        Objects.equals(this.isSystem, sidebarInfo.isSystem) &&
        Objects.equals(this.iconType, sidebarInfo.iconType) &&
        Objects.equals(this.authType, sidebarInfo.authType) &&
        Objects.equals(this.enabledFlag, sidebarInfo.enabledFlag) &&
        Objects.equals(this.orderNumber, sidebarInfo.orderNumber) &&
        Objects.equals(this.linkUrl, sidebarInfo.linkUrl) &&
        Objects.equals(this.iconUrl, sidebarInfo.iconUrl) &&
        Objects.equals(this.displayName, sidebarInfo.displayName) &&
        Objects.equals(this.positionType, sidebarInfo.positionType) &&
        Objects.equals(this.displayNameLangKey, sidebarInfo.displayNameLangKey) &&
        Objects.equals(this.programmeId, sidebarInfo.programmeId) &&
        Objects.equals(this.menuType, sidebarInfo.menuType) &&
        Objects.equals(this.pageId, sidebarInfo.pageId) &&
        Objects.equals(this.openType, sidebarInfo.openType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, columnCode, columnName, isSystem, iconType, authType, enabledFlag, orderNumber, linkUrl, iconUrl, displayName, positionType, displayNameLangKey, programmeId, menuType, pageId, openType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SidebarInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    columnCode: ").append(toIndentedString(columnCode)).append("\n");
    sb.append("    columnName: ").append(toIndentedString(columnName)).append("\n");
    sb.append("    isSystem: ").append(toIndentedString(isSystem)).append("\n");
    sb.append("    iconType: ").append(toIndentedString(iconType)).append("\n");
    sb.append("    authType: ").append(toIndentedString(authType)).append("\n");
    sb.append("    enabledFlag: ").append(toIndentedString(enabledFlag)).append("\n");
    sb.append("    orderNumber: ").append(toIndentedString(orderNumber)).append("\n");
    sb.append("    linkUrl: ").append(toIndentedString(linkUrl)).append("\n");
    sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    positionType: ").append(toIndentedString(positionType)).append("\n");
    sb.append("    displayNameLangKey: ").append(toIndentedString(displayNameLangKey)).append("\n");
    sb.append("    programmeId: ").append(toIndentedString(programmeId)).append("\n");
    sb.append("    menuType: ").append(toIndentedString(menuType)).append("\n");
    sb.append("    pageId: ").append(toIndentedString(pageId)).append("\n");
    sb.append("    openType: ").append(toIndentedString(openType)).append("\n");
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
