package com.wisedu.minos.casp.portal.model.v353beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 下拉菜单排序
 */
@ApiModel(description = "下拉菜单排序")
@Validated

public class SortSelectMenuReq   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("programmeWid")
  private String programmeWid = null;

  @JsonProperty("orderNumber")
  private Long orderNumber = null;

  public SortSelectMenuReq wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * wid
   * @return wid
  **/
  @ApiModelProperty(value = "wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public SortSelectMenuReq programmeWid(String programmeWid) {
    this.programmeWid = programmeWid;
    return this;
  }

  /**
   * 展示方案wid
   * @return programmeWid
  **/
  @ApiModelProperty(value = "展示方案wid")
  
    public String getProgrammeWid() {
    return programmeWid;
  }

  public void setProgrammeWid(String programmeWid) {
    this.programmeWid = programmeWid;
  }

  public SortSelectMenuReq orderNumber(Long orderNumber) {
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
    SortSelectMenuReq sortSelectMenuReq = (SortSelectMenuReq) o;
    return Objects.equals(this.wid, sortSelectMenuReq.wid) &&
        Objects.equals(this.programmeWid, sortSelectMenuReq.programmeWid) &&
        Objects.equals(this.orderNumber, sortSelectMenuReq.orderNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, programmeWid, orderNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SortSelectMenuReq {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    programmeWid: ").append(toIndentedString(programmeWid)).append("\n");
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
