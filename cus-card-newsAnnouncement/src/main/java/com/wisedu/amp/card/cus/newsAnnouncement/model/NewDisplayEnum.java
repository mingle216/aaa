package com.wisedu.amp.card.cus.newsAnnouncement.model;

public enum NewDisplayEnum {
    /***
     * 展示近24h内容
     */
    ONE_DAY("1", "展示近24h内容"),
    /***
     * 展示近三天内容
     */
    THREE_DAYS("2", "展示近三天内容"),
    /***
     * 展示近一周内容
     */
    ONE_WEEK("3", "展示近一周内容"),
    /***
     * 隐藏new标签
     */
    HIDDEN("4", "隐藏new标签"),
    ;
    private String code;
    private String desc;

    NewDisplayEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
