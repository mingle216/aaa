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
 * 站点管理员
 */
@ApiModel(description = "站点管理员")
@Validated

public class PortalSiteManagerInfo   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("siterWid")
  private String siterWid = null;

  @JsonProperty("magRelWid")
  private String magRelWid = null;

  @JsonProperty("magRelName")
  private String magRelName = null;

  @JsonProperty("magType")
  private Integer magType = null;

  public PortalSiteManagerInfo wid(String wid) {
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

  public PortalSiteManagerInfo siterWid(String siterWid) {
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

  public PortalSiteManagerInfo magRelWid(String magRelWid) {
    this.magRelWid = magRelWid;
    return this;
  }

  /**
   * 管理员对象wid
   * @return magRelWid
  **/
  @ApiModelProperty(value = "管理员对象wid")
  
    public String getMagRelWid() {
    return magRelWid;
  }

  public void setMagRelWid(String magRelWid) {
    this.magRelWid = magRelWid;
  }

  public PortalSiteManagerInfo magRelName(String magRelName) {
    this.magRelName = magRelName;
    return this;
  }

  /**
   * 管理员对象名称
   * @return magRelName
  **/
  @ApiModelProperty(value = "管理员对象名称")
  
    public String getMagRelName() {
    return magRelName;
  }

  public void setMagRelName(String magRelName) {
    this.magRelName = magRelName;
  }

  public PortalSiteManagerInfo magType(Integer magType) {
    this.magType = magType;
    return this;
  }

  /**
   * 授权类型 0 角色 1 个人
   * @return magType
  **/
  @ApiModelProperty(value = "授权类型 0 角色 1 个人")
  
    public Integer getMagType() {
    return magType;
  }

  public void setMagType(Integer magType) {
    this.magType = magType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortalSiteManagerInfo portalSiteManagerInfo = (PortalSiteManagerInfo) o;
    return Objects.equals(this.wid, portalSiteManagerInfo.wid) &&
        Objects.equals(this.siterWid, portalSiteManagerInfo.siterWid) &&
        Objects.equals(this.magRelWid, portalSiteManagerInfo.magRelWid) &&
        Objects.equals(this.magRelName, portalSiteManagerInfo.magRelName) &&
        Objects.equals(this.magType, portalSiteManagerInfo.magType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, siterWid, magRelWid, magRelName, magType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortalSiteManagerInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    siterWid: ").append(toIndentedString(siterWid)).append("\n");
    sb.append("    magRelWid: ").append(toIndentedString(magRelWid)).append("\n");
    sb.append("    magRelName: ").append(toIndentedString(magRelName)).append("\n");
    sb.append("    magType: ").append(toIndentedString(magType)).append("\n");
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
