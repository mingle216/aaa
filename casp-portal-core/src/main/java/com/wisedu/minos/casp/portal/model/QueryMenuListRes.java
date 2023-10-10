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
 * QueryMenuListRes
 */
@Validated

public class QueryMenuListRes   {
  @JsonProperty("programmeId")
  private String programmeId = null;

  @JsonProperty("parentId")
  private String parentId = null;

  public QueryMenuListRes programmeId(String programmeId) {
    this.programmeId = programmeId;
    return this;
  }

  /**
   * 展示方案id
   * @return programmeId
  **/
  @ApiModelProperty(value = "展示方案id")
  
    public String getProgrammeId() {
    return programmeId;
  }

  public void setProgrammeId(String programmeId) {
    this.programmeId = programmeId;
  }

  public QueryMenuListRes parentId(String parentId) {
    this.parentId = parentId;
    return this;
  }

  /**
   * 父级菜单id
   * @return parentId
  **/
  @ApiModelProperty(value = "父级菜单id")
  
    public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueryMenuListRes queryMenuListRes = (QueryMenuListRes) o;
    return Objects.equals(this.programmeId, queryMenuListRes.programmeId) &&
        Objects.equals(this.parentId, queryMenuListRes.parentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(programmeId, parentId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryMenuListRes {\n");
    
    sb.append("    programmeId: ").append(toIndentedString(programmeId)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
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
