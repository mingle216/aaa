package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * 评价维度得分实体类
 */
@ApiModel(description = "评价维度得分实体类")
@Validated

public class AppraiseScore {
  @JsonProperty("dimenWid")
  private String dimenWid = null;

  @JsonProperty("dimenName")
  private String dimenName = null;

  @JsonProperty("dimenScore")
  private Integer dimenScore = null;

  public AppraiseScore dimenWid(String dimenWid) {
    this.dimenWid = dimenWid;
    return this;
  }

  /**
   * 维度wid
   * @return dimenWid
  **/
  @ApiModelProperty(value = "维度wid")
  
    public String getDimenWid() {
    return dimenWid;
  }

  public void setDimenWid(String dimenWid) {
    this.dimenWid = dimenWid;
  }

  public AppraiseScore dimenName(String dimenName) {
    this.dimenName = dimenName;
    return this;
  }

  /**
   * 维度名称
   * @return dimenName
  **/
  @ApiModelProperty(value = "维度名称")
  
    public String getDimenName() {
    return dimenName;
  }

  public void setDimenName(String dimenName) {
    this.dimenName = dimenName;
  }

  public AppraiseScore dimenScore(Integer dimenScore) {
    this.dimenScore = dimenScore;
    return this;
  }

  /**
   * 维度评价得分
   * @return dimenScore
  **/
  @ApiModelProperty(value = "维度评价得分")
  
    public Integer getDimenScore() {
    return dimenScore;
  }

  public void setDimenScore(Integer dimenScore) {
    this.dimenScore = dimenScore;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppraiseScore appraiseScore = (AppraiseScore) o;
    return Objects.equals(this.dimenWid, appraiseScore.dimenWid) &&
        Objects.equals(this.dimenName, appraiseScore.dimenName) &&
        Objects.equals(this.dimenScore, appraiseScore.dimenScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dimenWid, dimenName, dimenScore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppraiseScore {\n");
    
    sb.append("    dimenWid: ").append(toIndentedString(dimenWid)).append("\n");
    sb.append("    dimenName: ").append(toIndentedString(dimenName)).append("\n");
    sb.append("    dimenScore: ").append(toIndentedString(dimenScore)).append("\n");
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
