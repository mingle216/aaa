package com.wisedu.amp.card.cus.detailsofserviceitems.model.appraise;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import org.springframework.validation.annotation.Validated;

/**
 * 服务评价明细列表的请求对象
 */
@ApiModel(description = "服务评价明细列表的请求对象")
@Validated
public class ItemAppraiseDetailSearchRequest {

    @JsonProperty("itemWid")
    private String itemWid = null;

    public String getItemWid() {
        return itemWid;
    }

    public void setItemWid(String itemWid) {
        this.itemWid = itemWid;
    }

    @JsonProperty("pageNumber")
    private Integer pageNumber = 1;

    @JsonProperty("pageSize")
    private Integer pageSize = 10;

    @JsonProperty("markType")
    private Integer markType = 0;

    @JsonProperty("userId")
    private String userId = null;

    public ItemAppraiseDetailSearchRequest pageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    /**
     * 查询的当前页
     *
     * @return pageNumber
     **/
    @ApiModelProperty(value = "查询的当前页")

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public ItemAppraiseDetailSearchRequest pageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * 查询每页显示的条数
     *
     * @return pageSize
     **/
    @ApiModelProperty(value = "查询每页显示的条数")

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public ItemAppraiseDetailSearchRequest markType(Integer markType) {
        this.markType = markType;
        return this;
    }

    /**
     * 评价类型（0：全部,1：好评,2:差评）
     *
     * @return markType
     **/
    @ApiModelProperty(value = "评价类型（0：全部,1：好评,2:差评）")

    public Integer getMarkType() {
        return markType;
    }

    public void setMarkType(Integer markType) {
        this.markType = markType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemAppraiseDetailSearchRequest itemAppraiseDetailSearchRequest = (ItemAppraiseDetailSearchRequest) o;
        return Objects.equals(this.pageNumber, itemAppraiseDetailSearchRequest.pageNumber) &&
                Objects.equals(this.pageSize, itemAppraiseDetailSearchRequest.pageSize) &&
                Objects.equals(this.markType, itemAppraiseDetailSearchRequest.markType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNumber, pageSize, markType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ItemAppraiseDetailSearchRequest {\n");

        sb.append("    pageNumber: ").append(toIndentedString(pageNumber)).append("\n");
        sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
        sb.append("    markType: ").append(toIndentedString(markType)).append("\n");
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
