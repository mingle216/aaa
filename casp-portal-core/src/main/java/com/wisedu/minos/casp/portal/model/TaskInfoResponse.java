package com.wisedu.minos.casp.portal.model;

import java.io.Serializable;

public class TaskInfoResponse implements Serializable {
    private String errcode;
    private String errmsg;

    private TaskInfo data;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public TaskInfo getData() {
        return data;
    }

    public void setData(TaskInfo data) {
        this.data = data;
    }
}
