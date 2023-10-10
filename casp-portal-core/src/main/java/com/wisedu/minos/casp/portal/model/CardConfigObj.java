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
 * CardConfigObj
 */
@Validated

public class CardConfigObj   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("cardWid")
  private String cardWid = null;

  @JsonProperty("pageWid")
  private String pageWid = null;

  @JsonProperty("cardId")
  private String cardId = null;

  @JsonProperty("cardConfig")
  private String cardConfig = null;

  public CardConfigObj wid(String wid) {
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

  public CardConfigObj cardWid(String cardWid) {
    this.cardWid = cardWid;
    return this;
  }

  /**
   * 页面ID
   * @return cardWid
  **/
  @ApiModelProperty(value = "页面ID")
  
    public String getCardWid() {
    return cardWid;
  }

  public void setCardWid(String cardWid) {
    this.cardWid = cardWid;
  }

  public CardConfigObj pageWid(String pageWid) {
    this.pageWid = pageWid;
    return this;
  }

  /**
   * 卡片运行时ID
   * @return pageWid
  **/
  @ApiModelProperty(value = "卡片运行时ID")
  
    public String getPageWid() {
    return pageWid;
  }

  public void setPageWid(String pageWid) {
    this.pageWid = pageWid;
  }

  public CardConfigObj cardId(String cardId) {
    this.cardId = cardId;
    return this;
  }

  /**
   * 卡片ID
   * @return cardId
  **/
  @ApiModelProperty(value = "卡片ID")
  
    public String getCardId() {
    return cardId;
  }

  public void setCardId(String cardId) {
    this.cardId = cardId;
  }

  public CardConfigObj cardConfig(String cardConfig) {
    this.cardConfig = cardConfig;
    return this;
  }

  /**
   * 卡片配置
   * @return cardConfig
  **/
  @ApiModelProperty(value = "卡片配置")
  
    public String getCardConfig() {
    return cardConfig;
  }

  public void setCardConfig(String cardConfig) {
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
    CardConfigObj cardConfigObj = (CardConfigObj) o;
    return Objects.equals(this.wid, cardConfigObj.wid) &&
        Objects.equals(this.cardWid, cardConfigObj.cardWid) &&
        Objects.equals(this.pageWid, cardConfigObj.pageWid) &&
        Objects.equals(this.cardId, cardConfigObj.cardId) &&
        Objects.equals(this.cardConfig, cardConfigObj.cardConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, cardWid, pageWid, cardId, cardConfig);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CardConfigObj {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    cardWid: ").append(toIndentedString(cardWid)).append("\n");
    sb.append("    pageWid: ").append(toIndentedString(pageWid)).append("\n");
    sb.append("    cardId: ").append(toIndentedString(cardId)).append("\n");
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
