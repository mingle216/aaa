package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;

/**
 * 功能描述：卡片配置入参信息类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CardConfigReq
 * @Author: d
 * @Date: 2021/1/26
 */
public class CardConfigReq {

    /**
     * 卡片ID
     */
    private String cardId;
    /**
     * 卡片运行时ID
     */
    private String cardWid;
    /**
     * 卡片默认配置 config.json数据
     */
    private String defaultConfig;
    /**
     *  适配终端0：PC1：移动端
     */
    private Integer platformType;
    /**
     * 组件对象
     */
    private ComponentContainer componentContainer;

    private String templateWid;

    public Integer getPlatformType() {
        return platformType;
    }

    public void setPlatformType(Integer platformType) {
        this.platformType = platformType;
    }

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

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardWid() {
        return cardWid;
    }

    public void setCardWid(String cardWid) {
        this.cardWid = cardWid;
    }

    public String getTemplateWid() {
        return templateWid;
    }

    public void setTemplateWid(String templateWid) {
        this.templateWid = templateWid;
    }

    public CardConfigReq(String cardId, String cardWid, String defaultConfig, ComponentContainer componentContainer, Integer platformType, String templateWid) {
        this.cardId = cardId;
        this.cardWid = cardWid;
        this.defaultConfig = defaultConfig;
        this.platformType = platformType;
        this.componentContainer = componentContainer;
        this.templateWid = templateWid;
    }


}
