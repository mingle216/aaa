package com.wisedu.amp.card.cuscuc.detailsofserviceitems.model.appraise;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 评价维度得分实体类
 */
@ApiModel(description = "评价维度得分实体类")
@Validated

public class AppraiseDetail   {
  @JsonProperty("score")
  @Valid
  private List<AppraiseScore> score = null;

  public AppraiseDetail score(List<AppraiseScore> score) {
    this.score = score;
    return this;
  }

  public AppraiseDetail addScoreItem(AppraiseScore scoreItem) {
    if (this.score == null) {
      this.score = new ArrayList<AppraiseScore>();
    }
    this.score.add(scoreItem);
    return this;
  }

  /**
   * 维度得分
   * @return score
  **/
  @ApiModelProperty(value = "维度得分")
      @Valid
    public List<AppraiseScore> getScore() {
    return score;
  }

  public void setScore(List<AppraiseScore> score) {
    this.score = score;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppraiseDetail appraiseDetail = (AppraiseDetail) o;
    return Objects.equals(this.score, appraiseDetail.score);
  }

  @Override
  public int hashCode() {
    return Objects.hash(score);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppraiseDetail {\n");
    
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
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
