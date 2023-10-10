package com.wisedu.amp.card.cus.detailsofserviceitems.model;

import java.io.Serializable;

public class AppInfoResponse extends AmpBaseResponse implements Serializable {
    private AppInfo data;

    public AppInfo getData() {
        return data;
    }

    public void setData(AppInfo data) {
        this.data = data;
    }
}
