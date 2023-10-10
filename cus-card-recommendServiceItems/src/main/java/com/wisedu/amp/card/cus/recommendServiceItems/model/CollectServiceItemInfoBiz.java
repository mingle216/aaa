package com.wisedu.amp.card.cus.recommendServiceItems.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CollectServiceInfoBiz
 * @Author: u
 * @Date: 2020/10/20
 */
@Getter
@Setter
public class CollectServiceItemInfoBiz implements Serializable {
    private String itemWid;
    private String itemName;
    private String iconLink;
    private String itemCategory;
    private String itemDept;
    private Boolean favorite;
}
