package com.wisedu.amp.card.cus.allserviceitem.enums;
/**
 * DisplayTypeEnum
 * z展示方式<p/>
 *
 * @author 01119092
 * @date 2021/6/8 10:27
 * Copyright (c) 2018 wisedu
 */
public enum DisplayTypeEnum {
    /***
     *
     */
    LIST("1","列表"),
    TILE("0","平铺"),
    ;
    private  String code;
    private  String desc;

    DisplayTypeEnum(String code, String desc) {
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
