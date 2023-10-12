package com.wisedu.amp.card.sys.cqdetail.model.appraise;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * AppraiseReplyDto
 */
@Validated

public class AppraiseReplyDto {
  @JsonProperty("replyName")
  private String replyName = null;

  @JsonProperty("content")
  private String content = null;

  @JsonProperty("replyTime")
  private String replyTime = null;

  public AppraiseReplyDto replyName(String replyName) {
    this.replyName = replyName;
    return this;
  }

  /**
   * 回复人姓名
   * @return replyName
  **/
  @ApiModelProperty(value = "回复人姓名")
  
    public String getReplyName() {
    return replyName;
  }

  public void setReplyName(String replyName) {
    this.replyName = replyName;
  }

  public AppraiseReplyDto content(String content) {
    this.content = content;
    return this;
  }

  /**
   * 回复内容
   * @return content
  **/
  @ApiModelProperty(value = "回复内容")
  
    public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public AppraiseReplyDto replyTime(String replyTime) {
    this.replyTime = replyTime;
    return this;
  }

  /**
   * 回复时间
   * @return replyTime
  **/
  @ApiModelProperty(value = "回复时间")
  
    public String getReplyTime() {
    return replyTime;
  }

  public void setReplyTime(String replyTime) {
    this.replyTime = replyTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppraiseReplyDto appraiseReplyDto = (AppraiseReplyDto) o;
    return Objects.equals(this.replyName, appraiseReplyDto.replyName) &&
        Objects.equals(this.content, appraiseReplyDto.content) &&
        Objects.equals(this.replyTime, appraiseReplyDto.replyTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(replyName, content, replyTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppraiseReplyDto {\n");
    
    sb.append("    replyName: ").append(toIndentedString(replyName)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    replyTime: ").append(toIndentedString(replyTime)).append("\n");
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
