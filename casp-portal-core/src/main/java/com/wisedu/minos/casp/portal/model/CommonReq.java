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
 * CommonReq
 */
@Validated

public class CommonReq   {
  @JsonProperty("wid")
  private String wid = null;

  public CommonReq wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 主键Wid
   * @return wid
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommonReq commonReq = (CommonReq) o;
    return Objects.equals(this.wid, commonReq.wid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommonReq {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
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
