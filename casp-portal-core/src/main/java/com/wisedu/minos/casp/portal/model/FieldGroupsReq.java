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
 * FieldGroupsReq
 */
@Validated

public class FieldGroupsReq   {
  @JsonProperty("isTree")
  private String isTree = null;

  @JsonProperty("wid")
  private String wid = null;

  public FieldGroupsReq isTree(String isTree) {
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

  public FieldGroupsReq wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 域及用户组主键Wid
   * @return wid
  **/
  @ApiModelProperty(value = "域及用户组主键Wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FieldGroupsReq fieldGroupsReq = (FieldGroupsReq) o;
    return Objects.equals(this.isTree, fieldGroupsReq.isTree) &&
        Objects.equals(this.wid, fieldGroupsReq.wid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isTree, wid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FieldGroupsReq {\n");
    
    sb.append("    isTree: ").append(toIndentedString(isTree)).append("\n");
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
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
