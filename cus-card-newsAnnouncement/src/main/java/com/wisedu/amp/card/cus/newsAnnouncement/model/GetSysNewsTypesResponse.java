package com.wisedu.amp.card.cus.newsAnnouncement.model;

import java.io.Serializable;
import java.util.List;

public class GetSysNewsTypesResponse extends AmpBaseResponse implements Serializable {
    private  List<NewsSourceModel> data;

    public List<NewsSourceModel> getData() {
        return data;
    }

    public void setData(List<NewsSourceModel> data) {
        this.data = data;
    }
}
