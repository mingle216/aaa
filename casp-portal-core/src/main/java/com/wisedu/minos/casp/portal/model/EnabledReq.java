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
 * EnabledReq
 */
@Validated

public class EnabledReq   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("enabledFlag")
  private Integer enabledFlag = null;

  public EnabledReq wid(String wid) {
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

  public EnabledReq enabledFlag(Integer enabledFlag) {
    this.enabledFlag = enabledFlag;
    return this;
  }

  /**
   * 是否启用（0：否；1：是）
   * @return enabledFlag
  **/
  @ApiModelProperty(value = "是否启用（0：否；1：是）")
  
    public Integer getEnabledFlag() {
    return enabledFlag;
  }

  public void setEnabledFlag(Integer enabledFlag) {
    this.enabledFlag = enabledFlag;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnabledReq enabledReq = (EnabledReq) o;
    return Objects.equals(this.wid, enabledReq.wid) &&
        Objects.equals(this.enabledFlag, enabledReq.enabledFlag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, enabledFlag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnabledReq {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    enabledFlag: ").append(toIndentedString(enabledFlag)).append("\n");
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
