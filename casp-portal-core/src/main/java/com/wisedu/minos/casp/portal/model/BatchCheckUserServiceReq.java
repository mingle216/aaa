package com.wisedu.minos.casp.portal.model;

import java.util.List;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CheckUserServiceReq
 * @Author: d
 * @Date: 2020/12/23
 */
public class BatchCheckUserServiceReq {
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
