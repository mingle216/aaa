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
 * ProgrammeListReq
 */
@Validated

public class ProgrammeListReq   {
  @JsonProperty("platformType")
  private Integer platformType = null;

  @JsonProperty("siteWid")
  private String siteWid = null;

  public ProgrammeListReq platformType(Integer platformType) {
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

  public ProgrammeListReq siteWid(String siteWid) {
    this.siteWid = siteWid;
    return this;
  }

  /**
   * 站点wid
   * @return siteWid
  **/
  @ApiModelProperty(value = "站点wid")
  
    public String getSiteWid() {
    return siteWid;
  }

  public void setSiteWid(String siteWid) {
    this.siteWid = siteWid;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgrammeListReq programmeListReq = (ProgrammeListReq) o;
    return Objects.equals(this.platformType, programmeListReq.platformType) &&
        Objects.equals(this.siteWid, programmeListReq.siteWid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(platformType, siteWid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProgrammeListReq {\n");
    
    sb.append("    platformType: ").append(toIndentedString(platformType)).append("\n");
    sb.append("    siteWid: ").append(toIndentedString(siteWid)).append("\n");
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
