package com.wisedu.amp.card.sys.mynews.model;

public enum TimeDisplayEnum {
    /***
     *精确到月
     */
    MONTH_FORMAT("1"),
    /**
     * 精确到日
     */
    DAY_FORMAT("2"),
    /***
     *精确到时分
     */
    MINUTE_FORMAT("3"),
    /***
     *隐藏时间
     */
    HIDDEN("0"),
    ;
    private String code;

    TimeDisplayEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
