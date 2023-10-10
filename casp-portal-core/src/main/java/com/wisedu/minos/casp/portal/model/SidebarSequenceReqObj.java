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
 * SidebarSequenceReqObj
 */
@Validated

public class SidebarSequenceReqObj   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("orderNumber")
  private Long orderNumber = null;

  public SidebarSequenceReqObj wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 侧边栏主键id
   * @return wid
  **/
  @ApiModelProperty(value = "侧边栏主键id")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public SidebarSequenceReqObj orderNumber(Long orderNumber) {
    this.orderNumber = orderNumber;
    return this;
  }

  /**
   * 排序
   * @return orderNumber
  **/
  @ApiModelProperty(value = "排序")
  
    public Long getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(Long orderNumber) {
    this.orderNumber = orderNumber;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SidebarSequenceReqObj sidebarSequenceReqObj = (SidebarSequenceReqObj) o;
    return Objects.equals(this.wid, sidebarSequenceReqObj.wid) &&
        Objects.equals(this.orderNumber, sidebarSequenceReqObj.orderNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, orderNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SidebarSequenceReqObj {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    orderNumber: ").append(toIndentedString(orderNumber)).append("\n");
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
