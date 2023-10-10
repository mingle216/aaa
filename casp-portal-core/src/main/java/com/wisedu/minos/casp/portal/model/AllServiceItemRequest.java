package com.wisedu.minos.casp.portal.model;

import lombok.Data;

@Data
public class AllServiceItemRequest {
    int pageNumber = 1;
    int pageSize = 10;
    boolean service =true;
    private String userId;
    private String userAccount;
    private String searchKey;
    private String categoryWids;
    private String deptWids;
    private String roleWids;
    private Integer isRelate;
    private String srvDeptWids;
    private String dimensions;
    private String groupWid;
    private String lang;
    private boolean availableOnline = false;
    private boolean orderByVisitCount = false;
    //是否开启权限判断
    private Integer authorityEnabled;
}
