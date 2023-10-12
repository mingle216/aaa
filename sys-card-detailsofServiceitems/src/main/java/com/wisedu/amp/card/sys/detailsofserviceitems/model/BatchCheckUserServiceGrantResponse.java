package com.wisedu.amp.card.sys.detailsofserviceitems.model;

import java.util.List;

/**
 * @author 01319115
 */
public class BatchCheckUserServiceGrantResponse extends AmpBaseResponse {
    private List<ServiceGrantModel> data;

    public List<ServiceGrantModel> getData() {
        return data;
    }

    public void setData(List<ServiceGrantModel> data) {
        this.data = data;
    }
}
