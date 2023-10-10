package com.wisedu.minos.casp.portal.model.v353beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 下拉菜单启停用
 */
@ApiModel(description = "下拉菜单启停用")
@Validated

public class EditStatusReq   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("programmeWid")
  private String programmeWid = null;

  @JsonProperty("isEnabled")
  private Integer isEnabled = null;

  public EditStatusReq wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * wid
   * @return wid
  **/
  @ApiModelProperty(value = "wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public EditStatusReq programmeWid(String programmeWid) {
    this.programmeWid = programmeWid;
    return this;
  }

  /**
   * 展示方案wid
   * @return programmeWid
  **/
  @ApiModelProperty(value = "展示方案wid")
  
    public String getProgrammeWid() {
    return programmeWid;
  }

  public void setProgrammeWid(String programmeWid) {
    this.programmeWid = programmeWid;
  }

  public EditStatusReq isEnabled(Integer isEnabled) {
    this.isEnabled = isEnabled;
    return this;
  }

  /**
   * 是否启用 0:停用 1:启用
   * @return isEnabled
  **/
  @ApiModelProperty(value = "是否启用 0:停用 1:启用")
  
    public Integer getIsEnabled() {
    return isEnabled;
  }

  public void setIsEnabled(Integer isEnabled) {
    this.isEnabled = isEnabled;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EditStatusReq editStatusReq = (EditStatusReq) o;
    return Objects.equals(this.wid, editStatusReq.wid) &&
        Objects.equals(this.programmeWid, editStatusReq.programmeWid) &&
        Objects.equals(this.isEnabled, editStatusReq.isEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, programmeWid, isEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EditStatusReq {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    programmeWid: ").append(toIndentedString(programmeWid)).append("\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
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
