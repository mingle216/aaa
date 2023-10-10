package com.wisedu.minos.casp.portal.model;

import java.util.Map;

/**
 * 功能描述：卡片入参信息类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CardAjaxRequest
 * @Author: jcx
 * @Date: 2020/8/03
 */
public class CardAjaxRequest {
    /**
     * 卡片ID
     */
    private String cardId;
    /**
     * 卡片运行时ID
     */
    private String cardWid;
    /**
     * 刷新目标区域
     */
    private String target;
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
    private Integer platform = 1;
    /**
     * 参数
     */
    private Map<String,String> param;

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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
