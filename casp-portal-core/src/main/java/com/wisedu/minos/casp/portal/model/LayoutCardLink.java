package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.i18n.Lang;

import java.io.Serializable;
import java.util.List;

/**
 * 卡片链接
 */
public class LayoutCardLink implements Serializable {
    /**
     * 链接图标
     */
    private String linkIcon;
    /**
     * 链接文字
     */
    private String linkTitle;
    /**
     * 国际化信息
     */
    private List<Lang> linkLang;
    /**
     * 链接地址
     */
    private String linkUrl;

    public String getLinkIcon() {
        return linkIcon;
    }

    public void setLinkIcon(String linkIcon) {
        this.linkIcon = linkIcon;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public List<Lang> getLinkLang() {
        return linkLang;
    }

    public void setLinkLang(List<Lang> linkLang) {
        this.linkLang = linkLang;
    }
}
