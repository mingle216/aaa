package com.wisedu.amp.card.cus.recommendServiceItems.model;


import com.wisedu.minos.casp.portal.model.RecommendServiceItems;

import java.io.Serializable;
import java.util.List;

public class ConfigInfo implements Serializable {
    //初始化展示高度
    private int height=466;
    //初始化卡片高度选项
    private String heightFlag="1";
    private String configure="0;2";//初始化服务配置事项
    //初始化任务来源列表
    private String sourceVal="1";

    //获取配置的推荐应用
    private List<RecommendServiceItems> appInfoBizList;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getConfigure() {
        return configure;
    }

    public void setConfigure(String configure) {
        this.configure = configure;
    }

    public String getSourceVal() {
        return sourceVal;
    }

    public void setSourceVal(String sourceVal) {
        this.sourceVal = sourceVal;
    }

    public String getHeightFlag() {
        return heightFlag;
    }

    public void setHeightFlag(String heightFlag) {
        this.heightFlag = heightFlag;
    }

    public List<RecommendServiceItems> getAppInfoBizList () {
        return appInfoBizList;
    }

    public void setAppInfoBizList (List<RecommendServiceItems> appInfoBizList) {
        this.appInfoBizList = appInfoBizList;
    }
}
