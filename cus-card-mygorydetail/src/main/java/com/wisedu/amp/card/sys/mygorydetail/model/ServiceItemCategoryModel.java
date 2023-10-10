package com.wisedu.amp.card.sys.mygorydetail.model;

import java.io.Serializable;
import java.util.List;

public class ServiceItemCategoryModel implements Serializable {

    private String categoryWid;

    private String categoryName;

    private String iconLink;

    private int total;

    private List<ServiceItemCategoryModel> children;

    public String getCategoryWid() {
        return categoryWid;
    }

    public void setCategoryWid(String categoryWid) {
        this.categoryWid = categoryWid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ServiceItemCategoryModel> getChildren() {
        return children;
    }

    public void setChildren(List<ServiceItemCategoryModel> children) {
        this.children = children;
    }
}
