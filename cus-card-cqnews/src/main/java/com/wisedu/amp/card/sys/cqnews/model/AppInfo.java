package com.wisedu.amp.card.sys.cqnews.model;

import java.io.Serializable;
import java.util.List;

public class AppInfo implements Serializable {
        private String typeName;
        private int dayCount ;
        private boolean flag;
        private boolean hasChild;
        private String moreNameAll;
        private String moreLinkAll;
        private List<AppNewsInfo> newsList;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getDayCount() {
        return dayCount ;
    }

    public void setDayCount(Integer dayCount ) {
        this.dayCount  = dayCount ;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }

    public String getMoreNameAll(){ return  moreNameAll;}

    public void setMoreNameAll(String moreNameAll){ this.moreNameAll = moreNameAll;}

    public String getMoreLinkAll(){ return  moreLinkAll;}

    public void setMoreLinkAll(String moreLinkAll){ this.moreLinkAll = moreLinkAll;}

    public List<AppNewsInfo> getNewsList(){ return  newsList;}

    public void setNewsList(List<AppNewsInfo> newsList){ this.newsList = newsList;}
}
