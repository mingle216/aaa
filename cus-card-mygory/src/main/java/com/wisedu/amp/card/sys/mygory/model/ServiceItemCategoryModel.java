package com.wisedu.amp.card.sys.mygory.model;

import java.io.Serializable;
import java.util.List;

public class ServiceItemCategoryModel implements Serializable {

    private String subjectWid;

    private String subjectName;

    private String iconLink;

    private String picLink;

    private List<ItemModel> itemList;

    private List<ServiceItemModel> children;

    private String secondCategory;

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

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public List<ItemModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemModel> itemList) {
        this.itemList = itemList;
    }

    public List<ServiceItemModel> getChildren() {
        return children;
    }

    public void setChildren(List<ServiceItemModel> children) {
        this.children = children;
    }

    public String getSecondCategory() {
        return secondCategory;
    }

    public void setSecondCategory(String secondCategory) {
        this.secondCategory = secondCategory;
    }

    public String getPicLink() {
        return picLink;
    }

    public void setPicLink(String picLink) {
        this.picLink = picLink;
    }
}
