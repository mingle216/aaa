package com.wisedu.amp3compatible.controller;

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

}
