package com.wisedu.amp.card.cus.link.model;

public class LinkInfo {
    //图标类别 icon/png
    private String iconOrPng;
    //示例：linkIcon: "iconfont icon-AllServices iconfont-font-size-CUS_CARD_LINK"
    private String linkIcon;
    //链接文本
    private String linkText;
    //链接地址
    private String linkUrl;
    //链接地址前缀 1 http://  2 https://
    private String linkUrlPre;
    //每个链接的唯一标识
    private String subWid;

    public String getIconOrPng() {
        return iconOrPng;
    }

    public void setIconOrPng(String iconOrPng) {
        this.iconOrPng = iconOrPng;
    }

    public String getLinkIcon() {
        return linkIcon;
    }

    public void setLinkIcon(String linkIcon) {
        this.linkIcon = linkIcon;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkUrlPre() {
        return linkUrlPre;
    }

    public void setLinkUrlPre(String linkUrlPre) {
        this.linkUrlPre = linkUrlPre;
    }

    public String getSubWid() {
        return subWid;
    }

    public void setSubWid(String subWid) {
        this.subWid = subWid;
    }
}
