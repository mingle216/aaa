package com.wisedu.amp.card.sys.searchResults.model.serviceItem;

import com.wisedu.amp.card.sys.searchResults.model.AmpBaseResponse;

public class CheckUserServiceGrantResp extends AmpBaseResponse {
    private boolean data;

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}
