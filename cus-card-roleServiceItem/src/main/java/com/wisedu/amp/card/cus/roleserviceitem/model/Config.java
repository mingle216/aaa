package com.wisedu.amp.card.cus.roleserviceitem.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Config implements Serializable {

    /**
     * 搜索框
     */
    private String search;
    /***
     * 全部服务事项
     */
    AllServiceItemsDisplayDB allServiceItemsDisplay;
    /**
     * 事项显示方式(0-显示全部 1-显示与我相关)
     */
    private Integer authorityEnabled;
}
