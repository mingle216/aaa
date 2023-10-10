package com.wisedu.minos.casp.portal.model.v353beta2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title EnableServerInfosRes
 * @Author: 01120034
 * @Date: 2022/10/8
 */
@Getter
@Setter
public class EnableServerInfosRes {
    @JsonProperty("errcode")
    private String errcode = "0";

    @JsonProperty("errmsg")
    private String errmsg = "success";

    @JsonProperty("traceId")
    private String traceId = null;

    @JsonProperty("data")
    private List<EnableServerInfo> data;
}
