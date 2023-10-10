package com.wisedu.amp.card.cus.serviceItemCount.model;

import java.util.List;

/**
 * @ClassName ItemStatisticsRequest
 * @Description
 * @Date 2021/1/27 0027 13:50
 * @Author zkpu
 * @Version 1.0
 **/
public class ItemStatisticsRequest {

    private List<String> roleIds;

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
