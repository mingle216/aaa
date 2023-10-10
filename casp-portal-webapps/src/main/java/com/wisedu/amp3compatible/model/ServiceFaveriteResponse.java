package com.wisedu.amp3compatible.model;

import java.io.Serializable;
import java.util.List;

public class ServiceFaveriteResponse extends AmpBaseResponse implements Serializable {
    private List<FaveriteServiceInfo> data;

    public List<FaveriteServiceInfo> getData() {
        return data;
    }

    public void setData(List<FaveriteServiceInfo> data) {
        this.data = data;
    }
}
