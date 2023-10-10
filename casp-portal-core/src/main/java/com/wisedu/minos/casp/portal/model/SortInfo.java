package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.SortInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SortInfo
 */
@Validated

public class SortInfo   {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("parentId")
  private String parentId = null;

  @JsonProperty("orderNum")
  private String orderNum = null;

  @JsonProperty("childMenu")
  @Valid
  private List<SortInfo> childMenu = null;

  public SortInfo wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 菜单WID
   * @return wid
  **/
  @ApiModelProperty(value = "菜单WID")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public SortInfo parentId(String parentId) {
    this.parentId = parentId;
    return this;
  }

  /**
   * 父菜单ID
   * @return parentId
  **/
  @ApiModelProperty(value = "父菜单ID")
  
    public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public SortInfo orderNum(String orderNum) {
    this.orderNum = orderNum;
    return this;
  }

  /**
   * 排序号
   * @return orderNum
  **/
  @ApiModelProperty(value = "排序号")
  
    public String getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }

  public SortInfo childMenu(List<SortInfo> childMenu) {
    this.childMenu = childMenu;
    return this;
  }

  public SortInfo addChildMenuItem(SortInfo childMenuItem) {
    if (this.childMenu == null) {
      this.childMenu = new ArrayList<SortInfo>();
    }
    this.childMenu.add(childMenuItem);
    return this;
  }

  /**
   * 子菜单
   * @return childMenu
  **/
  @ApiModelProperty(value = "子菜单")
      @Valid
    public List<SortInfo> getChildMenu() {
    return childMenu;
  }

  public void setChildMenu(List<SortInfo> childMenu) {
    this.childMenu = childMenu;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SortInfo sortInfo = (SortInfo) o;
    return Objects.equals(this.wid, sortInfo.wid) &&
        Objects.equals(this.parentId, sortInfo.parentId) &&
        Objects.equals(this.orderNum, sortInfo.orderNum) &&
        Objects.equals(this.childMenu, sortInfo.childMenu);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, parentId, orderNum, childMenu);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SortInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    orderNum: ").append(toIndentedString(orderNum)).append("\n");
    sb.append("    childMenu: ").append(toIndentedString(childMenu)).append("\n");
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
