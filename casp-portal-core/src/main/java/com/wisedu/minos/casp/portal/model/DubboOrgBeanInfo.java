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
 * DubboOrgBeanInfo
 */
@Validated

public class DubboOrgBeanInfo   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("pWid")
  private String pWid = null;

  @JsonProperty("categoryWid")
  private String categoryWid = null;

  @JsonProperty("orderIndex")
  private Integer orderIndex = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("shortName")
  private String shortName = null;

  @JsonProperty("isVisible")
  private String isVisible = null;

  public DubboOrgBeanInfo wid(String wid) {
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

  public DubboOrgBeanInfo code(String code) {
    this.code = code;
    return this;
  }

  /**
   * 编码
   * @return code
  **/
  @ApiModelProperty(value = "编码")
  
    public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public DubboOrgBeanInfo name(String name) {
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

  public DubboOrgBeanInfo pWid(String pWid) {
    this.pWid = pWid;
    return this;
  }

  /**
   * 父级id
   * @return pWid
  **/
  @ApiModelProperty(value = "父级id")
  
    public String getPWid() {
    return pWid;
  }

  public void setPWid(String pWid) {
    this.pWid = pWid;
  }

  public DubboOrgBeanInfo categoryWid(String categoryWid) {
    this.categoryWid = categoryWid;
    return this;
  }

  /**
   * 分类wid
   * @return categoryWid
  **/
  @ApiModelProperty(value = "分类wid")
  
    public String getCategoryWid() {
    return categoryWid;
  }

  public void setCategoryWid(String categoryWid) {
    this.categoryWid = categoryWid;
  }

  public DubboOrgBeanInfo orderIndex(Integer orderIndex) {
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

  public DubboOrgBeanInfo type(String type) {
    this.type = type;
    return this;
  }

  /**
   * 类型
   * @return type
  **/
  @ApiModelProperty(value = "类型")
  
    public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public DubboOrgBeanInfo shortName(String shortName) {
    this.shortName = shortName;
    return this;
  }

  /**
   * 简洁名称
   * @return shortName
  **/
  @ApiModelProperty(value = "简洁名称")
  
    public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public DubboOrgBeanInfo isVisible(String isVisible) {
    this.isVisible = isVisible;
    return this;
  }

  /**
   * 是否可见
   * @return isVisible
  **/
  @ApiModelProperty(value = "是否可见")
  
    public String getIsVisible() {
    return isVisible;
  }

  public void setIsVisible(String isVisible) {
    this.isVisible = isVisible;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DubboOrgBeanInfo dubboOrgBeanInfo = (DubboOrgBeanInfo) o;
    return Objects.equals(this.wid, dubboOrgBeanInfo.wid) &&
        Objects.equals(this.code, dubboOrgBeanInfo.code) &&
        Objects.equals(this.name, dubboOrgBeanInfo.name) &&
        Objects.equals(this.pWid, dubboOrgBeanInfo.pWid) &&
        Objects.equals(this.categoryWid, dubboOrgBeanInfo.categoryWid) &&
        Objects.equals(this.orderIndex, dubboOrgBeanInfo.orderIndex) &&
        Objects.equals(this.type, dubboOrgBeanInfo.type) &&
        Objects.equals(this.shortName, dubboOrgBeanInfo.shortName) &&
        Objects.equals(this.isVisible, dubboOrgBeanInfo.isVisible);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, code, name, pWid, categoryWid, orderIndex, type, shortName, isVisible);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DubboOrgBeanInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    pWid: ").append(toIndentedString(pWid)).append("\n");
    sb.append("    categoryWid: ").append(toIndentedString(categoryWid)).append("\n");
    sb.append("    orderIndex: ").append(toIndentedString(orderIndex)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    shortName: ").append(toIndentedString(shortName)).append("\n");
    sb.append("    isVisible: ").append(toIndentedString(isVisible)).append("\n");
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
