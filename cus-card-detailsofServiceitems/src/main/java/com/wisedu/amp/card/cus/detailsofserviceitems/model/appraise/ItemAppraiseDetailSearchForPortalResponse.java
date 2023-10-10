package com.wisedu.amp.card.cus.detailsofserviceitems.model.appraise;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wisedu.minos.casp.portal.model.PaginationApiResponse;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * InlineResponse3005
 */
@Validated

public class ItemAppraiseDetailSearchForPortalResponse extends PaginationApiResponse {
    @JsonProperty("data")
    @Valid
    private ItemAppraiseForPortal data = null;

    public ItemAppraiseForPortal getData() {
        return data;
    }

    public void setData(ItemAppraiseForPortal data) {
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
        ItemAppraiseDetailSearchForPortalResponse itemAppraiseDetailSearchResponse = (ItemAppraiseDetailSearchForPortalResponse) o;
        return Objects.equals(this.data, itemAppraiseDetailSearchResponse.data) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ItemAppraiseDetailSearchResponse {\n");
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
