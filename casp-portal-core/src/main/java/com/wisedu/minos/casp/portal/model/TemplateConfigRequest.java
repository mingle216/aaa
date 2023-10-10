package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title TemplateConfigRequest
 * @Author: d
 * @Date: 2021/1/15
 */
public class TemplateConfigRequest {
    /**
     * 模板id
     */
    private String templateId;
    /**
     * 模板wid
     */
    private String templateWid;
    /**
     * 模板默认配置 config.json数据
     */
    private String defaultConfig;
    /**
     * 组件对象
     */
    private ComponentContainer componentContainer;

    public ComponentContainer getComponentContainer () {
        return componentContainer;
    }

    public void setComponentContainer (ComponentContainer componentContainer) {
        this.componentContainer = componentContainer;
    }

    public String getDefaultConfig () {
        return defaultConfig;
    }

    public void setDefaultConfig (String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public String getTemplateId () {
        return templateId;
    }

    public void setTemplateId (String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateWid () {
        return templateWid;
    }

    public void setTemplateWid (String templateWid) {
        this.templateWid = templateWid;
    }

    public TemplateConfigRequest(String templateId,String templateWid,String defaultConfig,ComponentContainer componentContainer){
        this.templateId=templateId;
        this.templateWid=templateWid;
        this.defaultConfig=defaultConfig;
        this.componentContainer=componentContainer;
    }
}
