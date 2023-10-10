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
 * AuthScope
 */
@Validated

public class AuthScope   {
  @JsonProperty("groupId")
  private String groupId = null;

  @JsonProperty("groupType")
  private Integer groupType = null;

  @JsonProperty("groupName")
  private String groupName = null;

  public AuthScope groupId(String groupId) {
    this.groupId = groupId;
    return this;
  }

  /**
   * 分组/机构id
   * @return groupId
  **/
  @ApiModelProperty(value = "分组/机构id")
  
    public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public AuthScope groupType(Integer groupType) {
    this.groupType = groupType;
    return this;
  }

  /**
   * 类型0机构1分组
   * @return groupType
  **/
  @ApiModelProperty(value = "类型0机构1分组")
  
    public Integer getGroupType() {
    return groupType;
  }

  public void setGroupType(Integer groupType) {
    this.groupType = groupType;
  }

  public AuthScope groupName(String groupName) {
    this.groupName = groupName;
    return this;
  }

  /**
   * 名称
   * @return groupName
  **/
  @ApiModelProperty(value = "名称")
  
    public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthScope authScope = (AuthScope) o;
    return Objects.equals(this.groupId, authScope.groupId) &&
        Objects.equals(this.groupType, authScope.groupType) &&
        Objects.equals(this.groupName, authScope.groupName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupId, groupType, groupName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthScope {\n");
    
    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
    sb.append("    groupType: ").append(toIndentedString(groupType)).append("\n");
    sb.append("    groupName: ").append(toIndentedString(groupName)).append("\n");
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
