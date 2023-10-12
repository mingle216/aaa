package com.wisedu.amp.card.sys.cqnews.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName NewsColumnsAndChannelVo
 * @Description 新闻栏目和渠道
 * @Date 2021/4/16 14:30
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Data
@Accessors(chain=true)
public class NewsColumnsAndChannelVo implements  Comparable<NewsColumnsAndChannelVo> {
    /***
     * id
     */
    private String id;
    /***
     * 名字
     */
    private String name;
    /***
     * 父级id
     */
    private String parentId;
    /***
     * 是否是频道
     * false 为栏目
     */
    private boolean isChannel;
    /***
     * 是否固定
     * 只有频道可以
     */
    private boolean isFixed;
    /***
     * 同级排序
     * <p/>
     *  
     * @param null
     * @throws 
     * @return 
     * @date 2021/5/14 17:37
     */  
    private int sort;

    List<NewsColumnsAndChannelVo> children;

    @Override
    public int compareTo(NewsColumnsAndChannelVo o) {
        if (o.getIsChannel() != this.getIsChannel()) {
            return o.getIsChannel() ? -1 : 1;
        } else {
            return  o.getSort()-this.getSort();
        }
    }

    public static void main(String[] args) {
        NewsColumnsAndChannelVo v1 = new NewsColumnsAndChannelVo()
            .setSort(1)
            .setName("1")
            .setIsChannel(true)
            .setParentId("-1");

        NewsColumnsAndChannelVo v2 = new NewsColumnsAndChannelVo()
            .setSort(2)
            .setName("2")
            .setIsChannel(true)
            .setParentId("-1");

        NewsColumnsAndChannelVo v3 = new NewsColumnsAndChannelVo()
            .setSort(3)
            .setName("3")
            .setIsChannel(false)
            .setParentId("-1");

        List<NewsColumnsAndChannelVo> list = new ArrayList<>();
        list.add(v1);
        list.add(v2);
        list.add(v3);
        Collections.sort(list);
    }
}
