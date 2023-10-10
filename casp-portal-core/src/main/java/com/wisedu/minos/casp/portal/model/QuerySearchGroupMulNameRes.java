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
 * QuerySearchGroupMulNameRes
 */
@Validated

public class QuerySearchGroupMulNameRes   {
  @JsonProperty("groupWid")
  private String groupWid = null;

  @JsonProperty("groupName")
  private String groupName = null;

  @JsonProperty("groupType")
  private Integer groupType = null;

  public QuerySearchGroupMulNameRes groupWid(String groupWid) {
    this.groupWid = groupWid;
    return this;
  }

  /**
   * 分组wid
   * @return groupWid
  **/
  @ApiModelProperty(value = "分组wid")
  
    public String getGroupWid() {
    return groupWid;
  }

  public void setGroupWid(String groupWid) {
    this.groupWid = groupWid;
  }

  public QuerySearchGroupMulNameRes groupName(String groupName) {
    this.groupName = groupName;
    return this;
  }

  /**
   * 多语言名称
   * @return groupName
  **/
  @ApiModelProperty(value = "多语言名称")
  
    public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public QuerySearchGroupMulNameRes groupType(Integer groupType) {
    this.groupType = groupType;
    return this;
  }

  /**
   * 分组类型(1自建分组 2预置分组-服务 3预置分组-事项 4预置分组-新闻)
   * @return groupType
  **/
  @ApiModelProperty(value = "分组类型(1自建分组 2预置分组-服务 3预置分组-事项 4预置分组-新闻)")
  
    public Integer getGroupType() {
    return groupType;
  }

  public void setGroupType(Integer groupType) {
    this.groupType = groupType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuerySearchGroupMulNameRes querySearchGroupMulNameRes = (QuerySearchGroupMulNameRes) o;
    return Objects.equals(this.groupWid, querySearchGroupMulNameRes.groupWid) &&
        Objects.equals(this.groupName, querySearchGroupMulNameRes.groupName) &&
        Objects.equals(this.groupType, querySearchGroupMulNameRes.groupType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupWid, groupName, groupType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuerySearchGroupMulNameRes {\n");
    
    sb.append("    groupWid: ").append(toIndentedString(groupWid)).append("\n");
    sb.append("    groupName: ").append(toIndentedString(groupName)).append("\n");
    sb.append("    groupType: ").append(toIndentedString(groupType)).append("\n");
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
