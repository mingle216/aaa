package com.wisedu.amp.card.cus.myservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceItemCategoryRequest {
    /**
     * 用户id 游客为guest
     */
    private String userId;

    private Integer isRelate;

    private String categoryWids;

    private String lang;
}
