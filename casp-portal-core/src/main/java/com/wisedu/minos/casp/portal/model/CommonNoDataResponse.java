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
 * CommonNoDataResponse
 */
@Validated

public class CommonNoDataResponse   {
  @JsonProperty("errcode")
  private String errcode = "0";

  @JsonProperty("errmsg")
  private String errmsg = "success";

  @JsonProperty("traceId")
  private String traceId = null;

  public CommonNoDataResponse errcode(String errcode) {
    this.errcode = errcode;
    return this;
  }

  /**
   * 错误代码，0 表示无错误
   * @return errcode
  **/
  @ApiModelProperty(value = "错误代码，0 表示无错误")
  
    public String getErrcode() {
    return errcode;
  }

  public void setErrcode(String errcode) {
    this.errcode = errcode;
  }

  public CommonNoDataResponse errmsg(String errmsg) {
    this.errmsg = errmsg;
    return this;
  }

  /**
   * 错误信息
   * @return errmsg
  **/
  @ApiModelProperty(value = "错误信息")
  
    public String getErrmsg() {
    return errmsg;
  }

  public void setErrmsg(String errmsg) {
    this.errmsg = errmsg;
  }

  public CommonNoDataResponse traceId(String traceId) {
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
    CommonNoDataResponse commonNoDataResponse = (CommonNoDataResponse) o;
    return Objects.equals(this.errcode, commonNoDataResponse.errcode) &&
        Objects.equals(this.errmsg, commonNoDataResponse.errmsg) &&
        Objects.equals(this.traceId, commonNoDataResponse.traceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errcode, errmsg, traceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommonNoDataResponse {\n");
    
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
