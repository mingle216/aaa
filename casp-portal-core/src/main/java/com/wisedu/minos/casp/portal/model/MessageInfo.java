package com.wisedu.minos.casp.portal.model;

import lombok.Data;

/**
 * 未读消息
 */
@Data
public class MessageInfo {

    /**
     * 收件箱记录id
     */
    private String id;
    /**
     * 消息id
     */
    private String msgId;

    /**
     * 消息标题
     */
    private String msgTitle;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 是否已读（1：是，0：否）
     */
    private Integer isReaded;

    /**
     * 消息发送时间
     */
    private String msgSendDate;

    private String urlDesc;

    private String appName;


}
