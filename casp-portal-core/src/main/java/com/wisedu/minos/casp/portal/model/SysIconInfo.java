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
 * SysIconInfo
 */
@Validated

public class SysIconInfo   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("iconName")
  private String iconName = null;

  @JsonProperty("iconType")
  private String iconType = null;

  @JsonProperty("iconDesc")
  private String iconDesc = null;

  @JsonProperty("iconUrl")
  private String iconUrl = null;

  public SysIconInfo wid(String wid) {
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

  public SysIconInfo iconName(String iconName) {
    this.iconName = iconName;
    return this;
  }

  /**
   * 图标名称
   * @return iconName
  **/
  @ApiModelProperty(value = "图标名称")
  
    public String getIconName() {
    return iconName;
  }

  public void setIconName(String iconName) {
    this.iconName = iconName;
  }

  public SysIconInfo iconType(String iconType) {
    this.iconType = iconType;
    return this;
  }

  /**
   * 图标类型  0 字体图标  1 png图标
   * @return iconType
  **/
  @ApiModelProperty(value = "图标类型  0 字体图标  1 png图标")
  
    public String getIconType() {
    return iconType;
  }

  public void setIconType(String iconType) {
    this.iconType = iconType;
  }

  public SysIconInfo iconDesc(String iconDesc) {
    this.iconDesc = iconDesc;
    return this;
  }

  /**
   * 图标描述
   * @return iconDesc
  **/
  @ApiModelProperty(value = "图标描述")
  
    public String getIconDesc() {
    return iconDesc;
  }

  public void setIconDesc(String iconDesc) {
    this.iconDesc = iconDesc;
  }

  public SysIconInfo iconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
    return this;
  }

  /**
   * 图标地址
   * @return iconUrl
  **/
  @ApiModelProperty(value = "图标地址")
  
    public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SysIconInfo sysIconInfo = (SysIconInfo) o;
    return Objects.equals(this.wid, sysIconInfo.wid) &&
        Objects.equals(this.iconName, sysIconInfo.iconName) &&
        Objects.equals(this.iconType, sysIconInfo.iconType) &&
        Objects.equals(this.iconDesc, sysIconInfo.iconDesc) &&
        Objects.equals(this.iconUrl, sysIconInfo.iconUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, iconName, iconType, iconDesc, iconUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SysIconInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    iconName: ").append(toIndentedString(iconName)).append("\n");
    sb.append("    iconType: ").append(toIndentedString(iconType)).append("\n");
    sb.append("    iconDesc: ").append(toIndentedString(iconDesc)).append("\n");
    sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
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
