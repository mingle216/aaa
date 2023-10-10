package com.wisedu.amp.card.cuscuc.detailsofserviceitems.model;

import java.util.List;
import java.io.Serializable;

public class EvaluationResponse extends AmpBaseResponse implements Serializable{
    private List<Object> data;

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
