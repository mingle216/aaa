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
 * DubboGroupRule
 */
@Validated

public class DubboGroupRule   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("groupWid")
  private String groupWid = null;

  @JsonProperty("ruleDicWid")
  private String ruleDicWid = null;

  @JsonProperty("orderIndex")
  private Integer orderIndex = null;

  @JsonProperty("valueList")
  @Valid
  private List<String> valueList = null;

  public DubboGroupRule wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 主键id
   * @return wid
  **/
  @ApiModelProperty(value = "主键id")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public DubboGroupRule groupWid(String groupWid) {
    this.groupWid = groupWid;
    return this;
  }

  /**
   * Get groupWid
   * @return groupWid
  **/
  @ApiModelProperty(value = "")
  
    public String getGroupWid() {
    return groupWid;
  }

  public void setGroupWid(String groupWid) {
    this.groupWid = groupWid;
  }

  public DubboGroupRule ruleDicWid(String ruleDicWid) {
    this.ruleDicWid = ruleDicWid;
    return this;
  }

  /**
   * Get ruleDicWid
   * @return ruleDicWid
  **/
  @ApiModelProperty(value = "")
  
    public String getRuleDicWid() {
    return ruleDicWid;
  }

  public void setRuleDicWid(String ruleDicWid) {
    this.ruleDicWid = ruleDicWid;
  }

  public DubboGroupRule orderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
    return this;
  }

  /**
   * Get orderIndex
   * @return orderIndex
  **/
  @ApiModelProperty(value = "")
  
    public Integer getOrderIndex() {
    return orderIndex;
  }

  public void setOrderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
  }

  public DubboGroupRule valueList(List<String> valueList) {
    this.valueList = valueList;
    return this;
  }

  public DubboGroupRule addValueListItem(String valueListItem) {
    if (this.valueList == null) {
      this.valueList = new ArrayList<String>();
    }
    this.valueList.add(valueListItem);
    return this;
  }

  /**
   * Get valueList
   * @return valueList
  **/
  @ApiModelProperty(value = "")
  
    public List<String> getValueList() {
    return valueList;
  }

  public void setValueList(List<String> valueList) {
    this.valueList = valueList;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DubboGroupRule dubboGroupRule = (DubboGroupRule) o;
    return Objects.equals(this.wid, dubboGroupRule.wid) &&
        Objects.equals(this.groupWid, dubboGroupRule.groupWid) &&
        Objects.equals(this.ruleDicWid, dubboGroupRule.ruleDicWid) &&
        Objects.equals(this.orderIndex, dubboGroupRule.orderIndex) &&
        Objects.equals(this.valueList, dubboGroupRule.valueList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, groupWid, ruleDicWid, orderIndex, valueList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DubboGroupRule {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    groupWid: ").append(toIndentedString(groupWid)).append("\n");
    sb.append("    ruleDicWid: ").append(toIndentedString(ruleDicWid)).append("\n");
    sb.append("    orderIndex: ").append(toIndentedString(orderIndex)).append("\n");
    sb.append("    valueList: ").append(toIndentedString(valueList)).append("\n");
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
