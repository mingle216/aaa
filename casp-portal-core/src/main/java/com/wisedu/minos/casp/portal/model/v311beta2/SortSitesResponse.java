package com.wisedu.minos.casp.portal.model.v311beta2;

import java.util.Objects;
import com.wisedu.minos.casp.portal.model.v311beta2.ModelApiResponse;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SortSitesResponse
 */
@Validated

public class SortSitesResponse extends ModelApiResponse  {

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
    sb.append("class SortSitesResponse {\n");
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
