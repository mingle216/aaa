package com.wisedu.amp.card.cus.cucDoneTask.model;

import java.io.Serializable;
import java.util.Map;

public class TaskJsonInfoResponse extends AmpBaseResponse implements Serializable {
    private Map<String, Map<String, Object>> data;

    public Map<String, Map<String, Object>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, Object>> data) {
        this.data = data;
    }
}
