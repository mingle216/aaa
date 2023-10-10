package com.wisedu.amp.card.cus.cucmyTask.model;

import java.io.Serializable;

public class TaskObjResponse extends AmpBaseResponse implements Serializable {
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
