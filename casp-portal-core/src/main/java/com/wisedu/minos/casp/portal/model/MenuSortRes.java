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
 * MenuSortRes
 */
@Validated

public class MenuSortRes   {
  @JsonProperty("date")
  @Valid
  private List<SortInfo> date = null;

  public MenuSortRes date(List<SortInfo> date) {
    this.date = date;
    return this;
  }

  public MenuSortRes addDateItem(SortInfo dateItem) {
    if (this.date == null) {
      this.date = new ArrayList<SortInfo>();
    }
    this.date.add(dateItem);
    return this;
  }

  /**
   * 排序信息
   * @return date
  **/
  @ApiModelProperty(value = "排序信息")
      @Valid
    public List<SortInfo> getDate() {
    return date;
  }

  public void setDate(List<SortInfo> date) {
    this.date = date;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MenuSortRes menuSortRes = (MenuSortRes) o;
    return Objects.equals(this.date, menuSortRes.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MenuSortRes {\n");
    
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
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
