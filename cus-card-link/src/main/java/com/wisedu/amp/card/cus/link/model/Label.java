package com.wisedu.amp.card.cus.link.model;

import java.util.List;

public class Label {

    private String iconType;
    private String iconUrl;
    private String url;
    private String type;
    private boolean child;
    private List<Label> linkList;

    private List<LangResoure> label;

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<LangResoure> getLabel() {
        return label;
    }

    public void setLabel(List<LangResoure> label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isChild() {
        return child;
    }

    public void setChild(boolean child) {
        this.child = child;
    }

    public List<Label> getLinkList() {
        return linkList;
    }

    public void setLinkList(List<Label> linkList) {
        this.linkList = linkList;
    }
}
