package com.wisedu.amp.card.cus.newsAnnouncement.model;

import java.io.Serializable;

public class AppNewsInfo implements Serializable {
    private String wid;
    private String title ;
    private String publishTime;
    private String linkUrl;
    private String content;
    private String itemType;

public String getWid() {
    return wid;
}

public void setWid(String wid) {
    this.wid = wid;
}

public String getTitle() {
    return title ;
}

public void setTitle(String title ) {
    this.title  = title ;
}

public String getPublishTime() {
    return publishTime;
}

public void setPublishTime(String publishTime) {
    this.publishTime = publishTime;
}

public String getLinkUrl() {
    return linkUrl;
}

public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
}

public String getContent(){ return  content;}

public void setContent(String content){ this.content = content;}

public String getItemType(){ return  itemType;}

public void setItemType(String itemType){ this.itemType = itemType;}

}
