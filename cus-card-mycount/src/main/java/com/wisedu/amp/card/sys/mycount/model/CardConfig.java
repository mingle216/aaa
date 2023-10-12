package com.wisedu.amp.card.sys.mycount.model;

import java.util.List;

/**
 * @ClassName CardConfig
 * @Description
 * @Date 2021/1/27 0027 16:19
 * @Author zkpu
 * @Version 1.0
 **/
public class CardConfig {

    private int select;

    private List<ServiceItem> dataItemList;

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public List<ServiceItem> getDataItemList() {
        return dataItemList;
    }

    public void setDataItemList(
        List<ServiceItem> dataItemList) {
        this.dataItemList = dataItemList;
    }
}
