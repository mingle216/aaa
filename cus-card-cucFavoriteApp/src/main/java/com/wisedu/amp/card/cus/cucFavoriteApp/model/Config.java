package com.wisedu.amp.card.cus.cucFavoriteApp.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Config implements Serializable {

    /**
     * 高度
     */
    private ContainerType containerType;

    /**
     * 一行展示数量
     */
    private Integer columns;

    /**
     * 展示方式
     */
    private Integer linkDisplayRadio;

    /**
     * 行数
     */
    private Integer rows;

    /**
     * 图标
     */
    private Integer otherClassIcon;

    /**
     * 服务评价
     */
    private String serviceAppraise;
    /**
     * 管理按钮
     */
    private Integer showManage;


}
