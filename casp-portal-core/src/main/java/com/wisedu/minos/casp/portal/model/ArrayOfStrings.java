package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ArrayOfStrings
 */
@Validated
@EqualsAndHashCode
public class ArrayOfStrings extends ArrayList<String>  {

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ArrayOfStrings {\n");
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
