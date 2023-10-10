package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.common.result.ResultData;

import java.util.List;

/**
 * 功能描述：服务信息
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ServiceInfoRes
 * @Author: jcx
 * @Date: 2020/12/24
 */
public class ServiceInfoRes{

    private String wid;

    private String appWid;

    private String serviceId;

    private String serviceName;

    private String appName;

    private String serviceNameLangKey;

    private List<MultiLangData> serviceNameLangKeys;

    private String serviceDesc;

    private String serviceDescLangKey;

    private List<MultiLangData> serviceDescLangKeys;

    private Integer serviceType;

    private Integer serviceStation;

    private String pcIconUrl;

    private String pcIconPath;

    private String pcIconName;

    private String mobileIconUrl;

    private String mobileIconPath;

    private String mobileIconName;

    private String pcUrl;

    private String mobileUrl;

    private Integer serviceVisibility;

    private String authenUrl;

    private String pcAccessUrl;

    private String mobileAccessUrl;

    private String serviceAccessWid;

    private int isDeleted;

    private int appEnabled;

    public int getIsDeleted () {
        return isDeleted;
    }

    public void setIsDeleted (int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getAppEnabled () {
        return appEnabled;
    }

    public void setAppEnabled (int appEnabled) {
        this.appEnabled = appEnabled;
    }

    public String getWid () {
        return wid;
    }

    public void setWid (String wid) {
        this.wid = wid;
    }

    public String getAppWid () {
        return appWid;
    }

    public void setAppWid (String appWid) {
        this.appWid = appWid;
    }

    public String getServiceId () {
        return serviceId;
    }

    public void setServiceId (String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName () {
        return serviceName;
    }

    public void setServiceName (String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAppName () {
        return appName;
    }

    public void setAppName (String appName) {
        this.appName = appName;
    }

    public String getServiceNameLangKey () {
        return serviceNameLangKey;
    }

    public void setServiceNameLangKey (String serviceNameLangKey) {
        this.serviceNameLangKey = serviceNameLangKey;
    }

    public List<MultiLangData> getServiceNameLangKeys () {
        return serviceNameLangKeys;
    }

    public void setServiceNameLangKeys (List<MultiLangData> serviceNameLangKeys) {
        this.serviceNameLangKeys = serviceNameLangKeys;
    }

    public String getServiceDesc () {
        return serviceDesc;
    }

    public void setServiceDesc (String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getServiceDescLangKey () {
        return serviceDescLangKey;
    }

    public void setServiceDescLangKey (String serviceDescLangKey) {
        this.serviceDescLangKey = serviceDescLangKey;
    }

    public List<MultiLangData> getServiceDescLangKeys () {
        return serviceDescLangKeys;
    }

    public void setServiceDescLangKeys (List<MultiLangData> serviceDescLangKeys) {
        this.serviceDescLangKeys = serviceDescLangKeys;
    }

    public Integer getServiceType () {
        return serviceType;
    }

    public void setServiceType (Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getServiceStation () {
        return serviceStation;
    }

    public void setServiceStation (Integer serviceStation) {
        this.serviceStation = serviceStation;
    }

    public String getPcIconUrl () {
        return pcIconUrl;
    }

    public void setPcIconUrl (String pcIconUrl) {
        this.pcIconUrl = pcIconUrl;
    }

    public String getPcIconPath () {
        return pcIconPath;
    }

    public void setPcIconPath (String pcIconPath) {
        this.pcIconPath = pcIconPath;
    }

    public String getPcIconName () {
        return pcIconName;
    }

    public void setPcIconName (String pcIconName) {
        this.pcIconName = pcIconName;
    }

    public String getMobileIconUrl () {
        return mobileIconUrl;
    }

    public void setMobileIconUrl (String mobileIconUrl) {
        this.mobileIconUrl = mobileIconUrl;
    }

    public String getMobileIconPath () {
        return mobileIconPath;
    }

    public void setMobileIconPath (String mobileIconPath) {
        this.mobileIconPath = mobileIconPath;
    }

    public String getMobileIconName () {
        return mobileIconName;
    }

    public void setMobileIconName (String mobileIconName) {
        this.mobileIconName = mobileIconName;
    }

    public String getPcUrl () {
        return pcUrl;
    }

    public void setPcUrl (String pcUrl) {
        this.pcUrl = pcUrl;
    }

    public String getMobileUrl () {
        return mobileUrl;
    }

    public void setMobileUrl (String mobileUrl) {
        this.mobileUrl = mobileUrl;
    }

    public Integer getServiceVisibility () {
        return serviceVisibility;
    }

    public void setServiceVisibility (Integer serviceVisibility) {
        this.serviceVisibility = serviceVisibility;
    }

    public String getAuthenUrl () {
        return authenUrl;
    }

    public void setAuthenUrl (String authenUrl) {
        this.authenUrl = authenUrl;
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

    public String getServiceAccessWid () {
        return serviceAccessWid;
    }

    public void setServiceAccessWid (String serviceAccessWid) {
        this.serviceAccessWid = serviceAccessWid;
    }
}
