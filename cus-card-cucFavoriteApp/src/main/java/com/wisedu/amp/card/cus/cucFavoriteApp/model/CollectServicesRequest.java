package com.wisedu.amp.card.cus.cucFavoriteApp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CollectServicesRequest {
    private String userId;
    /**
     * 服务id列表
     */
    private List<String> serviceItemIds;
}
