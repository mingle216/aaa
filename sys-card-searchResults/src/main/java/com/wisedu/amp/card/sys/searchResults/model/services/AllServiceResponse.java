package com.wisedu.amp.card.sys.searchResults.model.services;


import com.wisedu.amp.card.sys.searchResults.model.AmpBaseResponse;

import java.util.List;

public class AllServiceResponse extends AmpBaseResponse {

    private List<ServiceInfo> data;
    private String traceId;
    private Integer pageSize;
    private Integer total;

    @Override
    public List<ServiceInfo> getData() {
        return data;
    }

    public void setData(List<ServiceInfo> data) {
        this.data = data;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
