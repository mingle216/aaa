package com.wisedu.amp3compatible.model;

import java.io.Serializable;

/**
 * Created by glhan on 2018/9/11 15:28.
 */
public class GrsjInfoExtra extends GrsjInfo implements Serializable {
    private String extraInfo;

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}
