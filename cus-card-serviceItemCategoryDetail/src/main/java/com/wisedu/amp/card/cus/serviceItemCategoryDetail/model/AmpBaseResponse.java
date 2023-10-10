package com.wisedu.amp.card.cus.serviceItemCategoryDetail.model;

public class AmpBaseResponse {

    private String errcode;
    private String errmsg;
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
