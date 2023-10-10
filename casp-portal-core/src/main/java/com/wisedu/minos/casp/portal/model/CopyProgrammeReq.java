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
 * CopyProgrammeReq
 */
@Validated

public class CopyProgrammeReq   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("siteWid")
  private String siteWid = null;

  @JsonProperty("programmeName")
  private String programmeName = null;

  public CopyProgrammeReq wid(String wid) {
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

  public CopyProgrammeReq siteWid(String siteWid) {
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

  public CopyProgrammeReq programmeName(String programmeName) {
    this.programmeName = programmeName;
    return this;
  }

  /**
   * 展示方案名称
   * @return programmeName
  **/
  @ApiModelProperty(value = "展示方案名称")
  
    public String getProgrammeName() {
    return programmeName;
  }

  public void setProgrammeName(String programmeName) {
    this.programmeName = programmeName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CopyProgrammeReq copyProgrammeReq = (CopyProgrammeReq) o;
    return Objects.equals(this.wid, copyProgrammeReq.wid) &&
        Objects.equals(this.siteWid, copyProgrammeReq.siteWid) &&
        Objects.equals(this.programmeName, copyProgrammeReq.programmeName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, siteWid, programmeName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CopyProgrammeReq {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    siteWid: ").append(toIndentedString(siteWid)).append("\n");
    sb.append("    programmeName: ").append(toIndentedString(programmeName)).append("\n");
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
