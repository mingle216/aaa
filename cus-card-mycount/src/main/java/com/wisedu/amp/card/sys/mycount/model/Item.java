package com.wisedu.amp.card.sys.mycount.model;

import java.util.List;

/**
 * @ClassName Item
 * @Description
 * @Date 2021/1/26 0026 16:11
 * @Author zkpu
 * @Version 1.0
 **/
public class Item {

    private String dataId;

    private List<MultiLangData> dataName;

    private int type;//1为预制，0为动态

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public List<MultiLangData> getDataName() {
        return dataName;
    }

    public void setDataName(
        List<MultiLangData> dataName) {
        this.dataName = dataName;
    }

    public Item(String dataId,
        List<MultiLangData> dataName, int type) {
        this.dataId = dataId;
        this.dataName = dataName;
        this.type = type;
    }
}
