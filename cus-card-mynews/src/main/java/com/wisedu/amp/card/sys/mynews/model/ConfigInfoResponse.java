package com.wisedu.amp.card.sys.mynews.model;

import java.io.Serializable;
import java.util.List;

public class ConfigInfoResponse extends AmpBaseResponse implements Serializable {
    private List<ConfigInfo> data;

    public List<ConfigInfo> getData() {
        return data;
    }

    public void setData(List<ConfigInfo> data) {
        this.data = data;
    }
}
