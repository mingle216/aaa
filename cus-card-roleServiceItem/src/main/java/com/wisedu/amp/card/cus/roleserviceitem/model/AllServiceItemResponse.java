package com.wisedu.amp.card.cus.roleserviceitem.model;

import java.util.List;

public class AllServiceItemResponse extends AmpBaseResponse {

    List<ServiceItem> data;

    @Override
    public List<ServiceItem> getData() {
        return data;
    }

    public void setData(List<ServiceItem> data) {
        this.data = data;
    }
}
