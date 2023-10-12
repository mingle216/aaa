package com.wisedu.amp.card.sys.cqdetail.model.appraise;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import org.springframework.validation.annotation.Validated;

/**
 * ItemAppraiseDetailList
 */
@Validated

public class ItemAppraiseDetailList {
    @JsonProperty("wid")
    private String wid = null;

    @JsonProperty("userName")
    private String userName = null;

    @JsonProperty("createTime")
    private String createTime = null;

    @JsonProperty("suggest")
    private String suggest = null;

    @JsonProperty("mannerScore")
    private Integer mannerScore = null;

    @JsonProperty("speedScore")
    private Integer speedScore = null;

    @JsonProperty("integrityScore")
    private Integer integrityScore = null;

    @JsonProperty("satusfyScore")
    private Integer satusfyScore = null;

    @JsonProperty("isHidden")
    private Integer isHidden;

    public ItemAppraiseDetailList wid(String wid) {
        this.wid = wid;
        return this;
    }

    /**
     * 服务事项评价明细wid
     *
     * @return wid
     **/
    @ApiModelProperty(value = "服务事项评价明细wid")

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public ItemAppraiseDetailList userName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * 评价人
     *
     * @return userName
     **/
    @ApiModelProperty(value = "评价人")

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ItemAppraiseDetailList createTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    /**
     * 评价时间
     *
     * @return createTime
     **/
    @ApiModelProperty(value = "评价时间")

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public ItemAppraiseDetailList suggest(String suggest) {
        this.suggest = suggest;
        return this;
    }

    /**
     * 评价内容
     *
     * @return suggest
     **/
    @ApiModelProperty(value = "评价内容")

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public ItemAppraiseDetailList mannerScore(Integer mannerScore) {
        this.mannerScore = mannerScore;
        return this;
    }

    /**
     * 服务态度
     *
     * @return mannerScore
     **/
    @ApiModelProperty(value = "服务态度")

    public Integer getMannerScore() {
        return mannerScore;
    }

    public void setMannerScore(Integer mannerScore) {
        this.mannerScore = mannerScore;
    }

    public ItemAppraiseDetailList speedScore(Integer speedScore) {
        this.speedScore = speedScore;
        return this;
    }

    /**
     * 办事速度
     *
     * @return speedScore
     **/
    @ApiModelProperty(value = "办事速度")

    public Integer getSpeedScore() {
        return speedScore;
    }

    public void setSpeedScore(Integer speedScore) {
        this.speedScore = speedScore;
    }

    public ItemAppraiseDetailList integrityScore(Integer integrityScore) {
        this.integrityScore = integrityScore;
        return this;
    }

    /**
     * 信息完整度
     *
     * @return integrityScore
     **/
    @ApiModelProperty(value = "信息完整度")

    public Integer getIntegrityScore() {
        return integrityScore;
    }

    public void setIntegrityScore(Integer integrityScore) {
        this.integrityScore = integrityScore;
    }

    public ItemAppraiseDetailList satusfyScore(Integer satusfyScore) {
        this.satusfyScore = satusfyScore;
        return this;
    }

    /**
     * 整体满意度
     *
     * @return satusfyScore
     **/
    @ApiModelProperty(value = "整体满意度")

    public Integer getSatusfyScore() {
        return satusfyScore;
    }

    public void setSatusfyScore(Integer satusfyScore) {
        this.satusfyScore = satusfyScore;
    }

    public Integer getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Integer isHidden) {
        this.isHidden = isHidden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemAppraiseDetailList itemAppraiseDetailList = (ItemAppraiseDetailList) o;
        return Objects.equals(this.wid, itemAppraiseDetailList.wid) &&
                Objects.equals(this.userName, itemAppraiseDetailList.userName) &&
                Objects.equals(this.createTime, itemAppraiseDetailList.createTime) &&
                Objects.equals(this.suggest, itemAppraiseDetailList.suggest) &&
                Objects.equals(this.mannerScore, itemAppraiseDetailList.mannerScore) &&
                Objects.equals(this.speedScore, itemAppraiseDetailList.speedScore) &&
                Objects.equals(this.integrityScore, itemAppraiseDetailList.integrityScore) &&
                Objects.equals(this.satusfyScore, itemAppraiseDetailList.satusfyScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wid, userName, createTime, suggest, mannerScore, speedScore, integrityScore, satusfyScore);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ItemAppraiseDetailList {\n");

        sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
        sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
        sb.append("    createTime: ").append(toIndentedString(createTime)).append("\n");
        sb.append("    suggest: ").append(toIndentedString(suggest)).append("\n");
        sb.append("    mannerScore: ").append(toIndentedString(mannerScore)).append("\n");
        sb.append("    speedScore: ").append(toIndentedString(speedScore)).append("\n");
        sb.append("    integrityScore: ").append(toIndentedString(integrityScore)).append("\n");
        sb.append("    satusfyScore: ").append(toIndentedString(satusfyScore)).append("\n");
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
