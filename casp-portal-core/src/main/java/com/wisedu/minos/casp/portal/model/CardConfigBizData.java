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
 * CardConfigBizData
 */
@Validated

public class CardConfigBizData   {
  @JsonProperty("configHtml")
  private String configHtml = null;

  public CardConfigBizData configHtml(String configHtml) {
    this.configHtml = configHtml;
    return this;
  }

  /**
   * 卡片配置页面HTML，前端通过该HTML进行页面渲染
   * @return configHtml
  **/
  @ApiModelProperty(value = "卡片配置页面HTML，前端通过该HTML进行页面渲染")
  
    public String getConfigHtml() {
    return configHtml;
  }

  public void setConfigHtml(String configHtml) {
    this.configHtml = configHtml;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CardConfigBizData cardConfigBizData = (CardConfigBizData) o;
    return Objects.equals(this.configHtml, cardConfigBizData.configHtml);
  }

  @Override
  public int hashCode() {
    return Objects.hash(configHtml);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CardConfigBizData {\n");
    
    sb.append("    configHtml: ").append(toIndentedString(configHtml)).append("\n");
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
