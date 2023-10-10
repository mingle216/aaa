package com.wisedu.minos.casp.portal.model;

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
public class CheckUserServiceReq {
    private String userId;
    private String serviceWid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServiceWid() {
        return serviceWid;
    }

    public void setServiceWid(String serviceWid) {
        this.serviceWid = serviceWid;
    }
}
