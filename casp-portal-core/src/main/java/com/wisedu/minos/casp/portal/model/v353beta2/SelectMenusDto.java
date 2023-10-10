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
 * 查询下拉菜单信息
 */
@ApiModel(description = "查询下拉菜单信息")
@Validated

public class SelectMenusDto   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("menuName")
  private String menuName = null;

  @JsonProperty("orderNumber")
  private Long orderNumber = null;

  @JsonProperty("isSystemMenu")
  private Integer isSystemMenu = null;

  @JsonProperty("isEnabled")
  private Integer isEnabled = null;

  @JsonProperty("isAllRequired")
  private Integer isAllRequired = 1;

  public SelectMenusDto wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * wid
   * @return wid
  **/
  @ApiModelProperty(value = "wid")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public SelectMenusDto menuName(String menuName) {
    this.menuName = menuName;
    return this;
  }

  /**
   * 菜单名称
   * @return menuName
  **/
  @ApiModelProperty(value = "菜单名称")
  
    public String getMenuName() {
    return menuName;
  }

  public void setMenuName(String menuName) {
    this.menuName = menuName;
  }

  public SelectMenusDto orderNumber(Long orderNumber) {
    this.orderNumber = orderNumber;
    return this;
  }

  /**
   * 排序
   * @return orderNumber
  **/
  @ApiModelProperty(value = "排序")
  
    public Long getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(Long orderNumber) {
    this.orderNumber = orderNumber;
  }

  public SelectMenusDto isSystemMenu(Integer isSystemMenu) {
    this.isSystemMenu = isSystemMenu;
    return this;
  }

  /**
   * 是否预置菜单 0:否 1:是
   * @return isSystemMenu
  **/
  @ApiModelProperty(value = "是否预置菜单 0:否 1:是")
  
    public Integer getIsSystemMenu() {
    return isSystemMenu;
  }

  public void setIsSystemMenu(Integer isSystemMenu) {
    this.isSystemMenu = isSystemMenu;
  }

  public SelectMenusDto isEnabled(Integer isEnabled) {
    this.isEnabled = isEnabled;
    return this;
  }

  /**
   * 是否启用  0:停用 1:启用
   * @return isEnabled
  **/
  @ApiModelProperty(value = "是否启用  0:停用 1:启用")
  
    public Integer getIsEnabled() {
    return isEnabled;
  }

  public void setIsEnabled(Integer isEnabled) {
    this.isEnabled = isEnabled;
  }

  public SelectMenusDto isAllRequired(Integer isAllRequired) {
    this.isAllRequired = isAllRequired;
    return this;
  }

  /**
   * 必填值是否全部必填  0:否 1:是
   * @return isAllRequired
  **/
  @ApiModelProperty(value = "必填值是否全部必填  0:否 1:是")
  
    public Integer getIsAllRequired() {
    return isAllRequired;
  }

  public void setIsAllRequired(Integer isAllRequired) {
    this.isAllRequired = isAllRequired;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SelectMenusDto selectMenusDto = (SelectMenusDto) o;
    return Objects.equals(this.wid, selectMenusDto.wid) &&
        Objects.equals(this.menuName, selectMenusDto.menuName) &&
        Objects.equals(this.orderNumber, selectMenusDto.orderNumber) &&
        Objects.equals(this.isSystemMenu, selectMenusDto.isSystemMenu) &&
        Objects.equals(this.isEnabled, selectMenusDto.isEnabled) &&
        Objects.equals(this.isAllRequired, selectMenusDto.isAllRequired);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, menuName, orderNumber, isSystemMenu, isEnabled, isAllRequired);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SelectMenusDto {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    menuName: ").append(toIndentedString(menuName)).append("\n");
    sb.append("    orderNumber: ").append(toIndentedString(orderNumber)).append("\n");
    sb.append("    isSystemMenu: ").append(toIndentedString(isSystemMenu)).append("\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
    sb.append("    isAllRequired: ").append(toIndentedString(isAllRequired)).append("\n");
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
