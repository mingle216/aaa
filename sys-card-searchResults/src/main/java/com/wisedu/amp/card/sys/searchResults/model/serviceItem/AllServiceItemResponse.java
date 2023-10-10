package com.wisedu.amp.card.sys.searchResults.model.serviceItem;

import com.wisedu.amp.card.sys.searchResults.model.AmpBaseResponse;

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
