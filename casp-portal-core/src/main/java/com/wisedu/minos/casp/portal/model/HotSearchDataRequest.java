package com.wisedu.minos.casp.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 门户搜索请求
 */
@Data
public class HotSearchDataRequest {
  @JsonProperty("userWid")
  private String userWid = null;

  @JsonProperty("lang")
  private String lang = null;

  @JsonProperty("serviceStation")
  private Integer serviceStation = null;

  @JsonProperty("pageSize")
  private Integer pageSize = null;

  @JsonProperty("dataTypes")
  private String dataTypes = null;

  @JsonProperty("timeDelay")
  private Integer timeDelay = null;

  @JsonProperty("authorityEnabled")
  private Integer authorityEnabled = null;
}
