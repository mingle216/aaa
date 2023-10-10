package com.wisedu.minos.casp.portal.model.v311beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 站点授权信息
 */
@ApiModel(description = "站点授权信息")
@Validated

public class PortalSiteAuthInfo   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("siterWid")
  private String siterWid = null;

  @JsonProperty("authRelWid")
  private String authRelWid = null;

  @JsonProperty("authRelName")
  private String authRelName = null;

  @JsonProperty("authType")
  private Integer authType = null;

  public PortalSiteAuthInfo wid(String wid) {
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

  public PortalSiteAuthInfo siterWid(String siterWid) {
    this.siterWid = siterWid;
    return this;
  }

  /**
   * 站点wid
   * @return siterWid
  **/
  @ApiModelProperty(value = "站点wid")
  
    public String getSiterWid() {
    return siterWid;
  }

  public void setSiterWid(String siterWid) {
    this.siterWid = siterWid;
  }

  public PortalSiteAuthInfo authRelWid(String authRelWid) {
    this.authRelWid = authRelWid;
    return this;
  }

  /**
   * 授权关联对象wid
   * @return authRelWid
  **/
  @ApiModelProperty(value = "授权关联对象wid")
  
    public String getAuthRelWid() {
    return authRelWid;
  }

  public void setAuthRelWid(String authRelWid) {
    this.authRelWid = authRelWid;
  }

  public PortalSiteAuthInfo authRelName(String authRelName) {
    this.authRelName = authRelName;
    return this;
  }

  /**
   * 管理对象名称
   * @return authRelName
  **/
  @ApiModelProperty(value = "管理对象名称")
  
    public String getAuthRelName() {
    return authRelName;
  }

  public void setAuthRelName(String authRelName) {
    this.authRelName = authRelName;
  }

  public PortalSiteAuthInfo authType(Integer authType) {
    this.authType = authType;
    return this;
  }

  /**
   * 授权类型 0 组织机构 1 个人 2 域及用户组
   * @return authType
  **/
  @ApiModelProperty(value = "授权类型 0 组织机构 1 个人 2 域及用户组")
  
    public Integer getAuthType() {
    return authType;
  }

  public void setAuthType(Integer authType) {
    this.authType = authType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortalSiteAuthInfo portalSiteAuthInfo = (PortalSiteAuthInfo) o;
    return Objects.equals(this.wid, portalSiteAuthInfo.wid) &&
        Objects.equals(this.siterWid, portalSiteAuthInfo.siterWid) &&
        Objects.equals(this.authRelWid, portalSiteAuthInfo.authRelWid) &&
        Objects.equals(this.authRelName, portalSiteAuthInfo.authRelName) &&
        Objects.equals(this.authType, portalSiteAuthInfo.authType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, siterWid, authRelWid, authRelName, authType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortalSiteAuthInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    siterWid: ").append(toIndentedString(siterWid)).append("\n");
    sb.append("    authRelWid: ").append(toIndentedString(authRelWid)).append("\n");
    sb.append("    authRelName: ").append(toIndentedString(authRelName)).append("\n");
    sb.append("    authType: ").append(toIndentedString(authType)).append("\n");
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
