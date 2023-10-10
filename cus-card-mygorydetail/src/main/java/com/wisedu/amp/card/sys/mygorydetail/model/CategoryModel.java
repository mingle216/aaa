package com.wisedu.amp.card.sys.mygorydetail.model;

/**
 * 一级分类
 */
public class CategoryModel {

    private String categoryWid;

    private String categoryName;

    private String iconLink;

    private String isChecked;

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

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }
}
