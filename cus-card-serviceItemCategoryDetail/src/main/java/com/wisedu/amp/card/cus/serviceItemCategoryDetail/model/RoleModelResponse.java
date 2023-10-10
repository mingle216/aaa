package com.wisedu.amp.card.cus.serviceItemCategoryDetail.model;

import java.util.List;

/**
 * @author yyu
 * @date 2021/1/5
 */
public class RoleModelResponse extends AmpBaseResponse{

    List<RoleModel> data;

    @Override
    public List<RoleModel> getData() {
        return data;
    }

    public void setData(List<RoleModel> data) {
        this.data = data;
    }
}
