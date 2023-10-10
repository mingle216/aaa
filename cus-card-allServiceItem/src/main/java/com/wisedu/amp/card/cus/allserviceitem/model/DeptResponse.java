package com.wisedu.amp.card.cus.allserviceitem.model;

import java.util.List;

public class DeptResponse extends AmpBaseResponse{

    private List<DeptInfo> data;

    @Override
    public List<DeptInfo> getData() {
        return data;
    }

    public void setData(List<DeptInfo> data) {
        this.data = data;
    }
}
