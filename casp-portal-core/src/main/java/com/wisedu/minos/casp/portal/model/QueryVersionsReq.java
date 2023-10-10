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
 * QueryVersionsReq
 */
@Validated

public class QueryVersionsReq   {
  @JsonProperty("foreignKey")
  private String foreignKey = null;

  @JsonProperty("versionType")
  private String versionType = null;

  @JsonProperty("versionNumber")
  private String versionNumber = null;

  public QueryVersionsReq foreignKey(String foreignKey) {
    this.foreignKey = foreignKey;
    return this;
  }

  /**
   * 模板/卡片id
   * @return foreignKey
  **/
  @ApiModelProperty(value = "模板/卡片id")
  
    public String getForeignKey() {
    return foreignKey;
  }

  public void setForeignKey(String foreignKey) {
    this.foreignKey = foreignKey;
  }

  public QueryVersionsReq versionType(String versionType) {
    this.versionType = versionType;
    return this;
  }

  /**
   * 类型0：模板1：卡片
   * @return versionType
  **/
  @ApiModelProperty(value = "类型0：模板1：卡片")
  
    public String getVersionType() {
    return versionType;
  }

  public void setVersionType(String versionType) {
    this.versionType = versionType;
  }

  public QueryVersionsReq versionNumber(String versionNumber) {
    this.versionNumber = versionNumber;
    return this;
  }

  /**
   * 当前版本号
   * @return versionNumber
  **/
  @ApiModelProperty(value = "当前版本号")
  
    public String getVersionNumber() {
    return versionNumber;
  }

  public void setVersionNumber(String versionNumber) {
    this.versionNumber = versionNumber;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueryVersionsReq queryVersionsReq = (QueryVersionsReq) o;
    return Objects.equals(this.foreignKey, queryVersionsReq.foreignKey) &&
        Objects.equals(this.versionType, queryVersionsReq.versionType) &&
        Objects.equals(this.versionNumber, queryVersionsReq.versionNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(foreignKey, versionType, versionNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryVersionsReq {\n");
    
    sb.append("    foreignKey: ").append(toIndentedString(foreignKey)).append("\n");
    sb.append("    versionType: ").append(toIndentedString(versionType)).append("\n");
    sb.append("    versionNumber: ").append(toIndentedString(versionNumber)).append("\n");
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
