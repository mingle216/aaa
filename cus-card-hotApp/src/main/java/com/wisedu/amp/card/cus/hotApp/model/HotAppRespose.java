package com.wisedu.amp.card.cus.hotApp.model;

import java.util.List;

public class HotAppRespose extends AmpBaseResponse {

    private List<HotAppInfo> data;

    public List<HotAppInfo> getData() {
        return data;
    }

    public void setData(List<HotAppInfo> data) {
        this.data = data;
    }
}
