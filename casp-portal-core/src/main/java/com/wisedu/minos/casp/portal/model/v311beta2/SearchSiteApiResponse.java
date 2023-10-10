package com.wisedu.minos.casp.portal.model.v311beta2;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.wisedu.minos.casp.portal.model.v311beta2.ModelApiResponse;
import com.wisedu.minos.casp.portal.model.v311beta2.PortalSiteInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SearchSiteApiResponse
 */
@Validated

public class SearchSiteApiResponse extends ModelApiResponse  {
  @JsonProperty("data")
  @Valid
  private List<PortalSiteInfo> data = null;

  public SearchSiteApiResponse data(List<PortalSiteInfo> data) {
    this.data = data;
    return this;
  }

  public SearchSiteApiResponse addDataItem(PortalSiteInfo dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<PortalSiteInfo>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<PortalSiteInfo> getData() {
    return data;
  }

  public void setData(List<PortalSiteInfo> data) {
    this.data = data;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchSiteApiResponse searchSiteApiResponse = (SearchSiteApiResponse) o;
    return Objects.equals(this.data, searchSiteApiResponse.data) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchSiteApiResponse {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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
