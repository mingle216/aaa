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
 * MenuAuthInfoBiz
 */
@Validated

public class MenuAuthInfoBiz   {
  @JsonProperty("menuId")
  private String menuId = null;

  @JsonProperty("authRelWid")
  private String authRelWid = null;

  @JsonProperty("authRelName")
  private String authRelName = null;

  @JsonProperty("authType")
  private String authType = null;

  public MenuAuthInfoBiz menuId(String menuId) {
    this.menuId = menuId;
    return this;
  }

  /**
   * 菜单ID
   * @return menuId
  **/
  @ApiModelProperty(value = "菜单ID")
  
    public String getMenuId() {
    return menuId;
  }

  public void setMenuId(String menuId) {
    this.menuId = menuId;
  }

  public MenuAuthInfoBiz authRelWid(String authRelWid) {
    this.authRelWid = authRelWid;
    return this;
  }

  /**
   * 授权的其他类型id
   * @return authRelWid
  **/
  @ApiModelProperty(value = "授权的其他类型id")
  
    public String getAuthRelWid() {
    return authRelWid;
  }

  public void setAuthRelWid(String authRelWid) {
    this.authRelWid = authRelWid;
  }

  public MenuAuthInfoBiz authRelName(String authRelName) {
    this.authRelName = authRelName;
    return this;
  }

  /**
   * 授权的其他类型Name
   * @return authRelName
  **/
  @ApiModelProperty(value = "授权的其他类型Name")
  
    public String getAuthRelName() {
    return authRelName;
  }

  public void setAuthRelName(String authRelName) {
    this.authRelName = authRelName;
  }

  public MenuAuthInfoBiz authType(String authType) {
    this.authType = authType;
    return this;
  }

  /**
   * 授权类型  0:组织机构 1:用户 2:域及用户组
   * @return authType
  **/
  @ApiModelProperty(value = "授权类型  0:组织机构 1:用户 2:域及用户组")
  
    public String getAuthType() {
    return authType;
  }

  public void setAuthType(String authType) {
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
    MenuAuthInfoBiz menuAuthInfoBiz = (MenuAuthInfoBiz) o;
    return Objects.equals(this.menuId, menuAuthInfoBiz.menuId) &&
        Objects.equals(this.authRelWid, menuAuthInfoBiz.authRelWid) &&
        Objects.equals(this.authRelName, menuAuthInfoBiz.authRelName) &&
        Objects.equals(this.authType, menuAuthInfoBiz.authType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(menuId, authRelWid, authRelName, authType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MenuAuthInfoBiz {\n");
    
    sb.append("    menuId: ").append(toIndentedString(menuId)).append("\n");
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
