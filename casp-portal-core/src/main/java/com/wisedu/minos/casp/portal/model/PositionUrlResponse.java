package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.common.result.ResultData;

public class PositionUrlResponse extends ResultData {

    private String data;

    @Override
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
