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
 * UserInfoReq
 */
@Validated

public class UserInfoReq   {
  @JsonProperty("keyword")
  private String keyword = null;

  @JsonProperty("userAccount")
  private String userAccount = null;

  @Max(value = 1000)
  @JsonProperty("pageSize")
  private Integer pageSize = null;

  @JsonProperty("pageNumber")
  private Integer pageNumber = null;

  public UserInfoReq keyword(String keyword) {
    this.keyword = keyword;
    return this;
  }

  /**
   * 工号/姓名关键字查询
   * @return keyword
  **/
  @ApiModelProperty(value = "工号/姓名关键字查询")
  
    public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public UserInfoReq userAccount(String userAccount) {
    this.userAccount = userAccount;
    return this;
  }

  /**
   * 用户帐号
   * @return userAccount
  **/
  @ApiModelProperty(value = "用户帐号")
  
    public String getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(String userAccount) {
    this.userAccount = userAccount;
  }

  public UserInfoReq pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * Get pageSize
   * @return pageSize
  **/
  @ApiModelProperty(example = "10", value = "")
  
    public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public UserInfoReq pageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

  /**
   * Get pageNumber
   * @return pageNumber
  **/
  @ApiModelProperty(example = "1", value = "")
  
    public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfoReq userInfoReq = (UserInfoReq) o;
    return Objects.equals(this.keyword, userInfoReq.keyword) &&
        Objects.equals(this.userAccount, userInfoReq.userAccount) &&
        Objects.equals(this.pageSize, userInfoReq.pageSize) &&
        Objects.equals(this.pageNumber, userInfoReq.pageNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keyword, userAccount, pageSize, pageNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfoReq {\n");
    
    sb.append("    keyword: ").append(toIndentedString(keyword)).append("\n");
    sb.append("    userAccount: ").append(toIndentedString(userAccount)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
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
