package com.wisedu.amp.card.sys.detailsofserviceitems.model;

import java.io.Serializable;

public class ConfigInfo implements Serializable {
    /**
     * 初始化展示高度
     */
    private int height = 466;
    /**
     * 初始化卡片高度选项
     */
    private String heightFlag = "1";
    private String configure;

    /**
     * 限制数量
     */
    private String num;


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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
