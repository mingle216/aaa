package com.wisedu.minos.casp.portal.model.v353beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.v353beta2.SelectMenuInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 下拉菜单详情
 */
@ApiModel(description = "下拉菜单详情")
@Validated

public class SelectMenuDetailRes   {
  @JsonProperty("errcode")
  private String errcode = "0";

  @JsonProperty("errmsg")
  private String errmsg = "success";

  @JsonProperty("data")
  private SelectMenuInfo data = null;

  public SelectMenuDetailRes errcode(String errcode) {
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

  public SelectMenuDetailRes errmsg(String errmsg) {
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

  public SelectMenuDetailRes data(SelectMenuInfo data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public SelectMenuInfo getData() {
    return data;
  }

  public void setData(SelectMenuInfo data) {
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
    SelectMenuDetailRes selectMenuDetailRes = (SelectMenuDetailRes) o;
    return Objects.equals(this.errcode, selectMenuDetailRes.errcode) &&
        Objects.equals(this.errmsg, selectMenuDetailRes.errmsg) &&
        Objects.equals(this.data, selectMenuDetailRes.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errcode, errmsg, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SelectMenuDetailRes {\n");
    
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
