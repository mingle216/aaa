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
 * VersionInfo
 */
@Validated

public class VersionInfo   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("versionNumber")
  private String versionNumber = null;

  @JsonProperty("foreignKey")
  private String foreignKey = null;

  @JsonProperty("updateTime")
  private String updateTime = null;

  public VersionInfo wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 主键
   * @return wid
  **/
  @ApiModelProperty(value = "主键")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public VersionInfo versionNumber(String versionNumber) {
    this.versionNumber = versionNumber;
    return this;
  }

  /**
   * 版本号
   * @return versionNumber
  **/
  @ApiModelProperty(value = "版本号")
  
    public String getVersionNumber() {
    return versionNumber;
  }

  public void setVersionNumber(String versionNumber) {
    this.versionNumber = versionNumber;
  }

  public VersionInfo foreignKey(String foreignKey) {
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

  public VersionInfo updateTime(String updateTime) {
    this.updateTime = updateTime;
    return this;
  }

  /**
   * 更新时间
   * @return updateTime
  **/
  @ApiModelProperty(value = "更新时间")
  
    public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VersionInfo versionInfo = (VersionInfo) o;
    return Objects.equals(this.wid, versionInfo.wid) &&
        Objects.equals(this.versionNumber, versionInfo.versionNumber) &&
        Objects.equals(this.foreignKey, versionInfo.foreignKey) &&
        Objects.equals(this.updateTime, versionInfo.updateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, versionNumber, foreignKey, updateTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VersionInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    versionNumber: ").append(toIndentedString(versionNumber)).append("\n");
    sb.append("    foreignKey: ").append(toIndentedString(foreignKey)).append("\n");
    sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
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
