package com.wisedu.amp.card.cus.serviceItemCategory1.model;

/**
 * 一级分类
 */
public class CategoryModel {

    private String categoryWid;

    private String categoryName;

    private String iconLink;

    private String isChecked;

    private String showCategoryName;

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

    public String getShowCategoryName() {
        return showCategoryName;
    }

    public void setShowCategoryName(String showCategoryName) {
        this.showCategoryName = showCategoryName;
    }
}
