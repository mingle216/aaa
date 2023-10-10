package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.CardConfigObj;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PreviewPageReq
 */
@Validated

public class PreviewPageReq   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("cardLayout")
  private String cardLayout = null;

  @JsonProperty("cardConfig")
  @Valid
  private List<CardConfigObj> cardConfig = null;

  public PreviewPageReq wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 主键Wid
   * @return wid
  **/
  @ApiModelProperty(value = "主键Wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public PreviewPageReq cardLayout(String cardLayout) {
    this.cardLayout = cardLayout;
    return this;
  }

  /**
   * 页面布局信息：例子单独发给前端人员
   * @return cardLayout
  **/
  @ApiModelProperty(value = "页面布局信息：例子单独发给前端人员")
  
    public String getCardLayout() {
    return cardLayout;
  }

  public void setCardLayout(String cardLayout) {
    this.cardLayout = cardLayout;
  }

  public PreviewPageReq cardConfig(List<CardConfigObj> cardConfig) {
    this.cardConfig = cardConfig;
    return this;
  }

  public PreviewPageReq addCardConfigItem(CardConfigObj cardConfigItem) {
    if (this.cardConfig == null) {
      this.cardConfig = new ArrayList<CardConfigObj>();
    }
    this.cardConfig.add(cardConfigItem);
    return this;
  }

  /**
   * 卡片运行时配置
   * @return cardConfig
  **/
  @ApiModelProperty(value = "卡片运行时配置")
      @Valid
    public List<CardConfigObj> getCardConfig() {
    return cardConfig;
  }

  public void setCardConfig(List<CardConfigObj> cardConfig) {
    this.cardConfig = cardConfig;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreviewPageReq previewPageReq = (PreviewPageReq) o;
    return Objects.equals(this.wid, previewPageReq.wid) &&
        Objects.equals(this.cardLayout, previewPageReq.cardLayout) &&
        Objects.equals(this.cardConfig, previewPageReq.cardConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, cardLayout, cardConfig);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreviewPageReq {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    cardLayout: ").append(toIndentedString(cardLayout)).append("\n");
    sb.append("    cardConfig: ").append(toIndentedString(cardConfig)).append("\n");
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
