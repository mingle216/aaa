package com.wisedu.amp.card.sys.mygory.model;

public class ConfigInfo {

    //初始化登录前选项
    private String preLogin = "0";
    //初始化登录后选项
    private String afterLogin = "0";
    //初始化角色展现方式选项
    private String showRoleRules = "1";
    //分类维度展示信息选项
    private String categoryRules;
    //初始化分类内容
    private String contentRules = "1";
    //初始化展示行数
    private int showRows = 2;
    //初始化一行展示数量
    private int  rowNumber = 4;

    public String getPreLogin() {
        return preLogin;
    }

    public void setPreLogin(String preLogin) {
        this.preLogin = preLogin;
    }

    public String getAfterLogin() {
        return afterLogin;
    }

    public void setAfterLogin(String afterLogin) {
        this.afterLogin = afterLogin;
    }

    public String getShowRoleRules() {
        return showRoleRules;
    }

    public void setShowRoleRules(String showRoleRules) {
        this.showRoleRules = showRoleRules;
    }

    public String getCategoryRules() {
        return categoryRules;
    }

    public void setCategoryRules(String categoryRules) {
        this.categoryRules = categoryRules;
    }

    public String getContentRules() {
        return contentRules;
    }

    public void setContentRules(String contentRules) {
        this.contentRules = contentRules;
    }

    public int getShowRows() {
        return showRows;
    }

    public void setShowRows(int showRows) {
        this.showRows = showRows;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }
}
