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
 * PreviewPageResponse
 */
@Validated

public class PreviewPageResponse   {
  @JsonProperty("resultHtml")
  private String resultHtml = null;

  public PreviewPageResponse resultHtml(String resultHtml) {
    this.resultHtml = resultHtml;
    return this;
  }

  /**
   * html字符串
   * @return resultHtml
  **/
  @ApiModelProperty(value = "html字符串")
  
    public String getResultHtml() {
    return resultHtml;
  }

  public void setResultHtml(String resultHtml) {
    this.resultHtml = resultHtml;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreviewPageResponse previewPageResponse = (PreviewPageResponse) o;
    return Objects.equals(this.resultHtml, previewPageResponse.resultHtml);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resultHtml);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreviewPageResponse {\n");
    
    sb.append("    resultHtml: ").append(toIndentedString(resultHtml)).append("\n");
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
