package com.wisedu.amp.card.cus.newsAnnouncement.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName SubscribedChannelVo
 * @Description 订阅频道的对象
 * @Date 2021/4/19 15:06
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Data
@Accessors(chain=true)
public class SubscribedChannelVo {
    /***
     * wid
     */
    private String wid;
    /***
     * 名字
     */
    private String name;
    /***
     * 是否为固定频道
     */
    private boolean fixed;


}
