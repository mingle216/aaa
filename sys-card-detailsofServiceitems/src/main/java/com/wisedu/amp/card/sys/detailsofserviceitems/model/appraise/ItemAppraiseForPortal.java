package com.wisedu.amp.card.sys.detailsofserviceitems.model.appraise;

import java.util.List;
import lombok.Data;

/**
 * @ClassName ItemAppraiseForPortal
 * @Description 门户使用的评价
 * @Date 2021/6/15 6:32
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Data
public class ItemAppraiseForPortal {
    private List<ItemAppraiseDetailList> itemAppraiseDetailList;
    private List<ItemDimenAppraiseList> itemDimenAppraiseDetailList;
    private Integer badAppraiseHidden;
}
