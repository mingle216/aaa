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
 * MenuNameLangBiz
 */
@Validated

public class MenuNameLangBiz   {
  @JsonProperty("sourceType")
  private String sourceType = null;

  @JsonProperty("langCountry")
  private String langCountry = null;

  @JsonProperty("langValue")
  private String langValue = null;

  public MenuNameLangBiz sourceType(String sourceType) {
    this.sourceType = sourceType;
    return this;
  }

  /**
   * 数据来源,0 系统 1 手工  默认值 0
   * @return sourceType
  **/
  @ApiModelProperty(value = "数据来源,0 系统 1 手工  默认值 0")
  
    public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  public MenuNameLangBiz langCountry(String langCountry) {
    this.langCountry = langCountry;
    return this;
  }

  /**
   * 语言类型（例zh_CN  、en_US等）
   * @return langCountry
  **/
  @ApiModelProperty(value = "语言类型（例zh_CN  、en_US等）")
  
    public String getLangCountry() {
    return langCountry;
  }

  public void setLangCountry(String langCountry) {
    this.langCountry = langCountry;
  }

  public MenuNameLangBiz langValue(String langValue) {
    this.langValue = langValue;
    return this;
  }

  /**
   * 属性值
   * @return langValue
  **/
  @ApiModelProperty(value = "属性值")
  
    public String getLangValue() {
    return langValue;
  }

  public void setLangValue(String langValue) {
    this.langValue = langValue;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MenuNameLangBiz menuNameLangBiz = (MenuNameLangBiz) o;
    return Objects.equals(this.sourceType, menuNameLangBiz.sourceType) &&
        Objects.equals(this.langCountry, menuNameLangBiz.langCountry) &&
        Objects.equals(this.langValue, menuNameLangBiz.langValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceType, langCountry, langValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MenuNameLangBiz {\n");
    
    sb.append("    sourceType: ").append(toIndentedString(sourceType)).append("\n");
    sb.append("    langCountry: ").append(toIndentedString(langCountry)).append("\n");
    sb.append("    langValue: ").append(toIndentedString(langValue)).append("\n");
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
