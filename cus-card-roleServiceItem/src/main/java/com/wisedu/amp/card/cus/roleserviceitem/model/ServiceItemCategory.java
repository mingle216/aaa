package com.wisedu.amp.card.cus.roleserviceitem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceItemCategory {

    /**
     * wid
     */
    private String categoryWid;
    /**
     * 分类名称
     */
    private String categoryName;

    private List<ServiceItemCategory> children;

    private Integer total;
}
