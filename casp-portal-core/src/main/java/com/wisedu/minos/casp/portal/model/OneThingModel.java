package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * OneThingModel
 */
@Validated

public class OneThingModel   {
  @JsonProperty("oneThingWid")
  private String oneThingWid = null;

  @JsonProperty("iconLink")
  private String iconLink = null;

  @JsonProperty("oneThingName")
  private String oneThingName = null;

  @JsonProperty("visitCount")
  private Integer visitCount = null;

  @JsonProperty("oneThingClassify")
  @Valid
  private List<String> oneThingClassify = null;

  @JsonProperty("favorite")
  private Boolean favorite = null;

  @JsonProperty("roleList")
  @Valid
  private List<String> roleList = null;

  @JsonProperty("order")
  private String order = null;

  public OneThingModel oneThingWid(String oneThingWid) {
    this.oneThingWid = oneThingWid;
    return this;
  }

  /**
   * Get oneThingWid
   * @return oneThingWid
  **/
  @ApiModelProperty(value = "")
  
    public String getOneThingWid() {
    return oneThingWid;
  }

  public void setOneThingWid(String oneThingWid) {
    this.oneThingWid = oneThingWid;
  }

  public OneThingModel iconLink(String iconLink) {
    this.iconLink = iconLink;
    return this;
  }

  /**
   * Get iconLink
   * @return iconLink
  **/
  @ApiModelProperty(value = "")
  
    public String getIconLink() {
    return iconLink;
  }

  public void setIconLink(String iconLink) {
    this.iconLink = iconLink;
  }

  public OneThingModel oneThingName(String oneThingName) {
    this.oneThingName = oneThingName;
    return this;
  }

  /**
   * Get oneThingName
   * @return oneThingName
  **/
  @ApiModelProperty(value = "")
  
    public String getOneThingName() {
    return oneThingName;
  }

  public void setOneThingName(String oneThingName) {
    this.oneThingName = oneThingName;
  }

  public OneThingModel visitCount(Integer visitCount) {
    this.visitCount = visitCount;
    return this;
  }

  /**
   * Get visitCount
   * @return visitCount
  **/
  @ApiModelProperty(value = "")
  
    public Integer getVisitCount() {
    return visitCount;
  }

  public void setVisitCount(Integer visitCount) {
    this.visitCount = visitCount;
  }

  public OneThingModel oneThingClassify(List<String> oneThingClassify) {
    this.oneThingClassify = oneThingClassify;
    return this;
  }

  public OneThingModel addOneThingClassifyItem(String oneThingClassifyItem) {
    if (this.oneThingClassify == null) {
      this.oneThingClassify = new ArrayList<String>();
    }
    this.oneThingClassify.add(oneThingClassifyItem);
    return this;
  }

  /**
   * 一件事分类信息
   * @return oneThingClassify
  **/
  @ApiModelProperty(value = "一件事分类信息")
  
    public List<String> getOneThingClassify() {
    return oneThingClassify;
  }

  public void setOneThingClassify(List<String> oneThingClassify) {
    this.oneThingClassify = oneThingClassify;
  }

  public OneThingModel favorite(Boolean favorite) {
    this.favorite = favorite;
    return this;
  }

  /**
   * Get favorite
   * @return favorite
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(Boolean favorite) {
    this.favorite = favorite;
  }

  public OneThingModel roleList(List<String> roleList) {
    this.roleList = roleList;
    return this;
  }

  public OneThingModel addRoleListItem(String roleListItem) {
    if (this.roleList == null) {
      this.roleList = new ArrayList<String>();
    }
    this.roleList.add(roleListItem);
    return this;
  }

  /**
   * 服务对象信息
   * @return roleList
  **/
  @ApiModelProperty(value = "服务对象信息")
  
    public List<String> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<String> roleList) {
    this.roleList = roleList;
  }

  public OneThingModel order(String order) {
    this.order = order;
    return this;
  }

  /**
   * Get order
   * @return order
  **/
  @ApiModelProperty(value = "")
  
    public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OneThingModel oneThingModel = (OneThingModel) o;
    return Objects.equals(this.oneThingWid, oneThingModel.oneThingWid) &&
        Objects.equals(this.iconLink, oneThingModel.iconLink) &&
        Objects.equals(this.oneThingName, oneThingModel.oneThingName) &&
        Objects.equals(this.visitCount, oneThingModel.visitCount) &&
        Objects.equals(this.oneThingClassify, oneThingModel.oneThingClassify) &&
        Objects.equals(this.favorite, oneThingModel.favorite) &&
        Objects.equals(this.roleList, oneThingModel.roleList) &&
        Objects.equals(this.order, oneThingModel.order);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oneThingWid, iconLink, oneThingName, visitCount, oneThingClassify, favorite, roleList, order);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OneThingModel {\n");
    
    sb.append("    oneThingWid: ").append(toIndentedString(oneThingWid)).append("\n");
    sb.append("    iconLink: ").append(toIndentedString(iconLink)).append("\n");
    sb.append("    oneThingName: ").append(toIndentedString(oneThingName)).append("\n");
    sb.append("    visitCount: ").append(toIndentedString(visitCount)).append("\n");
    sb.append("    oneThingClassify: ").append(toIndentedString(oneThingClassify)).append("\n");
    sb.append("    favorite: ").append(toIndentedString(favorite)).append("\n");
    sb.append("    roleList: ").append(toIndentedString(roleList)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
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
