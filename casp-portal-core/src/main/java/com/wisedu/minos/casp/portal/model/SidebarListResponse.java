package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.SidebarObj;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SidebarListResponse
 */
@Validated

public class SidebarListResponse   {
  @JsonProperty("resultCode")
  private Integer resultCode = null;

  @JsonProperty("resultMsg")
  private String resultMsg = null;

  @JsonProperty("data")
  @Valid
  private List<SidebarObj> data = null;

  public SidebarListResponse resultCode(Integer resultCode) {
    this.resultCode = resultCode;
    return this;
  }

  /**
   * 操作状态码（200：成功，其他操作码：失败）
   * @return resultCode
  **/
  @ApiModelProperty(value = "操作状态码（200：成功，其他操作码：失败）")
  
    public Integer getResultCode() {
    return resultCode;
  }

  public void setResultCode(Integer resultCode) {
    this.resultCode = resultCode;
  }

  public SidebarListResponse resultMsg(String resultMsg) {
    this.resultMsg = resultMsg;
    return this;
  }

  /**
   * 请求成功相关信息
   * @return resultMsg
  **/
  @ApiModelProperty(value = "请求成功相关信息")
  
    public String getResultMsg() {
    return resultMsg;
  }

  public void setResultMsg(String resultMsg) {
    this.resultMsg = resultMsg;
  }

  public SidebarListResponse data(List<SidebarObj> data) {
    this.data = data;
    return this;
  }

  public SidebarListResponse addDataItem(SidebarObj dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<SidebarObj>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * 侧边栏列表
   * @return data
  **/
  @ApiModelProperty(value = "侧边栏列表")
      @Valid
    public List<SidebarObj> getData() {
    return data;
  }

  public void setData(List<SidebarObj> data) {
    this.data = data;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SidebarListResponse sidebarListResponse = (SidebarListResponse) o;
    return Objects.equals(this.resultCode, sidebarListResponse.resultCode) &&
        Objects.equals(this.resultMsg, sidebarListResponse.resultMsg) &&
        Objects.equals(this.data, sidebarListResponse.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resultCode, resultMsg, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SidebarListResponse {\n");
    
    sb.append("    resultCode: ").append(toIndentedString(resultCode)).append("\n");
    sb.append("    resultMsg: ").append(toIndentedString(resultMsg)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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
