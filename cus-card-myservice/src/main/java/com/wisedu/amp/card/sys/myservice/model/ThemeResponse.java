package com.wisedu.amp.card.sys.myservice.model;

import java.util.List;

public class ThemeResponse extends AmpBaseResponse{

    private List<ThemeInfo> data;

    @Override
    public List<ThemeInfo> getData() {
        return data;
    }

    public void setData(List<ThemeInfo> data) {
        this.data = data;
    }
}
