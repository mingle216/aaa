package com.wisedu.amp.card.cus.serviceItemCategoryDetail.model;



import java.util.List;

public class ServiceDetailResponse extends AmpBaseResponse {
    List<ServiceDetail> data;

    @Override
    public List<ServiceDetail> getData() {
        return data;
    }

    public void setData(List<ServiceDetail> data) {
        this.data = data;
    }
}
