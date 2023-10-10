package com.wisedu.amp.card.custom.mytodo.model;

import java.io.Serializable;
import java.util.List;

public class FavoriteResponse extends AmpBaseResponse implements Serializable {
    private List<FavoriteInfo> data;

    public List<FavoriteInfo> getData() {
        return data;
    }

    public void setData(List<FavoriteInfo> data) {
        this.data = data;
    }
}
