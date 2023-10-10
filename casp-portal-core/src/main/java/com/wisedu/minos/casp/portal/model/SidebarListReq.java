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
 * SidebarListReq
 */
@Validated

public class SidebarListReq   {
  @JsonProperty("programmeId")
  private String programmeId = null;

  @JsonProperty("positionType")
  private Integer positionType = null;

  public SidebarListReq programmeId(String programmeId) {
    this.programmeId = programmeId;
    return this;
  }

  /**
   * 展示方案ID
   * @return programmeId
  **/
  @ApiModelProperty(value = "展示方案ID")
  
    public String getProgrammeId() {
    return programmeId;
  }

  public void setProgrammeId(String programmeId) {
    this.programmeId = programmeId;
  }

  public SidebarListReq positionType(Integer positionType) {
    this.positionType = positionType;
    return this;
  }

  /**
   * 方位0:(左侧),1:(右侧)
   * @return positionType
  **/
  @ApiModelProperty(value = "方位0:(左侧),1:(右侧)")
  
    public Integer getPositionType() {
    return positionType;
  }

  public void setPositionType(Integer positionType) {
    this.positionType = positionType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SidebarListReq sidebarListReq = (SidebarListReq) o;
    return Objects.equals(this.programmeId, sidebarListReq.programmeId) &&
        Objects.equals(this.positionType, sidebarListReq.positionType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(programmeId, positionType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SidebarListReq {\n");
    
    sb.append("    programmeId: ").append(toIndentedString(programmeId)).append("\n");
    sb.append("    positionType: ").append(toIndentedString(positionType)).append("\n");
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
