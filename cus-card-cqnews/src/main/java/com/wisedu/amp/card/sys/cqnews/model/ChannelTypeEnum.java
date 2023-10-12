package com.wisedu.amp.card.sys.cqnews.model;

import lombok.Getter;

/**
 * ChannelTypeEnum
 * 通道类型<p/>
 *
 * @author 01119092
 * @date 2021/4/19 15:12
 * Copyright (c) 2018 wisedu
 */
@Getter
public enum ChannelTypeEnum {
    /***
     * 订阅频道
     */
    ORDERED(1),
    /***
     * 固定频道
     */
    FIXED(0),
    ;
    private int code;

    ChannelTypeEnum(int code) {
        this.code = code;
    }
}
