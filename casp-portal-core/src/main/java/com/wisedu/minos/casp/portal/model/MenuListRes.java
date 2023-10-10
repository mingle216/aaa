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
 * MenuListRes
 */
@Validated

public class MenuListRes   {
  @JsonProperty("programmeId")
  private String programmeId = null;

  @JsonProperty("parentId")
  private String parentId = null;

  public MenuListRes programmeId(String programmeId) {
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

  public MenuListRes parentId(String parentId) {
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
    MenuListRes menuListRes = (MenuListRes) o;
    return Objects.equals(this.programmeId, menuListRes.programmeId) &&
        Objects.equals(this.parentId, menuListRes.parentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(programmeId, parentId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MenuListRes {\n");
    
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
