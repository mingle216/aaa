package com.wisedu.amp.card.cus.serviceItemCount.model;

import java.util.List;

/**
 * @ClassName ServiceItem
 * @Description
 * @Date 2021/1/27 0027 16:24
 * @Author zkpu
 * @Version 1.0
 **/
public class ServiceItem {

    private String dataId;

    private List<MultiLangData> dataName;

    private String oldName;

    private int type;

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

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
}
