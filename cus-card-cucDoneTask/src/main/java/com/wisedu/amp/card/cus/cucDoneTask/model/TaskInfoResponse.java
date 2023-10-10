package com.wisedu.amp.card.cus.cucDoneTask.model;

import java.io.Serializable;

public class TaskInfoResponse extends AmpBaseResponse implements Serializable {
    private TaskInfo data;

    public TaskInfo getData() {
        return data;
    }

    public void setData(TaskInfo data) {
        this.data = data;
    }
}
