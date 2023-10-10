package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DeleteSiteRequest
 */
@Validated

public class DeleteSiteRequest   {
  @JsonProperty("siteWids")
  @Valid
  private List<String> siteWids = null;

  @JsonProperty("isSysAdmin")
  private Boolean isSysAdmin = null;

  @JsonProperty("userWid")
  private String userWid = null;

  @JsonProperty("roleWids")
  @Valid
  private List<String> roleWids = null;

  public DeleteSiteRequest siteWids(List<String> siteWids) {
    this.siteWids = siteWids;
    return this;
  }

  public DeleteSiteRequest addSiteWidsItem(String siteWidsItem) {
    if (this.siteWids == null) {
      this.siteWids = new ArrayList<String>();
    }
    this.siteWids.add(siteWidsItem);
    return this;
  }

  /**
   * Get siteWids
   * @return siteWids
  **/
  @ApiModelProperty(value = "")
  
    public List<String> getSiteWids() {
    return siteWids;
  }

  public void setSiteWids(List<String> siteWids) {
    this.siteWids = siteWids;
  }

  public DeleteSiteRequest isSysAdmin(Boolean isSysAdmin) {
    this.isSysAdmin = isSysAdmin;
    return this;
  }

  /**
   * Get isSysAdmin
   * @return isSysAdmin
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isIsSysAdmin() {
    return isSysAdmin;
  }

  public void setIsSysAdmin(Boolean isSysAdmin) {
    this.isSysAdmin = isSysAdmin;
  }

  public DeleteSiteRequest userWid(String userWid) {
    this.userWid = userWid;
    return this;
  }

  /**
   * Get userWid
   * @return userWid
  **/
  @ApiModelProperty(value = "")
  
    public String getUserWid() {
    return userWid;
  }

  public void setUserWid(String userWid) {
    this.userWid = userWid;
  }

  public DeleteSiteRequest roleWids(List<String> roleWids) {
    this.roleWids = roleWids;
    return this;
  }

  public DeleteSiteRequest addRoleWidsItem(String roleWidsItem) {
    if (this.roleWids == null) {
      this.roleWids = new ArrayList<String>();
    }
    this.roleWids.add(roleWidsItem);
    return this;
  }

  /**
   * Get roleWids
   * @return roleWids
  **/
  @ApiModelProperty(value = "")
  
    public List<String> getRoleWids() {
    return roleWids;
  }

  public void setRoleWids(List<String> roleWids) {
    this.roleWids = roleWids;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeleteSiteRequest deleteSiteRequest = (DeleteSiteRequest) o;
    return Objects.equals(this.siteWids, deleteSiteRequest.siteWids) &&
        Objects.equals(this.isSysAdmin, deleteSiteRequest.isSysAdmin) &&
        Objects.equals(this.userWid, deleteSiteRequest.userWid) &&
        Objects.equals(this.roleWids, deleteSiteRequest.roleWids);
  }

  @Override
  public int hashCode() {
    return Objects.hash(siteWids, isSysAdmin, userWid, roleWids);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeleteSiteRequest {\n");
    
    sb.append("    siteWids: ").append(toIndentedString(siteWids)).append("\n");
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
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
