package com.wisedu.amp.card.cus.recommendServiceItems.model;

import com.wisedu.minos.casp.portal.model.RecommendServiceItems;
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


    private List<String> addServiceItemList;

    /**
     * 获取配置的推荐应用
     */
    private List<RecommendServiceItems> appInfoBizList;

    /**
     * 是否与我相关
     */
    private Integer isRelate;

    /**
     * 是否展示在线办理
     */
    private int showDealOnline;

    /**
     * 是否开启自动推荐
     */
    private String autoRecommend;
}
