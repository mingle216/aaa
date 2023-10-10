package com.wisedu.amp.card.cus.cucFavoriteApp.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AppInfo implements Serializable {
    private String serviceId;
    private String serviceWid;
    private String serviceName;
    private String iconLink;
    private String pcAccessUrl;
    private String mobileAccessUrl;
    private boolean permission;
    private Integer serviceStation;
    private String mobileIconLink;
}
