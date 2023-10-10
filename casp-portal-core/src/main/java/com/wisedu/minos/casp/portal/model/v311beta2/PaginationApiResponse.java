package com.wisedu.minos.casp.portal.model.v311beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PaginationApiResponse
 */
@Validated

public class PaginationApiResponse   {
  @JsonProperty("pageNumber")
  private Long pageNumber = null;

  @JsonProperty("pageSize")
  private Long pageSize = null;

  @JsonProperty("totalSize")
  private Long totalSize = null;

  @JsonProperty("errcode")
  private String errcode = "0";

  @JsonProperty("errmsg")
  private String errmsg = "success";

  @JsonProperty("traceId")
  private String traceId = null;

  public PaginationApiResponse pageNumber(Long pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

  /**
   * 页码，从0开始
   * @return pageNumber
  **/
  @ApiModelProperty(example = "0", value = "页码，从0开始")
  
    public Long getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Long pageNumber) {
    this.pageNumber = pageNumber;
  }

  public PaginationApiResponse pageSize(Long pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * 每页数据量
   * @return pageSize
  **/
  @ApiModelProperty(example = "10", value = "每页数据量")
  
    public Long getPageSize() {
    return pageSize;
  }

  public void setPageSize(Long pageSize) {
    this.pageSize = pageSize;
  }

  public PaginationApiResponse totalSize(Long totalSize) {
    this.totalSize = totalSize;
    return this;
  }

  /**
   * 总条数
   * @return totalSize
  **/
  @ApiModelProperty(example = "100", value = "总条数")
  
    public Long getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(Long totalSize) {
    this.totalSize = totalSize;
  }

  public PaginationApiResponse errcode(String errcode) {
    this.errcode = errcode;
    return this;
  }

  /**
   * 错误代码，0 表示无错误
   * @return errcode
  **/
  @ApiModelProperty(required = true, value = "错误代码，0 表示无错误")
      @NotNull

    public String getErrcode() {
    return errcode;
  }

  public void setErrcode(String errcode) {
    this.errcode = errcode;
  }

  public PaginationApiResponse errmsg(String errmsg) {
    this.errmsg = errmsg;
    return this;
  }

  /**
   * 错误信息
   * @return errmsg
  **/
  @ApiModelProperty(required = true, value = "错误信息")
      @NotNull

    public String getErrmsg() {
    return errmsg;
  }

  public void setErrmsg(String errmsg) {
    this.errmsg = errmsg;
  }

  public PaginationApiResponse traceId(String traceId) {
    this.traceId = traceId;
    return this;
  }

  /**
   * 用于唯一标识一次请求的，必须是 UUID 形式。
   * @return traceId
  **/
  @ApiModelProperty(example = "C1A4702B-ABFC-45BD-83DB-C70E71CCDD8D", value = "用于唯一标识一次请求的，必须是 UUID 形式。")
  
    public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaginationApiResponse paginationApiResponse = (PaginationApiResponse) o;
    return Objects.equals(this.pageNumber, paginationApiResponse.pageNumber) &&
        Objects.equals(this.pageSize, paginationApiResponse.pageSize) &&
        Objects.equals(this.totalSize, paginationApiResponse.totalSize) &&
        Objects.equals(this.errcode, paginationApiResponse.errcode) &&
        Objects.equals(this.errmsg, paginationApiResponse.errmsg) &&
        Objects.equals(this.traceId, paginationApiResponse.traceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pageNumber, pageSize, totalSize, errcode, errmsg, traceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginationApiResponse {\n");
    
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    totalSize: ").append(toIndentedString(totalSize)).append("\n");
    sb.append("    errcode: ").append(toIndentedString(errcode)).append("\n");
    sb.append("    errmsg: ").append(toIndentedString(errmsg)).append("\n");
    sb.append("    traceId: ").append(toIndentedString(traceId)).append("\n");
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
