package com.wisedu.amp.card.cuscuc.detailsofserviceitems.model;

import com.wisedu.amp.card.cuscuc.detailsofserviceitems.model.appraise.AppraiseName;
import java.io.Serializable;
import java.util.List;

public class AppInfo implements Serializable {

    private String itemName;
    private String iconUrl;
    private String appraiseMark;
    private String appraiseNum;
    private List<IndptModul> indptModuls;
    private List<IndptModul> baseInfos;
    private List<String> linkServices;

    /**
     * 是否停用
     * @return
     */
    private Integer isEnabled;

    /**
     * 是否删除
     * @return
     */
    private Integer isDeleted;

    private  List<AppraiseName> appraiseNames;

    private List<AppraiseName> dimenAppraiseNames;

    public List<String> getLinkServices() {
        return linkServices;
    }

    public void setLinkServices(List<String> linkServices) {
        this.linkServices = linkServices;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAppraiseMark() {
        return appraiseMark;
    }

    public void setAppraiseMark(String appraiseMark) {
        this.appraiseMark = appraiseMark;
    }

    public String getAppraiseNum() {
        return appraiseNum;
    }

    public void setAppraiseNum(String appraiseNum) {
        this.appraiseNum = appraiseNum;
    }

    public List<IndptModul> getIndptModuls () {
        return indptModuls;
    }

    public void setIndptModuls (List<IndptModul> indptModuls) {
        this.indptModuls = indptModuls;
    }

    public List<IndptModul> getBaseInfos () {
        return baseInfos;
    }

    public void setBaseInfos (List<IndptModul> baseInfos) {
        this.baseInfos = baseInfos;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<AppraiseName> getAppraiseNames() {
        return appraiseNames;
    }

    public void setAppraiseNames(
        List<AppraiseName> appraiseNames) {
        this.appraiseNames = appraiseNames;
    }

    public List<AppraiseName> getDimenAppraiseNames() {
        return dimenAppraiseNames;
    }

    public void setDimenAppraiseNames(List<AppraiseName> dimenAppraiseNames) {
        this.dimenAppraiseNames = dimenAppraiseNames;
    }
}
