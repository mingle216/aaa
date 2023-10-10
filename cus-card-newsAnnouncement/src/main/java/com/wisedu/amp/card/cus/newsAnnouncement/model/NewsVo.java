package com.wisedu.amp.card.cus.newsAnnouncement.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName NewsVo
 * @Description //TODO
 * @Date 2021/4/20 10:43
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Data
@Accessors(chain = true,fluent = true)
public class NewsVo {
    /***
     * 标题
     */
    private String title;
    /***
     * 频道
     */
    private String channelName;
    /***
     * 栏目
     */
    private String columnName;
    /***
     * 发布时间
     */
    private String pubTime;
    /***
     * 图片url
     */
    private String  picUrl;

    /***
     * 是否展示new标签
     */
    private boolean newDisplay;
    /***
     * 日期
     */
    private String day;
    /***
     * 月份
     */
    private String month;
    /***
     * 是否置顶
     */
    private int isTop;
    /***
     * 内容
     */
    private String contents;
    /***
     * 概览
     */
    private String abstractVal;
    /***
     * url
     */
    private String url;
    /***
     * 发布人
     */
    private String author;

    /**
     * wid
     */
    private String wid;

    /**
     * 已读未读标识 0 未读 1 已读
     */
    private boolean isRead;

    /**
     * 是否开启已读未读
     */
    private boolean isEnableRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isEnableRead() {
        return isEnableRead;
    }

    public void setEnableRead(boolean enableRead) {
        isEnableRead = enableRead;
    }

    public String getWid () {
        return wid;
    }

    public void setWid (String wid) {
        this.wid = wid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public boolean isNewDisplay() {
        return newDisplay;
    }

    public void setNewDisplay(boolean newDisplay) {
        this.newDisplay = newDisplay;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getAbstractVal() {
        return abstractVal;
    }

    public void setAbstractVal(String abstractVal) {
        this.abstractVal = abstractVal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
