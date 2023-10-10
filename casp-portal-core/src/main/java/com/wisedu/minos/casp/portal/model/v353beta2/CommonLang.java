package com.wisedu.minos.casp.portal.model.v353beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 公共多语言返回对象
 */
@ApiModel(description = "公共多语言返回对象")
@Validated

public class CommonLang   {
  @JsonProperty("langKey")
  private String langKey = null;

  @JsonProperty("langCountry")
  private String langCountry = null;

  @JsonProperty("langValue")
  private String langValue = null;

  public CommonLang langKey(String langKey) {
    this.langKey = langKey;
    return this;
  }

  /**
   * 语言key
   * @return langKey
  **/
  @ApiModelProperty(value = "语言key")
  
    public String getLangKey() {
    return langKey;
  }

  public void setLangKey(String langKey) {
    this.langKey = langKey;
  }

  public CommonLang langCountry(String langCountry) {
    this.langCountry = langCountry;
    return this;
  }

  /**
   * 语言
   * @return langCountry
  **/
  @ApiModelProperty(value = "语言")
  
    public String getLangCountry() {
    return langCountry;
  }

  public void setLangCountry(String langCountry) {
    this.langCountry = langCountry;
  }

  public CommonLang langValue(String langValue) {
    this.langValue = langValue;
    return this;
  }

  /**
   * 值
   * @return langValue
  **/
  @ApiModelProperty(value = "值")
  
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
    CommonLang commonLang = (CommonLang) o;
    return Objects.equals(this.langKey, commonLang.langKey) &&
        Objects.equals(this.langCountry, commonLang.langCountry) &&
        Objects.equals(this.langValue, commonLang.langValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(langKey, langCountry, langValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommonLang {\n");
    
    sb.append("    langKey: ").append(toIndentedString(langKey)).append("\n");
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
