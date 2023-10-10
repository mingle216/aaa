package com.wisedu.minos.casp.portal.constants;

public enum ResultStatus {
    SUCCESS("成功",200),ERR_SYS("系统错误",500),PARAM_ERR("参数错误",400),OTHER_ERR("其他错误",999);

    private String message;
    private Integer status;

    ResultStatus (String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
