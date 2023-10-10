package com.wisedu.amp.card.sys.mygorydetail.model;

import java.util.List;

public class ServiceItemModel {

    private String subjectWid;

    private String subjectName;

    private String iconLink;

    private List<ItemModel> itemList;

    public String getSubjectWid() {
        return subjectWid;
    }

    public void setSubjectWid(String subjectWid) {
        this.subjectWid = subjectWid;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<ItemModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemModel> itemList) {
        this.itemList = itemList;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }
}
