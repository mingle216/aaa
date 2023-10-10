package com.wisedu.amp.card.sys.searchResults.model.faq;

import java.util.List;

public class FaqResponse {

    private Integer totalSize;
    private List<Faq> datas;

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public List<Faq> getDatas() {
        return datas;
    }

    public void setDatas(List<Faq> datas) {
        this.datas = datas;
    }
}
