package com.wisedu.amp.card.sys.cqnews.model;

import java.io.Serializable;
import java.util.List;

public class ConfigInfo implements Serializable {
    private String typeName;
    private String typeId;
    //初始化展示高度
    private int height = 466;
    //初始化卡片高度选项
    private String heightFlag = "1";
    private String configure;
    //展示方式是否使用
    private String titleFlag = "1";
    //展示方式 1 仅标题、时间  2 标题、时间及摘要
    private String showType = "1";
    //通知公告展示数量
    private int pageSize = 6;
    //展示列数 1 一列  2 两列  3 三列
    private String columns = "1";
    //新闻来源
    private String source = "11";
    //new标签展示规则
    private String newDisplay = "1";
    //时间格式
    private String timeDisplay = "3";

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNewDisplay() {
        return newDisplay;
    }

    public void setNewDisplay(String newDisplay) {
        this.newDisplay = newDisplay;
    }

    public String getTimeDisplay() {
        return timeDisplay;
    }

    public void setTimeDisplay(String timeDisplay) {
        this.timeDisplay = timeDisplay;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId ;
    }

    public void setTypeId(String typeId ) {
        this.typeId  = typeId ;
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

    public String getHeightFlag() {
        return heightFlag;
    }

    public void setHeightFlag(String heightFlag) {
        this.heightFlag = heightFlag;
    }

    public String getTitleFlag() {
        return titleFlag;
    }

    public void setTitleFlag(String titleFlag) {
        this.titleFlag = titleFlag;
    }
}
