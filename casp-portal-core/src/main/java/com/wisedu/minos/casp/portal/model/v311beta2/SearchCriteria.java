package com.wisedu.minos.casp.portal.model.v311beta2;

import java.util.Objects;
import com.wisedu.minos.casp.portal.model.v311beta2.SearchObject;
import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 搜索条件。   &lt;/br&gt;  传入字段进行搜索，数组间根据condition这个进行判断是用or还是and   &lt;/br&gt; 在需求中存在对账户进行精确匹配，但对姓名模糊批量，但输入的关键字是同一个的场景，需要增加一个特殊的处理：field&#x3D;[useraccount],username，即[]包含的字段使用eq,不包含的字段使用comparator的条件，字段与字段之间使用or进行拼接 
 */
@ApiModel(description = "搜索条件。   </br>  传入字段进行搜索，数组间根据condition这个进行判断是用or还是and   </br> 在需求中存在对账户进行精确匹配，但对姓名模糊批量，但输入的关键字是同一个的场景，需要增加一个特殊的处理：field=[useraccount],username，即[]包含的字段使用eq,不包含的字段使用comparator的条件，字段与字段之间使用or进行拼接 ")
@Validated

public class SearchCriteria extends ArrayList<SearchObject>  {

  @Override
  public boolean equals(Object o) {
    boolean flag = true;
    if (o == null || getClass() != o.getClass()) {
      flag = false;
    }
    return flag;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }

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
