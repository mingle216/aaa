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
 * UpdateStatusReq
 */
@Validated

public class UpdateStatusReq   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("platformType")
  private Integer platformType = null;

  public UpdateStatusReq wid(String wid) {
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

  public UpdateStatusReq platformType(Integer platformType) {
    this.platformType = platformType;
    return this;
  }

  /**
   * 适配终端0：PC1：移动端
   * @return platformType
  **/
  @ApiModelProperty(value = "适配终端0：PC1：移动端")
  
    public Integer getPlatformType() {
    return platformType;
  }

  public void setPlatformType(Integer platformType) {
    this.platformType = platformType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateStatusReq updateStatusReq = (UpdateStatusReq) o;
    return Objects.equals(this.wid, updateStatusReq.wid) &&
        Objects.equals(this.platformType, updateStatusReq.platformType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, platformType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateStatusReq {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    platformType: ").append(toIndentedString(platformType)).append("\n");
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
