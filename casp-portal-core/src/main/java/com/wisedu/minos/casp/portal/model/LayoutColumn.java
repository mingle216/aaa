package com.wisedu.minos.casp.portal.model;

import java.io.Serializable;
import java.util.List;

/**
 * 列元素
 * 列元素有宽度(卡片区域的x/24)
 * 列元素中可以放行元素或一张卡片或卡片集合(tab叠加)，三选一
 */
public class LayoutColumn implements Serializable {
    /**
     * 宽度
     */
    private Integer width;
    /**
     * 行元素列表
     */
    private List<LayoutRow> rows;
    /**
     * 卡片集合(tab叠加)
     */
    private LayoutCards cards;
    /**
     * 卡片
     */
    private LayoutCard card;

    public LayoutCard getCard() {
        return card;
    }

    public void setCard(LayoutCard card) {
        this.card = card;
    }

    public LayoutCards getCards() {
        return cards;
    }

    public void setCards(LayoutCards cards) {
        this.cards = cards;
    }

    public List<LayoutRow> getRows() {
        return rows;
    }

    public void setRows(List<LayoutRow> rows) {
        this.rows = rows;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    enum ColumnType{
        NORMAL(0,"普通，单张卡片"),MULTIPLE(1,"卡片叠加");
        private int code;
        private String desc;

        ColumnType(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
