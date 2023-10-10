package com.wisedu.amp3compatible.model;



import com.wisedu.minos.casp.portal.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glhan on 2016/6/21.
 */
public class AppEntity implements Comparable<AppEntity> {
    private String appWid;//应用wid
    private String appId;//应用id
    private String appName;//应用名称
    private String appIcon;//应用图标地址
    private String version;//应用版本
    private List<Category> categoryList = new ArrayList<Category>();
    private double appScore;//平均评价等级
    private int collectCount;//被收藏次数
    private Integer isCanUse;//是否有权使用，0：否；1：是
    private String deployPrefix; //应用地址的前缀
    private String entranceUrl;//应用入口绝对地址
    private String firstChar;//首字母，用于分类
    private int status;//应用的状态
    private Integer isPcApp; //是否是PC应用
    private Integer isMobileApp; //是否是移动应用
    private Integer isPcCard;//是否是PC卡片
    private Integer isMobileCard;//是否是移动卡片

    public String getAppWid() {
        return appWid;
    }

    public void setAppWid(String appWid) {
        this.appWid = appWid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public double getAppScore() {
        return appScore;
    }

    public void setAppScore(double appScore) {
        this.appScore = appScore;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public String getDeployPrefix() {
        return deployPrefix;
    }

    public void setDeployPrefix(String deployPrefix) {
        this.deployPrefix = deployPrefix;
    }

    public String getEntranceUrl() {
        return entranceUrl;
    }

    public void setEntranceUrl(String entranceUrl) {
        this.entranceUrl = entranceUrl;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getIsCanUse() {
        return isCanUse;
    }

    public void setIsCanUse(Integer isCanUse) {
        this.isCanUse = isCanUse;
    }

    public Integer getIsPcApp() {
        return isPcApp;
    }

    public void setIsPcApp(Integer isPcApp) {
        this.isPcApp = isPcApp;
    }

    public Integer getIsMobileApp() {
        return isMobileApp;
    }

    public void setIsMobileApp(Integer isMobileApp) {
        this.isMobileApp = isMobileApp;
    }

    public Integer getIsPcCard() {
        return isPcCard;
    }

    public void setIsPcCard(Integer isPcCard) {
        this.isPcCard = isPcCard;
    }

    public Integer getIsMobileCard() {
        return isMobileCard;
    }

    public void setIsMobileCard(Integer isMobileCard) {
        this.isMobileCard = isMobileCard;
    }

    @Override
    public int compareTo(AppEntity o) {
        String thisPinyin = StringUtil.toPinyinLowCase(this.appName);
        String thatPinyin = StringUtil.toPinyinLowCase(o.getAppName());
        return thisPinyin.compareTo(thatPinyin);
    }

    @Override
    public boolean equals(Object o){
        return super.equals(o);
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }
}
