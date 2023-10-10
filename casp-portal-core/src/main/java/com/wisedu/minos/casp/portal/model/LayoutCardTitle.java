package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.i18n.Lang;

import java.io.Serializable;
import java.util.List;

/**
 * 卡片标题栏
 */
public class LayoutCardTitle implements Serializable {

    /**
     * 卡片标题
     */
    private String cardTitle;


    /**
     * 卡片标题国际化属性
     */
    private List<Lang> cardTitleLang;


    /**
     * 是否展示角标
     */
    private boolean showMark;

    /**
     * 角标类型: 0:红点 1:数字
     */
    private int markType;

    /**
     * 链接
     */
    private List<LayoutCardLink> layoutCardLink;

    public List<LayoutCardLink> getLayoutCardLink() {
        return layoutCardLink;
    }

    public void setLayoutCardLink(List<LayoutCardLink> layoutCardLink) {
        this.layoutCardLink = layoutCardLink;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public boolean isShowMark() {
        return showMark;
    }

    public void setShowMark(boolean showMark) {
        this.showMark = showMark;
    }

    public int getMarkType() {
        return markType;
    }

    public void setMarkType(int markType) {
        this.markType = markType;
    }

    public List<Lang> getCardTitleLang() {
        return cardTitleLang;
    }

    public void setCardTitleLang(List<Lang> cardTitleLang) {
        this.cardTitleLang = cardTitleLang;
    }
}
