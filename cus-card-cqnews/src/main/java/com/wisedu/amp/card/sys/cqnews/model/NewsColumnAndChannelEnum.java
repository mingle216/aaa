package com.wisedu.amp.card.sys.cqnews.model;
/**
 * NewsColumnAndChannelEnum
 * 频道栏目枚举类
 *
 * @author 01119092
 * @date 2021/4/16 14:41
 * Copyright (c) 2018 wisedu
 */
public enum NewsColumnAndChannelEnum {
    /***
     * 栏目
     */
    COLUMN(0),
    /***
     * 频道
     */
    CHANNEL(1),
    ;
    private int code;

    NewsColumnAndChannelEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
