package com.wisedu.amp3compatible.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class FaveriteServiceInfo implements Serializable {
    private String serviceId;
    private String serviceWid;
    private String serviceName;
    private String iconLink;
    private String pcAccessUrl;
    private String mobileAccessUrl;
    private boolean permission;
    private Integer serviceStation;
    private Integer favoriteCount;

    private String mobileIconLink;
    private List<ServiceClassifyInfo> classifyInfos;
}
