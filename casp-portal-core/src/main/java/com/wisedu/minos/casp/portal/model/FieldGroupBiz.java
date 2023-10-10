package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.FieldGroupBiz;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * FieldGroupBiz
 */
@Validated

public class FieldGroupBiz   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("pwid")
  private String pwid = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("orderIndex")
  private Integer orderIndex = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("childList")
  @Valid
  private List<FieldGroupBiz> childList = null;

  public FieldGroupBiz wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * Id
   * @return wid
  **/
  @ApiModelProperty(value = "Id")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public FieldGroupBiz pwid(String pwid) {
    this.pwid = pwid;
    return this;
  }

  /**
   * 父节点WID
   * @return pwid
  **/
  @ApiModelProperty(value = "父节点WID")
  
    public String getPwid() {
    return pwid;
  }

  public void setPwid(String pwid) {
    this.pwid = pwid;
  }

  public FieldGroupBiz name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 名称
   * @return name
  **/
  @ApiModelProperty(value = "名称")
  
    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FieldGroupBiz orderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
    return this;
  }

  /**
   * 排序字段 数字小的在前面
   * @return orderIndex
  **/
  @ApiModelProperty(value = "排序字段 数字小的在前面")
  
    public Integer getOrderIndex() {
    return orderIndex;
  }

  public void setOrderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
  }

  public FieldGroupBiz type(String type) {
    this.type = type;
    return this;
  }

  /**
   * 用户组树节点类型 0 根节点（XXX大学） 1 一级用户分类 2 原始组织机构 3 未分配组织架构 4 自定义节点 5 自定义组织机构 6 二级分类 7 业务域 8 用户组
   * @return type
  **/
  @ApiModelProperty(value = "用户组树节点类型 0 根节点（XXX大学） 1 一级用户分类 2 原始组织机构 3 未分配组织架构 4 自定义节点 5 自定义组织机构 6 二级分类 7 业务域 8 用户组")
  
    public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public FieldGroupBiz childList(List<FieldGroupBiz> childList) {
    this.childList = childList;
    return this;
  }

  public FieldGroupBiz addChildListItem(FieldGroupBiz childListItem) {
    if (this.childList == null) {
      this.childList = new ArrayList<FieldGroupBiz>();
    }
    this.childList.add(childListItem);
    return this;
  }

  /**
   * 子信息
   * @return childList
  **/
  @ApiModelProperty(value = "子信息")
      @Valid
    public List<FieldGroupBiz> getChildList() {
    return childList;
  }

  public void setChildList(List<FieldGroupBiz> childList) {
    this.childList = childList;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FieldGroupBiz fieldGroupBiz = (FieldGroupBiz) o;
    return Objects.equals(this.wid, fieldGroupBiz.wid) &&
        Objects.equals(this.pwid, fieldGroupBiz.pwid) &&
        Objects.equals(this.name, fieldGroupBiz.name) &&
        Objects.equals(this.orderIndex, fieldGroupBiz.orderIndex) &&
        Objects.equals(this.type, fieldGroupBiz.type) &&
        Objects.equals(this.childList, fieldGroupBiz.childList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, pwid, name, orderIndex, type, childList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FieldGroupBiz {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    pwid: ").append(toIndentedString(pwid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    orderIndex: ").append(toIndentedString(orderIndex)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    childList: ").append(toIndentedString(childList)).append("\n");
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
