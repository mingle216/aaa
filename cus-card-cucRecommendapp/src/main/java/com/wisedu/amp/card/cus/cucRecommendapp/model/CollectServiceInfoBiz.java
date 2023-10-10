package com.wisedu.amp.card.cus.cucRecommendapp.model;

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
 * @title CollectServiceInfoBiz
 * @Author: u
 * @Date: 2020/10/20
 */
public class CollectServiceInfoBiz implements Serializable {
    private String serviceWid;
    private String serviceName;
    private String pcAccessUrl;
    private String mobileAccessUrl;
    private String iconLink;

    public String getServiceWid () {
        return serviceWid;
    }

    public void setServiceWid (String serviceWid) {
        this.serviceWid = serviceWid;
    }

    public String getServiceName () {
        return serviceName;
    }

    public void setServiceName (String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPcAccessUrl () {
        return pcAccessUrl;
    }

    public void setPcAccessUrl (String pcAccessUrl) {
        this.pcAccessUrl = pcAccessUrl;
    }

    public String getMobileAccessUrl () {
        return mobileAccessUrl;
    }

    public void setMobileAccessUrl (String mobileAccessUrl) {
        this.mobileAccessUrl = mobileAccessUrl;
    }

    public String getIconLink () {
        return iconLink;
    }

    public void setIconLink (String iconLink) {
        this.iconLink = iconLink;
    }
}
