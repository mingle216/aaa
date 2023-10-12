package com.wisedu.amp.card.sys.cqnews.model;

import java.util.List;

public class AppInfoRequest {
    private String type;
    private int pageSize;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
