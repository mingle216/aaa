package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.common.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 卡片集合(tab叠加)
 *
 */
public class LayoutCards implements Serializable {
    /**
     * 卡片集唯一标识
     */
    private String cardsWid = StringUtil.simpleUuid();

    /**
     * 导航位置
     */
    private TabLocation tabLocation;

    /**
     * 卡片列表
     */
    private List<LayoutCard> cards;

    public TabLocation getTabLocation() {
        return tabLocation;
    }

    public void setTabLocation(TabLocation tabLocation) {
        this.tabLocation = tabLocation;
    }

    public List<LayoutCard> getCards() {
        return cards;
    }

    public void setCards(List<LayoutCard> cards) {
        this.cards = cards;
    }

    public String getCardsWid() {
        return cardsWid;
    }

    public void setCardsWid(String cardsWid) {
        this.cardsWid = cardsWid;
    }

    enum TabLocation{
        TOP(0,"上"),RIGHT(1,"右"),BOTTOM(2,"下"),LEFT(3,"左");
        private int code;
        private String desc;

        TabLocation(int code,String desc){
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
