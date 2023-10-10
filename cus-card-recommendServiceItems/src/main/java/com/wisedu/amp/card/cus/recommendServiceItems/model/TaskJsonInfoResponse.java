package com.wisedu.amp.card.cus.recommendServiceItems.model;

import com.wisedu.minos.casp.portal.model.AmpBaseResponse;

import java.io.Serializable;
import java.util.Map;

public class TaskJsonInfoResponse extends AmpBaseResponse implements Serializable {
    private Map<String,Object> data;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
