package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.MenuBiz;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * QueryMenuListResponse
 */
@Validated

public class QueryMenuListResponse   {
  @JsonProperty("resultCode")
  private Integer resultCode = null;

  @JsonProperty("resultMsg")
  private String resultMsg = null;

  @JsonProperty("data")
  @Valid
  private List<MenuBiz> data = null;

  public QueryMenuListResponse resultCode(Integer resultCode) {
    this.resultCode = resultCode;
    return this;
  }

  /**
   * 操作状态码（0：成功，500：失败）
   * @return resultCode
  **/
  @ApiModelProperty(value = "操作状态码（0：成功，500：失败）")
  
    public Integer getResultCode() {
    return resultCode;
  }

  public void setResultCode(Integer resultCode) {
    this.resultCode = resultCode;
  }

  public QueryMenuListResponse resultMsg(String resultMsg) {
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

  public QueryMenuListResponse data(List<MenuBiz> data) {
    this.data = data;
    return this;
  }

  public QueryMenuListResponse addDataItem(MenuBiz dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<MenuBiz>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * 菜单信息
   * @return data
  **/
  @ApiModelProperty(value = "菜单信息")
      @Valid
    public List<MenuBiz> getData() {
    return data;
  }

  public void setData(List<MenuBiz> data) {
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
    QueryMenuListResponse queryMenuListResponse = (QueryMenuListResponse) o;
    return Objects.equals(this.resultCode, queryMenuListResponse.resultCode) &&
        Objects.equals(this.resultMsg, queryMenuListResponse.resultMsg) &&
        Objects.equals(this.data, queryMenuListResponse.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resultCode, resultMsg, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryMenuListResponse {\n");
    
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
