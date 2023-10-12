package com.wisedu.amp.card.sys.detailsofserviceitems.model;

import java.util.List;

/**
 * @author 01319115
 */
public class BatchCheckUserServiceGrantRequest {
    private String userId;
    private List<String> serviceWids;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getServiceWids() {
        return serviceWids;
    }

    public void setServiceWids(List<String> serviceWids) {
        this.serviceWids = serviceWids;
    }
}
