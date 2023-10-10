package com.wisedu.amp.card.sys.searchResults.model.faq;

public class FaqRequest {
    private String keyword;
    private Integer pageNumber;
    private Integer pageSize;
    private String userWid;
    private String lang;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getUserWid () {
        return userWid;
    }

    public void setUserWid (String userWid) {
        this.userWid = userWid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }


}
