package com.wisedu.amp.card.cus.cucmyTask.model;

import java.io.Serializable;
import java.util.List;

public class TaskListResponse extends AmpBaseResponse implements Serializable {
    private List<TaskInfo> data;

    public List<TaskInfo> getData() {
        return data;
    }

    public void setData(List<TaskInfo> data) {
        this.data = data;
    }
}
