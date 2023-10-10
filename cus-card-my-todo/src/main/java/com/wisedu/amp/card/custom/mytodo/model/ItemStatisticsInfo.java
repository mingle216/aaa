package com.wisedu.amp.card.custom.mytodo.model;

import java.util.List;

/**
 * @ClassName ItemStatisticsInfo2
 * @Description
 * @Date 2021/1/27 0027 10:24
 * @Author zkpu
 * @Version 1.0
 **/
public class ItemStatisticsInfo {

    private String dataId;

    private List<MultiLangData> dataName;

    private String count;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
