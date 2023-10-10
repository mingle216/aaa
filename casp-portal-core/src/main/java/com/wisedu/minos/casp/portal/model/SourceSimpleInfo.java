package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * 数据源简略信息
 */
@ApiModel(description = "数据源简略信息")
@Validated

public class SourceSimpleInfo {
  @JsonProperty("wid")
  private String wid = null;

  @JsonProperty("sourceName")
  private String sourceName = null;

  @JsonProperty("period")
  private Integer period = null;

  @JsonProperty("syncTime")
  private String syncTime = null;

  public SourceSimpleInfo wid(String wid) {
    this.wid = wid;
    return this;
  }

  /**
   * 主键
   * @return wid
  **/
  @ApiModelProperty(value = "主键")
  
    public String getWid() {
    return wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  public SourceSimpleInfo sourceName(String sourceName) {
    this.sourceName = sourceName;
    return this;
  }

  /**
   * 数据源名称
   * @return sourceName
  **/
  @ApiModelProperty(value = "数据源名称")
  
    public String getSourceName() {
    return sourceName;
  }

  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }

  public SourceSimpleInfo period(Integer period) {
    this.period = period;
    return this;
  }

  /**
   * 自动采集周期 1:3h、2:12h、3:24h、4:3天、5:一周、0:不自动采集
   * @return period
  **/
  @ApiModelProperty(value = "自动采集周期 1:3h、2:12h、3:24h、4:3天、5:一周、0:不自动采集")
  
    public Integer getPeriod() {
    return period;
  }

  public void setPeriod(Integer period) {
    this.period = period;
  }

  public SourceSimpleInfo syncTime(String syncTime) {
    this.syncTime = syncTime;
    return this;
  }

  /**
   * 上次同步时间
   * @return syncTime
  **/
  @ApiModelProperty(value = "上次同步时间")
  
    public String getSyncTime() {
    return syncTime;
  }

  public void setSyncTime(String syncTime) {
    this.syncTime = syncTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SourceSimpleInfo sourceSimpleInfo = (SourceSimpleInfo) o;
    return Objects.equals(this.wid, sourceSimpleInfo.wid) &&
        Objects.equals(this.sourceName, sourceSimpleInfo.sourceName) &&
        Objects.equals(this.period, sourceSimpleInfo.period) &&
        Objects.equals(this.syncTime, sourceSimpleInfo.syncTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wid, sourceName, period, syncTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SourceSimpleInfo {\n");
    
    sb.append("    wid: ").append(toIndentedString(wid)).append("\n");
    sb.append("    sourceName: ").append(toIndentedString(sourceName)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    syncTime: ").append(toIndentedString(syncTime)).append("\n");
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
