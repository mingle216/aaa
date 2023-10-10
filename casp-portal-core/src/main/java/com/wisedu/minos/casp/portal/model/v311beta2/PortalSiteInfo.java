package com.wisedu.minos.casp.portal.model.v311beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.v311beta2.PortalSiteAuthInfo;
import com.wisedu.minos.casp.portal.model.v311beta2.PortalSiteLangInfo;
import com.wisedu.minos.casp.portal.model.v311beta2.PortalSiteManagerInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 站点信息
 */
@ApiModel(description = "站点信息")
@Validated

public class PortalSiteInfo   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("siteName")
  private String siteName = null;

  @JsonProperty("siteRoute")
  private String siteRoute = null;

  @JsonProperty("authType")
  private Integer authType = null;

  @JsonProperty("orderIndex")
  private Integer orderIndex = null;

  @JsonProperty("isEnabled")
  private Integer isEnabled = null;

  @JsonProperty("siteNameLangList")
  @Valid
  private List<PortalSiteLangInfo> siteNameLangList = null;

  @JsonProperty("siteManagers")
  @Valid
  private List<PortalSiteManagerInfo> siteManagers = null;

  @JsonProperty("authList")
  @Valid
  private List<PortalSiteAuthInfo> authList = null;

  @JsonProperty("isSysAdmin")
  private Boolean isSysAdmin = null;

  @JsonProperty("userWid")
  private String userWid = null;

  @JsonProperty("roleWids")
  @Valid
  private List<String> roleWids = null;

  public PortalSiteInfo wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * Get wid
   * @return wid
   **/
  @ApiModelProperty(value = "")

  public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public PortalSiteInfo siteName(String siteName) {
    this.siteName = siteName;
    return this;
  }

  /**
   * 站点名称
   * @return siteName
   **/
  @ApiModelProperty(value = "站点名称")

  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  public PortalSiteInfo siteRoute(String siteRoute) {
    this.siteRoute = siteRoute;
    return this;
  }

  /**
   * 站点路由
   * @return siteRoute
   **/
  @ApiModelProperty(value = "站点路由")

  public String getSiteRoute() {
    return siteRoute;
  }

  public void setSiteRoute(String siteRoute) {
    this.siteRoute = siteRoute;
  }

  public PortalSiteInfo authType(Integer authType) {
    this.authType = authType;
    return this;
  }

  /**
   * 授权类型 0游客可见 1登录后可见 2游客及登录后可见 3授权后可见
   * @return authType
   **/
  @ApiModelProperty(value = "授权类型 0游客可见 1登录后可见 2游客及登录后可见 3授权后可见")

  public Integer getAuthType() {
    return authType;
  }

  public void setAuthType(Integer authType) {
    this.authType = authType;
  }

  public PortalSiteInfo orderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
    return this;
  }

  /**
   * 排序
   * @return orderIndex
   **/
  @ApiModelProperty(value = "排序")

  public Integer getOrderIndex() {
    return orderIndex;
  }

  public void setOrderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
  }

  public PortalSiteInfo isEnabled(Integer isEnabled) {
    this.isEnabled = isEnabled;
    return this;
  }

  /**
   * 站点状态 0 停用 1启用
   * @return isEnabled
   **/
  @ApiModelProperty(value = "站点状态 0 停用 1启用")

  public Integer getIsEnabled() {
    return isEnabled;
  }

  public void setIsEnabled(Integer isEnabled) {
    this.isEnabled = isEnabled;
  }

  public PortalSiteInfo siteNameLangList(List<PortalSiteLangInfo> siteNameLangList) {
    this.siteNameLangList = siteNameLangList;
    return this;
  }

  public PortalSiteInfo addSiteNameLangListItem(PortalSiteLangInfo siteNameLangListItem) {
    if (this.siteNameLangList == null) {
      this.siteNameLangList = new ArrayList<PortalSiteLangInfo>();
    }
    this.siteNameLangList.add(siteNameLangListItem);
    return this;
  }

  /**
   * 站点名称多语言
   * @return siteNameLangList
   **/
  @ApiModelProperty(value = "站点名称多语言")
  @Valid
  public List<PortalSiteLangInfo> getSiteNameLangList() {
    return siteNameLangList;
  }

  public void setSiteNameLangList(List<PortalSiteLangInfo> siteNameLangList) {
    this.siteNameLangList = siteNameLangList;
  }

  public PortalSiteInfo siteManagers(List<PortalSiteManagerInfo> siteManagers) {
    this.siteManagers = siteManagers;
    return this;
  }

  public PortalSiteInfo addSiteManagersItem(PortalSiteManagerInfo siteManagersItem) {
    if (this.siteManagers == null) {
      this.siteManagers = new ArrayList<PortalSiteManagerInfo>();
    }
    this.siteManagers.add(siteManagersItem);
    return this;
  }

  /**
   * 站点管理员
   * @return siteManagers
   **/
  @ApiModelProperty(value = "站点管理员")
  @Valid
  public List<PortalSiteManagerInfo> getSiteManagers() {
    return siteManagers;
  }

  public void setSiteManagers(List<PortalSiteManagerInfo> siteManagers) {
    this.siteManagers = siteManagers;
  }

  public PortalSiteInfo authList(List<PortalSiteAuthInfo> authList) {
    this.authList = authList;
    return this;
  }

  public PortalSiteInfo addAuthListItem(PortalSiteAuthInfo authListItem) {
    if (this.authList == null) {
      this.authList = new ArrayList<PortalSiteAuthInfo>();
    }
    this.authList.add(authListItem);
    return this;
  }

  /**
   * 站点授权信息
   * @return authList
   **/
  @ApiModelProperty(value = "站点授权信息")
  @Valid
  public List<PortalSiteAuthInfo> getAuthList() {
    return authList;
  }

  public void setAuthList(List<PortalSiteAuthInfo> authList) {
    this.authList = authList;
  }

  public PortalSiteInfo isSysAdmin(Boolean isSysAdmin) {
    this.isSysAdmin = isSysAdmin;
    return this;
  }

  /**
   * 请求用户是否为系统管理员
   * @return isSysAdmin
   **/
  @ApiModelProperty(value = "请求用户是否为系统管理员")

  public Boolean isIsSysAdmin() {
    return isSysAdmin;
  }

  public void setIsSysAdmin(Boolean isSysAdmin) {
    this.isSysAdmin = isSysAdmin;
  }

  public PortalSiteInfo userWid(String userWid) {
    this.userWid = userWid;
    return this;
  }

  /**
   * 请求用户wid
   * @return userWid
   **/
  @ApiModelProperty(value = "请求用户wid")

  public String getUserWid() {
    return userWid;
  }

  public void setUserWid(String userWid) {
    this.userWid = userWid;
  }

  public PortalSiteInfo roleWids(List<String> roleWids) {
    this.roleWids = roleWids;
    return this;
  }

  public PortalSiteInfo addRoleWidsItem(String roleWidsItem) {
    if (this.roleWids == null) {
      this.roleWids = new ArrayList<String>();
    }
    this.roleWids.add(roleWidsItem);
    return this;
  }

  /**
   * 请求用户拥有的角色id列表
   * @return roleWids
   **/
  @ApiModelProperty(value = "请求用户拥有的角色id列表")

  public List<String> getRoleWids() {
    return roleWids;
  }

  public void setRoleWids(List<String> roleWids) {
    this.roleWids = roleWids;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortalSiteInfo portalSiteInfo = (PortalSiteInfo) o;
    return Objects.equals(this.wid, portalSiteInfo.wid) &&
            Objects.equals(this.siteName, portalSiteInfo.siteName) &&
            Objects.equals(this.siteRoute, portalSiteInfo.siteRoute) &&
            Objects.equals(this.authType, portalSiteInfo.authType) &&
            Objects.equals(this.orderIndex, portalSiteInfo.orderIndex) &&
            Objects.equals(this.isEnabled, portalSiteInfo.isEnabled) &&
            Objects.equals(this.siteNameLangList, portalSiteInfo.siteNameLangList) &&
            Objects.equals(this.siteManagers, portalSiteInfo.siteManagers) &&
            Objects.equals(this.authList, portalSiteInfo.authList) &&
            Objects.equals(this.isSysAdmin, portalSiteInfo.isSysAdmin) &&
            Objects.equals(this.userWid, portalSiteInfo.userWid) &&
            Objects.equals(this.roleWids, portalSiteInfo.roleWids);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, siteName, siteRoute, authType, orderIndex, isEnabled, siteNameLangList, siteManagers, authList, isSysAdmin, userWid, roleWids);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortalSiteInfo {\n");

    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    siteName: ").append(toIndentedString(siteName)).append("\n");
    sb.append("    siteRoute: ").append(toIndentedString(siteRoute)).append("\n");
    sb.append("    authType: ").append(toIndentedString(authType)).append("\n");
    sb.append("    orderIndex: ").append(toIndentedString(orderIndex)).append("\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
    sb.append("    siteNameLangList: ").append(toIndentedString(siteNameLangList)).append("\n");
    sb.append("    siteManagers: ").append(toIndentedString(siteManagers)).append("\n");
    sb.append("    authList: ").append(toIndentedString(authList)).append("\n");
    sb.append("    isSysAdmin: ").append(toIndentedString(isSysAdmin)).append("\n");
    sb.append("    userWid: ").append(toIndentedString(userWid)).append("\n");
    sb.append("    roleWids: ").append(toIndentedString(roleWids)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
