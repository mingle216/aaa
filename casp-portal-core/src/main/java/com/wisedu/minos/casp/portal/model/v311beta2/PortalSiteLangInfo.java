package com.wisedu.minos.casp.portal.model.v311beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 站点多语言
 */
@ApiModel(description = "站点多语言")
@Validated

public class PortalSiteLangInfo   {
  @JsonProperty("langCountry")
  private String langCountry = null;

  @JsonProperty("langValue")
  private String langValue = null;

  @JsonProperty("sourceType")
  private Integer sourceType = null;

  public PortalSiteLangInfo langCountry(String langCountry) {
    this.langCountry = langCountry;
    return this;
  }

  /**
   * Get langCountry
   * @return langCountry
  **/
  @ApiModelProperty(value = "")
  
    public String getLangCountry() {
    return langCountry;
  }

  public void setLangCountry(String langCountry) {
    this.langCountry = langCountry;
  }

  public PortalSiteLangInfo langValue(String langValue) {
    this.langValue = langValue;
    return this;
  }

  /**
   * Get langValue
   * @return langValue
  **/
  @ApiModelProperty(value = "")
  
    public String getLangValue() {
    return langValue;
  }

  public void setLangValue(String langValue) {
    this.langValue = langValue;
  }

  public PortalSiteLangInfo sourceType(Integer sourceType) {
    this.sourceType = sourceType;
    return this;
  }

  /**
   * 数据来源,0 系统 1 手工  传入1即可
   * @return sourceType
  **/
  @ApiModelProperty(value = "数据来源,0 系统 1 手工  传入1即可")
  
    public Integer getSourceType() {
    return sourceType;
  }

  public void setSourceType(Integer sourceType) {
    this.sourceType = sourceType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortalSiteLangInfo portalSiteLangInfo = (PortalSiteLangInfo) o;
    return Objects.equals(this.langCountry, portalSiteLangInfo.langCountry) &&
        Objects.equals(this.langValue, portalSiteLangInfo.langValue) &&
        Objects.equals(this.sourceType, portalSiteLangInfo.sourceType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(langCountry, langValue, sourceType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortalSiteLangInfo {\n");
    
    sb.append("    langCountry: ").append(toIndentedString(langCountry)).append("\n");
    sb.append("    langValue: ").append(toIndentedString(langValue)).append("\n");
    sb.append("    sourceType: ").append(toIndentedString(sourceType)).append("\n");
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
