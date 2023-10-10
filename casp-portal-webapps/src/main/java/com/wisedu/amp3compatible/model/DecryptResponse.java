package com.wisedu.amp3compatible.model;

/**
 * Created by 01111178 on 2017/11/2.
 */
public class DecryptResponse extends BaseResponse {
    private DecryptEntity data;

    public DecryptEntity getData() {
        return data;
    }

    public void setData(DecryptEntity data) {
        this.data = data;
    }
}
