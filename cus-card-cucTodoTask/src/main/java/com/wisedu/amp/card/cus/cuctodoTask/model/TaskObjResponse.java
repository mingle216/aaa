package com.wisedu.amp.card.cus.cuctodoTask.model;

import java.io.Serializable;
import java.util.List;

public class TaskObjResponse extends AmpBaseResponse implements Serializable {
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
