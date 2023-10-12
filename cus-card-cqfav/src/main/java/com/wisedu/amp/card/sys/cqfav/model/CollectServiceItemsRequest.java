package com.wisedu.amp.card.sys.cqfav.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectServiceItemsRequest {
    private String userId;
    /**
     * 服务id列表
     */
    private List<String> itemIds;
}
