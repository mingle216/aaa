package com.wisedu.minos.casp.portal.model.v353beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 菜单授权信息
 */
@ApiModel(description = "菜单授权信息")
@Validated

public class MenuAuthDto   {
  @JsonProperty("authRelWid")
  private String authRelWid = null;

  @JsonProperty("authRelName")
  private String authRelName = null;

  @JsonProperty("authType")
  private Integer authType = null;

  public MenuAuthDto authRelWid(String authRelWid) {
    this.authRelWid = authRelWid;
    return this;
  }

  /**
   * 授权wid
   * @return authRelWid
  **/
  @ApiModelProperty(value = "授权wid")
  
    public String getAuthRelWid() {
    return authRelWid;
  }

  public void setAuthRelWid(String authRelWid) {
    this.authRelWid = authRelWid;
  }

  public MenuAuthDto authRelName(String authRelName) {
    this.authRelName = authRelName;
    return this;
  }

  /**
   * 授权名称
   * @return authRelName
  **/
  @ApiModelProperty(value = "授权名称")
  
    public String getAuthRelName() {
    return authRelName;
  }

  public void setAuthRelName(String authRelName) {
    this.authRelName = authRelName;
  }

  public MenuAuthDto authType(Integer authType) {
    this.authType = authType;
    return this;
  }

  /**
   * 授权类型
   * @return authType
  **/
  @ApiModelProperty(value = "授权类型")
  
    public Integer getAuthType() {
    return authType;
  }

  public void setAuthType(Integer authType) {
    this.authType = authType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MenuAuthDto menuAuthDto = (MenuAuthDto) o;
    return Objects.equals(this.authRelWid, menuAuthDto.authRelWid) &&
        Objects.equals(this.authRelName, menuAuthDto.authRelName) &&
        Objects.equals(this.authType, menuAuthDto.authType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authRelWid, authRelName, authType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MenuAuthDto {\n");
    
    sb.append("    authRelWid: ").append(toIndentedString(authRelWid)).append("\n");
    sb.append("    authRelName: ").append(toIndentedString(authRelName)).append("\n");
    sb.append("    authType: ").append(toIndentedString(authType)).append("\n");
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
