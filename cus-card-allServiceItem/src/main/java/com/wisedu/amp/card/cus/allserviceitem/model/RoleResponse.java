package com.wisedu.amp.card.cus.allserviceitem.model;


import java.util.List;

public class RoleResponse extends AmpBaseResponse{
    List<Role> data;

    @Override
    public List<Role> getData() {
        return data;
    }

    public void setData(List<Role> data) {
        this.data = data;
    }
}
