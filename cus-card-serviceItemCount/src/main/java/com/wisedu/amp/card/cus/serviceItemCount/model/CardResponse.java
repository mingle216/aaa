package com.wisedu.amp.card.cus.serviceItemCount.model;

import java.util.List;

/**
 * @ClassName CardResponse
 * @Description
 * @Date 2021/1/28 0028 10:49
 * @Author zkpu
 * @Version 1.0
 **/
public class CardResponse {

    private int columns;

    private List<ItemStatisticsInfo> serviceItemList;

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<ItemStatisticsInfo> getServiceItemList() {
        return serviceItemList;
    }

    public void setServiceItemList(
        List<ItemStatisticsInfo> serviceItemList) {
        this.serviceItemList = serviceItemList;
    }
}
