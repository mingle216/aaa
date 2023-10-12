package com.wisedu.amp.card.sys.cqfav.model;

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
     * 列表展示信息
     */
    private String showItem;

    /**
     * 最多展示数量
     */
    private Integer rows;

    /**
     *  一行展示数量
     */
    private Integer columns;

    /**
     * 最多展示数量
     */
    private Integer isShowAllService;

    /**
     * 是否针对关联用户组过滤,0否，1是
     */
    private String userfilter;

    private List<String> addServiceItemList;

    /**
     * 管理按钮
     */
    private Integer showManage;

    /**
     * 是否展示在线办理
     */
    private int showDealOnline;

}
