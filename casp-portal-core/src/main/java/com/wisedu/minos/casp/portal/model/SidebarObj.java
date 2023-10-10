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
 * SidebarObj
 */
@Validated

public class SidebarObj   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("columnName")
  private String columnName = null;

  @JsonProperty("enabledFlag")
  private Integer enabledFlag = null;

  @JsonProperty("openType")
  private Integer openType = null;

  @JsonProperty("iconUrl")
  private String iconUrl = null;

  @JsonProperty("orderNumber")
  private Integer orderNumber = null;

  public SidebarObj wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 主键
   * @return wid
  **/
  @ApiModelProperty(value = "主键")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public SidebarObj columnName(String columnName) {
    this.columnName = columnName;
    return this;
  }

  /**
   * 栏目名称
   * @return columnName
  **/
  @ApiModelProperty(value = "栏目名称")
  
    public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public SidebarObj enabledFlag(Integer enabledFlag) {
    this.enabledFlag = enabledFlag;
    return this;
  }

  /**
   * 是否启用（0：否；1：是）
   * @return enabledFlag
  **/
  @ApiModelProperty(value = "是否启用（0：否；1：是）")
  
    public Integer getEnabledFlag() {
    return enabledFlag;
  }

  public void setEnabledFlag(Integer enabledFlag) {
    this.enabledFlag = enabledFlag;
  }

  public SidebarObj openType(Integer openType) {
    this.openType = openType;
    return this;
  }

  /**
   * 打开方式  0:当前页 1:新开窗口
   * @return openType
  **/
  @ApiModelProperty(value = "打开方式  0:当前页 1:新开窗口")
  
    public Integer getOpenType() {
    return openType;
  }

  public void setOpenType(Integer openType) {
    this.openType = openType;
  }

  public SidebarObj iconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
    return this;
  }

  /**
   * 图片地址
   * @return iconUrl
  **/
  @ApiModelProperty(value = "图片地址")
  
    public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public SidebarObj orderNumber(Integer orderNumber) {
    this.orderNumber = orderNumber;
    return this;
  }

  /**
   * 排序
   * @return orderNumber
  **/
  @ApiModelProperty(value = "排序")
  
    public Integer getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(Integer orderNumber) {
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
    SidebarObj sidebarObj = (SidebarObj) o;
    return Objects.equals(this.wid, sidebarObj.wid) &&
        Objects.equals(this.columnName, sidebarObj.columnName) &&
        Objects.equals(this.enabledFlag, sidebarObj.enabledFlag) &&
        Objects.equals(this.openType, sidebarObj.openType) &&
        Objects.equals(this.iconUrl, sidebarObj.iconUrl) &&
        Objects.equals(this.orderNumber, sidebarObj.orderNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, columnName, enabledFlag, openType, iconUrl, orderNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SidebarObj {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    columnName: ").append(toIndentedString(columnName)).append("\n");
    sb.append("    enabledFlag: ").append(toIndentedString(enabledFlag)).append("\n");
    sb.append("    openType: ").append(toIndentedString(openType)).append("\n");
    sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
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
