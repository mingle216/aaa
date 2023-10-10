package com.wisedu.minos.casp.portal.model;

import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DubboGroupBeanInfo
 */
@Validated

public class DubboGroupBeanInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("groupId")
  private String groupId = null;

  @JsonProperty("groupType")
  private String groupType = null;

  @JsonProperty("domainWid")
  private String domainWid = null;

  @JsonProperty("orderIndex")
  private Integer orderIndex = null;

  public DubboGroupBeanInfo wid(String wid) {
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

  public DubboGroupBeanInfo name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 用户组名称
   * @return name
  **/
  @ApiModelProperty(value = "用户组名称")
  
    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DubboGroupBeanInfo groupId(String groupId) {
    this.groupId = groupId;
    return this;
  }

  /**
   * 用户组id
   * @return groupId
  **/
  @ApiModelProperty(value = "用户组id")
  
    public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public DubboGroupBeanInfo groupType(String groupType) {
    this.groupType = groupType;
    return this;
  }

  /**
   * 用户组类型
   * @return groupType
  **/
  @ApiModelProperty(value = "用户组类型")
  
    public String getGroupType() {
    return groupType;
  }

  public void setGroupType(String groupType) {
    this.groupType = groupType;
  }

  public DubboGroupBeanInfo domainWid(String domainWid) {
    this.domainWid = domainWid;
    return this;
  }

  /**
   * Get domainWid
   * @return domainWid
  **/
  @ApiModelProperty(value = "")
  
    public String getDomainWid() {
    return domainWid;
  }

  public void setDomainWid(String domainWid) {
    this.domainWid = domainWid;
  }

  public DubboGroupBeanInfo orderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
    return this;
  }

  /**
   * 排序
   * @return orderIndex
  **/
  @ApiModelProperty(value = "排序")
  
    public Integer getOrderIndex() {
    return orderIndex;
  }

  public void setOrderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DubboGroupBeanInfo dubboGroupBeanInfo = (DubboGroupBeanInfo) o;
    return Objects.equals(this.wid, dubboGroupBeanInfo.wid) &&
        Objects.equals(this.name, dubboGroupBeanInfo.name) &&
        Objects.equals(this.groupId, dubboGroupBeanInfo.groupId) &&
        Objects.equals(this.groupType, dubboGroupBeanInfo.groupType) &&
        Objects.equals(this.domainWid, dubboGroupBeanInfo.domainWid) &&
        Objects.equals(this.orderIndex, dubboGroupBeanInfo.orderIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, name, groupId, groupType, domainWid, orderIndex);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DubboGroupBeanInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
    sb.append("    groupType: ").append(toIndentedString(groupType)).append("\n");
    sb.append("    domainWid: ").append(toIndentedString(domainWid)).append("\n");
    sb.append("    orderIndex: ").append(toIndentedString(orderIndex)).append("\n");
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
