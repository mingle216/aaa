package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * 日程分享处理请求接收类
 */
@ApiModel(description = "日程分享处理请求接收类")
@Validated

public class HandleScheduleRequest {
    @JsonProperty("eventWid")
    private String eventWid = null;

    @JsonProperty("shareState")
    private Integer shareState = null;

    public HandleScheduleRequest eventWid(String eventWid) {
        this.eventWid = eventWid;
        return this;
    }

    /**
     * 日程wid
     *
     * @return eventWid
     **/
    @ApiModelProperty(value = "日程wid")

    public String getEventWid() {
        return eventWid;
    }

    public void setEventWid(String eventWid) {
        this.eventWid = eventWid;
    }

    public HandleScheduleRequest shareState(Integer shareState) {
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


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HandleScheduleRequest handleScheduleRequest = (HandleScheduleRequest) o;
        return Objects.equals(this.eventWid, handleScheduleRequest.eventWid) &&
                Objects.equals(this.shareState, handleScheduleRequest.shareState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventWid, shareState);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class HandleScheduleRequest {\n");

        sb.append("    eventWid: ").append(toIndentedString(eventWid)).append("\n");
        sb.append("    shareState: ").append(toIndentedString(shareState)).append("\n");
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
