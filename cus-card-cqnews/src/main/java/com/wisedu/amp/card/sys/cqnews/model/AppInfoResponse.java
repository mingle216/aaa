package com.wisedu.amp.card.sys.cqnews.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class AppInfoResponse extends AmpBaseResponse implements Serializable {
    private Map<String,Object> data;

    public Map<String,Object> getData() {
        return data;
    }

    public void setData(Map<String,Object> data) {
        this.data = data;
    }
}
