package com.wisedu.amp3compatible.model;

/**
 * Created by glhan on 2016/6/22.
 */
public class BaseResponse {
    private int status;
    private String message;

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
}
