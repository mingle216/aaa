package com.wisedu.minos.casp.portal.model;

import java.util.List;

public class TemplateContent {
    private String name;
    private String iconUrl;
    private String code;
    private String defaultPageConfig;
    private String defaultPageConfigDesc;
    private String templateId;
    private Boolean isDefaultTemplate;
    private List<String> supportPlatform;

    public List<String> getSupportPlatform() {
        return supportPlatform;
    }

    public void setSupportPlatform(List<String> supportPlatform) {
        this.supportPlatform = supportPlatform;
    }

    public Boolean getDefaultTemplate() {
        return isDefaultTemplate;
    }

    public void setDefaultTemplate(Boolean defaultTemplate) {
        isDefaultTemplate = defaultTemplate;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDefaultPageConfig() {
        return defaultPageConfig;
    }

    public void setDefaultPageConfig(String defaultPageConfig) {
        this.defaultPageConfig = defaultPageConfig;
    }

    public String getDefaultPageConfigDesc() {
        return defaultPageConfigDesc;
    }

    public void setDefaultPageConfigDesc(String defaultPageConfigDesc) {
        this.defaultPageConfigDesc = defaultPageConfigDesc;
    }
}
