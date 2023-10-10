package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * GroupNameResponse
 */
@Validated

public class GroupNameResponse {
  @JsonProperty("groupNameList")
  @Valid
  private List<QuerySearchGroupMulNameRes> groupNameList = null;

  public GroupNameResponse groupNameList(List<QuerySearchGroupMulNameRes> groupNameList) {
    this.groupNameList = groupNameList;
    return this;
  }

  public GroupNameResponse addGroupNameListItem(QuerySearchGroupMulNameRes groupNameListItem) {
    if (this.groupNameList == null) {
      this.groupNameList = new ArrayList<QuerySearchGroupMulNameRes>();
    }
    this.groupNameList.add(groupNameListItem);
    return this;
  }

  /**
   * Get groupNameList
   * @return groupNameList
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<QuerySearchGroupMulNameRes> getGroupNameList() {
    return groupNameList;
  }

  public void setGroupNameList(List<QuerySearchGroupMulNameRes> groupNameList) {
    this.groupNameList = groupNameList;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupNameResponse groupNameResponse = (GroupNameResponse) o;
    return Objects.equals(this.groupNameList, groupNameResponse.groupNameList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupNameList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GroupNameResponse {\n");
    
    sb.append("    groupNameList: ").append(toIndentedString(groupNameList)).append("\n");
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
