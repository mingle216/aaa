package com.wisedu.amp.card.sys.cqfav.model;


import java.io.Serializable;

public class ConfigInfo implements Serializable {
    //初始化展示数量
    private int inputNum=10;
    //初始化展示高度
    private int height;
    //初始化卡片高度选项
    private String heightFlag="1";
    private String configure="0;2";//初始化服务配置事项
    //初始化任务来源列表
    private String sourceVal="1";
    //服务事项列表图标
    private String iconShow="1";

    public int getInputNum() {
        return inputNum;
    }

    public void setInputNum(int inputNum) {
        this.inputNum = inputNum;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getConfigure() {
        return configure;
    }

    public void setConfigure(String configure) {
        this.configure = configure;
    }

    public String getSourceVal() {
        return sourceVal;
    }

    public void setSourceVal(String sourceVal) {
        this.sourceVal = sourceVal;
    }

    public String getHeightFlag() {
        return heightFlag;
    }

    public void setHeightFlag(String heightFlag) {
        this.heightFlag = heightFlag;
    }

    public String getIconShow() {
        return iconShow;
    }

    public void setIconShow(String iconShow) {
        this.iconShow = iconShow;
    }
}
