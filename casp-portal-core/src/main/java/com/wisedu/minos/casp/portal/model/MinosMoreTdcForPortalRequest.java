package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * 更多任务请求
 */
@ApiModel(description = "更多任务请求")
@Validated

public class MinosMoreTdcForPortalRequest {
  @JsonProperty("pageNumber")
  private Integer pageNumber = null;

  @JsonProperty("pageSize")
  private Integer pageSize = null;

  @JsonProperty("userId")
  private String userId = null;

  @JsonProperty("userWid")
  private String userWid = null;

  @JsonProperty("searchKey")
  private String searchKey = null;

  @JsonProperty("lang")
  private String lang = null;

  @JsonProperty("serviceStation")
  private Integer serviceStation = null;

  @JsonProperty("sortType")
  private Integer sortType = null;

  public MinosMoreTdcForPortalRequest pageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

  /**
   * 页码 0表示第一个页，从第二页开始默认为1
   * @return pageNumber
  **/
  @ApiModelProperty(value = "页码 0表示第一个页，从第二页开始默认为1")
  
    public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public MinosMoreTdcForPortalRequest pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * 分页数
   * @return pageSize
  **/
  @ApiModelProperty(value = "分页数")
  
    public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public MinosMoreTdcForPortalRequest userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * 用户id(账号)
   * @return userId
  **/
  @ApiModelProperty(value = "用户id(账号)")
  
    public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public MinosMoreTdcForPortalRequest userWid(String userWid) {
    this.userWid = userWid;
    return this;
  }

  /**
   * 用户wid
   * @return userWid
  **/
  @ApiModelProperty(value = "用户wid")
  
    public String getUserWid() {
    return userWid;
  }

  public void setUserWid(String userWid) {
    this.userWid = userWid;
  }

  public MinosMoreTdcForPortalRequest searchKey(String searchKey) {
    this.searchKey = searchKey;
    return this;
  }

  /**
   * 搜索关键词
   * @return searchKey
  **/
  @ApiModelProperty(value = "搜索关键词")
  
    public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public MinosMoreTdcForPortalRequest lang(String lang) {
    this.lang = lang;
    return this;
  }

  /**
   * 多语言
   * @return lang
  **/
  @ApiModelProperty(value = "多语言")
  
    public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public MinosMoreTdcForPortalRequest serviceStation(Integer serviceStation) {
    this.serviceStation = serviceStation;
    return this;
  }

  /**
   * 平台类型 0PC 1移动 2PC和移动
   * @return serviceStation
  **/
  @ApiModelProperty(value = "平台类型 0PC 1移动 2PC和移动")
  
    public Integer getServiceStation() {
    return serviceStation;
  }

  public void setServiceStation(Integer serviceStation) {
    this.serviceStation = serviceStation;
  }

  public MinosMoreTdcForPortalRequest sortType(Integer sortType) {
    this.sortType = sortType;
    return this;
  }

  /**
   * 排序类型
   * @return sortType
  **/
  @ApiModelProperty(value = "排序类型")
  
    public Integer getSortType() {
    return sortType;
  }

  public void setSortType(Integer sortType) {
    this.sortType = sortType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MinosMoreTdcForPortalRequest minosMoreTdcForPortalRequest = (MinosMoreTdcForPortalRequest) o;
    return Objects.equals(this.pageNumber, minosMoreTdcForPortalRequest.pageNumber) &&
        Objects.equals(this.pageSize, minosMoreTdcForPortalRequest.pageSize) &&
        Objects.equals(this.userId, minosMoreTdcForPortalRequest.userId) &&
        Objects.equals(this.userWid, minosMoreTdcForPortalRequest.userWid) &&
        Objects.equals(this.searchKey, minosMoreTdcForPortalRequest.searchKey) &&
        Objects.equals(this.lang, minosMoreTdcForPortalRequest.lang) &&
        Objects.equals(this.serviceStation, minosMoreTdcForPortalRequest.serviceStation) &&
        Objects.equals(this.sortType, minosMoreTdcForPortalRequest.sortType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pageNumber, pageSize, userId, userWid, searchKey, lang, serviceStation, sortType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MinosMoreTdcForPortalRequest {\n");
    
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    userWid: ").append(toIndentedString(userWid)).append("\n");
    sb.append("    searchKey: ").append(toIndentedString(searchKey)).append("\n");
    sb.append("    lang: ").append(toIndentedString(lang)).append("\n");
    sb.append("    serviceStation: ").append(toIndentedString(serviceStation)).append("\n");
    sb.append("    sortType: ").append(toIndentedString(sortType)).append("\n");
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
