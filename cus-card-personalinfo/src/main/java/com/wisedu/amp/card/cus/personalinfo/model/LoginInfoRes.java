package com.wisedu.amp.card.cus.personalinfo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * LoginInfoRes
 */
@Validated

public class LoginInfoRes {
  @JsonProperty("userAccount")
  private String userAccount = null;

  @JsonProperty("lastLoginTime")
  private java.util.Date lastLoginTime = null;

  @JsonProperty("lastLoginIp")
  private String lastLoginIp = null;

  public LoginInfoRes userAccount(String userAccount) {
    this.userAccount = userAccount;
    return this;
  }

  /**
   * 用户账号
   * @return userAccount
  **/
  @ApiModelProperty(value = "用户账号")
  
    public String getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(String userAccount) {
    this.userAccount = userAccount;
  }

  public LoginInfoRes lastLoginTime(java.util.Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
    return this;
  }

  /**
   * 上一次登录时间
   * @return lastLoginTime
  **/
  @ApiModelProperty(value = "上一次登录时间")
  
    @Valid
    public java.util.Date getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(java.util.Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  public LoginInfoRes lastLoginIp(String lastLoginIp) {
    this.lastLoginIp = lastLoginIp;
    return this;
  }

  /**
   * 上一次登录ip
   * @return lastLoginIp
  **/
  @ApiModelProperty(value = "上一次登录ip")
  
    public String getLastLoginIp() {
    return lastLoginIp;
  }

  public void setLastLoginIp(String lastLoginIp) {
    this.lastLoginIp = lastLoginIp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginInfoRes loginInfoRes = (LoginInfoRes) o;
    return Objects.equals(this.userAccount, loginInfoRes.userAccount) &&
        Objects.equals(this.lastLoginTime, loginInfoRes.lastLoginTime) &&
        Objects.equals(this.lastLoginIp, loginInfoRes.lastLoginIp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userAccount, lastLoginTime, lastLoginIp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginInfoRes {\n");
    
    sb.append("    userAccount: ").append(toIndentedString(userAccount)).append("\n");
    sb.append("    lastLoginTime: ").append(toIndentedString(lastLoginTime)).append("\n");
    sb.append("    lastLoginIp: ").append(toIndentedString(lastLoginIp)).append("\n");
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
