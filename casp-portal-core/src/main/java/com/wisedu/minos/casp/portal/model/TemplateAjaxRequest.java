package com.wisedu.minos.casp.portal.model;

import java.io.Serializable;
import java.util.Map;

public class TemplateAjaxRequest implements Serializable {
    /**
     * 方法
     */
    private String method;
    /**
     * 语言
     */
    private String lang;
    /**
     * 运行平台 0:PC 1:mobile
     */
    private Integer platformType = 1;
    /**
     * 参数
     */
    private Map<String,String> param;

    private UserInfo currentUser;

    public UserInfo getCurrentUser () {
        return currentUser;
    }

    public void setCurrentUser (UserInfo currentUser) {
        this.currentUser = currentUser;
    }

    public Integer getPlatformType() {
        return platformType;
    }

    public void setPlatformType(Integer platformType) {
        this.platformType = platformType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }
}
