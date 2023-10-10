package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

/**
 * PopupWindowReq
 */
@Validated

public class PopupWindowReq {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("popTitle")
  private List<MultiLangData> popTitle = null;

  @JsonProperty("pageWid")
  private String pageWid= null;

  @JsonProperty("titleLangKey")
  private String titleLangKey= null;

  @JsonProperty("contentsLangKey")
  private String contentsLangKey= null;

  @JsonProperty("popContents")
  private List<MultiLangData> popContents=null;


  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonProperty("begTime")
  private LocalDateTime begTime=null;


  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonProperty("endTime")
  private LocalDateTime endTime=null;

  @JsonProperty("popType")
  private int popType=0;

  @JsonProperty("isEnabled")
  private int isEnabled=0;

  public PopupWindowReq wid(String wid) {
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

  public PopupWindowReq popTitle(List<MultiLangData> popTitle) {
    this.popTitle = popTitle;
    return this;
  }

  @ApiModelProperty(value = "")
  public List<MultiLangData> getPopTitle() {
    return popTitle;
  }

  public void setPopTitle(List<MultiLangData> popTitle) {
    this.popTitle = popTitle;
  }

  public PopupWindowReq pageWid(String pageWid) {
    this.pageWid = pageWid;
    return this;
  }

  @ApiModelProperty(value = "")
  public String getPageWid() {
    return pageWid;
  }

  public void setPageWid(String pageWid) {
    this.pageWid = pageWid;
  }


  public PopupWindowReq popContents(List<MultiLangData> popContents) {
    this.popContents = popContents;
    return this;
  }

  @ApiModelProperty(value = "")
  public  List<MultiLangData> getPopContents() {
    return popContents;
  }

  public void setPopContents(List<MultiLangData> popContents) {
    this.popContents = popContents;
  }


  public PopupWindowReq begTime(LocalDateTime begTime) {
    this.begTime = begTime;
    return this;
  }

  @ApiModelProperty(value = "")
  public LocalDateTime getBegTime() {
    return begTime;
  }

  public void setBegTime(LocalDateTime begTime) {
    this.begTime = begTime;
  }

  public PopupWindowReq endTime(LocalDateTime endTime) {
    this.endTime = endTime;
    return this;
  }

  @ApiModelProperty(value = "")
  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public PopupWindowReq popType(int popType) {
    this.popType = popType;
    return this;
  }

  @ApiModelProperty(value = "")
  public int getPopType() {
    return popType;
  }

  public void setPopType(int popType) {
    this.popType = popType;
  }

  public PopupWindowReq isEnabled(int isEnabled) {
    this.isEnabled = isEnabled;
    return this;
  }

  @ApiModelProperty(value = "")
  public int getIsEnabled() {
    return isEnabled;
  }

  public void setIsEnabled(int isEnabled) {
    this.isEnabled = isEnabled;
  }

  public PopupWindowReq contentsLangKey(String contentsLangKey) {
    this.contentsLangKey = contentsLangKey;
    return this;
  }

  @ApiModelProperty(value = "")
  public String getContentsLangKey() {
    return contentsLangKey;
  }

  public void setContentsLangKey(String contentsLangKey) {
    this.contentsLangKey = contentsLangKey;
  }


  public PopupWindowReq titleLangKey(String titleLangKey) {
    this.titleLangKey = titleLangKey;
    return this;
  }

  @ApiModelProperty(value = "")
  public String getTitleLangKey() {
    return titleLangKey;
  }

  public void setTitleLangKey(String titleLangKey) {
    this.titleLangKey = titleLangKey;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PopupWindowReq popupWindowReq = (PopupWindowReq) o;
    return Objects.equals(this.wid, popupWindowReq.wid) &&
        Objects.equals(this.popTitle, popupWindowReq.popTitle)&&
        Objects.equals(this.pageWid, popupWindowReq.pageWid)&&
        Objects.equals(this.begTime, popupWindowReq.begTime)&&
        Objects.equals(this.endTime, popupWindowReq.endTime)&&
        Objects.equals(this.popType, popupWindowReq.popType)&&
        Objects.equals(this.isEnabled, popupWindowReq.isEnabled)&&
        Objects.equals(this.titleLangKey, popupWindowReq.titleLangKey)&&
    Objects.equals(this.contentsLangKey, popupWindowReq.contentsLangKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, popTitle,pageWid,begTime,endTime,popType,isEnabled,popContents,titleLangKey,contentsLangKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class popupWindowReq {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    popTitle: ").append(toIndentedString(popTitle)).append("\n");
    sb.append("    pageWid: ").append(toIndentedString(pageWid)).append("\n");
    sb.append("    begTime: ").append(toIndentedString(begTime)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    popType: ").append(toIndentedString(popType)).append("\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
    sb.append("    popContents: ").append(toIndentedString(popContents)).append("\n");
    sb.append("    titleLangKey: ").append(toIndentedString(titleLangKey)).append("\n");
    sb.append("    contentsLangKey: ").append(toIndentedString(contentsLangKey)).append("\n");
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
