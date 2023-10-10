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
 * AllSidebarStatusReq
 */
@Validated

public class AllSidebarStatusReq   {
  @JsonProperty("type")
  private Integer type = null;

  @JsonProperty("enabledFlag")
  private Integer enabledFlag = null;

  public AllSidebarStatusReq type(Integer type) {
    this.type = type;
    return this;
  }

  /**
   * 类型0:(左侧),1:(右侧)
   * @return type
  **/
  @ApiModelProperty(value = "类型0:(左侧),1:(右侧)")
  
    public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public AllSidebarStatusReq enabledFlag(Integer enabledFlag) {
    this.enabledFlag = enabledFlag;
    return this;
  }

  /**
   * 0不启用1启用
   * @return enabledFlag
  **/
  @ApiModelProperty(value = "0不启用1启用")
  
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
    AllSidebarStatusReq allSidebarStatusReq = (AllSidebarStatusReq) o;
    return Objects.equals(this.type, allSidebarStatusReq.type) &&
        Objects.equals(this.enabledFlag, allSidebarStatusReq.enabledFlag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, enabledFlag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllSidebarStatusReq {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
