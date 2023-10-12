package com.wisedu.amp.card.sys.cqfav.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @ClassName BatchFavoriteItemRequest
 * @Description //批量收藏服务事项
 * @Date 2021/3/26 10:55
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Data
public class BatchFavoriteItemRequest implements Serializable {
    private String userId;
    private List<String> itemIds;
}
