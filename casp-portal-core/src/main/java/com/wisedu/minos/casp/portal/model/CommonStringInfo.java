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
 * CommonStringInfo
 */
@Validated

public class CommonStringInfo   {
  @JsonProperty("dataInfo")
  private String dataInfo = null;

  public CommonStringInfo dataInfo(String dataInfo) {
    this.dataInfo = dataInfo;
    return this;
  }

  /**
   * 统一String属性字段
   * @return dataInfo
  **/
  @ApiModelProperty(value = "统一String属性字段")
  
    public String getDataInfo() {
    return dataInfo;
  }

  public void setDataInfo(String dataInfo) {
    this.dataInfo = dataInfo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommonStringInfo commonStringInfo = (CommonStringInfo) o;
    return Objects.equals(this.dataInfo, commonStringInfo.dataInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommonStringInfo {\n");
    
    sb.append("    dataInfo: ").append(toIndentedString(dataInfo)).append("\n");
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
