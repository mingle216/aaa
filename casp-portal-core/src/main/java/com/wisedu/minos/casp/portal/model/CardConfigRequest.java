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
 * @title CardConfigRequest
 * @Author: jcx
 * @Date: 2020/8/03
 */
public class CardConfigRequest {
    /**
     * 卡片ID
     */
    private String cardId;
    /**
     * 卡片运行时ID
     */
    private String cardWid;
    /**
     * 参数
     */
    private String param;

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

    public String getParam () {
        return param;
    }

    public void setParam (String param) {
        this.param = param;
    }
}
