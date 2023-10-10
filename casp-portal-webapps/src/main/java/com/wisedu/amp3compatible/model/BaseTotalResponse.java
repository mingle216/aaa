package com.wisedu.amp3compatible.model;

/**
 * Created by glhan on 2016/6/21.
 */
public class BaseTotalResponse {
    private int status;
    private String message;
    private long total;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
