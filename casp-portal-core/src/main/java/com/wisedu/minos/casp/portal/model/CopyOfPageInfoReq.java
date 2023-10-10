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
 * CopyOfPageInfoReq
 */
@Validated

public class CopyOfPageInfoReq   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("pageCode")
  private String pageCode = null;

  public CopyOfPageInfoReq wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 页面主键id
   * @return wid
  **/
  @ApiModelProperty(value = "页面主键id")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public CopyOfPageInfoReq pageCode(String pageCode) {
    this.pageCode = pageCode;
    return this;
  }

  /**
   * 页面code
   * @return pageCode
  **/
  @ApiModelProperty(value = "页面code")
  
    public String getPageCode() {
    return pageCode;
  }

  public void setPageCode(String pageCode) {
    this.pageCode = pageCode;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CopyOfPageInfoReq copyOfPageInfoReq = (CopyOfPageInfoReq) o;
    return Objects.equals(this.wid, copyOfPageInfoReq.wid) &&
        Objects.equals(this.pageCode, copyOfPageInfoReq.pageCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, pageCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CopyOfPageInfoReq {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    pageCode: ").append(toIndentedString(pageCode)).append("\n");
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
