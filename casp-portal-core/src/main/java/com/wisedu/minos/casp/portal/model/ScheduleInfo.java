package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * 日程分享实体类
 */
@ApiModel(description = "日程分享实体类")
@Validated

public class ScheduleInfo {
    @JsonProperty("content")
    private String content = null;

    @JsonProperty("title")
    private String title = null;

    @JsonProperty("shareState")
    private Integer shareState = null;

    @JsonProperty("startTime")
    private String startTime = null;

    @JsonProperty("endTime")
    private String endTime = null;

    @JsonProperty("languageKey")
    private String languageKey = null;

    public ScheduleInfo content(String content) {
        this.content = content;
        return this;
    }

    /**
     * 内容
     *
     * @return content
     **/
    @ApiModelProperty(value = "内容")

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ScheduleInfo title(String title) {
        this.title = title;
        return this;
    }

    /**
     * 标题
     *
     * @return title
     **/
    @ApiModelProperty(value = "标题")

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ScheduleInfo shareState(Integer shareState) {
        this.shareState = shareState;
        return this;
    }

    /**
     * 处理状态
     *
     * @return shareState
     **/
    @ApiModelProperty(value = "处理状态")

    public Integer getShareState() {
        return shareState;
    }

    public void setShareState(Integer shareState) {
        this.shareState = shareState;
    }

    public ScheduleInfo startTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    /**
     * 开始时间
     *
     * @return startTime
     **/
    @ApiModelProperty(value = "开始时间")

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public ScheduleInfo endTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    /**
     * 结束时间
     *
     * @return endTime
     **/
    @ApiModelProperty(value = "结束时间")

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ScheduleInfo languageKey(String languageKey) {
        this.languageKey = languageKey;
        return this;
    }

    /**
     * 用户语言
     *
     * @return languageKey
     **/
    @ApiModelProperty(value = "用户语言")

    public String getLanguageKey() {
        return languageKey;
    }

    public void setLanguageKey(String languageKey) {
        this.languageKey = languageKey;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScheduleInfo scheduleInfo = (ScheduleInfo) o;
        return Objects.equals(this.content, scheduleInfo.content) &&
                Objects.equals(this.title, scheduleInfo.title) &&
                Objects.equals(this.shareState, scheduleInfo.shareState) &&
                Objects.equals(this.startTime, scheduleInfo.startTime) &&
                Objects.equals(this.endTime, scheduleInfo.endTime) &&
                Objects.equals(this.languageKey, scheduleInfo.languageKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, title, shareState, startTime, endTime, languageKey);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ScheduleInfo {\n");

        sb.append("    content: ").append(toIndentedString(content)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    shareState: ").append(toIndentedString(shareState)).append("\n");
        sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
        sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
        sb.append("    languageKey: ").append(toIndentedString(languageKey)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
