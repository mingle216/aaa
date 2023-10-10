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
 * OrgInfoBiz
 */
@Validated

public class OrgInfoBiz   {
  @JsonProperty("orgWid")
  private String orgWid = null;

  @JsonProperty("orgName")
  private String orgName = null;

  public OrgInfoBiz orgWid(String orgWid) {
    this.orgWid = orgWid;
    return this;
  }

  /**
   * 机构Wid
   * @return orgWid
  **/
  @ApiModelProperty(value = "机构Wid")
  
    public String getOrgWid() {
    return orgWid;
  }

  public void setOrgWid(String orgWid) {
    this.orgWid = orgWid;
  }

  public OrgInfoBiz orgName(String orgName) {
    this.orgName = orgName;
    return this;
  }

  /**
   * 机构名称
   * @return orgName
  **/
  @ApiModelProperty(value = "机构名称")
  
    public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrgInfoBiz orgInfoBiz = (OrgInfoBiz) o;
    return Objects.equals(this.orgWid, orgInfoBiz.orgWid) &&
        Objects.equals(this.orgName, orgInfoBiz.orgName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orgWid, orgName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrgInfoBiz {\n");
    
    sb.append("    orgWid: ").append(toIndentedString(orgWid)).append("\n");
    sb.append("    orgName: ").append(toIndentedString(orgName)).append("\n");
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
