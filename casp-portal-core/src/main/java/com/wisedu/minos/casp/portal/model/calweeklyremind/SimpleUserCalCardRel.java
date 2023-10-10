package com.wisedu.minos.casp.portal.model.calweeklyremind;

import lombok.Data;

/**
 *
 * //todo 添加描述
 * @date 2023/3/29 12:24
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Data
public class SimpleUserCalCardRel {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 卡片wid
     */
    private String cardWid;

    /**
     * 提醒方式
     */
    private String remindWay;

}
