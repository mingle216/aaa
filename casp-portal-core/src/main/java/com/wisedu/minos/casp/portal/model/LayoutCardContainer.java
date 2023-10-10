package com.wisedu.minos.casp.portal.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.List;

public class LayoutCardContainer implements Serializable {
    private List<LayoutRow> rows;

    public List<LayoutRow> getRows() {
        return rows;
    }

    public void setRows(List<LayoutRow> rows) {
        this.rows = rows;
    }

    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }
}
