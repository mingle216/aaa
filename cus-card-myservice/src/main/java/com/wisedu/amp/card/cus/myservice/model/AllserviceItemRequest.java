package com.wisedu.amp.card.cus.myservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllserviceItemRequest {
    private String userId;
    private String searchKey;
    private String categoryWids;
    private String deptWids;
    private String roleWids;
    private Integer isRelate;
    private String srvDeptWids;
    private String dimensions;
    private String groupWid;

}



