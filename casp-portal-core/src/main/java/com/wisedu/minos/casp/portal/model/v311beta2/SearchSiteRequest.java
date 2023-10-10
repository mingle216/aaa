package com.wisedu.minos.casp.portal.model.v311beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.v311beta2.SearchCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SearchSiteRequest
 */
@Validated

public class SearchSiteRequest   {
  @JsonProperty("pageNumber")
  private Integer pageNumber = 1;

  @JsonProperty("pageSize")
  private Integer pageSize = 10;

  @JsonProperty("isSysAdmin")
  private Boolean isSysAdmin = null;

  @JsonProperty("userWid")
  private String userWid = null;

  @JsonProperty("roleWids")
  @Valid
  private List<String> roleWids = null;

  @JsonProperty("searchCriteria")
  private SearchCriteria searchCriteria = null;

  public SearchSiteRequest pageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

  /**
   * 查询的当前页，-1表示查询所有
   * @return pageNumber
  **/
  @ApiModelProperty(value = "查询的当前页，-1表示查询所有")
  
    public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public SearchSiteRequest pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * 查询每页显示的条数
   * @return pageSize
  **/
  @ApiModelProperty(value = "查询每页显示的条数")
  
    public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public SearchSiteRequest isSysAdmin(Boolean isSysAdmin) {
    this.isSysAdmin = isSysAdmin;
    return this;
  }

  /**
   * Get isSysAdmin
   * @return isSysAdmin
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isIsSysAdmin() {
    return isSysAdmin;
  }

  public void setIsSysAdmin(Boolean isSysAdmin) {
    this.isSysAdmin = isSysAdmin;
  }

  public SearchSiteRequest userWid(String userWid) {
    this.userWid = userWid;
    return this;
  }

  /**
   * Get userWid
   * @return userWid
  **/
  @ApiModelProperty(value = "")
  
    public String getUserWid() {
    return userWid;
  }

  public void setUserWid(String userWid) {
    this.userWid = userWid;
  }

  public SearchSiteRequest roleWids(List<String> roleWids) {
    this.roleWids = roleWids;
    return this;
  }

  public SearchSiteRequest addRoleWidsItem(String roleWidsItem) {
    if (this.roleWids == null) {
      this.roleWids = new ArrayList<String>();
    }
    this.roleWids.add(roleWidsItem);
    return this;
  }

  /**
   * Get roleWids
   * @return roleWids
  **/
  @ApiModelProperty(value = "")
  
    public List<String> getRoleWids() {
    return roleWids;
  }

  public void setRoleWids(List<String> roleWids) {
    this.roleWids = roleWids;
  }

  public SearchSiteRequest searchCriteria(SearchCriteria searchCriteria) {
    this.searchCriteria = searchCriteria;
    return this;
  }

  /**
   * Get searchCriteria
   * @return searchCriteria
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public SearchCriteria getSearchCriteria() {
    return searchCriteria;
  }

  public void setSearchCriteria(SearchCriteria searchCriteria) {
    this.searchCriteria = searchCriteria;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchSiteRequest searchSiteRequest = (SearchSiteRequest) o;
    return Objects.equals(this.pageNumber, searchSiteRequest.pageNumber) &&
        Objects.equals(this.pageSize, searchSiteRequest.pageSize) &&
        Objects.equals(this.isSysAdmin, searchSiteRequest.isSysAdmin) &&
        Objects.equals(this.userWid, searchSiteRequest.userWid) &&
        Objects.equals(this.roleWids, searchSiteRequest.roleWids) &&
        Objects.equals(this.searchCriteria, searchSiteRequest.searchCriteria);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pageNumber, pageSize, isSysAdmin, userWid, roleWids, searchCriteria);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchSiteRequest {\n");
    
    sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    isSysAdmin: ").append(toIndentedString(isSysAdmin)).append("\n");
    sb.append("    userWid: ").append(toIndentedString(userWid)).append("\n");
    sb.append("    roleWids: ").append(toIndentedString(roleWids)).append("\n");
    sb.append("    searchCriteria: ").append(toIndentedString(searchCriteria)).append("\n");
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
