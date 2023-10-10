package com.wisedu.amp.card.cus.recommendServiceItems.model;

import com.wisedu.minos.casp.portal.model.AmpBaseResponse;

import java.io.Serializable;
import java.util.List;

public class RecommendServiceResponse extends AmpBaseResponse implements Serializable {

    private List<RecommendServiceInfo> data = null;

    public List<RecommendServiceInfo> getData() {
        return data;
    }

    public void setData(List<RecommendServiceInfo> data) {
        this.data = data;
    }
}
