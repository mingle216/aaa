package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.api.model.DubboMultiLangData;

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
 * @title ServiceGrant
 * @Author: d
 * @Date: 2020/12/23
 */
public class ServiceGrant {
    private String modelType;
    private String modelWid;
    private String modelName;
    private String modelAccount;
    private String orderIndex;
    private String userSex;
    private String orgName;
    private String nameMultiLangKey;
    private List<DubboMultiLangData> nameMultiLang;

    private String serviceUrl;

    public String getNameMultiLangKey() {
        return nameMultiLangKey;
    }

    public void setNameMultiLangKey(String nameMultiLangKey) {
        this.nameMultiLangKey = nameMultiLangKey;
    }

    public List<DubboMultiLangData> getNameMultiLang() {
        return nameMultiLang;
    }

    public void setNameMultiLang(List<DubboMultiLangData> nameMultiLang) {
        this.nameMultiLang = nameMultiLang;
    }

    public String getServiceUrl () {
        return serviceUrl;
    }

    public void setServiceUrl (String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getModelType () {
        return modelType;
    }

    public void setModelType (String modelType) {
        this.modelType = modelType;
    }

    public String getModelWid () {
        return modelWid;
    }

    public void setModelWid (String modelWid) {
        this.modelWid = modelWid;
    }

    public String getModelName () {
        return modelName;
    }

    public void setModelName (String modelName) {
        this.modelName = modelName;
    }

    public String getModelAccount () {
        return modelAccount;
    }

    public void setModelAccount (String modelAccount) {
        this.modelAccount = modelAccount;
    }

    public String getOrderIndex () {
        return orderIndex;
    }

    public void setOrderIndex (String orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getUserSex () {
        return userSex;
    }

    public void setUserSex (String userSex) {
        this.userSex = userSex;
    }

    public String getOrgName () {
        return orgName;
    }

    public void setOrgName (String orgName) {
        this.orgName = orgName;
    }
}
