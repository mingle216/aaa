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
 * DubboExtInfoReq
 */
@Validated

public class DubboExtInfoReq   {
  @JsonProperty("extKey")
  private String extKey = null;

  @JsonProperty("extValue")
  private String extValue = null;

  public DubboExtInfoReq extKey(String extKey) {
    this.extKey = extKey;
    return this;
  }

  /**
   * Get extKey
   * @return extKey
  **/
  @ApiModelProperty(value = "")
  
    public String getExtKey() {
    return extKey;
  }

  public void setExtKey(String extKey) {
    this.extKey = extKey;
  }

  public DubboExtInfoReq extValue(String extValue) {
    this.extValue = extValue;
    return this;
  }

  /**
   * Get extValue
   * @return extValue
  **/
  @ApiModelProperty(value = "")
  
    public String getExtValue() {
    return extValue;
  }

  public void setExtValue(String extValue) {
    this.extValue = extValue;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DubboExtInfoReq dubboExtInfoReq = (DubboExtInfoReq) o;
    return Objects.equals(this.extKey, dubboExtInfoReq.extKey) &&
        Objects.equals(this.extValue, dubboExtInfoReq.extValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(extKey, extValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DubboExtInfoReq {\n");
    
    sb.append("    extKey: ").append(toIndentedString(extKey)).append("\n");
    sb.append("    extValue: ").append(toIndentedString(extValue)).append("\n");
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
