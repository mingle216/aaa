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
 * CommonStringInfo
 */
@Validated

public class CommonStringInfo   {
  @JsonProperty("errcode")
  private String errcode = "0";

  @JsonProperty("errmsg")
  private String errmsg = "success";

  @JsonProperty("data")
  private String data = null;

  public CommonStringInfo errcode(String errcode) {
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

  public CommonStringInfo errmsg(String errmsg) {
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

  public CommonStringInfo data(String data) {
    this.data = data;
    return this;
  }

  /**
   * 统一的返回String属性字段
   * @return data
  **/
  @ApiModelProperty(value = "统一的返回String属性字段")
  
    public String getData() {
    return data;
  }

  public void setData(String data) {
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
    CommonStringInfo commonStringInfo = (CommonStringInfo) o;
    return Objects.equals(this.errcode, commonStringInfo.errcode) &&
        Objects.equals(this.errmsg, commonStringInfo.errmsg) &&
        Objects.equals(this.data, commonStringInfo.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errcode, errmsg, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommonStringInfo {\n");
    
    sb.append("    errcode: ").append(toIndentedString(errcode)).append("\n");
    sb.append("    errmsg: ").append(toIndentedString(errmsg)).append("\n");
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
