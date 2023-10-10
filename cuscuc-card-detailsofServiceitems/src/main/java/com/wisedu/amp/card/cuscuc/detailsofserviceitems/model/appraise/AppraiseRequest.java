package com.wisedu.amp.card.cuscuc.detailsofserviceitems.model.appraise;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 事项评价实体
 */
@ApiModel(description = "事项评价实体")
@Validated

public class AppraiseRequest   {
  @JsonProperty("appraiseDetail")
  private AppraiseDetail appraiseDetail = null;

  @JsonProperty("userId")
  private String userId = null;

  @JsonProperty("userName")
  private String userName = null;

  @JsonProperty("serviceWid")
  private String serviceWid = null;

  @JsonProperty("suggest")
  private String suggest = null;

  public AppraiseRequest appraiseDetail(AppraiseDetail appraiseDetail) {
    this.appraiseDetail = appraiseDetail;
    return this;
  }

  /**
   * Get appraiseDetails
   * @return appraiseDetails
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public AppraiseDetail getAppraiseDetails() {
    return appraiseDetail;
  }

  public void setAppraiseDetails(AppraiseDetail appraiseDetail) {
    this.appraiseDetail = appraiseDetail;
  }

  public AppraiseRequest userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * 用户id
   * @return userId
  **/
  @ApiModelProperty(value = "用户id")
  
    public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public AppraiseRequest userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * 评价人姓名
   * @return userName
  **/
  @ApiModelProperty(value = "评价人姓名")
  
    public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public AppraiseRequest serviceWid(String serviceWid) {
    this.serviceWid = serviceWid;
    return this;
  }

  /**
   * 服务wid
   * @return serviceWid
  **/
  @ApiModelProperty(value = "服务wid")
  
    public String getServiceWid() {
    return serviceWid;
  }

  public void setServiceWid(String serviceWid) {
    this.serviceWid = serviceWid;
  }

  public AppraiseRequest suggest(String suggest) {
    this.suggest = suggest;
    return this;
  }

  /**
   * 建议
   * @return suggest
  **/
  @ApiModelProperty(value = "建议")
  
    public String getSuggest() {
    return suggest;
  }

  public void setSuggest(String suggest) {
    this.suggest = suggest;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppraiseRequest appraiseRequest = (AppraiseRequest) o;
    return Objects.equals(this.appraiseDetail, appraiseRequest.appraiseDetail) &&
        Objects.equals(this.userId, appraiseRequest.userId) &&
        Objects.equals(this.userName, appraiseRequest.userName) &&
        Objects.equals(this.serviceWid, appraiseRequest.serviceWid) &&
        Objects.equals(this.suggest, appraiseRequest.suggest);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appraiseDetail, userId, userName, serviceWid, suggest);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppraiseRequest {\n");
    
    sb.append("    appraiseDetails: ").append(toIndentedString(appraiseDetail)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    serviceWid: ").append(toIndentedString(serviceWid)).append("\n");
    sb.append("    suggest: ").append(toIndentedString(suggest)).append("\n");
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
