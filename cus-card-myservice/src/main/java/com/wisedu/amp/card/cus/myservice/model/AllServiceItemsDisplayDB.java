package com.wisedu.amp.card.cus.myservice.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName AllServiceItemsDisplayDB
 * @Description 数据库存放全部服务事项配置的json对象
 * @Date 2021/6/7 17:42
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Getter
@Setter
public class AllServiceItemsDisplayDB {
    /***
     * 展示方式
     */
    private String displayType;

    /**
     * 筛选项展示信息
     */
    private List<String> filterFields;

    /**
     * 服务事项列表展示分组
     */
    private String itemDisplayGroups;

    /**
     * 列表展示信息
     */
    private List<String> itemDisplayInfo;
}
