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
 * 公共返回类
 */
@ApiModel(description = "公共返回类")
@Validated

public class CommonRes   {
  @JsonProperty("errcode")
  private String errcode = "0";

  @JsonProperty("errmsg")
  private String errmsg = "success";

  @JsonProperty("data")
  private Object data = null;

  public CommonRes errcode(String errcode) {
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

  public CommonRes errmsg(String errmsg) {
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

  public CommonRes data(Object data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(value = "")
  
    public Object getData() {
    return data;
  }

  public void setData(Object data) {
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
    CommonRes commonRes = (CommonRes) o;
    return Objects.equals(this.errcode, commonRes.errcode) &&
        Objects.equals(this.errmsg, commonRes.errmsg) &&
        Objects.equals(this.data, commonRes.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errcode, errmsg, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommonRes {\n");
    
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
