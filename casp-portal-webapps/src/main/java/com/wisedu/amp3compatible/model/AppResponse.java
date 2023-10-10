package com.wisedu.amp3compatible.model;

/**
 * Created by glhan on 2016/6/22.
 */
public class AppResponse extends BaseResponse {
    private AppEntity[] datas;

    public AppEntity[] getDatas() {
        return (null == datas) ? null : datas.clone();
    }

    public void setDatas(AppEntity[] datas) {
        this.datas = (null == datas) ? null : datas.clone();
    }
}
