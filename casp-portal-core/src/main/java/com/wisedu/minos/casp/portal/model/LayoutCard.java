package com.wisedu.minos.casp.portal.model;

import java.io.Serializable;

/**
 * 卡片
 */
public class LayoutCard implements Serializable {
    /**
     * 运行时id
     */
    private String cardWid;
    /**
     * 卡片id
     */
    private String cardId;
    /**
     * 是否展示标题栏
     */
    private boolean showTitle;
    /**
     * 是否启用
     */
    private boolean enabled;
    /**
     * 是否启用
     */
    private Integer cardStatus;
    /**
     * 标题栏
     */
    private LayoutCardTitle layoutCardTitle;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    public String getCardWid() {
        return cardWid;
    }

    public void setCardWid(String cardWid) {
        this.cardWid = cardWid;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public LayoutCardTitle getLayoutCardTitle() {
        return layoutCardTitle;
    }

    public void setLayoutCardTitle(LayoutCardTitle layoutCardTitle) {
        this.layoutCardTitle = layoutCardTitle;
    }

    public Integer getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }
}
