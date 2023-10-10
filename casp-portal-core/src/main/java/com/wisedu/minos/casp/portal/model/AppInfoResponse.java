package com.wisedu.minos.casp.portal.model;

import java.io.Serializable;
import java.util.List;

public class AppInfoResponse extends AmpBaseResponse implements Serializable {
    private List<AppInfo> data;

    public List<AppInfo> getData() {
        return data;
    }

    public void setData(List<AppInfo> data) {
        this.data = data;
    }
}
