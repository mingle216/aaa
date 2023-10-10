package com.wisedu.amp.card.cuscuc.detailsofserviceitems.model;

import java.io.Serializable;
import java.util.List;

public class LinkServicesResponse extends AmpBaseResponse implements Serializable {
    private String traceId;
    private List<LinkInfo> data;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public List<LinkInfo> getData() {
        return data;
    }

    public void setData(List<LinkInfo> data) {
        this.data = data;
    }
}
