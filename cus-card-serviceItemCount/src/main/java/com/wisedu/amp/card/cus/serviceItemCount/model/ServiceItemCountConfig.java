package com.wisedu.amp.card.cus.serviceItemCount.model;


import com.wisedu.amp.card.sys.serviceItemCount.util.ConstantUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ServiceItemCountConfig {

    private List<ItemList> itemList;

    public List<ItemList> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemList> itemList) {
        this.itemList = itemList;
    }

    public ServiceItemCountConfig(List<ItemList> itemList) {
        this.itemList = initConfigData();
    }

    public ServiceItemCountConfig() {
    }

    private List<ItemList> initConfigData(){
        List<ItemList> list = new ArrayList<>();
        for(ConstantUtil.SERVICE_ITEM serviceItem : ConstantUtil.SERVICE_ITEM.values()){
            ItemList itemList = new ItemList();
            itemList.setName(serviceItem.getName());
            itemList.setOrder(serviceItem.getIndex());
            itemList.setUse(serviceItem.isUse());//默认全部启用
            itemList.setCount(serviceItem.getCount());//初始化为0，后面调用接口赋值
            itemList.setCustomerName(serviceItem.getCustomerName());
            list.add(itemList);
        }
        return list;
    }

}
