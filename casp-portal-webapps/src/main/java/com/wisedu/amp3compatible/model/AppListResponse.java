package com.wisedu.amp3compatible.model;

/**
 * Created by glhan on 2016/6/22.
 */
public class AppListResponse extends BaseResponse {
    private AppInfoForOA[] datas;

    public void setDatas(AppInfoForOA[] datas) {
        this.datas = (null == datas) ? null : datas.clone();
    }

    public AppInfoForOA[] getDatas() {
        return (null == datas) ? null : datas.clone();
    }
}
