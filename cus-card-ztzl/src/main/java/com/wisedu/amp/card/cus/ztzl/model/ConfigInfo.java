package com.wisedu.amp.card.cus.ztzl.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ConfigInfo implements Serializable {
    //初始化展示高度
    private int height = 466;
    //初始化卡片高度选项
    private String heightFlag = "1";
    private String configure;
    //展示方式 (左右布局/上下布局)
    private String layoutFlag = "1";
    //链接数据
    private List<LinkInfo> linkData;

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

    public String getHeightFlag() {
        return heightFlag;
    }

    public void setHeightFlag(String heightFlag) {
        this.heightFlag = heightFlag;
    }

    public String getLayoutFlag() {
        return layoutFlag;
    }

    public void setLayoutFlag(String layoutFlag) {
        this.layoutFlag = layoutFlag;
    }

    public List<LinkInfo> getLinkData() {
        return linkData;
    }

    public void setLinkData(List<LinkInfo> linkData) {
        this.linkData = linkData;
    }
}
