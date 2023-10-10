package com.wisedu.amp.card.cus.serviceItemCategory1.model;

import java.io.Serializable;

public class ServiceItemCategoryResponse extends AmpBaseResponse implements Serializable {

   private Object data;

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }
}
