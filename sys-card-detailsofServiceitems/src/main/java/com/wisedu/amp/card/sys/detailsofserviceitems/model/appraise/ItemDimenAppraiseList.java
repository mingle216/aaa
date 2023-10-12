package com.wisedu.amp.card.sys.detailsofserviceitems.model.appraise;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ItemDimenAppraiseList
 */
@Validated

public class ItemDimenAppraiseList {
  @JsonProperty("createTime")
  private String createTime = null;

  @JsonProperty("isHidden")
  private Integer isHidden = null;

  @JsonProperty("appraiseList")
  @Valid
  private List<AppraiseScore> appraiseList = null;

  @JsonProperty("suggest")
  private String suggest = null;

  @JsonProperty("userName")
  private String userName = null;

  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("replyInfo")
  private AppraiseReplyDto replyInfo = null;

  @JsonProperty("appraisePics")
  private List<String> appraisePics;

  public ItemDimenAppraiseList createTime(String createTime) {
    this.createTime = createTime;
    return this;
  }

  /**
   * 评价时间
   * @return createTime
  **/
  @ApiModelProperty(value = "评价时间")
  
    public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public ItemDimenAppraiseList isHidden(Integer isHidden) {
    this.isHidden = isHidden;
    return this;
  }

  /**
   * Get isHidden
   * @return isHidden
  **/
  @ApiModelProperty(value = "")
  
    public Integer getIsHidden() {
    return isHidden;
  }

  public void setIsHidden(Integer isHidden) {
    this.isHidden = isHidden;
  }

  public ItemDimenAppraiseList appraiseList(List<AppraiseScore> appraiseList) {
    this.appraiseList = appraiseList;
    return this;
  }

  public ItemDimenAppraiseList addAppraiseListItem(AppraiseScore appraiseListItem) {
    if (this.appraiseList == null) {
      this.appraiseList = new ArrayList<AppraiseScore>();
    }
    this.appraiseList.add(appraiseListItem);
    return this;
  }

  /**
   * 事项维度评价数据
   * @return appraiseList
  **/
  @ApiModelProperty(value = "事项维度评价数据")
      @Valid
    public List<AppraiseScore> getAppraiseList() {
    return appraiseList;
  }

  public void setAppraiseList(List<AppraiseScore> appraiseList) {
    this.appraiseList = appraiseList;
  }

  public ItemDimenAppraiseList suggest(String suggest) {
    this.suggest = suggest;
    return this;
  }

  /**
   * 评价内容
   * @return suggest
  **/
  @ApiModelProperty(value = "评价内容")
  
    public String getSuggest() {
    return suggest;
  }

  public void setSuggest(String suggest) {
    this.suggest = suggest;
  }

  public ItemDimenAppraiseList userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * 评价人
   * @return userName
  **/
  @ApiModelProperty(value = "评价人")
  
    public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public ItemDimenAppraiseList wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 服务事项评价明细wid
   * @return wid
  **/
  @ApiModelProperty(value = "服务事项评价明细wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  /**
   * Get replyInfo
   * @return replyInfo
  **/
  @ApiModelProperty(value = "")

    @Valid
    public AppraiseReplyDto getReplyInfo() {
    return replyInfo;
  }

  public void setReplyInfo(AppraiseReplyDto replyInfo) {
    this.replyInfo = replyInfo;
  }

  public ItemDimenAppraiseList appraisePics(List<String> appraisePics) {
    this.appraisePics = appraisePics;
    return this;
  }

  /**
   * Get replyInfo
   * @return replyInfo
   **/
  @ApiModelProperty(value = "")

  @Valid
  public List<String> getAppraisePics() {
    return appraisePics;
  }

  public void setAppraisePics(List<String> appraisePics) {
    this.appraisePics = appraisePics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemDimenAppraiseList itemDimenAppraiseList = (ItemDimenAppraiseList) o;
    return Objects.equals(this.createTime, itemDimenAppraiseList.createTime) &&
        Objects.equals(this.isHidden, itemDimenAppraiseList.isHidden) &&
        Objects.equals(this.appraiseList, itemDimenAppraiseList.appraiseList) &&
        Objects.equals(this.suggest, itemDimenAppraiseList.suggest) &&
        Objects.equals(this.userName, itemDimenAppraiseList.userName) &&
        Objects.equals(this.wid, itemDimenAppraiseList.wid) &&
        Objects.equals(this.replyInfo, itemDimenAppraiseList.replyInfo) &&
        Objects.equals(this.appraisePics, itemDimenAppraiseList.appraisePics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(createTime, isHidden, appraiseList, suggest, userName, wid, replyInfo, appraisePics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemDimenAppraiseList {\n");
    
    sb.append("    createTime: ").append(toIndentedString(createTime)).append("\n");
    sb.append("    isHidden: ").append(toIndentedString(isHidden)).append("\n");
    sb.append("    appraiseList: ").append(toIndentedString(appraiseList)).append("\n");
    sb.append("    suggest: ").append(toIndentedString(suggest)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    replyInfo: ").append(toIndentedString(replyInfo)).append("\n");
    sb.append("    appraisePics: ").append(toIndentedString(appraisePics)).append("\n");
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
