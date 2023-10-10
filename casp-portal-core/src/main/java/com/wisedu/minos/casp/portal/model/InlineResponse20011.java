package com.wisedu.minos.casp.portal.model;

import java.util.Objects;
import com.wisedu.minos.casp.portal.model.ModelApiResponse;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * InlineResponse20011
 */
@Validated
@EqualsAndHashCode
public class InlineResponse20011 extends ModelApiResponse  {

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse20011 {\n");
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
