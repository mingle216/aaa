package com.wisedu.amp.card.cus.recommendapp.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

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
    private Integer isShowAllService;

    /**
     *  一行展示数量
     */
    private Integer columns;

    private List<String> addServiceList;

    private String allServiceUrl = "/apps";

    /**
     * 服务评价
     */
    private String serviceAppraise;
    /**
     * 是否开启自动推荐
     */
    private String autoRecommend;

}
