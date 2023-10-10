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
 * SupportProgrammeResp
 */
@Validated

public class SupportProgrammeResp   {
  @JsonProperty("portalPC")
  private Boolean portalPC = null;

  @JsonProperty("portalH5")
  private Boolean portalH5 = null;

  public SupportProgrammeResp portalPC(Boolean portalPC) {
    this.portalPC = portalPC;
    return this;
  }

  /**
   * 是否支持PC端
   * @return portalPC
  **/
  @ApiModelProperty(value = "是否支持PC端")
  
    public Boolean isPortalPC() {
    return portalPC;
  }

  public void setPortalPC(Boolean portalPC) {
    this.portalPC = portalPC;
  }

  public SupportProgrammeResp portalH5(Boolean portalH5) {
    this.portalH5 = portalH5;
    return this;
  }

  /**
   * Get portalH5
   * @return portalH5
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isPortalH5() {
    return portalH5;
  }

  public void setPortalH5(Boolean portalH5) {
    this.portalH5 = portalH5;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SupportProgrammeResp supportProgrammeResp = (SupportProgrammeResp) o;
    return Objects.equals(this.portalPC, supportProgrammeResp.portalPC) &&
        Objects.equals(this.portalH5, supportProgrammeResp.portalH5);
  }

  @Override
  public int hashCode() {
    return Objects.hash(portalPC, portalH5);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SupportProgrammeResp {\n");
    
    sb.append("    portalPC: ").append(toIndentedString(portalPC)).append("\n");
    sb.append("    portalH5: ").append(toIndentedString(portalH5)).append("\n");
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
