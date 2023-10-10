package com.wisedu.amp.card.cus.cucRecommendapp.model;

import java.io.Serializable;
import java.util.List;

public class AppInfoResponse extends AmpBaseResponse implements Serializable {
    private List<AppInfoBiz> data;

    public List<AppInfoBiz> getData() {
        return data;
    }

    public void setData(List<AppInfoBiz> data) {
        this.data = data;
    }
}
