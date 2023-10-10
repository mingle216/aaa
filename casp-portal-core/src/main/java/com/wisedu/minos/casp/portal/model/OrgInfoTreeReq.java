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
 * OrgInfoTreeReq
 */
@Validated

public class OrgInfoTreeReq   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("orgType")
  private String orgType = null;

  @JsonProperty("isTree")
  private String isTree = null;

  public OrgInfoTreeReq wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 机构主键Wid
   * @return wid
  **/
  @ApiModelProperty(value = "机构主键Wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public OrgInfoTreeReq orgType(String orgType) {
    this.orgType = orgType;
    return this;
  }

  /**
   * 人员授权机构类型：1-教职工 2-学生 3-其他人员
   * @return orgType
  **/
  @ApiModelProperty(value = "人员授权机构类型：1-教职工 2-学生 3-其他人员")
  
    public String getOrgType() {
    return orgType;
  }

  public void setOrgType(String orgType) {
    this.orgType = orgType;
  }

  public OrgInfoTreeReq isTree(String isTree) {
    this.isTree = isTree;
    return this;
  }

  /**
   * 是否以树形数据展示 1：是  0：否
   * @return isTree
  **/
  @ApiModelProperty(value = "是否以树形数据展示 1：是  0：否")
  
    public String getIsTree() {
    return isTree;
  }

  public void setIsTree(String isTree) {
    this.isTree = isTree;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrgInfoTreeReq orgInfoTreeReq = (OrgInfoTreeReq) o;
    return Objects.equals(this.wid, orgInfoTreeReq.wid) &&
        Objects.equals(this.orgType, orgInfoTreeReq.orgType) &&
        Objects.equals(this.isTree, orgInfoTreeReq.isTree);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, orgType, isTree);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrgInfoTreeReq {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    orgType: ").append(toIndentedString(orgType)).append("\n");
    sb.append("    isTree: ").append(toIndentedString(isTree)).append("\n");
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
