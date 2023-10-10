package com.wisedu.amp.card.cuscuc.servicebus.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ServiceBiz
 * @Author: u
 * @Date: 2020/10/13
 */
@Getter
@Setter
public class ServiceBiz implements Serializable {
    private String serviceId;
    private String serviceWid;
    private String classifyName;
    private String serviceName;
    private String iconUrl;
    private String pcAccessUrl;
    private String mobileAccessUrl;
    private boolean favorite;
    private boolean permission;
    private Integer serviceStation;
    private String mobileIconLink;

}
