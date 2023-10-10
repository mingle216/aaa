package com.wisedu.amp.card.cus.hotApp.model;

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
     * 最多展示数量
     */
    private Integer rows;

    /**
     *  一行展示数量
     */
    private Integer columns;

    /**
     * 热门服务时间
     */
    private Integer hotServiceTimeRadio;

    /**
     * 服务评价
     */
    private String serviceAppraise;


}
