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
 * QueryProgrammeListReq
 */
@Validated

public class QueryProgrammeListReq   {
  @JsonProperty("platformType")
  private Integer platformType = null;

  public QueryProgrammeListReq platformType(Integer platformType) {
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
    QueryProgrammeListReq queryProgrammeListReq = (QueryProgrammeListReq) o;
    return Objects.equals(this.platformType, queryProgrammeListReq.platformType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(platformType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryProgrammeListReq {\n");
    
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
