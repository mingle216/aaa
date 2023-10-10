package com.wisedu.minos.casp.portal.model.license;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 查看一网通办license信息
 */
@ApiModel(description = "查看一网通办license信息")
@Validated

public class CooskLicenseRes   {
  @JsonProperty("errcode")
  private String errcode = "0";

  @JsonProperty("errmsg")
  private String errmsg = "success";

  @JsonProperty("data")
  private CooskLicenseDto data = null;

  public CooskLicenseRes errcode(String errcode) {
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

  public CooskLicenseRes errmsg(String errmsg) {
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

  public CooskLicenseRes data(CooskLicenseDto data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public CooskLicenseDto getData() {
    return data;
  }

  public void setData(CooskLicenseDto data) {
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
    CooskLicenseRes cooskLicenseRes = (CooskLicenseRes) o;
    return Objects.equals(this.errcode, cooskLicenseRes.errcode) &&
        Objects.equals(this.errmsg, cooskLicenseRes.errmsg) &&
        Objects.equals(this.data, cooskLicenseRes.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errcode, errmsg, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CooskLicenseRes {\n");
    
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
