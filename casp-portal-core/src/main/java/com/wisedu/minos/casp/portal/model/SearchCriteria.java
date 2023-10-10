package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.wisedu.minos.casp.portal.model.SearchObject;
import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 搜索条件。  传入字段进行搜索，数组间根据condition这个进行判断是用or还是and 
 */
@ApiModel(description = "搜索条件。  传入字段进行搜索，数组间根据condition这个进行判断是用or还是and ")
@Validated
@EqualsAndHashCode
public class SearchCriteria extends ArrayList<SearchObject>  {


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchCriteria {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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
