package com.wisedu.amp.card.cus.allserviceitem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceItem {

    /**
     * 服务事项wid
     */
    private String itemWid;
    /**
     * 图标
     */
    private String iconLink;
    /**
     * 服务事项名称
     */
    private String itemName;
    /**
     * 名称拼音
     */
    private String itemPinYin;
    /**
     * 访问数量
     */
    private Integer visitCount;
    /**
     * 服务主题
     */
    private String itemCategory;
    /**
     * 责任部门
     */
    private String itemDept;
    /**
     * 二级责任部门
     */
    private String itemSecondDept;
    /**
     * 评价分数
     */
    private String score;
    /**
     * 首字母
     */
    private String headChar;
    /**
     * 所属二级分类
     */
    private List<String> groupCategoryList;
    /**
     * 是否收藏
     */
    private boolean favorite;

    /**
     * 自定义：排序值
     */
    private String order;

}
