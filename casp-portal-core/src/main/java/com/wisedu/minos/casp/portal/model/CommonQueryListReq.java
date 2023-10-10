package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.SearchCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CommonQueryListReq
 */
@Validated

public class CommonQueryListReq   {
  @JsonProperty("searchCriteria")
  private SearchCriteria searchCriteria = null;

  @Max(value = 1000)
  @JsonProperty("pageSize")
  private Integer pageSize = null;

  @JsonProperty("pageNumber")
  private Integer pageNumber = null;

  public CommonQueryListReq searchCriteria(SearchCriteria searchCriteria) {
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

  public CommonQueryListReq pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * 当前页码
   * @return pageSize
  **/
  @ApiModelProperty(value = "当前页码")
  
    public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public CommonQueryListReq pageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
    return this;
  }

  /**
   * 每页条数
   * @return pageNumber
  **/
  @ApiModelProperty(value = "每页条数")
  
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
    CommonQueryListReq commonQueryListReq = (CommonQueryListReq) o;
    return Objects.equals(this.searchCriteria, commonQueryListReq.searchCriteria) &&
        Objects.equals(this.pageSize, commonQueryListReq.pageSize) &&
        Objects.equals(this.pageNumber, commonQueryListReq.pageNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(searchCriteria, pageSize, pageNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommonQueryListReq {\n");
    
    sb.append("    searchCriteria: ").append(toIndentedString(searchCriteria)).append("\n");
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
