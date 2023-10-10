package com.wisedu.amp.card.cus.hotApp.model;

import java.util.List;

public class HotAppResposeOld extends AmpBaseResponse {

    private List<AppInfo> data;

    public List<AppInfo> getData() {
        return data;
    }

    public void setData(List<AppInfo> data) {
        this.data = data;
    }
}
