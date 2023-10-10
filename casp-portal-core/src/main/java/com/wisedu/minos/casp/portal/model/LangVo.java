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
 * LangVo
 */
@Validated

public class LangVo   {
  @JsonProperty("langName")
  private String langName = null;

  @JsonProperty("langCname")
  private String langCname = null;

  @JsonProperty("langCode")
  private String langCode = null;

  @JsonProperty("isDefault")
  private Boolean isDefault = null;

  public LangVo langName(String langName) {
    this.langName = langName;
    return this;
  }

  /**
   * 语言名称
   * @return langName
  **/
  @ApiModelProperty(value = "语言名称")
  
    public String getLangName() {
    return langName;
  }

  public void setLangName(String langName) {
    this.langName = langName;
  }

  public LangVo langCode(String langCode) {
    this.langCode = langCode;
    return this;
  }



  /**
   * 语言中文名称
   * @return langCname
  **/
  @ApiModelProperty(value = "语言中文名称")
  public String getLangCname () {
    return langCname;
  }

  public void setLangCname (String langCname) {
    this.langCname = langCname;
  }



  /**
   * 语言编码
   * @return langCode
  **/
  @ApiModelProperty(value = "语言编码")
  
    public String getLangCode() {
    return langCode;
  }

  public void setLangCode(String langCode) {
    this.langCode = langCode;
  }

  public LangVo isDefault(Boolean isDefault) {
    this.isDefault = isDefault;
    return this;
  }

  /**
   * 是否默认
   * @return isDefault
  **/
  @ApiModelProperty(value = "是否默认")
  
    public Boolean isIsDefault() {
    return isDefault;
  }

  public void setIsDefault(Boolean isDefault) {
    this.isDefault = isDefault;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LangVo langVo = (LangVo) o;
    return Objects.equals(this.langName, langVo.langName) &&
            Objects.equals(this.langCname, langVo.langCname) &&
        Objects.equals(this.langCode, langVo.langCode) &&
        Objects.equals(this.isDefault, langVo.isDefault);
  }

  @Override
  public int hashCode() {
    return Objects.hash(langName,langCname,langCode, isDefault);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LangVo {\n");
    
    sb.append("    langName: ").append(toIndentedString(langName)).append("\n");
    sb.append("    langCname: ").append(toIndentedString(langCname)).append("\n");
    sb.append("    langCode: ").append(toIndentedString(langCode)).append("\n");
    sb.append("    isDefault: ").append(toIndentedString(isDefault)).append("\n");
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
