package com.wisedu.minos.casp.portal.model.service;

import java.util.List;

/**
 * @author 01319115
 */
public class QueryUserGrantedServiceResponse {
    private List<ServiceDetail> data;

    public List<ServiceDetail> getData() {
        return data;
    }

    public void setData(List<ServiceDetail> data) {
        this.data = data;
    }
}
